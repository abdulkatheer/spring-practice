package org.example.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String id;

    public ResourceNotFoundException(String id) {
        super();
        this.id = id;
    }
}
