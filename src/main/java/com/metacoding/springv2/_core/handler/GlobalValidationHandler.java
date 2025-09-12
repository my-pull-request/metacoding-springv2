package com.metacoding.springv2._core.handler;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.metacoding.springv2._core.handler.ex.Exception400;

import java.util.List;

@Aspect // 관점 관리
@Component
public class GlobalValidationHandler {

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void badRequestAdvice(JoinPoint jp) { // jp는 실행될 실제 메서드의 모든 것을 투영하고 있다.
        Object[] args = jp.getArgs(); // 메서드의 매개변수들
        for (Object arg : args) { // 매개변수 개수만큼 반복 (어노테이션은 제외)

            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;

                // 에러가 존재한다면!!
                if (errors.hasErrors()) {
                    List<FieldError> fErrors = errors.getFieldErrors();

                    for (FieldError fieldError : fErrors) {
                        throw new Exception400(fieldError.getField() + ":" + fieldError.getDefaultMessage());
                    }
                }
            }
        }
    }
}