package com.trading;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("COUNTERPARTY-SERVICE")
interface CounterpartyClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/exchange/mic/{micCode}")
    Exchange getExchange(@PathVariable("micCode") String micCode);

    @RequestMapping(method = RequestMethod.GET, value = "/api/party/{id}")
    String getPartyName(@PathVariable("id") String id);
}
