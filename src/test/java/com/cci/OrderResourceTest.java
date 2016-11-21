package com.cci;

import com.cci.model.LambdaFunctionRequest;
import com.cci.model.LambdaFunctionResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class OrderResourceTest {

    private OrderResource orderResource;

    @Before
    public void setup() {
        orderResource = new OrderResource();
    }

    @Test
    public void handleRequest_getOrders() {
        HashMap<String, String> pathParameters = new HashMap<>();
        pathParameters.put("accountId", "c101");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2VxZS5ldS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NTgyZDk3ZGY3ODgyYjI5NzAyNTNiY2VlIiwiYXVkIjoiNnF2QUxSTXo1bDhHbVdzeEtmRWZRMWdZM1RGS1kzcHEiLCJleHAiOjE0Nzk3MTIyMzgsImlhdCI6MTQ3OTY3NjIzOH0.iuo0g4Im860B2QQpb4ny8ZkI6sIxDrAhwq-7L5_MjIU");

        LambdaFunctionRequest lambdaFunctionRequest = new LambdaFunctionRequest();
        lambdaFunctionRequest.setHttpMethod("GET");
        lambdaFunctionRequest.setHeaders(headers);
        lambdaFunctionRequest.setPathParameters(pathParameters);

        LambdaFunctionResponse lambdaFunctionResponse = orderResource.handleRequest(lambdaFunctionRequest, null);

        System.out.println(lambdaFunctionResponse.getHeaders());
        System.out.println(lambdaFunctionResponse.getBody());
        assertEquals(200, lambdaFunctionResponse.getStatusCode());
    }

    @Test
    public void handleRequest_postOrder() {
        HashMap<String, String> pathParameters = new HashMap<>();
        pathParameters.put("accountId", "c101");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2VxZS5ldS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NTgyZDk3ZGY3ODgyYjI5NzAyNTNiY2VlIiwiYXVkIjoiNnF2QUxSTXo1bDhHbVdzeEtmRWZRMWdZM1RGS1kzcHEiLCJleHAiOjE0Nzk3MTIyMzgsImlhdCI6MTQ3OTY3NjIzOH0.iuo0g4Im860B2QQpb4ny8ZkI6sIxDrAhwq-7L5_MjIU");

        LambdaFunctionRequest lambdaFunctionRequest = new LambdaFunctionRequest();
        lambdaFunctionRequest.setHttpMethod("POST");
        lambdaFunctionRequest.setBody("{\"accountId\":\"a101\",\"brand\":\"b102\"}");
        lambdaFunctionRequest.setHeaders(headers);
        lambdaFunctionRequest.setPathParameters(pathParameters);

        LambdaFunctionResponse lambdaFunctionResponse = orderResource.handleRequest(lambdaFunctionRequest, null);

        System.out.println(lambdaFunctionResponse.getHeaders());
        System.out.println(lambdaFunctionResponse.getBody());
        assertEquals(200, lambdaFunctionResponse.getStatusCode());
    }

}