package com.trading;

public class InstrumentDetails {

    private String ticker;
    private String cusip;
    private String sedol;
    private String name;
    private String ric;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCusip() {
        return cusip;
    }

    public void setCusip(String cusip) {
        this.cusip = cusip;
    }

    public String getSedol() {
        return sedol;
    }

    public void setSedol(String sedol) {
        this.sedol = sedol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRic() {
        return ric;
    }

    public void setRic(String ric) {
        this.ric = ric;
    }

    @Override
    public String toString() {
        return String.format(
                "InstrumentDetails{ticker='%s', cusip='%s', sedol='%s', name='%s', ric='%s'}",
                ticker, cusip, sedol, name, ric
        );
    }

    public static InstrumentDetails empty() {
        return new InstrumentDetails();
    }
}
