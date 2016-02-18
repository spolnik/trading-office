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

    public AllocationReport parse(String message) throws FixmlParserException {

        StringReader stringReader = new StringReader(message);

        try {
            Document fixmlMessage = SAX_BUILDER.build(stringReader);
            AllocationReport allocationReport = new AllocationReport();

            setId(fixmlMessage, allocationReport);
            checkTransactionType(fixmlMessage);
            setSecurityId(fixmlMessage, allocationReport);
            checkSecurityIdSource(fixmlMessage);
            setTradeSide(fixmlMessage, allocationReport);
            setQuantity(fixmlMessage, allocationReport);
            checkStatus(fixmlMessage);
            setPrice(fixmlMessage, allocationReport);
            setTradeDate(fixmlMessage, allocationReport);
            setExchange(fixmlMessage, allocationReport);
            setCounterparty(fixmlMessage, allocationReport);
            setExecutingParty(fixmlMessage, allocationReport);

            LOG.info("Parsed: " + allocationReport);

            return allocationReport;
        } catch (JDOMException | IOException | JaxenException e) {
            LOG.error(e.getMessage(), e);
            throw new FixmlParserException(e);
        }
    }

    private static void setExecutingParty(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> executingPartyId = getAttribute(fixmlMessage, EXECUTING_PARTY_ID_XPATH);
        allocationReport.setExecutingPartyId(executingPartyId.get().getValue());
    }

    private static void setCounterparty(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> counterpartyId = getAttribute(fixmlMessage, COUNTERPARTY_ID_XPATH);
        allocationReport.setCounterpartyId(counterpartyId.get().getValue());
    }

    private static void setExchange(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> exchangeMicCode = getAttribute(fixmlMessage, EXCHANGE_MIC_CODE_XPATH);
        Exchange exchange = new Exchange();
        exchange.setMic(exchangeMicCode.get().getValue());
        allocationReport.setExchange(exchange);
    }

    private static void setTradeDate(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> tradeDate = getAttribute(fixmlMessage, TRADE_DATE_XPATH);
        allocationReport.setTradeDate(tradeDate.get().getValue());
    }

    private static void setPrice(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> price = getAttribute(fixmlMessage, PRICE_XPATH);
        allocationReport.setPrice(new BigDecimal(price.get().getValue()));
    }

    private static void checkStatus(Document fixmlMessage) throws JaxenException, FixmlParserException {
        Optional<Attribute> status = getAttribute(fixmlMessage, ALLOCATION_STATUS_XPATH);

        String value = status.get().getValue();

        if (!FIX_RECEIVED_ALLOCATION_STATUS.equals(value)) {
            throw new FixmlParserException("Only allocations with Received status are allowed");
        }
    }

    private static void setQuantity(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException, DataConversionException {
        Optional<Attribute> quantity = getAttribute(fixmlMessage, QUANTITY_XPATH);
        allocationReport.setQuantity(quantity.get().getIntValue());
    }

    private static void setTradeSide(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> side = getAttribute(fixmlMessage, TRADE_SIDE_XPATH);
        allocationReport.setTradeSide(deriveTradeSide(side));
    }

    private static void checkSecurityIdSource(Document fixmlMessage) throws JaxenException, FixmlParserException {
        Optional<Attribute> instrumentIdSource = getAttribute(fixmlMessage, INSTRUMENT_ID_SOURCE_XPATH);

        String value = instrumentIdSource.get().getValue();

        if (!FIX_SEDOL_SOURCE_ID.equals(value)) {
            throw new FixmlParserException("Only SEDOL security id is supported");
        }
    }

    private static void setSecurityId(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> instrumentId = getAttribute(fixmlMessage, INSTRUMENT_ID_XPATH);
        allocationReport.setSecurityId(instrumentId.get().getValue());
    }

    private static void setId(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> id = getAttribute(fixmlMessage, ALLOCATION_ID_XPATH);
        allocationReport.setAllocationId(id.get().getValue());
    }

    private static void checkTransactionType(Document fixmlMessage) throws JaxenException, FixmlParserException {
        Optional<Attribute> transactionType = getAttribute(fixmlMessage, TRANSACTION_TYPE_XPATH);

        String value = transactionType.get().getValue();

        if (!FIX_NEW_TRANSACTION_TYPE.equals(value)) {
            throw new FixmlParserException("Only NEW Transaction Type is allowed.");
        }
    }

    private static TradeSide deriveTradeSide(Optional<Attribute> side) {
        String value = side.get().getValue();
        return TradeSide.getTradeSide(value);
    }

    @SuppressWarnings("all")
    private static Optional<Attribute> getAttribute(Document allocationMessage, String path) throws JaxenException {
        XPath xpath = new JDOMXPath(path);
        return xpath.selectNodes(allocationMessage).stream().findFirst();
    }
}
