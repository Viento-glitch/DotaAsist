package ru.sa.dotaassist.client;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSubscribe {

    @Subscriber(key = "auto-update")
    boolean autoUpdate;
    @Test
    @DisplayName("Test subscribing, boolean value")
    public void test1() {
        PropertyManager.Companion.subscribe(this);
        assertEquals(false, autoUpdate);
    }
}
