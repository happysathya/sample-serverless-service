package com.happysathya.core;

import java.util.Map;

public class LambdaResponse {

    private int statusCode;
    private Map<String, String> headers;
    private String body;

    public LambdaResponse(int statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
