package com.trading;

public class TestData {
    static InstrumentDetails instrumentDetails() {
        InstrumentDetails instrumentDetails = new InstrumentDetails();

        instrumentDetails.setTicker("AMZN");
        instrumentDetails.setName("AMAZON.COM INC");
        instrumentDetails.setSecurityType("Common Stock");

        return instrumentDetails;
    }
}
