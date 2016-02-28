package com.trading;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Optional;

class FixmlMessageParser {

    private static final Logger LOG = LoggerFactory.getLogger(FixmlMessageParser.class);

    private static final SAXBuilder SAX_BUILDER = new SAXBuilder();

    private static final String TRANSACTION_TYPE_XPATH = "/FIXML/AllocRpt/@TransTyp";
    private static final String ALLOCATION_ID_XPATH = "/FIXML/AllocRpt/@RptID";
    private static final String INSTRUMENT_ID_XPATH = "/FIXML/AllocRpt/Instrmt/@ID";
    private static final String INSTRUMENT_ID_SOURCE_XPATH = "/FIXML/AllocRpt/Instrmt/@Src";
    private static final String TRADE_SIDE_XPATH = "/FIXML/AllocRpt/@Side";
    private static final String QUANTITY_XPATH = "/FIXML/AllocRpt/@Qty";
    private static final String ALLOCATION_STATUS_XPATH = "/FIXML/AllocRpt/@Stat";
    private static final String PRICE_XPATH = "/FIXML/AllocRpt/@AvgPx";
    private static final String TRADE_DATE_XPATH = "/FIXML/AllocRpt/@TrdDt";
    private static final String EXCHANGE_MIC_CODE_XPATH = "/FIXML/AllocRpt/Pty[@R=\"22\" and @Src=\"G\"]/@ID";
    private static final String COUNTERPARTY_ID_XPATH = "/FIXML/AllocRpt/Pty[@R=\"3\" and @Src=\"D\"]/@ID";
    private static final String EXECUTING_PARTY_ID_XPATH = "/FIXML/AllocRpt/Pty[@R=\"1\" and @Src=\"D\"]/@ID";
    private static final String FIX_SEDOL_SOURCE_ID = "2";
    private static final String FIX_RECEIVED_ALLOCATION_STATUS = "3";
    private static final String FIX_NEW_TRANSACTION_TYPE = "0";
    private static final String FIX_TRADE_SIDE_SELL = "2";
    private static final String FIX_TRADE_SIDE_BUY = "1";

    public Allocation parse(String message) throws FixmlParserException {

        StringReader stringReader = new StringReader(message);

        try {
            Document fixmlMessage = SAX_BUILDER.build(stringReader);
            Allocation allocation = new Allocation();

            setId(fixmlMessage, allocation);
            checkTransactionType(fixmlMessage);
            setSecurityId(fixmlMessage, allocation);
            checkSecurityIdSource(fixmlMessage);
            setTradeSide(fixmlMessage, allocation);
            setQuantity(fixmlMessage, allocation);
            checkStatus(fixmlMessage);
            setPrice(fixmlMessage, allocation);
            setTradeDate(fixmlMessage, allocation);
            setExchange(fixmlMessage, allocation);
            setCounterparty(fixmlMessage, allocation);
            setExecutingParty(fixmlMessage, allocation);

            LOG.info("Parsed: " + allocation);

            return allocation;
        } catch (JDOMException | IOException | JaxenException e) {
            LOG.error(e.getMessage(), e);
            throw new FixmlParserException(e);
        }
    }

    private static void setExecutingParty(Document fixmlMessage, Allocation allocation) throws JaxenException {
        Optional<Attribute> executingPartyId = getAttribute(fixmlMessage, EXECUTING_PARTY_ID_XPATH);
        allocation.setExecutingPartyId(executingPartyId.get().getValue());
    }

    private static void setCounterparty(Document fixmlMessage, Allocation allocation) throws JaxenException {
        Optional<Attribute> counterpartyId = getAttribute(fixmlMessage, COUNTERPARTY_ID_XPATH);
        allocation.setCounterpartyId(counterpartyId.get().getValue());
    }

    private static void setExchange(Document fixmlMessage, Allocation allocation) throws JaxenException {
        Optional<Attribute> exchangeMicCode = getAttribute(fixmlMessage, EXCHANGE_MIC_CODE_XPATH);
        allocation.setMicCode(exchangeMicCode.get().getValue());
    }

    private static void setTradeDate(Document fixmlMessage, Allocation allocation) throws JaxenException {
        Optional<Attribute> tradeDate = getAttribute(fixmlMessage, TRADE_DATE_XPATH);
        allocation.setTradeDate(tradeDate.get().getValue());
    }

    private static void setPrice(Document fixmlMessage, Allocation allocation) throws JaxenException {
        Optional<Attribute> price = getAttribute(fixmlMessage, PRICE_XPATH);
        allocation.setPrice(new BigDecimal(price.get().getValue()));
    }

    private static void checkStatus(Document fixmlMessage) throws JaxenException, FixmlParserException {
        Optional<Attribute> status = getAttribute(fixmlMessage, ALLOCATION_STATUS_XPATH);

        String value = status.get().getValue();

        if (!FIX_RECEIVED_ALLOCATION_STATUS.equals(value)) {
            throw new FixmlParserException("Only allocations with Received status are allowed");
        }
    }

    private static void setQuantity(Document fixmlMessage, Allocation allocation) throws JaxenException, DataConversionException {
        Optional<Attribute> quantity = getAttribute(fixmlMessage, QUANTITY_XPATH);
        allocation.setQuantity(quantity.get().getIntValue());
    }

    private static void setTradeSide(Document fixmlMessage, Allocation allocation) throws JaxenException {
        Optional<Attribute> side = getAttribute(fixmlMessage, TRADE_SIDE_XPATH);
        allocation.setTradeSide(deriveTradeSide(side));
    }

    private static void checkSecurityIdSource(Document fixmlMessage) throws JaxenException, FixmlParserException {
        Optional<Attribute> instrumentIdSource = getAttribute(fixmlMessage, INSTRUMENT_ID_SOURCE_XPATH);

        String value = instrumentIdSource.get().getValue();

        if (!FIX_SEDOL_SOURCE_ID.equals(value)) {
            throw new FixmlParserException("Only SEDOL security id is supported");
        }
    }

    private static void setSecurityId(Document fixmlMessage, Allocation allocation) throws JaxenException {
        Optional<Attribute> instrumentId = getAttribute(fixmlMessage, INSTRUMENT_ID_XPATH);
        allocation.setSecurityId(instrumentId.get().getValue());
    }

    private static void setId(Document fixmlMessage, Allocation allocation) throws JaxenException {
        Optional<Attribute> id = getAttribute(fixmlMessage, ALLOCATION_ID_XPATH);
        allocation.setAllocationId(id.get().getValue());
    }

    private static void checkTransactionType(Document fixmlMessage) throws JaxenException, FixmlParserException {
        Optional<Attribute> transactionType = getAttribute(fixmlMessage, TRANSACTION_TYPE_XPATH);

        String value = transactionType.get().getValue();

        if (!FIX_NEW_TRANSACTION_TYPE.equals(value)) {
            throw new FixmlParserException("Only NEW Transaction Type is allowed.");
        }
    }

    private static String deriveTradeSide(Optional<Attribute> side) {
        String value = side.get().getValue();

        if (FIX_TRADE_SIDE_BUY.equals(value)) {
            return Allocation.BUY;
        } else if (FIX_TRADE_SIDE_SELL.equals(value)){
            return Allocation.SELL;
        }

        throw new UnsupportedOperationException("Trade Side is unsupported: " + value);
    }

    @SuppressWarnings("all")
    private static Optional<Attribute> getAttribute(Document allocationMessage, String path) throws JaxenException {
        XPath xpath = new JDOMXPath(path);
        return xpath.selectNodes(allocationMessage).stream().findFirst();
    }
}
