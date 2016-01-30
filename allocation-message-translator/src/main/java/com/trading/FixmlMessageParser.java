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

            LOG.info("Parsed: " + allocationReport);

            return allocationReport;
        } catch (JDOMException | IOException | JaxenException e) {
            LOG.error(e.getMessage(), e);
            throw new FixmlParserException(e);
        }
    }

    private void setPrice(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> price = getElement(fixmlMessage, PRICE_XPATH);
        allocationReport.setPrice(new BigDecimal(price.get().getValue()));
    }

    private void setStatus(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> status = getElement(fixmlMessage, ALLOCATION_STATUS_XPATH);
        allocationReport.setStatus(deriveAllocationStatus(status));
    }

    private static void setQuantity(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException, DataConversionException {
        Optional<Attribute> quantity = getElement(fixmlMessage, QUANTITY_XPATH);
        allocationReport.setQuantity(quantity.get().getIntValue());
    }

    private static void setTradeSide(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> side = getElement(fixmlMessage, TRADE_SIDE_XPATH);
        allocationReport.setTradeSide(deriveTradeSide(side));
    }

    private static void setSecurityIdSource(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> instrumentIdSource = getElement(fixmlMessage, INSTRUMENT_ID_SOURCE_XPATH);
        allocationReport.setSecurityIdSource(deriveSecurityIdSource(instrumentIdSource));
    }

    private static void setSecurityId(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> instrumentId = getElement(fixmlMessage, INSTRUMENT_ID_XPATH);
        allocationReport.setSecurityId(instrumentId.get().getValue());
    }

    private static void setId(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> id = getElement(fixmlMessage, ALLOCATION_ID_XPATH);
        allocationReport.setAllocationId(id.get().getValue());
    }

    private static void setTransactionType(Document fixmlMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> transactionType = getElement(fixmlMessage, TRANSACTION_TYPE_XPATH);
        allocationReport.setTransactionType(deriveTransactionType(transactionType));
    }

    private static TradeSide deriveTradeSide(Optional<Attribute> side) {
        String value = side.get().getValue();

        switch (value) {
            case "1":
                return TradeSide.BUY;
            case "2":
                return TradeSide.SELL;
            default:
                throw new UnsupportedOperationException(
                        "Trade Side is unsupported: " + value
                );
        }
    }

    private AllocationStatus deriveAllocationStatus(Optional<Attribute> status) {
        String value = status.get().getValue();

        switch (value) {
            case "0":
                return AllocationStatus.ACCEPTED;
            case "3":
                return AllocationStatus.RECEIVED;
            case "6":
                return AllocationStatus.ALLOCATION_PENDING;
            default:
                throw new UnsupportedOperationException(
                        "Allocation Status is unsupported: " + value
                );
        }
    }

    private static SecurityIDSource deriveSecurityIdSource(Optional<Attribute> instrumentIdSource) {
        String value = instrumentIdSource.get().getValue();

        if ("2".equals(value)) {
            return SecurityIDSource.SEDOL;
        }

        throw new UnsupportedOperationException(
                "Instrument ID Source is unsupported: " + value
        );
    }

    private static TransactionType deriveTransactionType(Optional<Attribute> transactionType) {
        String value = transactionType.get().getValue();

        if ("0".equals(value)) {
            return TransactionType.NEW;
        }

        throw new UnsupportedOperationException(
                "Transaction type is unsupported: " + value
        );
    }

    @SuppressWarnings("all")
    private static Optional<Attribute> getElement(Document allocationMessage, String path) throws JaxenException {
        XPath xpath = new JDOMXPath(path);
        return xpath.selectNodes(allocationMessage).stream().findFirst();
    }
}
