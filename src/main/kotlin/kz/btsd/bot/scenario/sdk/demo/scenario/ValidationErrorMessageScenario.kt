package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import java.lang.UnsupportedOperationException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.command.SendMessage
import kz.btsd.messenger.bot.api.model.peer.Peer
import kz.btsd.messenger.bot.api.model.peer.PeerUser
import kz.btsd.messenger.bot.api.model.update.Message
import kz.btsd.messenger.bot.api.model.update.Update

@Scenario(DemoDispatcher::class, "/validationMessage", "validationMessage")
class ValidationErrorMessageScenario(
    dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

    override suspend fun start(update: Update) {
        sendRequest(SendMessage(
                recipient = PeerUser(messengerId),
                content = "Enter date in format dd.mm.yyyy"),
                validationErrorMessage = "Please, enter valid date in format dd.mm.yyyy") {
            if (it is Message) {
                val date = LocalDate.parse(it.content, dateTimeFormatter)
                sendMessage("Entered: ${date.format(dateTimeFormatter)}")
            } else {
                throw UnsupportedOperationException()
            }
        }
    }
}
