package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.command.ReplyCommand
import kz.btsd.messenger.bot.api.model.command.UiState
import kz.btsd.messenger.bot.api.model.update.Message
import kz.btsd.messenger.bot.api.model.update.Update

@Scenario(DemoDispatcher::class, "/replyCommand", "replyCommand")
class ReplyCommandScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

    enum class DateOption(val title: String) {
        TODAY("\uD83D\uDCC5 Сегодня"),
        TOMORROW("\uD83D\uDCC6 Завтра")
    }

    override suspend fun start(update: Update) {
        val uiState = UiState(replyKeyboard = listOf(
                ReplyCommand(DateOption.TODAY.title),
                ReplyCommand(DateOption.TOMORROW.title)))

        val date: LocalDate = sendRequest(text = "Choose option from reply menu", uiState = uiState,
                validationErrorMessage = "Please, choose option from reply menu") {
            when (it) {
                is Message -> when (it.content) {
                    DateOption.TODAY.title -> LocalDate.now()
                    DateOption.TOMORROW.title -> LocalDate.now().plus(1, ChronoUnit.DAYS)
                    else -> throw UnsupportedOperationException()
                }
                else -> throw UnsupportedOperationException()
            }
        }
        sendMessage("Entered: ${date.format(dateTimeFormatter)}")
    }
}
