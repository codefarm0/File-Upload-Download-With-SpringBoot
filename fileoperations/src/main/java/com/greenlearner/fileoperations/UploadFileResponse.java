package com.greenlearner.fileoperations;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
public class UploadFileResponse {
    String fileName;
    String uri;
    String contentType;
    long size;

    public UploadFileResponse(String fileName, String uri, String contentType, long size) {
        this.fileName = fileName;
        this.uri = uri;
        this.contentType = contentType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUri() {
        return uri;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }
}
