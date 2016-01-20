import com.trading.FinanceDataController;
import com.trading.Instrument;
import com.trading.StockToInstrumentConverter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceDataControllerSpec {

    public static final String INTEL_SYMBOL = "INTC";

    private Instrument instrument;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        FinanceDataController controller = new FinanceDataController(
                new StockToInstrumentConverter()
        );

        instrument = controller.getInstrument(INTEL_SYMBOL);
    }

    @Test
    public void maps_symbol_of_instrument() throws Exception {

        assertThat(instrument.getSymbol()).isEqualTo(INTEL_SYMBOL);
    }

    @Test
    public void maps_name_of_instrument() throws Exception {
        assertThat(instrument.getName()).isEqualTo("Intel Corporation Stocks");
    }

    @Test
    public void maps_currency_of_instrument() throws Exception {
        assertThat(instrument.getCurrency()).isEqualTo("USD");
    }
}
