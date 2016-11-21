package com.cci;

import com.amazonaws.services.lambda.runtime.Context;
import com.cci.handler.CCIRequestHandler;
import com.cci.model.CCI_Order;
import com.cci.model.LambdaFunctionRequest;
import com.cci.model.LambdaFunctionResponse;
import com.cci.model.od.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class OrderResource extends CCIRequestHandler<LambdaFunctionRequest, LambdaFunctionResponse> {

    private static OkHttpClient httpClient = new OkHttpClient();
    private static TypeReference<List<CCI_Order>> listOrderReference = new TypeReference<List<CCI_Order>>() {
    };
    private static TypeReference<CCI_Order> orderReference = new TypeReference<CCI_Order>() {
    };
    private static TypeReference<Order> odOrderReference = new TypeReference<Order>() {
    };
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static HttpUrl httpUrl = HttpUrl.parse("http://cci-uat-services.northeurope.cloudapp.azure.com:7004/");

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static Order mapOrder(CCI_Order cci_order) {
        Order od_order = new Order();
        od_order.setBrand(cci_order.getBrandName());
        od_order.setOrderStatus(getOrderStatus(cci_order.getOrderStatus()));
        return od_order;
    }

    private static Order.OrderStatusEnum getOrderStatus(String orderStatus) {
        switch (orderStatus.toUpperCase()) {
            case "DRAFT":
                return Order.OrderStatusEnum.PENDING;
            case "APPROVED":
                return Order.OrderStatusEnum.APPROVED;
            default:
                return Order.OrderStatusEnum.REJECTED;
        }
    }

    @Override
    public LambdaFunctionResponse handleRequest(LambdaFunctionRequest request, Context context) {
        switch (request.getHttpMethod().toUpperCase()) {
            case "GET":
                return getOrders(request);
            case "POST":
                return postOrder(request);
            default:
                return new LambdaFunctionResponse(403, Collections.emptyMap(), "Forbidden");
        }
    }

    private LambdaFunctionResponse getOrders(LambdaFunctionRequest request) {
        try {
            Request httpRequest = new Request.Builder()
                    .url(httpUrl.newBuilder()
                            .addPathSegment("customers")
                            .addPathSegment(request.getPathParameters().getOrDefault("accountId", "invalidAccountId"))
                            .addPathSegment("orders").build())
                    .headers(Headers.of(request.getHeaders()))
                    .addHeader("Accept-Encoding", "gzip")
                    .get()
                    .build();
            Response httpResponse = httpClient.newCall(httpRequest).execute();
            if (httpResponse.code() == 200) {
                List<CCI_Order> cci_orders = objectMapper.readValue(new GZIPInputStream(httpResponse.body().byteStream()), listOrderReference);
                List<Order> od_orders = cci_orders.stream().map(OrderResource::mapOrder).collect(Collectors.toList());
                return new LambdaFunctionResponse(200, getFlatHeaders(httpResponse), objectMapper.writeValueAsString(od_orders));
            } else {
                return new LambdaFunctionResponse(httpResponse.code(), getFlatHeaders(httpResponse), httpResponse.body().string());
            }
        } catch (IOException ex) {
            return new LambdaFunctionResponse(500, Collections.emptyMap(), ex.getMessage());
        }
    }

    private LambdaFunctionResponse postOrder(LambdaFunctionRequest request) {
        try {
            Order order = objectMapper.readValue(request.getBody(), odOrderReference);
            CCI_Order cci_order = new CCI_Order();
            cci_order.setAdvertiserOrgId(order.getAccountId());
            cci_order.setBrandId(order.getBrand());
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(cci_order));
            Request httpRequest = new Request.Builder()
                    .url(httpUrl.newBuilder()
                            .addPathSegment("customers")
                            .addPathSegment(request.getPathParameters().getOrDefault("accountId", "invalidAccountId"))
                            .addPathSegment("orders").build())
                    .headers(Headers.of(request.getHeaders()))
                    .addHeader("Accept-Encoding", "gzip")
                    .post(requestBody)
                    .build();
            Response httpResponse = httpClient.newCall(httpRequest).execute();
            if (httpResponse.code() == 200) {
                CCI_Order createdOrder = objectMapper.readValue(httpResponse.body().byteStream(), orderReference);
                return new LambdaFunctionResponse(200, getFlatHeaders(httpResponse), objectMapper.writeValueAsString(mapOrder(createdOrder)));
            } else {
                return new LambdaFunctionResponse(httpResponse.code(), getFlatHeaders(httpResponse), httpResponse.body().string());
            }
        } catch (IOException ex) {
            return new LambdaFunctionResponse(500, Collections.emptyMap(), ex.getMessage());
        }
    }
}
