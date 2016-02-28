package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void save(Confirmation confirmation) throws IOException {
        savePdfConfirmation(confirmation);

        confirmations.computeIfAbsent(
                confirmation.getAllocationId(),
                key -> confirmation
        );
    }

    private static void savePdfConfirmation(Confirmation confirmation) throws IOException {

        Path confirmationPath = Files.write(
                buildConfirmationFilePath(confirmation),
                confirmation.getContent()
        );
        LOG.info("Confirmation content saved: " + confirmationPath.toAbsolutePath().toString());
    }

    private static Path buildConfirmationFilePath(Confirmation confirmation) throws IOException {
        Path confirmationsDir = Paths.get("confirmations");
        if (!Files.exists(confirmationsDir)) {
            Files.createDirectory(confirmationsDir);
        }

        return Paths.get("confirmations/Confirmation-" + confirmation.getAllocationId() + getFileType(confirmation.getConfirmationType()));
    }

    private static String getFileType(String confirmationType) {
        return "SWIFT".equals(confirmationType) ? ".swift" : ".pdf";
    }
}
