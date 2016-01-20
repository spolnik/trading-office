import com.trading.FinanceDataController;
import com.trading.Instrument;
import com.trading.StockToInstrumentConverter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceDataControllerSpec {

    public static final String INTEL_SYMBOL = "INTC";

    private static Instrument instrument;

    private static FinanceDataController controller = new FinanceDataController(
            new StockToInstrumentConverter()
    );

    @BeforeClass
    public static void setUp() throws Exception {
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

    @Test
    public void maps_exchange_of_instrument() throws Exception {
        assertThat(instrument.getExchange()).isEqualTo("NMS");
    }

    @Test
    public void maps_bid_price_of_instrument() throws Exception {
        assertThat(instrument.getPrice()).isBetween(new BigDecimal(20), new BigDecimal(40));
    }
}
