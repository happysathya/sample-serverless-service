package com.cci;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cci.model.LambdaFunctionRequest;
import com.cci.model.LambdaFunctionResponse;
import com.cci.model.data.CCI_Order;
import com.cci.model.data.OD_Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class GetOrders implements RequestHandler<LambdaFunctionRequest, LambdaFunctionResponse> {

    private static OkHttpClient httpClient = new OkHttpClient();
    private static TypeReference<List<CCI_Order>> listOrderReference = new TypeReference<List<CCI_Order>>() {
    };
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static HttpUrl httpUrl = HttpUrl.parse("http://cci-uat-services.northeurope.cloudapp.azure.com:7004/");

    private static OD_Order mapOrder(CCI_Order cci_order) {
        OD_Order od_order = new OD_Order();
        od_order.setBrand(cci_order.getBrandName());
        od_order.setOrderStatus(cci_order.getOrderStatus());
        return od_order;
    }

    @Override
    public LambdaFunctionResponse handleRequest(LambdaFunctionRequest request, Context context) {
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
                List<OD_Order> od_orders = cci_orders.stream().map(GetOrders::mapOrder).collect(Collectors.toList());
                return new LambdaFunctionResponse(200, getFlatHeaders(httpResponse), objectMapper.writeValueAsString(od_orders));
            } else {
                return new LambdaFunctionResponse(httpResponse.code(), getFlatHeaders(httpResponse), httpResponse.body().string());
            }
        } catch (IOException ex) {
            return new LambdaFunctionResponse(500, Collections.emptyMap(), ex.getMessage());
        }
    }

    private Map<String, String> getFlatHeaders(Response httpResponse) {
        return httpResponse.headers()
                .toMultimap()
                .entrySet()
                .stream()
                .filter(entry -> !entry.getKey().toLowerCase().equals("content-encoding"))
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().stream().findFirst().orElse(null)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
