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
import java.time.*;
import java.time.format.DateTimeFormatter;
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

    public AllocationReport parse(String message) throws FixmlParserException {

        StringReader stringReader = new StringReader(message);

        try {
            Document fixmlMessage = SAX_BUILDER.build(stringReader);
            AllocationReport allocationReport = new AllocationReport();

            setId(fixmlMessage, allocationReport);
            setTransactionType(fixmlMessage, allocationReport);
            setSecurityId(fixmlMessage, allocationReport);
            setSecurityIdSource(fixmlMessage, allocationReport);
            setTradeSide(fixmlMessage, allocationReport);
            setQuantity(fixmlMessage, allocationReport);
            setStatus(fixmlMessage, allocationReport);
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
        Party executingParty = new Party();
        executingParty.setId(executingPartyId.get().getValue());
        allocationReport.setExecutingParty(executingParty);
    }

    private static void setCounterparty(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> counterpartyId = getAttribute(fixmlMessage, COUNTERPARTY_ID_XPATH);
        Party counterparty = new Party();
        counterparty.setId(counterpartyId.get().getValue());
        allocationReport.setCounterparty(counterparty);
    }

    private static void setExchange(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> exchangeMicCode = getAttribute(fixmlMessage, EXCHANGE_MIC_CODE_XPATH);
        Exchange exchange = new Exchange();
        exchange.setMic(exchangeMicCode.get().getValue());
        allocationReport.setExchange(exchange);
    }

    private static void setTradeDate(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> tradeDate = getAttribute(fixmlMessage, TRADE_DATE_XPATH);
        allocationReport.setTradeDate(deriveTradeDate(tradeDate));
    }

    private static ZonedDateTime deriveTradeDate(Optional<Attribute> tradeDate) {
        String value = tradeDate.get().getValue();

        LocalDate localDate = LocalDate.parse(
                value, DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        return localDate.atStartOfDay(ZoneId.of("GMT"));
    }

    private static void setPrice(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> price = getAttribute(fixmlMessage, PRICE_XPATH);
        allocationReport.setPrice(new BigDecimal(price.get().getValue()));
    }

    private static void setStatus(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> status = getAttribute(fixmlMessage, ALLOCATION_STATUS_XPATH);
        allocationReport.setStatus(deriveAllocationStatus(status));
    }

    private static void setQuantity(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException, DataConversionException {
        Optional<Attribute> quantity = getAttribute(fixmlMessage, QUANTITY_XPATH);
        allocationReport.setQuantity(quantity.get().getIntValue());
    }

    private static void setTradeSide(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> side = getAttribute(fixmlMessage, TRADE_SIDE_XPATH);
        allocationReport.setTradeSide(deriveTradeSide(side));
    }

    private static void setSecurityIdSource(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> instrumentIdSource = getAttribute(fixmlMessage, INSTRUMENT_ID_SOURCE_XPATH);
        allocationReport.setInstrumentType(deriveSecurityIdSource(instrumentIdSource));
    }

    private static void setSecurityId(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> instrumentId = getAttribute(fixmlMessage, INSTRUMENT_ID_XPATH);
        allocationReport.setSecurityId(instrumentId.get().getValue());
    }

    private static void setId(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> id = getAttribute(fixmlMessage, ALLOCATION_ID_XPATH);
        allocationReport.setAllocationId(id.get().getValue());
    }

    private static void setTransactionType(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> transactionType = getAttribute(fixmlMessage, TRANSACTION_TYPE_XPATH);
        allocationReport.setTransactionType(deriveTransactionType(transactionType));
    }

    private static TradeSide deriveTradeSide(Optional<Attribute> side) {
        String value = side.get().getValue();
        return TradeSide.getTradeSide(value);
    }

    private static AllocationStatus deriveAllocationStatus(Optional<Attribute> status) {
        String value = status.get().getValue();
        return AllocationStatus.getAllocationStatus(value);
    }

    private static InstrumentType deriveSecurityIdSource(Optional<Attribute> instrumentIdSource) {
        String value = instrumentIdSource.get().getValue();
        return InstrumentType.getInstrumentType(value);
    }

    private static TransactionType deriveTransactionType(Optional<Attribute> transactionType) {
        String value = transactionType.get().getValue();
        return TransactionType.getTransactionType(value);
    }

    @SuppressWarnings("all")
    private static Optional<Attribute> getAttribute(Document allocationMessage, String path) throws JaxenException {
        XPath xpath = new JDOMXPath(path);
        return xpath.selectNodes(allocationMessage).stream().findFirst();
    }
}
