package ru.sa.dotaassist.client

fun main(args: Array<String>) {
    val assistant = Assistant()
    assistant.process("-459 1001", true)
    assistant.process("-9 0", true)
    assistant.process("-49 1", true)
    assistant.process("-340 300", true)
    assistant.process("-100 1057", true)
    assistant.process("-235 260", true) // crash
}