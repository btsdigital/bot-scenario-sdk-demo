package kz.btsd.bot.scenario.sdk.demo.scenario

import kz.btsd.bot.scenario.sdk.demo.dispatcher.DemoDispatcher
import kz.btsd.bot.botscenariosdk.operations.SessionAwareOperations
import kz.btsd.bot.botscenariosdk.scenario.Scenario
import kz.btsd.bot.botscenariosdk.scenario.ScenarioEntryPoint
import kz.btsd.messenger.bot.api.model.command.Command
import kz.btsd.messenger.bot.api.model.command.FormMessage
import kz.btsd.messenger.bot.api.model.command.QuickButtonCommand
import kz.btsd.messenger.bot.api.model.command.UiState
import kz.btsd.messenger.bot.api.model.update.FormClosed
import kz.btsd.messenger.bot.api.model.update.FormMessageSent
import kz.btsd.messenger.bot.api.model.update.FormSubmitted
import kz.btsd.messenger.bot.api.model.update.QuickButtonSelected
import kz.btsd.messenger.bot.api.model.update.Update

@Scenario(DemoDispatcher::class, "/qb", "qb")
class QBScenario(dispatcher: DemoDispatcher) : SessionAwareOperations(dispatcher), ScenarioEntryPoint {

    enum class QuickButton(val title: String) {
        ACTION("Действие"),
        FORM("Форма"),
        CALL("Звонок"),
        PHONE("Мой телефон"),
        PHONE_META("Мой телефон с метаданными"),
    }

    private final val actionQB = QuickButtonCommand(QuickButton.ACTION.title,
            action = QuickButtonCommand.QuickButtonAction.QUICK_REQUEST,
            metadata = QuickButton.ACTION.name)

    private final val formQB = QuickButtonCommand(QuickButton.FORM.title,
            action = QuickButtonCommand.QuickButtonAction.QUICK_REQUEST,
            metadata = QuickButton.FORM.name)

    private final val callQB = QuickButtonCommand("Call",
            QuickButtonCommand.QuickButtonAction.QUICK_FORM_ACTION,
            metadata = "{\n" +
            "\"action\" : \"redirect_call\",\n" +
            " \"data_template\" : \"There should be valid number\"\n" +
            "}")

    private final val phoneQB = QuickButtonCommand("Send my phone",
            QuickButtonCommand.QuickButtonAction.QUICK_FORM_ACTION,
            metadata = """
            {
              "action" : "send_private_data",
              "data_template" : "phone"
            }
        """.trimIndent())

    private final val phoneMetadataQB = QuickButtonCommand("Send my phone(with metadata)",
            QuickButtonCommand.QuickButtonAction.QUICK_FORM_ACTION,
            metadata = """
            {
              "action" : "send_private_data",
              "data_template" : "phone it is metadata"
            }
        """.trimIndent())


    val qbUiState = UiState(canWriteText = false,
            showCameraButton = false,
            showShareContactButton = false,
            showRecordAudioButton = false,
            showGalleryButton = false,
            replyKeyboard = emptyList(),
            quickButtonCommands = listOf(actionQB, formQB, callQB, phoneQB, phoneMetadataQB),
            formMessage = emptyFormMessage)

    val formUiState = UiState(canWriteText = false,
            showCameraButton = false,
            showShareContactButton = false,
            showRecordAudioButton = false,
            showGalleryButton = false,
            replyKeyboard = emptyList(),
            formMessage = FormMessage(form.toJson()))

    override suspend fun start(update: Update) {

        var command: Command = buildSendMessage("Демонстрация форм и quickButton", uiState = qbUiState)

        while(true) {
            sendRequest(command) {
                command  = when (it) {
                    is QuickButtonSelected -> {
                        when (it.metadata) {
                            QuickButton.ACTION.name -> buildSendMessage("Выбрана кнопка:" +
                                    " ${QuickButton.ACTION.title}. Здесь может быть любая обработка")
                            QuickButton.FORM.name -> buildSendUiState(formUiState)
                            else -> throw IllegalArgumentException("Unexpected type message")
                        }
                    }
                    is FormMessageSent -> buildSendUiState(qbUiState)
                    is FormClosed -> buildSendUiState(qbUiState)
                    is FormSubmitted -> buildSendMessage("Полученная метаинформация: ${it.metadata}")
                    else -> throw IllegalArgumentException("Unexpected type message")
                }
            }
        }
    }
}
