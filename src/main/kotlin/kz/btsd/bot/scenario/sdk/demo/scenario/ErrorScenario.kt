package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.update.Update

@Scenario(DemoDispatcher::class, "/error", "error")
class ErrorScenario(
        dispatcher: DemoDispatcher
) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    enum class ErrorOption(val title: String) {
        REQUEST_ERROR_DEFAULT("REQUEST_ERROR_DEFAULT"),
        REQUEST_ERROR_CUSTOM("REQUEST_ERROR_CUSTOM"),
        ENUM_REQUEST_ERROR_DEFAULT("ENUM_REQUEST_ERROR_DEFAULT"),
        ENUM_REQUEST_ERROR_CUSTOM("ENUM_REQUEST_ERROR_CUSTOM")
    }

    enum class OkOption(val title: String)

    override suspend fun start(update: Update) {
        when (sendEnumRequest<ErrorOption>("Error demo scenario. Choose option") { it.title }) {
            ErrorOption.REQUEST_ERROR_DEFAULT -> sendRequest("Sending error request without " +
                    "validationErrorMessage. Should receive default error message. Write me anything") {
                throw IllegalArgumentException("demo exception") }
            ErrorOption.REQUEST_ERROR_CUSTOM -> sendRequest(text = "Sending error request with " +
                    "validationErrorMessage = Custom error message. Should receive 'Custom error message'. " +
                    "Write me anything", validationErrorMessage = "Custom error message") {
                throw IllegalArgumentException("demo exception") }
            ErrorOption.ENUM_REQUEST_ERROR_DEFAULT -> sendEnumRequest<OkOption>("Sending error enumRequest " +
                    "without validationErrorMessage. Should receive default enum error message. Write me anything") {
                it.title }
            ErrorOption.ENUM_REQUEST_ERROR_CUSTOM -> sendEnumRequest<OkOption>("Sending error enumRequest " +
                    "with validationErrorMessage = Custom enum error message. Should receive " +
                    "'Custom enum error message'. Write me anything",
                    validationErrorMessage = "Custom enum error message") { it.title }
        }
    }
}
