package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.update.Update

const val COUNTER_KEY = "counter"

@Scenario(DemoDispatcher::class, "/session", "session")
class SessionScenario(
    dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override suspend fun start(update: Update) {
        // get messengerId
        val messengerId = session[messengerId]
        sendMessage("messengerId: $messengerId")
        // put value in session
        session.putIfAbsent(COUNTER_KEY, 0)
        session[COUNTER_KEY] = session[COUNTER_KEY] as Int + 1
        val counterValue = session[COUNTER_KEY] as Int
        sendMessage("Counter value: $counterValue")
    }
}
