package com.io.project.schema;

import lombok.Getter;

public class ArticleResSchema {
    String code;
    @Getter
    String message;
    String data;

    public ArticleResSchema(String code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ArticleResSchema{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
