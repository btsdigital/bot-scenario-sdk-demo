package kz.btsd.bot.scenario.sdk.demo.util

object ResourceReader {
    fun readContent(filePath: String) = ResourceReader::class.java.getResource(filePath).readText()
}

