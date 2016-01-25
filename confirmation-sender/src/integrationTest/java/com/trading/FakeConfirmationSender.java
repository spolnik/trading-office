package com.trading;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class FakeConfirmationSender implements Sender<Confirmation> {

    private static Confirmation confirmation;

    @Override
    public void send(Confirmation confirmation) {
        FakeConfirmationSender.confirmation = confirmation;
    }

    public static Confirmation getConfirmation() {
        return confirmation;
    }
}
