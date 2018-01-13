package com.happysathya.handler;

import com.happysathya.core.LambdaRequest;
import com.happysathya.core.LambdaResponse;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class InformationHandlerTest {

    @Test
    public void handleRequest() {
        HashMap<String, String> headers = new HashMap<>();

        LambdaRequest lambdaFunctionRequest = new LambdaRequest();
        lambdaFunctionRequest.setHttpMethod("GET");
        lambdaFunctionRequest.setHeaders(headers);

        LambdaResponse lambdaFunctionResponse = new InformationHandler().handleRequest(lambdaFunctionRequest, null);

        System.out.println(lambdaFunctionResponse.getHeaders());
        System.out.println(lambdaFunctionResponse.getBody());
        assertEquals(200, lambdaFunctionResponse.getStatusCode());
    }
}