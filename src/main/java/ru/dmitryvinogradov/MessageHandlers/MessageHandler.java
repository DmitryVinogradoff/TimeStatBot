package ru.dmitryvinogradov.MessageHandlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitryvinogradov.Keyboards.Inline.Keyboards;
import ru.dmitryvinogradov.Menu;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Services.TasksService;
import ru.dmitryvinogradov.StateMachine.States;

import java.util.Optional;

import static ru.dmitryvinogradov.GlobalConfig.NOWSTATE;

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
        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        switch (command){
            case "/start":
                StringBuilder sb = new StringBuilder();
                sb.append("Привет, ").append(message.getFrom().getFirstName()).append("!\n");
                sb.append("Я TimeStatBot, предназначеный для учета и анализа времени, затраченного на выполнение каких-либо задач\n\n");
                sb.append("Я помогу понять, на что было потрачено время в течении дня, недели или месяца\n\n");
                sb.append("Бот выводит статистику только по тем задачам, которые суммарно выполнялись больше 1 минуты, округляя при этом время в меньшую сторону\n\n");
                sb.append("<i>Чтобы оценить возможности бота сразу, без отслеживания личной статистики, можно заполнить базу <b>тестовыми данными</b>, нажав на соответствующую кнопку</i>");
                Menu.sendMenu(chatId, sb.toString(), Keyboards.getStartMenuKeyboard());
                break;
            default:
                Menu.deleteMesaage(chatId, messageId);
        }
    }

    private void messageManager (String taskName) throws TelegramApiException {
        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        if(NOWSTATE.getNowState().equals(States.ADDINGTASK)) {
            Tasks task = new Tasks(taskName, message.getFrom().getId(), false);
            TasksService tasksService = new TasksService();
            long idTask = tasksService.saveTask(task);
            StringBuilder sb = new StringBuilder();
            sb.append("Задача \"").append(taskName).append("\" успешно добавлена!\n\nМожно начать её отслеживание прямо сейчас\n\n<i>Для этого нажмите кнопку \"Начать отслеживание\"</i>");
            Menu.sendMenu(chatId, sb.toString(), Keyboards.getBackToManageTasksKeyboard(idTask, taskName));
            NOWSTATE.setDefaultState();
        } else {
            Menu.deleteMesaage(chatId, messageId);
        }
    }
}
