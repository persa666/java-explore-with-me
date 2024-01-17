package ru.practicum.exp;

public class ApiError {
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;

    public ApiError(String status, String reason, String message, String timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
