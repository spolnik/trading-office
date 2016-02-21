package com.trading;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationRestServiceIntegrationTest {

    private static final String DUMMY_CONFIRMATION = "dummy confirmation";
    private RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void setUp() throws Exception {
        ConfirmationServiceApplication.main(new String[0]);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        File confirmationsDir = Paths.get("confirmations").toFile();

        if (confirmationsDir == null) {
            return;
        }

        File[] files = confirmationsDir.listFiles();

        if (files == null) {
            return;
        }

        Arrays.asList(files)
                .stream()
                .filter(file -> file.getPath().contains("Confirmation"))
                .forEach(File::deleteOnExit);
    }

    @Test
    public void saves_confirmation_and_then_allows_to_query_for_it() throws Exception {

        Confirmation confirmationToSave = confirmation();
        restTemplate.postForObject("http://localhost:9003/api/confirmation", confirmationToSave, Object.class);

        Confirmation confirmation = restTemplate.getForObject(
                "http://localhost:9003/api/confirmation/" + confirmationToSave.getAllocationId(),
                Confirmation.class
        );

        assertThat(confirmation).isEqualToComparingFieldByField(confirmationToSave);
    }

    private Confirmation confirmation() {

        Confirmation confirmation = new Confirmation();
        confirmation.setAllocationId(UUID.randomUUID().toString());
        confirmation.setConfirmationType(Confirmation.EMAIL);
        confirmation.setContent(DUMMY_CONFIRMATION.getBytes(StandardCharsets.UTF_8));

        return confirmation;
    }
}
