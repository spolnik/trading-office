package com.trading;

class TestData {
    static OpenFigiResponse openFigiResponse() {
        OpenFigiResponse openFigiResponse = new OpenFigiResponse();

        openFigiResponse.setTicker("AMZN");
        openFigiResponse.setName("AMAZON.COM INC");
        openFigiResponse.setSecurityType("Common Stock");

        return openFigiResponse;
    }
}
