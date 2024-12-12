package ru.brombin.user_service.util;

import lombok.extern.slf4j.Slf4j;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.WebApplicationException;
import ru.brombin.user_service.util.exception.NotFoundException;
import ru.brombin.user_service.util.exception.ValidationException;

@Slf4j
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Unhandled exception: {}", exception.getMessage(), exception);

        return switch (exception) {
            case NotFoundException notFoundException -> Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()))
                    .build();
            case ValidationException validationException -> Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()))
                    .build();
            case WebApplicationException webApplicationException -> Response
                    .status(webApplicationException.getResponse().getStatus())
                    .entity(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()))
                    .build();
            default -> Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An unexpected error occurred", System.currentTimeMillis()))
                    .build();
        };

    }
}
