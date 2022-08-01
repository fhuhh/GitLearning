package com.example.server.exception;

import com.example.server.pojo.common.ResponseBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public ResponseBean mySQLException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return ResponseBean.error("该数据是其他数据的外键");
        }
        return ResponseBean.error("数据库异常");
    }

}
