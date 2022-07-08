package ru.sa.dotaassist.client

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Test {

    // Приватный финализированный экземпляр класса Assistant
    private val assistant = Assistant()

    @Test
    @DisplayName("Process two equal numbers")
    fun test1() {
        assertEquals("expected", assistant.process("-235 235", true))
    }
    @Test
    @DisplayName("First bigger then second number")
    fun test2() {
        assertEquals("expected", assistant.process("-320 15", true))
    }
    @Test
    @DisplayName("Second bigger then first number")
    fun test3() {
        assertEquals("expected", assistant.process("-320 3456", true))
    }
    @Test
    @DisplayName("Second is 0")
    fun test4() {
        assertEquals("expected", assistant.process("-120 0", true))
    }
    @Test
    @DisplayName("First is 0")
    fun test5() {
        assertEquals("expected", assistant.process("-0 450", true))
    }
    @Test
    @DisplayName("Both is 0")
    fun test6() {
        assertEquals("expected", assistant.process("-0 0", true))
    }
    @Test
    @DisplayName("One number is null")
    fun test7() {
        assertEquals("expected", assistant.process("0", true))
    }
    @Test
    @DisplayName("One number")
    fun test8() {
        assertEquals("expected", assistant.process("4356", true))
    }
    @Test
    @DisplayName("One number with minus")
    fun test9() {
        assertEquals("expected", assistant.process("-450", true))
    }
    @Test
    @DisplayName("Three numbers")
    fun test10() {
        assertEquals("expected", assistant.process("-450 464 234", true))
    }
    @Test
    @DisplayName("Space in the start position")
    fun test11() {
        assertEquals("expected", assistant.process(" -450 464", true))
    }
    @Test
    @DisplayName("Double space")
    fun test12() {
        assertEquals("expected", assistant.process("-450  464", true))
    }
    @Test
    @DisplayName("Typos")
    fun test13() {
        assertEquals("expected", assistant.process("-4y0  d64", true))
    }
    @Test
    @DisplayName("Wrong symbols")
    fun test14() {
        assertEquals("expected", assistant.process("Hi, its wrong text", true))
    }
    @Test
    @DisplayName("Forgot minus")
    fun test15() {
        assertEquals("expected", assistant.process("460  264", true))
    }
}