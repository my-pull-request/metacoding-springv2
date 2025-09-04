package com.metacoding.springv2.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.metacoding.springv2.core.handler.ex.Exception400;
import com.metacoding.springv2.core.handler.ex.Exception401;
import com.metacoding.springv2.core.handler.ex.Exception403;
import com.metacoding.springv2.core.handler.ex.Exception404;
import com.metacoding.springv2.core.util.Resp;


@Slf4j
@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> exApi400(Exception400 e) {
        log.warn(e.getMessage());
        return Resp.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> exApi401(Exception401 e) {
        log.warn(e.getMessage());
        return Resp.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> exApi403(Exception403 e) {
        log.warn(e.getMessage());
        return Resp.fail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> exApi404(Exception404 e) {
        log.warn(e.getMessage());
        return Resp.fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exUnKnown(Exception e) {
        log.error(e.getMessage());
        return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의하세요");
    }
}