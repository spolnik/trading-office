package com.trading;

import com.prowidesoftware.swift.io.ConversionService;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.field.Field11A;
import com.prowidesoftware.swift.model.mt.mt5xx.MT518;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SwiftConfirmationParserSpec {

    private ConversionService conversionService;
    private ConfirmationParser confirmationParser;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        confirmationParser = new SwiftConfirmationParser();
        conversionService = new ConversionService();
    }

    @Test
    public void generates_swift_confirmation_with_price_value() throws Exception {

        assertThat(new BigDecimal(confirmation_for_buy().getField90A().get(0).getPrice())).isEqualTo(
                BigDecimal.valueOf(45.124)
        );
    }

    @Test
    public void generates_swift_confirmation_with_trade_transaction_type() throws Exception {

        assertThat(confirmation_for_buy().getField22F().get(0).getIndicator()).isEqualTo(
                MT518.TRAD
        );
    }

    @Test
    public void generates_swift_confirmation_with_function_of_message_as_new() throws Exception {

        assertThat(confirmation_for_buy().getField23G().getFunction()).isEqualTo(
                "NEWM"
        );
    }

    @Test
    public void generates_swift_confirmation_with_trade_date() throws Exception {

        assertThat(confirmation_for_buy().getField98A().get(0).getDate()).isEqualTo(
                "20160603"
        );
    }

    @Test
    public void generates_swift_confirmation_with_buyi_indicator_for_buy_trade_side() throws Exception {

        assertThat(confirmation_for_buy().getSequenceB().getFieldByName("22A").getComponent(1)).isEqualTo(
                ":BUSE/BUYI"
        );
    }

    @Test
    public void generates_swift_confirmation_with_sell_indicator_for_sell_trade_side() throws Exception {

        assertThat(confirmation_for_sell().getSequenceB().getFieldByName("22A").getComponent(1)).isEqualTo(
                ":BUSE/SELL"
        );
    }

    @Test
    public void generates_swift_confirmation_with_currency() throws Exception {

        Field11A field11A = confirmation_for_buy().getField11A().get(0);
        assertThat(field11A.getCurrency()).isEqualTo(
                "USD"
        );
    }

    @Test
    public void generates_swift_confirmation_with_quantity() throws Exception {

        assertThat(confirmation_for_buy().getField36B().get(0).getQuantity()).isEqualTo(
                "1234"
        );
    }

    @Test
    public void generates_swift_confirmation_with_instrument_description() throws Exception {

        assertThat(confirmation_for_buy().getField35B().get(0).getDescription()).isEqualTo(
                "AMAZON STOCKS"
        );
    }

    @Test
    public void generates_swift_confirmation_with_currency_qualifier_for_buy() throws Exception {
        Field11A field11A = confirmation_for_buy().getField11A().get(0);
        assertThat(field11A.getQualifier()).isEqualTo(
                MT518.FXIB
        );
    }

    @Test
    public void generates_swift_confirmation_with_currency_qualifier_for_sell() throws Exception {
        Field11A field11A = confirmation_for_sell().getField11A().get(0);
        assertThat(field11A.getQualifier()).isEqualTo(
                MT518.FXIS
        );
    }

    private MT518 confirmation_for_buy() throws IOException {
        Confirmation allocationReportBuy = TestData.confirmation(
                UUID.randomUUID().toString(), "BUY"
        );
        return mt518(allocationReportBuy);
    }

    private MT518 confirmation_for_sell() throws IOException {
        Confirmation allocationReportSell = TestData.confirmation(
                UUID.randomUUID().toString(), "SELL"
        );
        return mt518(allocationReportSell);
    }

    private MT518 mt518(Confirmation allocationReportSell) throws IOException {


        Optional<Confirmation> confirmation = confirmationParser.parse(allocationReportSell);

        String fin = new String(confirmation.get().getContent(), StandardCharsets.UTF_8);
        SwiftMessage swiftMessage = conversionService.getMessageFromFIN(fin);

        return new MT518(swiftMessage);
    }
}
