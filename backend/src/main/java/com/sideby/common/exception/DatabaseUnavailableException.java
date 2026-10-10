package com.sideby.common.exception;

import org.springframework.http.HttpStatus;

public final class DatabaseUnavailableException extends BusinessException {

    public DatabaseUnavailableException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, "DATABASE_UNAVAILABLE", "Database is currently unavailable");
    }
}

