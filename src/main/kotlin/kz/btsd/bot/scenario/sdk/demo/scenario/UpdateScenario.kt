package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kz.btsd.bot.botscenariosdk.keyboards.InlineUniqueKeyboard
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.command.InlineCommand
import kz.btsd.messenger.bot.api.model.update.InlineCommandSelected
import kz.btsd.messenger.bot.api.model.update.Message
import kz.btsd.messenger.bot.api.model.update.Update

@Scenario(DemoDispatcher::class, "/update", "update")
class UpdateScenario(
    dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

    enum class DateOption(val title: String) {
        TODAY("\uD83D\uDCC5 Сегодня"),
        TOMORROW("\uD83D\uDCC6 Завтра")
    }

    override suspend fun start(update: Update) {
        val keyboard: InlineUniqueKeyboard = InlineUniqueKeyboard(listOf(
                InlineCommand(DateOption.TODAY.title, DateOption.TODAY.name),
                InlineCommand(DateOption.TOMORROW.title, DateOption.TOMORROW.name)))

        val date: LocalDate = sendRequest("Choose date option or enter date in format dd.mm.yyyy", keyboard,
                "Please, choose an option or enter valid date in format dd.mm.yyyy") {
            when (it) {
                is Message -> LocalDate.parse(it.content, dateTimeFormatter)
                is InlineCommandSelected -> when (DateOption.valueOf(it.metadata)) {
                    DateOption.TODAY -> LocalDate.now()
                    DateOption.TOMORROW -> LocalDate.now().plus(1, ChronoUnit.DAYS)
                }
                else -> throw UnsupportedOperationException()
            }
        }
        sendMessage("Entered: ${date.format(dateTimeFormatter)}")
    }
}
