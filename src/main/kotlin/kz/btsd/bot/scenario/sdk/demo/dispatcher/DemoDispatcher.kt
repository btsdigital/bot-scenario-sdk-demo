package kz.btsd.bot.scenario.sdk.demo.dispatcher

import kz.btsd.bot.botscenariosdk.Dispatcher
import kz.btsd.bot.botscenariosdk.scenario.ScenarioFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class DemoDispatcher(
    scenarioFactory: ScenarioFactory,
    @Value("\${bot.api.url}") botApiUrl: String,
    @Value("\${token}") token: String
) : Dispatcher(
        botApiUrl,
        token,
        scenarioFactory)
