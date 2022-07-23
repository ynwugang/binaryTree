package com.wugang.exception;

/**
 * 自定义异常
 */
public class ConditionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private boolean success;

    private conditionExceptionCode code;

    public ConditionException(boolean success, conditionExceptionCode code) {
        super(code.getDesc());
        this.success = success;
    }

    public ConditionException(conditionExceptionCode code) {
        super(code.getDesc());
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
