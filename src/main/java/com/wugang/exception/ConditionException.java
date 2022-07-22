package com.wugang.exception;

/**
 * 自定义异常
 */
public class ConditionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private boolean success;

    public ConditionException(boolean success, String name) {
        super(name);
        this.success = success;
    }

    public ConditionException(String name) {
        super(name);
        success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 不写入堆栈信息
     * @return
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
