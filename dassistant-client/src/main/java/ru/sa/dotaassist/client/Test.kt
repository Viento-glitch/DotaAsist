package ru.sa.dotaassist.client

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Test {

    private val assistant = Assistant()

    @Test
    @DisplayName("Default calculating")
    fun test1() {
        assertEquals("10:00 (A)15:00  (R)18:00-21:00", assistant.process("1000", true))
    }
    @Test
    @DisplayName("With aegis timer")
    fun test2() {
        assertEquals("10:00 (A)15:00  (R)18:00-21:00", assistant.process("-500 1000", true))
    }
    @Test
    @DisplayName("Second bigger then first number")
    fun test3() {
        assertEquals("10:00 (A)15:00  (R)18:00-21:00", assistant.process("-1 1459", true))
    }
    @Test
    @DisplayName("First is 0")
    fun test4() {
        assertEquals("0:00 (A)5:00  (R)8:00-11:00", assistant.process("-0 500", true))
    }
//    @Test
//    @DisplayName("Second is 0")
//    fun test5() {
//        assertEquals("expected", assistant.process("-350 0000", true))
//    }
//    @Test
//    @DisplayName("Both is 0")
//    fun test6() {
//        assertEquals("expected", assistant.process("-0 0", true))
//    }
//    @Test
//    @DisplayName("One number is null")
//    fun test7() {
//        assertEquals("expected", assistant.process("0", true))
//    }
//    @Test
//    @DisplayName("One number")
//    fun test8() {
//        assertEquals("expected", assistant.process("4356", true))
//    }
//    @Test
//    @DisplayName("One number with minus")
//    fun test9() {
//        assertEquals("expected", assistant.process("-450", true))
//    }
//    @Test
//    @DisplayName("Three numbers")
//    fun test10() {
//        assertEquals("expected", assistant.process("-450 464 234", true))
//    }
//    @Test
//    @DisplayName("Space in the start position")
//    fun test11() {
//        assertEquals("expected", assistant.process(" -450 464", true))
//    }
//    @Test
//    @DisplayName("Double space")
//    fun test12() {
//        assertEquals("expected", assistant.process("-450  464", true))
//    }
//    @Test
//    @DisplayName("Typos")
//    fun test13() {
//        assertEquals("expected", assistant.process("-4y0  d64", true))
//    }
//    @Test
//    @DisplayName("Wrong symbols")
//    fun test14() {
//        assertEquals("expected", assistant.process("Hi, its wrong text", true))
//    }
//    @Test
//    @DisplayName("Forgot minus")
//    fun test15() {
//        assertEquals("expected", assistant.process("460  264", true))
//    }
}