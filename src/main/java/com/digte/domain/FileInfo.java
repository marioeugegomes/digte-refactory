package com.digte.domain;

public class FileInfo {
    private String fileName;
    private String disposition;
    private String contentType;
    private int contentLength;

    public FileInfo(String fileName,String disposition, String contentType, int contentLength) {
        this.fileName = fileName;
        this.disposition = disposition;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public String getFileName() {
        return this.fileName;
    }

     public String getDisposition() {
        return this.disposition;
    }

     public String getContentType() {
        return this.contentType;
    }

     public int getContentLength() {
        return this.contentLength;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\n\tfileName: ").append(this.fileName).append(",");
        builder.append("\n\tdisposition: ").append(this.disposition).append(",");
        builder.append("\n\tcontentType: ").append(this.contentType).append(",");
        builder.append("\n\tcontentLength: ").append(this.contentLength).append(",");
        builder.append("\n}");
        return builder.toString();
     }
}
