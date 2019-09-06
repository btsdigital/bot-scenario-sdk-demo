package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.command.FormMessage
import kz.btsd.messenger.bot.api.model.command.UiState
import kz.btsd.messenger.bot.api.model.update.Update

val defaultUiState = UiState(replyKeyboard = emptyList(),
        quickButtonCommands = emptyList(),
        formMessage = FormMessage(""))

@Scenario(DemoDispatcher::class, "/start", "start")
class StartScenario(
    dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        sendUiState(defaultUiState) // set default UiState
        sendMessage("Hello from demo service")
    }
}
