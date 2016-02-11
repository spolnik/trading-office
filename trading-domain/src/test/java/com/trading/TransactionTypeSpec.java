package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionTypeSpec {

    @Test(expected = UnsupportedOperationException.class)
    public void throws_exception_for_unsupported_id() throws Exception {
        TransactionType.getTransactionType("1");
    }

    @Test
    public void returns_new_for_id_0() throws Exception {
        assertThat(TransactionType.getTransactionType("0"))
                .isEqualTo(TransactionType.NEW);
    }
}