package ru.brombin.user_service.util;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
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

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        String path = uriInfo != null ? uriInfo.getPath() : "unknown";

        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), System.currentTimeMillis(), path);

        return switch (exception) {
            case NotFoundException notFoundException -> {
                log.info("Resource not found: {}", exception.getMessage());
                yield buildResponse(Response.Status.NOT_FOUND, errorResponse);
            }
            case ValidationException validationException -> {
                log.warn("Validation error: {}", exception.getMessage());
                yield buildResponse(Response.Status.BAD_REQUEST, errorResponse);
            }
            case WebApplicationException webEx -> {
                log.error("Web application error: {}", exception.getMessage(), exception);
                yield buildResponse(Response.Status.fromStatusCode(webEx.getResponse().getStatus()), errorResponse);
            }
            default -> {
                log.error("Unhandled exception: {}", exception.getMessage(), exception);
                ErrorResponse genericResponse = new ErrorResponse(
                        "An unexpected error occurred", System.currentTimeMillis(), path);
                yield buildResponse(Response.Status.INTERNAL_SERVER_ERROR, genericResponse);
            }
        };
    }

    private Response buildResponse(Response.Status status, ErrorResponse errorResponse) {
        return Response.status(status)
                .entity(errorResponse)
                .build();
    }
}
