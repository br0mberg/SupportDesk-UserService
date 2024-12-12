package ru.brombin.user_service.util;

public record ErrorResponse (
    String message,
    Long timestamp
) {}
