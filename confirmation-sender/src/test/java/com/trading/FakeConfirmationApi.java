package com.trading;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class FakeConfirmationApi implements ConfirmationApi {

    private static final String LONDON_STOCK_EXCHANGE_MIC_CODE = "XLON";

    @Override
    public ConfirmationType confirmationTypeFor(String micCode) {
        return LONDON_STOCK_EXCHANGE_MIC_CODE.equals(micCode)
                ? ConfirmationType.SWIFT
                : ConfirmationType.EMAIL;
    }
}
