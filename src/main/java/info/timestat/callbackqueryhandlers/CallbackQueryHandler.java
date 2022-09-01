package info.timestat.callbackqueryhandlers;


import info.timestat.keyboards.inline.Keyboards;
import info.timestat.menu.Menu;
import info.timestat.menu.MenuText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



@Component
public class CallbackQueryHandler {
    private CallbackQuery callbackQuery;
    private Menu menu;
    private MenuText menuText;
    private Keyboards keyboard;

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

    public void handleCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        this.callbackQuery = callbackQuery;

        switch (callbackQuery.getData()) {
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
            case "delete_task":
                deleteTask();
                break;
            case "tracking_task":
                trackingTask();
                break;
        }
    }

    private void about() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getAboutBotMenuText(), keyboard.getBackToStartMenuKeyboard());
    }

    private void startMenu() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getStartMenu(callbackQuery.getFrom().getFirstName()),
                keyboard.getStartMenuKeyboard());
    }

    private void manageTasks() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getControlTasksMenuText(), keyboard.getManageTasksKeyboard());
    }

    private void addTask() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getAddTaskMenuText(), keyboard.getBackToManageTaskKeyboard());
    }

    private void statsTasks() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getStatsTasksMenuText(false), keyboard.getStatsTasksKeyboard(false));
    }

    private void trackingTask() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getTrackingTasksMenuText(false), keyboard.getTrackingTasksKeyboard(false));
    }

    private void deleteTask() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getDeleteTasksMenuText(false), keyboard.getDeleteTasksKeyboard(false));
    }
}
