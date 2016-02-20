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
class MarketDataController {

    private final YahooApi yahooApi;

    @Autowired
    public MarketDataController(YahooApi yahooApi) {
        this.yahooApi = yahooApi;
    }

    @ApiOperation(value = "getInstrument", nickname = "getInstrument")
    @RequestMapping(value = "instrument/{symbol}", method = RequestMethod.GET)
    public Instrument getInstrument(
            @ApiParam(name = "symbol", value = "Instrument symbol", defaultValue = "AMZN", required = true)
            @PathVariable String symbol
    ) {
        return yahooApi.getInstrument(symbol);
    }
}
