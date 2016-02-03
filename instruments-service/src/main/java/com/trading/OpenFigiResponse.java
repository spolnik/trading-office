package com.trading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFigiResponse implements Serializable {

    private String ticker;
    private String name;
    private String securityType;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public InstrumentDetails toInstrumentDetails() {
        InstrumentDetails instrumentDetails = new InstrumentDetails();

        instrumentDetails.setName(getName());
        instrumentDetails.setTicker(getTicker());
        instrumentDetails.setSecurityType(getSecurityType());

        return instrumentDetails;
    }
}
