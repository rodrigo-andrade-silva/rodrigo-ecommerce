package br.com.rodrigo.ecommerce.server.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerValidationException(MethodArgumentNotValidException exception,
                                               HttpServletRequest request) {
        BindingResult result = exception.getBindingResult();
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation error!",
                request.getServletPath(), validationErrors);
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerValidationException(IllegalStateException exception,
                                               HttpServletRequest request) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation error!",
                request.getServletPath(), null);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerValidationException(HttpMessageNotReadableException exception,
                                               HttpServletRequest request) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation error!",
                request.getServletPath(), null);
    }

    // Regras de negócio dos services (livro inexistente, quantidade inválida, etc.)
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerIllegalArgumentException(IllegalArgumentException exception,
                                                    HttpServletRequest request) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), exception.getMessage(),
                request.getServletPath(), null);
    }

    // Restrições do banco de dados (ex.: username repetido)
    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                           HttpServletRequest request) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(),
                "Dado já cadastrado ou em uso. Verifique se o registro já existe.",
                request.getServletPath(), null);
    }
}