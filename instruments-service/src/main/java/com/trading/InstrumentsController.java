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
class InstrumentsController {

    private final InstrumentsRepository instrumentsRepository;

    @Autowired
    public InstrumentsController(InstrumentsRepository instrumentsRepository) {

        this.instrumentsRepository = instrumentsRepository;
    }

    @ApiOperation(value = "getInstrumentDetails", nickname = "getInstrumentDetails", notes = "Example SEDOL: 2000019")
    @RequestMapping(value = "/instruments/sedol/{id}", method = RequestMethod.GET)
    @ApiParam(name = "id", example = "2000019", required = true)
    public InstrumentDetails getInstrumentDetailsBySedol(@PathVariable String id) {
        return instrumentsRepository.queryBySedol(id);
    }
}
