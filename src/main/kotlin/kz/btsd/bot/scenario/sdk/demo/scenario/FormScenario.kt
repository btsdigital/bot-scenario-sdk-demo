package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import kz.btsd.bot.scenario.sdk.demo.util.ResourceReader
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.command.FormMessage
import kz.btsd.messenger.bot.api.model.command.QuickButtonCommand
import kz.btsd.messenger.bot.api.model.command.UiState
import kz.btsd.messenger.bot.api.model.update.FormClosed
import kz.btsd.messenger.bot.api.model.update.FormMessageSent
import kz.btsd.messenger.bot.api.model.update.QuickButtonSelected
import kz.btsd.messenger.bot.api.model.update.Update
import kz.btsd.messenger.bot.domain.Form

val form: Form = Form.of(ResourceReader.readContent("/FormMessage.json"))
val emptyFormMessage = FormMessage("")

@Scenario(DemoDispatcher::class, "/form", "form")
class FormScenario(dispatcher: DemoDispatcher) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    enum class QuickButton(val title: String) {
        SELECT_CURRENCY("Выбрать валюту")
    }

    private final val selectQB = QuickButtonCommand(QuickButton.SELECT_CURRENCY.title,
            action = QuickButtonCommand.QuickButtonAction.QUICK_REQUEST,
            metadata = QuickButton.SELECT_CURRENCY.name)

    val qbUiState = UiState(canWriteText = false,
            showCameraButton = false,
            showShareContactButton = false,
            showRecordAudioButton = false,
            showGalleryButton = false,
            replyKeyboard = emptyList(),
            quickButtonCommands = listOf(selectQB),
            formMessage = emptyFormMessage)

    val formUiState = UiState(canWriteText = false,
            showCameraButton = false,
            showShareContactButton = false,
            showRecordAudioButton = false,
            showGalleryButton = false,
            replyKeyboard = emptyList(),
            formMessage = FormMessage(form.toJson()))

    override suspend fun start(update: Update) {

        sendRequest(buildSendMessage("Демонстрация форм и quickButton", uiState = qbUiState)) { qbUpdate ->
            when (qbUpdate) {
                is QuickButtonSelected -> {
                    sendRequest(buildSendUiState(when (qbUpdate.metadata) {
                        QuickButton.SELECT_CURRENCY.name -> formUiState
                        else -> throw IllegalArgumentException("Unexpected type message")
                    })) { formUpdate ->
                        when (formUpdate) {
                            is FormMessageSent -> sendMessage("Значения на форме: ${formUpdate.message}. Сценарий завершен")
                            is FormClosed -> {
                                sendMessage("Форма закрыта. Сценарий завершен")
                            }
                        }
                    }
                }
                else -> throw IllegalArgumentException("Unexpected type message")
            }
        }

        sendUiState(defaultUiState) // set default UiState
    }
}
