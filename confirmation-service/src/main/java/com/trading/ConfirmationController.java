package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class ConfirmationController {

    private final static Logger LOG = LoggerFactory.getLogger(ConfirmationController.class);

    private final Map<String, Confirmation> confirmations = new ConcurrentHashMap<>();

    @RequestMapping("confirmation")
    public Confirmation getConfirmation(@RequestParam(value="id", required = true) String id) {
        return confirmations.getOrDefault(id, Confirmation.EMPTY_CONFIRMATION);
    }

    @RequestMapping(value = "confirmation", method = RequestMethod.POST)
    public ResponseEntity<?> addConfirmation(@RequestBody Confirmation confirmation) {

        savePdfConfirmation(confirmation);

        confirmations.computeIfAbsent(
                confirmation.getAllocationReport().getAllocationId(),
                key -> confirmation
        );

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    private void savePdfConfirmation(@RequestBody Confirmation confirmation) {
        try {
            Path confirmationPath = Files.write(
                    buildConfirmationFilePath(confirmation),
                    confirmation.getContent()
            );
            LOG.info("Confirmation PDF saved: " + confirmationPath.toAbsolutePath().toString());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private Path buildConfirmationFilePath(@RequestBody Confirmation confirmation) {
        return Paths.get("confirmations/Confirmation-" + confirmation.id() + ".pdf");
    }
}
