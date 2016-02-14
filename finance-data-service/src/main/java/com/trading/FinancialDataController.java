package com.trading;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class FinancialDataController {

    private final YahooApi yahooApi;

    @Autowired
    public FinancialDataController(YahooApi yahooApi) {
        this.yahooApi = yahooApi;
    }

    @ApiOperation(value = "getInstrument", nickname = "getInstrument", notes = "Example: AMZN")
    @RequestMapping(value = "instrument/{symbol}", method = RequestMethod.GET)
    @ApiParam(name = "symbol", example = "AMZN", required = true)
    public Instrument getInstrument(@PathVariable String symbol) {
        return yahooApi.getInstrument(symbol);
    }
}
