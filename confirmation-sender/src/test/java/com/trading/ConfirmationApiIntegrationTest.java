package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationApiIntegrationTest {

    private final ConfirmationApi confirmationApi = new ConfirmationApiClient("http://confirmation-service.herokuapp.com");

    @Test
    public void returns_email_confirmation_type_for_non_london_stock_exchange_mic_code() throws Exception {
        ConfirmationType confirmationType = confirmationApi.confirmationTypeFor("XNAS");

        assertThat(confirmationType).isEqualTo(ConfirmationType.EMAIL);
    }

    @Test
    public void returns_swift_confirmation_type_for_london_stock_exchange_mic_code() throws Exception {
        ConfirmationType confirmationType = confirmationApi.confirmationTypeFor("XLON");

        assertThat(confirmationType).isEqualTo(ConfirmationType.SWIFT);
    }
}