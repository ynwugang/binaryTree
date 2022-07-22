package com.wugang.handler;

import com.wugang.exception.ConditionException;
import com.wugang.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonGlobalExceptionHandler.class);

    /**
     * validation校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResponse validExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        LOGGER.warn("参数校验失败：{}", message);
        return new CommonResponse(false, message);
    }

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConditionException.class)
    @ResponseBody
    public CommonResponse<String> commonExceptionHandler(ConditionException e) {
        String errorMsg = e.getMessage();
        LOGGER.warn("业务异常：{}", errorMsg);
        return new CommonResponse<>(false, errorMsg);
    }

    /**
     * exception异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResponse<String> commonExceptionHandler(Exception e) {
        LOGGER.error("系统异常：", e);
        return new CommonResponse<>(false, "系统出现异常，请联系管理员");
    }
}
