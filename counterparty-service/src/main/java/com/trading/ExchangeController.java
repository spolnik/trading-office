package com.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class ExchangeController {

    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeController(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @RequestMapping("exchange/mic/{micCode}")
    public Exchange getExchange(@PathVariable String micCode) {
        return exchangeRepository.getByMicCode(micCode);
    }
}
