package com.alconn.copang.aop;

import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.UnauthorizedException;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseMessage<String> handleUnauthorizedException(UnauthorizedException e) {
        return  ResponseMessage.<String>builder().message(e.getMessage()).code(e.getCode()).build();
    }

    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseMessage<String> handleLoginFiled(LoginFailedException e){
        return  ResponseMessage.<String>builder()
                .message(e.getMessage())
                .code(e.getCode())
                .build();
    }

    //    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseMessage<String> handleInternalServerError(HttpServletRequest request, Exception e){
        return ResponseMessage.<String>builder()
                .message("something when wrong i'll figure it out")
                .code(-1001)
                .build();
    }


}
