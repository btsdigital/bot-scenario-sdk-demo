package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.command.SendMessage
import kz.btsd.messenger.bot.api.model.peer.Peer
import kz.btsd.messenger.bot.api.model.peer.PeerUser
import kz.btsd.messenger.bot.api.model.update.Update

@Scenario(DemoDispatcher::class, "/simpleResponseExtractor", "simpleResponseExtractor")
class SimpleResponseExtractorScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        val response = sendRequest(SendMessage(
                recipient = PeerUser(messengerId),
                content = "Enter text")) { it }
        sendMessage(response.toString())
    }
}
