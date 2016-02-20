package com.trading;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfirmationControllerSpec {

    private ConfirmationController confirmationController;

    private static final String DUMMY_ID = "DUMMY";

    private ConfirmationRepository confirmationRepository;

    @Before
    public void setUp() throws Exception {
        confirmationRepository = mock(ConfirmationRepository.class);
        confirmationController = new ConfirmationController(confirmationRepository);
    }


    @Test
    public void pass_confirmation_to_confirmation_repository() throws Exception {

        Confirmation confirmation = mock(Confirmation.class);

        confirmationController.addConfirmation(confirmation);

        verify(confirmationRepository).save(confirmation);
    }

    @Test
    public void pass_query_by_id_to_confirmation_repository() throws Exception {

        confirmationController.getConfirmation(DUMMY_ID);

        verify(confirmationRepository).queryById(DUMMY_ID);
    }

    @Test
    public void returns_confirmation_for_given_id() throws Exception {

        Confirmation confirmation = mock(Confirmation.class);
        when(confirmationRepository.queryById(DUMMY_ID)).thenReturn(confirmation);

        Confirmation queriedConfirmation = confirmationController.getConfirmation(DUMMY_ID);

        assertThat(queriedConfirmation).isEqualTo(confirmation);
    }
}