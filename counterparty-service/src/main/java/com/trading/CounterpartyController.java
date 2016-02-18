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

    @ApiOperation(value = "getPartyName", nickname = "getPartyName")
    @RequestMapping(value = "party/{id}", method = RequestMethod.GET)
    public String getPartyName(
            @ApiParam(name = "id", value = "Party id (Custom)", defaultValue = "CUSTUK", required = true)
            @PathVariable String id
    ) {
        return partyRepository.getName(id);
    }
}
