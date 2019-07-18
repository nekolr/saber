package com.nekolr.saber.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 错误响应实体
 *
 * @author nekolr
 */
@Data
public class ErrorResponse {

    private Integer status;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private ErrorResponse() {
        timestamp = LocalDateTime.now();
    }

    public ErrorResponse(Integer status, String message) {
        this();
        this.status = status;
        this.message = message;
    }
}
