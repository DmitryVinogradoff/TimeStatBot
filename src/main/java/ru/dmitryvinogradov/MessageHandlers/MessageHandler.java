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
                StringBuilder sb = new StringBuilder();
                sb.append("Привет, ").append(message.getFrom().getFirstName()).append("!\n");
                sb.append("Я TimeStatBot предназначеный для учета и анализа времени, потраченного на выполнение каких-либо задач.\n\n");
                sb.append("Я помогу тебе понять, распределение твоего времени в течении дня.");
                BOT.execute(SendMessage
                            .builder()
                            .text(sb.toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboards.getStartMenuKeyboard()).build())
                            .chatId(message.getChatId().toString())
                            .build()
                    );
                break;
        }
    }

    private void messageManager (String taskName) throws TelegramApiException {
        //TODO сделать проверку
        Tasks task = new Tasks(taskName, Math.toIntExact(message.getFrom().getId()));
        TasksService tasksService = new TasksService();
        long idTask = tasksService.saveTask(task);
        StringBuilder sb = new StringBuilder();
        sb.append("Задача \"").append(taskName).append("\" успешно добавлена!\nМожно начать её отслеживание прямо сейчас. Для этого нажми кнопку \"Начать отслеживание\"");
        BOT.execute(
                SendMessage
                .builder()
                .text(sb.toString()).parseMode("HTML")
                .chatId(message.getChatId().toString())
                .replyMarkup(
                        InlineKeyboardMarkup
                                .builder()
                                .keyboard(Keyboards.getBackToManageTasksKeyboard(idTask, taskName))
                                .build()
                )
                .build()
        );
    }
}
