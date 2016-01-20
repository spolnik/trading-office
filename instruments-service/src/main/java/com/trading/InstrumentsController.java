package com.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InstrumentsController {

    private final InstrumentsRepository instrumentsRepository;

    @Autowired
    public InstrumentsController(InstrumentsRepository instrumentsRepository) {

        this.instrumentsRepository = instrumentsRepository;
    }

    @RequestMapping("/instruments")
    public List<InstrumentDetails> getAll() {
        return instrumentsRepository.getAll();
    }

    @RequestMapping("/instruments/sedol/{id}")
    public InstrumentDetails getBySedol(@PathVariable String id) {
        return instrumentsRepository.queryBySedol(id);
    }
}
