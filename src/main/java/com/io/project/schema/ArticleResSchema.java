package com.io.project.schema;

public class ArticleResSchema {
    String code;
    String message;
    String data;

    public ArticleResSchema(String code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }
}
