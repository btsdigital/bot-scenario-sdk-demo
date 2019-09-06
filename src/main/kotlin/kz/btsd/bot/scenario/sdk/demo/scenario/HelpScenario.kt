package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.bot.botscenariosdk.session.Session
import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import kz.btsd.messenger.bot.api.model.update.Update

@Scenario(DemoDispatcher::class, "/help", "help")
class HelpScenario(
        dispatcher: DemoDispatcher,
        val startScenario: StartScenario
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    override fun init(session: Session) {
        super.init(session)
        startScenario.init(session)
    }

    enum class HelpOption(val title: String) {
        START("\uD83D\uDCCD Start")
    }

    override suspend fun start(update: Update) {
        val helpOption: HelpOption = sendEnumRequest("Помогу стартовать сценарий start") { it.title }
        when (helpOption) {
            HelpOption.START -> startScenario.start(update)
        }
    }
}
