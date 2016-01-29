package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class OpenFigiInstrumentsRepository implements InstrumentsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(OpenFigiInstrumentsRepository.class);

    private static final MediaType TEXT_JSON = MediaType.parse("text/json");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public InstrumentDetails queryBySedol(String sedol) {

        String requestAsJson = request(sedol);

        try {
            String responseAsJson = post("https://api.openfigi.com/v1/mapping", requestAsJson);
            OpenFigiResponse response = parseResponse(responseAsJson);
            return response.toInstrumentDetails();
        } catch (IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        }

        return null;
    }

    private OpenFigiResponse parseResponse(String responseAsJson) throws IOException {
        LOG.info("Response: " + responseAsJson);

        OpenFigiDataResponse[] data = objectMapper.readValue(responseAsJson, OpenFigiDataResponse[].class);

        return data[0].getData()[0];
    }

    private String request(String sedol) {
        OpenFigiRequest request = new OpenFigiRequest();
        request.setIdType("ID_SEDOL");
        request.setIdValue(sedol);
        request.setExchCode("US");

        try {
            String requestAsJson = objectMapper.writeValueAsString(request);
            LOG.info("Request: " + requestAsJson);
            return requestAsJson;
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    private String post(String url, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(TEXT_JSON, wrapInArray(json));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static String wrapInArray(String json) {
        return String.format("[%s]", json);
    }
}
