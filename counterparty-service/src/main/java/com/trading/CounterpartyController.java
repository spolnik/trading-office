package com.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class CounterpartyController {

    private final PartyRepository partyRepository;

    @Autowired
    public CounterpartyController(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @RequestMapping("party/{id}")
    public Party getCounterparty(@PathVariable String id) {
        return partyRepository.getById(id);
    }
}
