package com.happysathya.core;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import okhttp3.Response;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CustomRequestHandler<I, O> implements RequestHandler<I, O> {

    protected Map<String, String> getFlatHeaders(Response httpResponse) {
        return httpResponse.headers()
                .toMultimap()
                .entrySet()
                .stream()
                .filter(entry -> !entry.getKey().toLowerCase().equals("content-encoding"))
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().stream().findFirst().orElse(null)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
