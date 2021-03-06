package com.alconn.copang.aop;

import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseMessage<String> handleUnauthorizedException(UnauthorizedException e) {
        return  ResponseMessage.<String>builder()
                .message(e.getMessage())
                .code(e.getCode()).build();
    }

    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseMessage<String> handleLoginFiled(LoginFailedException e){
        return  ResponseMessage.<String>builder()
                .message(e.getMessage())
                .code(e.getCode())
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected ResponseMessage<String> methodNotAllowed(HttpServletRequest request, Exception e){
        log.warn("?????? ?????? {}\n{}\n",request.getRequestURI(), request.getRequestURL());
        return ResponseMessage.<String>builder()
            .message("???????????? ?????? method")
            .data(e.getMessage())
            .code(-1001)
            .build();
    }

    @ExceptionHandler({Exception.class,RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseMessage<String> handleInternalServerError(HttpServletRequest request, Exception e){
        log.warn("?????? ?????? {}\n{}\n",request.getRequestURI(), request.getRequestURL());
        log.warn("???????????? ?????? Exception ", e);
        return ResponseMessage.<String>builder()
                .message("?????????????????????")
                .data(e.getMessage())
                .code(-1001)
                .build();
    }

//    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseMessage<String> handleGlobal(Throwable t){
        System.out.println("ExceptionHandle! \n\n\n\n\n\n\n\n\n");
        return ResponseMessage.<String>builder()
                .message("eerrr")
                .build();
    }

    @ExceptionHandler(NoSuchEntityExceptions.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseMessage<String > selectFailed(NoSuchEntityExceptions e){
        return ResponseMessage.<String>builder()
            .message("???????????? ????????? ???????????? ????????????")
            .data(e.getMessage())
            .code(-111)
            .build();
    }


    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseMessage<String > selectFailedElement(NoSuchElementException e){
        return ResponseMessage.<String>builder()
            .message("???????????? ????????? ???????????? ????????????")
            .data(e.getMessage())
            .code(-111)
            .build();
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class,ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseMessage<String> jpaException(SQLIntegrityConstraintViolationException e) {
        return ResponseMessage.<String>builder()
            .message("???????????? ????????? ???????????? ????????????")
            .data(e.getMessage())
            .code(-111)
            .build();
    }
    @ExceptionHandler({ DataIntegrityViolationException.class, })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseMessage<String> dataIntegrateException(DataIntegrityViolationException e) {
        return ResponseMessage.<String>builder()
                .message("???????????? ????????? ???????????? ????????????")
                .data(e.getMessage())
                .code(-111)
                .build();
    }


//    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseMessage<String> authException(AuthenticationCredentialsNotFoundException e){
        return ResponseMessage.<String>builder()
                .message("??????????????? ????????????!")
                .build();
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseMessage<String> handleAccessDenied(AccessDeniedException e){
//        httpServletResponse.setCharacterEncoding("utf-8");
//        System.out.println("ExceptionHandle! \n\n\n\n\n\n\n\n\n");
        log.warn("access denied ", e);
        return ResponseMessage.<String>builder()
                .message("????????? ????????????")
                .data(e.getMessage())
                .code(403)
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage<?> handleValidate(ValidationException e){
        e.printStackTrace();
        return ResponseMessage.builder()
            .message("???????????? ????????? ???????????? ????????????")
            .data(e.getMessage())
            .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage<List<String>> handleValidate(MethodArgumentNotValidException e) {
        log.info("validate exception", e);
        List<String> msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> "field :[" + f.getField() +"] " + f.getDefaultMessage()).collect(Collectors.toList());
//                .collect(Collectors.joining("\n"));
        return ResponseMessage.<List<String>>builder()
                .message("???????????? ????????? ???????????? ????????????")
                .data(msg)
                .build();
    }

    @ExceptionHandler(TransientPropertyValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage<String > handleUnSaved(TransientPropertyValueException e) {
        return ResponseMessage.<String>builder()
            .message("?????? ?????? ????????? ?????? ???????????????!")
            .code(-11)
            .data(e.getMessage())
            .build();
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseMessage<String> handleUnmapped(MethodArgumentNotValidException e) {
//        return ResponseMessage.<String>builder()
//            .message("???????????? ????????? ???????????? ????????????")
//            .code(-111)
//            .data(e.getMessage() + "\n" + e.getAllErrors().stream().map(
//                DefaultMessageSourceResolvable::getDefaultMessage).collect(
//                Collectors.joining("\n")))
//            .build();
//    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage<String > handleConflict(ConversionFailedException e){
        return ResponseMessage.badRequest(
            Objects.requireNonNull(e.getSourceType()).getType().getSimpleName() + e.getMessage()
        );
    }

}
