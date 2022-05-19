package com.adrabazha.gypsy.board.exception;

import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.utils.HttpUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.adrabazha.gypsy.board.dto.UserMessage.ERROR_FLASH_ATTRIBUTE;

@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException exception, RedirectAttributes attributes, HttpServletRequest request) {
        if (exception.getFieldErrorCount() == 0) {
            return "redirect:/";
        }
        String message = exception.getFieldError().getDefaultMessage();
        attributes.addFlashAttribute(ERROR_FLASH_ATTRIBUTE, UserMessage.error(message));
        return getRedirectUrl(request);
    }

    private String getRedirectUrl(HttpServletRequest request) {
        return "redirect:" + HttpUtils.getPreviousPageUrl(request);
    }
}
