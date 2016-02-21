package com.trading;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllocationMessageReceiverListenerSpec {


    private static final String DUMMY = "DUMMY";
    private AllocationMessageReceiverListener listener;
    private FixmlMessageParser parser;

    @Before
    public void setUp() throws Exception {
        parser = mock(FixmlMessageParser.class);

        Allocation allocation = new Allocation();
        allocation.setAllocationId(DUMMY);

        when(parser.parse(DUMMY)).thenReturn(allocation);

        listener = new AllocationMessageReceiverListener(parser);
    }

    @Test
    public void uses_parser_to_parse_item() throws Exception {

        listener.processAllocationReport(DUMMY);
        verify(parser).parse(DUMMY);
    }
}