package com.abin.transaction.dao;

import com.abin.transaction.domain.SecureInvokeRecord;
import com.abin.transaction.mapper.SecureInvokeRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class SecureInvokeRecordDao extends ServiceImpl<SecureInvokeRecordMapper, SecureInvokeRecord> {
}
