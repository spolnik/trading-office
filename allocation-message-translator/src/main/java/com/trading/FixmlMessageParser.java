package com.trading;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

class FixmlMessageParser {

    private static final Logger LOG = LoggerFactory.getLogger(FixmlMessageParser.class);

    private static final SAXBuilder SAX_BUILDER = new SAXBuilder();

    private static final String TRANSACTION_TYPE_XPATH = "/FIXML/AllocRpt/@TransTyp";
    private static final String ALLOCATION_ID_XPATH = "/FIXML/AllocRpt/@RptID";
    private static final String INSTRUMENT_ID_XPATH = "/FIXML/AllocRpt/Instrmt/@ID";
    private static final String INSTRUMENT_ID_SOURCE_XPATH = "/FIXML/AllocRpt/Instrmt/@Src";

    public AllocationReport parse(String message) throws FixmlParserException {

        StringReader stringReader = new StringReader(message);

        try {
            Document allocationMessage = SAX_BUILDER.build(stringReader);
            AllocationReport allocationReport = new AllocationReport();

            setId(allocationMessage, allocationReport);
            setTransactionType(allocationMessage, allocationReport);
            setSecurityId(allocationMessage, allocationReport);
            setSecurityIdSource(allocationMessage, allocationReport);

            LOG.info("Parsed: " + allocationReport);

            return allocationReport;
        } catch (JDOMException | IOException | JaxenException e) {
            LOG.error(e.getMessage(), e);
            throw new FixmlParserException(e);
        }
    }

    private void setSecurityIdSource(Document allocationMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> instrumentIdSource = getElement(allocationMessage, INSTRUMENT_ID_SOURCE_XPATH);
        allocationReport.setSecurityIdSource(deriveSecurityIdSource(instrumentIdSource));
    }

    private void setSecurityId(Document allocationMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> instrumentId = getElement(allocationMessage, INSTRUMENT_ID_XPATH);
        allocationReport.setSecurityId(instrumentId.get().getValue());
    }


    private void setId(Document allocationMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> id = getElement(allocationMessage, ALLOCATION_ID_XPATH);
        allocationReport.setAllocationId(id.get().getValue());
    }

    private void setTransactionType(Document allocationMessage, AllocationReport allocationReport) throws JaxenException {
        Optional<Attribute> transactionType = getElement(allocationMessage, TRANSACTION_TYPE_XPATH);
        allocationReport.setTransactionType(deriveTransactionType(transactionType));
    }

    private static SecurityIDSource deriveSecurityIdSource(Optional<Attribute> instrumentIdSource) {
        String value = instrumentIdSource.get().getValue();

        if ("2".equals(value)) {
            return SecurityIDSource.SEDOL;
        }

        throw new UnsupportedOperationException(
                "Instrument ID Source is unsupported: " + instrumentIdSource
        );
    }

    private static TransactionType deriveTransactionType(Optional<Attribute> transactionType) {
        String value = transactionType.get().getValue();

        if ("0".equals(value)) {
            return TransactionType.NEW;
        }

        throw new UnsupportedOperationException(
                "Transaction type is unsupported: " + transactionType
        );
    }

    @SuppressWarnings("all")
    private Optional<Attribute> getElement(Document allocationMessage, String path) throws JaxenException {
        XPath xpath = new JDOMXPath(path);
        return xpath.selectNodes(allocationMessage).stream().findFirst();
    }
}
