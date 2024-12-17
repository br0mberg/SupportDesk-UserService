package ru.brombin.user_service.util;

public enum LogMessages {
    USER_NOT_FOUND("User with ID '%s' not found"),
    USERS_FETCHED("Successfully fetched {} users"),
    USER_FETCHED("Successfully fetched user with ID: {}"),
    USER_CREATED("User with ID: {} created successfully"),
    USER_UPDATED("User with ID: {} updated successfully"),
    USER_DELETED("User with ID: {} deleted successfully");

    private final String message;

    LogMessages(String message) {
        this.message = message;
    }

    public String getFormatted(Object... args) {
        return String.format(message, args);
    }
}