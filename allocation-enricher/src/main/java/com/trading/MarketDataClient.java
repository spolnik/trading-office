package com.trading;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("MARKET-DATA-SERVICE")
interface MarketDataClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/instruments/sedol/{id}")
    InstrumentDetails getInstrumentDetails(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.GET, value = "/api/instrument/{symbol}")
    Instrument getInstrument(@PathVariable("symbol") String symbol);
}
