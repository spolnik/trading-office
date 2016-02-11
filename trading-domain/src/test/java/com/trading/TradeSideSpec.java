package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TradeSideSpec {

    @Test(expected = UnsupportedOperationException.class)
    public void throws_exception_for_unsupported_id() throws Exception {
        TradeSide.getTradeSide("3");
    }

    @Test
    public void returns_buy_for_id_1() throws Exception {
        assertThat(TradeSide.getTradeSide("1")).isEqualTo(TradeSide.BUY);
    }

    @Test
    public void returns_sell_for_id_2() throws Exception {
        assertThat(TradeSide.getTradeSide("2")).isEqualTo(TradeSide.SELL);
    }
}