package com.aljovic.amer.exception;

public class FileNotParsableException extends RuntimeException {
    private final String fileName;

    public FileNotParsableException(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
