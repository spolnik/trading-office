package com.trading;

import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FixmlAllocationMessageReceiverSpec {

    private static final String DUMMY = "DUMMY";

    private Allocation allocation = new Allocation();

    private FixmlMessageParser parser = mock(FixmlMessageParser.class);
    private AmqpTemplate amqpTemplate = mock(AmqpTemplate.class);

    private FixmlAllocationMessageReceiver receiver;

    @Before
    public void setUp() throws Exception {

        when(parser.parse(DUMMY)).thenReturn(allocation);

        receiver = new FixmlAllocationMessageReceiver(amqpTemplate, parser);
    }

    @Test
    public void uses_parser_to_transform_incoming_fixml_message_to_allocation() throws Exception {

        receiver.handleMessage(DUMMY);
        verify(parser).parse(DUMMY);
    }

    @Test
    public void sends_transformed_allocation_using_amqp_template() throws Exception {

        receiver.handleMessage(DUMMY);
        verify(amqpTemplate).convertAndSend(
                "trading-office-exchange", "received.json.allocation.report", TestData.emptyAllocationAsJson()
        );
    }
}