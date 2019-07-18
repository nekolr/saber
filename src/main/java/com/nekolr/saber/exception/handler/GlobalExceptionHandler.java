package com.nekolr.saber.exception.handler;

import com.nekolr.saber.exception.BadRequestException;
import com.nekolr.saber.exception.ErrorResponse;
import com.nekolr.saber.support.I18nUtils;
import com.nekolr.saber.util.ThrowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private I18nUtils i18nUtils;

    /**
     * 处理所有未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(ThrowableUtils.getStackTrace(e));
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), e.getMessage());
        return this.buildResponseEntity(errorResponse);
    }

    /**
     * 处理所有无效请求异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        log.error(ThrowableUtils.getStackTrace(e));
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());
        return this.buildResponseEntity(errorResponse);
    }

    /**
     * 处理所有无效请求异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(ThrowableUtils.getStackTrace(e));
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), this.buildErrorMessage(errorList));
        return this.buildResponseEntity(errorResponse);
    }

    /**
     * 构建错误消息
     *
     * @param errorList
     * @return
     */
    private String buildErrorMessage(List<ObjectError> errorList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectError error : errorList) {
            String message = i18nUtils.getMessage(error.getDefaultMessage());
            stringBuilder.append(message + ", ");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    /**
     * 创建响应实体
     *
     * @param errorResponse
     * @return
     */
    private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }
}
