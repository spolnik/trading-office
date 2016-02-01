package com.trading;

import com.prowidesoftware.swift.io.ConversionService;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.field.Field11A;
import com.prowidesoftware.swift.model.mt.mt5xx.MT518;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SwiftConfirmationMessageListenerSpec {

    private SwiftConfirmationMessageListener listener;
    private Sender<Confirmation> confirmationSender;
    private ArgumentCaptor<Confirmation> argument;
    private ConversionService conversionService;
    private String json;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        argument = ArgumentCaptor.forClass(Confirmation.class);

        confirmationSender = mock(Sender.class);

        listener = new SwiftConfirmationMessageListener(confirmationSender);
        conversionService = new ConversionService();

        AllocationReport allocationReport = TestData.allocationReport(UUID.randomUUID().toString());
        json = objectMapper().toJson(allocationReport);
    }

    @Test
    public void generates_swift_confirmation_with_price_value() throws Exception {

        assertThat(new BigDecimal(confirmation().getField90A().get(0).getPrice())).isEqualTo(
                BigDecimal.valueOf(45.124)
        );
    }

    @Test
    public void generates_swift_confirmation_with_trade_transaction_type() throws Exception {

        assertThat(confirmation().getField22F().get(0).getIndicator()).isEqualTo(
                MT518.TRAD
        );
    }

    @Test
    public void generates_swift_confirmation_with_function_of_message_as_new() throws Exception {

        assertThat(confirmation().getField23G().getFunction()).isEqualTo(
                "NEWM"
        );
    }

    @Test
    public void generates_swift_confirmation_with_trade_date() throws Exception {

        assertThat(confirmation().getField98A().get(0).getDate()).isEqualTo(
                "20160603"
        );
    }

    @Test
    public void generates_swift_confirmation_with_buy_sell_indicator() throws Exception {

        assertThat(confirmation().getSequenceB().getFieldByName("22A").getComponent(1)).isEqualTo(
                ":BUSE/BUYI"
        );
    }

    @Test
    public void generates_swift_confirmation_with_currency() throws Exception {

        Field11A field11A = confirmation().getField11A().get(0);
        assertThat(field11A.getCurrency()).isEqualTo(
                "USD"
        );
    }

    private MT518 confirmation() throws IOException {
        listener.processEnrichedAllocationReport(json);
        verify(confirmationSender).send(argument.capture());

        String fin = new String(argument.getValue().getContent());
        SwiftMessage swiftMessage = conversionService.getMessageFromFIN(fin);

        return new MT518(swiftMessage);
    }
}
