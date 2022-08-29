package info.timestat.CallbackQuertHandlers;

import info.timestat.Keyboards.Inline.Keyboards;
import info.timestat.Menu.Menu;
import info.timestat.Menu.MenuText;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static info.timestat.GlobalConfig.BOT;

public class CallbackQueryHandler {
    private String textCallbackQuery;
    private String chatId;
    private Integer messageId;
    private CallbackQuery callbackQuery;

    private Menu menu = new Menu();
    private Keyboards keyboard = new Keyboards();
    private MenuText menuText = new MenuText();

    public void handleCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        this.callbackQuery = callbackQuery;
        this.textCallbackQuery = callbackQuery.getData();
        this.chatId = callbackQuery.getMessage().getChatId().toString();
        this.messageId = callbackQuery.getMessage().getMessageId();
        switch (this.textCallbackQuery) {
            case "start_menu":
                startMenu();
                break;
            case "manage_tasks":
                manageTasks();
                break;
            case "stats_tasks":
                statsTasks();
                break;
            case "about":
                about();
                break;
            case "add_task":
                addTask();
                break;
        }
    }

    private void about() throws TelegramApiException {
        menu.editMenu(chatId, messageId, menuText.getAboutBotMenuText(), keyboard.getBackToStartMenuKeyboard());
        removeClock();
    }

    private void startMenu() throws TelegramApiException {
        menu.editMenu(chatId, messageId, menuText.getStartMenu(callbackQuery.getFrom().getFirstName()),
                keyboard.getStartMenuKeyboard());
        removeClock();
    }

    private void manageTasks() throws TelegramApiException {
        menu.editMenu(chatId, messageId, menuText.getControlTasksMenuText(), keyboard.getManageTasksKeyboard());
        removeClock();
    }

    private void addTask() throws TelegramApiException {
        menu.editMenu(chatId, messageId, menuText.getAddTaskMenuText(), keyboard.getBackToManageTaskKeyboard());
        removeClock();
    }

    private void statsTasks() throws TelegramApiException {
        menu.editMenu(chatId, messageId, menuText.getStatsTasksMenuText(false), keyboard.getStatsTasksKeyboard(false));
        removeClock();
    }

    private void removeClock() throws TelegramApiException {
        BOT.execute(AnswerCallbackQuery
                .builder()
                .callbackQueryId(callbackQuery.getId())
                .build()
        );
    }
}
