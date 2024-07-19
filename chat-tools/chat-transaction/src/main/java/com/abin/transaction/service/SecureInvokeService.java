package com.abin.transaction.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.abin.transaction.dao.SecureInvokeRecordDao;
import com.abin.transaction.domain.SecureInvokeDTO;
import com.abin.transaction.domain.SecureInvokeRecord;
import com.abin.transaction.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class SecureInvokeService {

    public static double RETRY_INTERVAL_SECONDS = 2D;

    private final SecureInvokeRecordDao secureInvokeRecordDao;

    private final Executor executor;

    @Scheduled(cron = "*/5 * * * * ?")
    public void retry() {
        List<SecureInvokeRecord> waitRetryRecords = secureInvokeRecordDao.getWaitRetryRecords();
        for (SecureInvokeRecord waitRetryRecord : waitRetryRecords) {
            doAsyncInvoke(waitRetryRecord);
        }
    }

    public void invoke(SecureInvokeRecord record, boolean async) {
        boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        if (!inTransaction) {
            return;
        }
        //  先入库
        save(record);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        if (async) {
                            doAsyncInvoke(record);
                        } else {
                            doInvoke(record);
                        }
                    }
                }
        );
    }

    private void doInvoke(SecureInvokeRecord record) {
        SecureInvokeDTO secureInvokeDTO = record.getSecureInvokeDTO();
        try {
            SecureInvokeHolder.setInvoking();
            Class<?> clazz = Class.forName(secureInvokeDTO.getClassName());
            Object bean = SpringUtil.getBean(clazz);
            List<String> parameterStrings = JsonUtils.toList(secureInvokeDTO.getParameterTypes(), String.class);
            List<Class<?>> parameterClasses = getParameters(parameterStrings);
            Method method = ReflectUtil.getMethod(clazz, secureInvokeDTO.getMethodName(), parameterClasses.toArray(new Class[]{}));
            Object[] args = getArgs(secureInvokeDTO, parameterClasses);
            //  执行方法
            method.invoke(bean, args);
            //  执行成功更新状态
            removeRecord(record.getId());
        } catch (Throwable e) {
            log.error("SecureInvokeService invoke fail", e);
            //  下次执行
            retryRecord(record, e.getMessage());
        } finally {
            SecureInvokeHolder.invoked();
        }
    }

    private void retryRecord(SecureInvokeRecord record, String errMsg) {
        int retryTimes = record.getRetryTimes() + 1;
        SecureInvokeRecord update = new SecureInvokeRecord();
        update.setId(record.getId());
        update.setFailReason(errMsg);
        update.setNextRetryTime(getNextRetryTime(retryTimes));

        if (retryTimes > record.getMaxRetryTimes()) {
            update.setStatus(SecureInvokeRecord.FAIL);
        } else {
            update.setRetryTimes(retryTimes);
        }
        secureInvokeRecordDao.updateById(update);
    }

    private Date getNextRetryTime(int retryTimes) {
        double waitMinutes = Math.pow(RETRY_INTERVAL_SECONDS, retryTimes);//重试时间指数上升 2m 4m 8m 16m
        return DateUtil.offsetSecond(new Date(), (int) waitMinutes);
    }

    private void removeRecord(Long id) {
        secureInvokeRecordDao.removeById(id);
    }

    @NotNull
    private Object[] getArgs(SecureInvokeDTO secureInvokeDTO, List<Class<?>> parameterClasses) {
        JsonNode jsonNode = JsonUtils.toJsonNode(secureInvokeDTO.getArgs());
        Object[] args = new Object[jsonNode.size()];
        for (int i = 0; i < jsonNode.size(); i++) {
            Class<?> aClass = parameterClasses.get(i);
            args[i] = JsonUtils.nodeToValue(jsonNode.get(i), aClass);
        }
        return args;
    }

    @NotNull
    private List<Class<?>> getParameters(List<String> parameterStrings) {
        return parameterStrings.stream()
                .map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        log.error("SecureInvokeService class not found", e);
                    }
                    return null;
                }).collect(Collectors.toList());
    }

    private void doAsyncInvoke(SecureInvokeRecord record) {
        executor.execute(() -> doInvoke(record));
    }

    private void save(SecureInvokeRecord record) {
        secureInvokeRecordDao.save(record);
    }
}
