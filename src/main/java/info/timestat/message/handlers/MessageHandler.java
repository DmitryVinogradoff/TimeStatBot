package info.timestat.message.handlers;

import info.timestat.callbackquery.LastCallbackQuery;
import info.timestat.message.LastMessage;
import info.timestat.entity.Task;
import info.timestat.keyboards.inline.Keyboards;
import info.timestat.menu.Menu;
import info.timestat.menu.MenuText;
import info.timestat.service.impl.TaskServiceImpl;
import info.timestat.statemachine.CurrentState;
import info.timestat.statemachine.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class MessageHandler {
    private Message message;
    private Menu menu;
    private MenuText menuText;
    private Keyboards keyboard;

    @Autowired
    LastMessage lastMessage;

    @Autowired
    LastCallbackQuery lastCallbackQuery;

    @Autowired
    CurrentState currentState;

    @Autowired
    TaskServiceImpl taskServiceImpl;

    @Autowired
    public void setMenu(@Lazy Menu menu) {
        this.menu = menu;
    }

    @Autowired
    public void setMenuText(MenuText menuText) {
        this.menuText = menuText;
    }

    @Autowired
    public void setKeyboard(Keyboards keyboard) {
        this.keyboard = keyboard;
    }

    public void handleMessage(Message message) throws TelegramApiException {
        this.message = message;
        if (message.hasText()) {
            if (message.hasEntities()) {
                Optional<MessageEntity> commandEntity =
                        message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
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

    private void commandManager(String command) throws TelegramApiException {
        switch (command) {
            case "/start":
                menu.editMenu(message, menuText.getStartMenu(message.getFrom().getFirstName()), keyboard.getStartMenuKeyboard());
                break;
        }
    }

    private void messageManager(String text) throws TelegramApiException {
        if(currentState.getState().equals(State.DEFAULT)){
            menu.deleteMessage(message);
        } else if (currentState.getState().equals(State.ADDINGTASK)) {
            Optional<Task> optionalTask = taskServiceImpl.getByIdUserTelegramAndName(message.getFrom().getId(), text);
            if (!optionalTask.isPresent()) {
                Task task = taskServiceImpl.save(new Task(text, message.getFrom().getId()));
                menu.editMenu(message, menuText.getAfterAddingTaskMenu(text), keyboard.getAfterAddingTaskKeyboard(task.getId()));
            } else {
                menu.editMenu(message, menuText.getTaskIsAlreadyPresentMenu(text), keyboard.getAfterAddingTaskKeyboard(optionalTask.get().getId()));
            }
            currentState.setState(State.DEFAULT);
            menu.deleteMessage(message);
            menu.deleteMessage(lastCallbackQuery.getCallbackQuery().getMessage());
        } else if (currentState.getState().equals(State.RENAMETASK)) {
            Task task = taskServiceImpl.getLastAddedTask(message.getFrom().getId());
            task.setName(message.getText());
            taskServiceImpl.save(task);
            menu.editMenu(message, menuText.getAfterRenameTaskMenu(lastMessage.getMessage().getText(), text), keyboard.getAfterAddingTaskKeyboard(task.getId()));
            currentState.setState(State.DEFAULT);
            menu.deleteMessage(message);
            menu.deleteMessage(lastCallbackQuery.getCallbackQuery().getMessage());
        }
        lastMessage.setMessage(message);
    }
}
