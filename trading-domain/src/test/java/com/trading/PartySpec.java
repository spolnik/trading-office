package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PartySpec {

    @Test
    public void two_instances_of_party_with_same_id_are_equal() throws Exception {
        Party partyA = new Party();
        partyA.setId("1");
        partyA.setName("A");

        Party partyB = new Party();
        partyB.setId("1");
        partyB.setName("B");

        assertThat(partyA).isEqualTo(partyB);
    }
}