package com.trading;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class ConfirmationController {

    private final Map<String, Confirmation> confirmations = new ConcurrentHashMap<>();

    @RequestMapping("confirmation")
    public Confirmation getConfirmation(@RequestParam(value="id", required = true) String id) {
        return confirmations.getOrDefault(id, Confirmation.EMPTY_CONFIRMATION);
    }

    @RequestMapping(value = "confirmation", method = RequestMethod.POST)
    public ResponseEntity<?> addConfirmation(@RequestBody Confirmation confirmation) {
        confirmations.computeIfAbsent(
                confirmation.getAllocationReport().getAllocationId(),
                key -> confirmation
        );

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
