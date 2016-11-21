package com.cci;

import com.cci.model.LambdaFunctionRequest;
import com.cci.model.LambdaFunctionResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OrderResourceTest {

    private static String authorizationToken = null;
    private OrderResource orderResource;

    @BeforeClass
    public static void authorize() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String url = "https://eqe.eu.auth0.com/oauth/ro";
        String clientId = "6qvALRMz5l8GmWsxKfEfQ1gY3TFKY3pq";
        String username = "test_user@clearchannel.com";
        String password = "P@ssw0rd";

        HashMap<String, String> request = new HashMap<>();
        request.put("client_id", clientId);
        request.put("username", username);
        request.put("password", password);
        request.put("connection", "Username-Password-Authentication");
        request.put("grant_type", "password");
        request.put("scope", "openid");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(request));
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response httpResponse = okHttpClient.newCall(httpRequest).execute();

        Map<String, String> responseBody = objectMapper.readValue(httpResponse.body().byteStream(), new TypeReference<Map<String, String>>() {
        });
        authorizationToken = responseBody.get("id_token");
    }

    @Before
    public void setup() {
        orderResource = new OrderResource();
    }

    @Test
    public void handleRequest_getOrders() {
        HashMap<String, String> pathParameters = new HashMap<>();
        pathParameters.put("accountId", "c101");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", authorizationToken);

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
        headers.put("Authorization", authorizationToken);

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