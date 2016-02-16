package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvPartyRepositoryIntegrationTest {

    @Test
    public void returns_party_from_underlying_csv_file() throws Exception {
        CsvPartyRepository repository = new CsvPartyRepository();

        Party tradingOfficeParty = repository.getById("TROF");

        assertThat(tradingOfficeParty).isEqualToComparingFieldByField(
                executingParty()
        );
    }

    private static Party executingParty() {
        Party party = new Party();
        party.setId("TROF");
        party.setName("Trading Office Ltd.");
        return party;
    }
}