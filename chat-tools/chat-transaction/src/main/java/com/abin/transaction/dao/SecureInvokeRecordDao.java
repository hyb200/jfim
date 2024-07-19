package com.abin.transaction.dao;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.abin.transaction.domain.SecureInvokeDTO;
import com.abin.transaction.domain.SecureInvokeRecord;
import com.abin.transaction.mapper.SecureInvokeRecordMapper;
import com.abin.transaction.service.SecureInvokeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SecureInvokeRecordDao extends ServiceImpl<SecureInvokeRecordMapper, SecureInvokeRecord> {

    public List<SecureInvokeRecord> getWaitRetryRecords() {
        Date date = new Date();
        DateTime offset = DateUtil.offsetSecond(date, (int) -SecureInvokeService.RETRY_INTERVAL_SECONDS);
        return lambdaQuery().eq(SecureInvokeRecord::getStatus, SecureInvokeRecord.WAIT)
                .lt(SecureInvokeRecord::getNextRetryTime, new Date())
                .lt(SecureInvokeRecord::getCreateTime, offset)
                .list();
    }
}
