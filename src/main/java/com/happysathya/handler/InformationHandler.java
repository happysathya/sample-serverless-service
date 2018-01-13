package com.happysathya.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happysathya.core.CustomRequestHandler;
import com.happysathya.core.LambdaRequest;
import com.happysathya.core.LambdaResponse;
import com.happysathya.model.Information;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collections;

public class InformationHandler extends CustomRequestHandler<LambdaRequest, LambdaResponse> {

    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final TypeReference<Information> informationReference = new TypeReference<Information>() {
    };
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpUrl httpUrl = HttpUrl.parse("https://httpbin.org/");

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public LambdaResponse handleRequest(LambdaRequest request, Context context) {
        switch (request.getHttpMethod().toUpperCase()) {
            case "GET":
                return getInformation(request);
            default:
                return new LambdaResponse(403, Collections.emptyMap(), "Forbidden");
        }
    }

    private LambdaResponse getInformation(LambdaRequest request) {
        try {
            Request httpRequest = new Request.Builder()
                    .url(httpUrl.newBuilder()
                            .addPathSegment("get")
                            .build())
                    .get()
                    .build();
            Response httpResponse = httpClient.newCall(httpRequest).execute();
            if (httpResponse.code() == 200) {
                Information information = objectMapper.readValue(httpResponse.body().byteStream(), informationReference);
                return new LambdaResponse(200, getFlatHeaders(httpResponse), objectMapper.writeValueAsString(information));
            } else {
                return new LambdaResponse(httpResponse.code(), getFlatHeaders(httpResponse), httpResponse.body().string());
            }
        } catch (IOException ex) {
            return new LambdaResponse(500, Collections.emptyMap(), ex.getMessage());
        }
    }
}
