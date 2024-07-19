package com.abin.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecureInvokeDTO {

    private String className;

    private String methodName;

    private String parameterTypes;

    private String args;
}
