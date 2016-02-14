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
class CounterpartyController {

    private final PartyRepository partyRepository;

    @Autowired
    public CounterpartyController(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @ApiOperation(value = "getParty", nickname = "getParty", notes = "Example: CUSTUK, TROF")
    @RequestMapping(value = "/party/{id}", method = RequestMethod.GET)
    @ApiParam(name = "id", example = "CUSTUK", required = true)
    public Party getParty(@PathVariable String id) {
        return partyRepository.getById(id);
    }
}
