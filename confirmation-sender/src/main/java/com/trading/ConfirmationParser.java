package com.trading;

import java.util.Optional;

@FunctionalInterface
public interface ConfirmationParser {

    Optional<Confirmation> parse(Confirmation allocationReport);
}
