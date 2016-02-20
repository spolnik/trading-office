package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileBasedConfirmationRepository implements ConfirmationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FileBasedConfirmationRepository.class);

    private final Map<String, Confirmation> confirmations = new ConcurrentHashMap<>();

    @Override
    public Confirmation queryById(String id) {
        return confirmations.getOrDefault(id, new Confirmation());
    }

    @Override
    public void save(Confirmation confirmation) {
        savePdfConfirmation(confirmation);

        confirmations.computeIfAbsent(
                confirmation.getAllocationId(),
                key -> confirmation
        );
    }

    private static void savePdfConfirmation(@RequestBody Confirmation confirmation) {
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

    private static Path buildConfirmationFilePath(@RequestBody Confirmation confirmation) {
        return Paths.get("confirmations/Confirmation-" + confirmation.getAllocationId() + ".pdf");
    }
}
