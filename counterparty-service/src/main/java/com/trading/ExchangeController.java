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
class ExchangeController {

    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeController(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @ApiOperation(value = "getExchange", nickname = "getExchange", notes = "Example: XNAS, XLON")
    @RequestMapping(value = "exchange/mic/{micCode}", method = RequestMethod.GET)
    @ApiParam(name = "micCode", required = true)
    public Exchange getExchange(@PathVariable String micCode) {
        return exchangeRepository.getByMicCode(micCode);
    }
}
