package com.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
class ConfirmationController {

    private final ConfirmationRepository confirmationRepository;

    @Autowired
    public ConfirmationController(ConfirmationRepository confirmationRepository) {
        this.confirmationRepository = confirmationRepository;
    }

    @RequestMapping("confirmation")
    public Confirmation getConfirmation(@RequestParam(value="id", required = true) String id) {
        return confirmationRepository.queryById(id);
    }

    @RequestMapping(value = "confirmation", method = RequestMethod.POST)
    public ResponseEntity addConfirmation(@RequestBody Confirmation confirmation) {

        confirmationRepository.save(confirmation);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
