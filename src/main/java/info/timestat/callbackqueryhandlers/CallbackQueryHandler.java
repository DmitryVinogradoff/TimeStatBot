package info.timestat.callbackqueryhandlers;


import info.timestat.entity.Task;
import info.timestat.entity.TimeTable;
import info.timestat.keyboards.inline.Keyboards;
import info.timestat.menu.Menu;
import info.timestat.menu.MenuText;

import info.timestat.service.impl.TaskServiceImpl;
import info.timestat.service.impl.TimeTableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;


@Component
public class CallbackQueryHandler {
    private CallbackQuery callbackQuery;
    private Menu menu;
    private MenuText menuText;
    private Keyboards keyboard;
    private  String[] callback;

    @Autowired
    TaskServiceImpl taskServiceImpl;

    @Autowired
    TimeTableImpl timeTableImpl;

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
        this.callback = callbackQuery.getData().split(":");

        switch (callback[0]) {
            case "start_menu":
                startMenu();
                break;
            case "manage_tasks_menu":
                manageTasksMenu();
                break;
            case "stats_tasks_menu":
                statsTasksMenu();
                break;
            case "about_menu":
                aboutMenu();
                break;
            case "add_task_menu":
                addTaskMenu();
                break;
            case "delete_task_menu":
                deleteTaskMenu();
                break;
            case "tracking_task_menu":
                trackingTaskMenu();
                break;
            case "delete":
                deleteTask();
                break;
            case "tracking":
                startTrackingTask();
                break;
            case "stop":
                stopTrackingTask();
                break;
        }
    }

    private void aboutMenu() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getAboutBotMenuText(), keyboard.getBackToStartMenuKeyboard());
    }

    private void startMenu() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getStartMenu(callbackQuery.getFrom().getFirstName()),
                keyboard.getStartMenuKeyboard());
    }

    private void manageTasksMenu() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getControlTasksMenuText(), keyboard.getManageTasksKeyboard());
    }

    private void addTaskMenu() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getAddTaskMenuText(), keyboard.getBackToManageTaskKeyboard());
    }

    private void statsTasksMenu() throws TelegramApiException {
        menu.editMenu(callbackQuery, menuText.getStatsTasksMenuText(false), keyboard.getStatsTasksKeyboard(false));
    }

    private void trackingTaskMenu() throws TelegramApiException {
        List<Task> taskList = taskServiceImpl.getAll();
        menu.editMenu(callbackQuery, menuText.getTrackingTasksMenuText(!taskList.isEmpty()), keyboard.getTrackingTasksKeyboard(taskList));
    }

    private void deleteTaskMenu() throws TelegramApiException {
        List<Task> taskList = taskServiceImpl.getAll();
        menu.editMenu(callbackQuery, menuText.getDeleteTasksMenuText(!taskList.isEmpty()), keyboard.getDeleteTasksKeyboard(taskList));
    }

    private void deleteTask() throws TelegramApiException {
        //TODO сделать в таблице пометку "удаленное", но не удалять
        taskServiceImpl.delete(Long.parseLong(callback[1]));
        deleteTaskMenu();
    }

    private void startTrackingTask() throws TelegramApiException{
        TimeTable timeTable = new TimeTable(Long.parseLong(callback[1]), Timestamp.from(Instant.now()));
        timeTableImpl.save(timeTable);
        menu.editMenu(callbackQuery, menuText.getAfterStartTaskMenu(), keyboard.getAfterStartTaskKeyboard(timeTable.getId()));
    }

    private void stopTrackingTask() throws TelegramApiException{
        TimeTable timeTable = timeTableImpl.findById(Long.parseLong(callback[1])).get();
        timeTable.setStoppedAt(Timestamp.from(Instant.now()));
        timeTableImpl.save(timeTable);
        menu.editMenu(callbackQuery, menuText.getAfterStopTaskMenu(), keyboard.getBackToManageTaskKeyboard());
    }
}
