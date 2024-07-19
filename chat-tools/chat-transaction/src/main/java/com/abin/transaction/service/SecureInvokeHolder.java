package com.abin.transaction.service;

import java.util.Objects;

public class SecureInvokeHolder {

    private static final ThreadLocal<Boolean> INVOKE_LOCAL = new ThreadLocal<>();

    public static Boolean isInvoking() {
        return Objects.nonNull(INVOKE_LOCAL.get());
    }

    public static void setInvoking() {
        INVOKE_LOCAL.set(Boolean.TRUE);
    }

    public static void invoked() {
        INVOKE_LOCAL.remove();
    }
}
