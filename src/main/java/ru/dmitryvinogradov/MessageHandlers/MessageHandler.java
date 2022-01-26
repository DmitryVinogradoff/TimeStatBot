package ru.dmitryvinogradov.MessageHandlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitryvinogradov.Keyboards.Inline.Keyboards;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Services.TasksService;

import java.util.Optional;

import static ru.dmitryvinogradov.GlobalConfig.BOT;

public class MessageHandler {
    private Message message;

    public void handleMessage(Message msg) throws TelegramApiException {
        this.message = msg;
        if (message.hasText()) {
            if (message.hasEntities()) {
                Optional<MessageEntity> commandEntity =
                        message.getEntities().stream().filter(e -> "bot_command" .equals(e.getType())).findFirst();
                if (commandEntity.isPresent()) {
                    String command = message
                            .getText()
                            .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                    commandManager(command);
                }
            } else {
                messageManager(message.getText());
            }
        }
    }

    private void commandManager (String command) throws TelegramApiException {
        switch (command){
            case "/start":
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Добрый день, ").append(message.getFrom().getFirstName()).append("!\n");
                stringBuilder.append("Данный бот предназначен для учета и анализа времени, потраченного на какие-либо задачи.");
                BOT.execute(SendMessage
                            .builder()
                            .text(stringBuilder.toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboards.getStartMenuKeyboard()).build())
                            .chatId(message.getChatId().toString())
                            .build()
                    );
                break;
        }
    }

    private void messageManager (String text) throws TelegramApiException {
        //TODO сделать проверку
        Tasks task = new Tasks(text, Math.toIntExact(message.getFrom().getId()));
        TasksService tasksService = new TasksService();
        tasksService.saveTask(task);

        BOT.execute(
                SendMessage
                .builder()
                .text("Задача " + text + " успешно добавлена")
                .chatId(message.getChatId().toString())
                        //TODO Добавить кнопку начать отслеживание добавленной задачи сейчас
                .replyMarkup(
                        InlineKeyboardMarkup
                                .builder()
                                .keyboard(Keyboards.getBackToManageTasksKeyboard("Управление задачами"))
                                .build()
                )
                .build()
        );
    }
}
