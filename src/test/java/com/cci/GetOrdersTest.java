package com.cci;

import com.cci.model.LambdaFunctionRequest;
import com.cci.model.LambdaFunctionResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class GetOrdersTest {

    private GetOrders getOrders;

    @Before
    public void setup() {
        getOrders = new GetOrders();
    }

    @Test
    public void handleRequest() {
        HashMap<String, String> pathParameters = new HashMap<>();
        pathParameters.put("accountId", "c101");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2VxZS5ldS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NTgyZDk3ZGY3ODgyYjI5NzAyNTNiY2VlIiwiYXVkIjoiNnF2QUxSTXo1bDhHbVdzeEtmRWZRMWdZM1RGS1kzcHEiLCJleHAiOjE0Nzk1MDQ2NzUsImlhdCI6MTQ3OTQ2ODY3NX0.XzkyAUAjp6gI_pQcyeCt1YZuSmsHJjIF2ARWvNoqN3w");

        LambdaFunctionRequest lambdaFunctionRequest = new LambdaFunctionRequest();
        lambdaFunctionRequest.setHeaders(headers);
        lambdaFunctionRequest.setPathParameters(pathParameters);

        LambdaFunctionResponse lambdaFunctionResponse = getOrders.handleRequest(lambdaFunctionRequest, null);

        System.out.println(lambdaFunctionResponse.getHeaders());
        System.out.println(lambdaFunctionResponse.getBody());
        assertEquals(200, lambdaFunctionResponse.getStatusCode());
    }

}