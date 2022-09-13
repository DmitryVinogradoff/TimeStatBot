package info.timestat.callbackqueryhandlers;


import info.timestat.entity.Task;
import info.timestat.entity.TimeTable;
import info.timestat.keyboards.inline.Keyboards;
import info.timestat.menu.Menu;
import info.timestat.menu.MenuText;

import info.timestat.service.impl.TaskServiceImpl;
import info.timestat.service.impl.TimeTableServiceImpl;
import info.timestat.statemachine.CurrentState;
import info.timestat.statemachine.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


@Component
public class CallbackQueryHandler {
    private CallbackQuery callbackQuery;
    private Menu menu;
    private MenuText menuText;
    private Keyboards keyboard;
    private  String[] callback;
    private long idUserTelegram;

    @Autowired
    CurrentState currentState;

    @Autowired
    TaskServiceImpl taskServiceImpl;

    @Autowired
    TimeTableServiceImpl timeTableServiceImpl;

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
        idUserTelegram = callbackQuery.getFrom().getId();
        this.callback = callbackQuery.getData().split(":");

        switch (callback[0]) {
            case "start_menu" -> startMenu();
            case "manage_tasks_menu" -> manageTasksMenu();
            case "stats_tasks_menu" -> statsTasksMenu();
            case "about_menu" -> aboutMenu();
            case "add_task_menu" -> addTaskMenu();
            case "delete_task_menu" -> deleteTaskMenu();
            case "tracking_task_menu" -> trackingTaskMenu();
            case "delete" -> deleteTask();
            case "tracking" -> startTrackingTask();
            case "stop" -> stopTrackingTask();
            case "stats" -> statsFromPeriod();
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
        currentState.setState(State.DEFAULT); // меняем состояние, потому что пользователь мог не вводить назваие задачи,
                                             // а вернуться по кнопке "Управление задачами"
        menu.editMenu(callbackQuery, menuText.getControlTasksMenuText(), keyboard.getManageTasksKeyboard());
    }

    private void addTaskMenu() throws TelegramApiException {
        currentState.setState(State.ADDINGTASK);
        menu.editMenu(callbackQuery, menuText.getAddTaskMenuText(), keyboard.getBackToManageTaskKeyboard());
    }

    private void statsTasksMenu() throws TelegramApiException {
        List<Task> taskList = taskServiceImpl.getAllByIdUserTelegram(idUserTelegram);
        menu.editMenu(callbackQuery, menuText.getStatsTasksMenuText(!taskList.isEmpty()), keyboard.getStatsTasksKeyboard(!taskList.isEmpty()));
    }

    private void trackingTaskMenu() throws TelegramApiException {
        List<Task> taskList = taskServiceImpl.getAllByIdUserTelegram(idUserTelegram);
        menu.editMenu(callbackQuery, menuText.getTrackingTasksMenuText(!taskList.isEmpty()), keyboard.getTrackingTasksKeyboard(taskList));
    }

    private void deleteTaskMenu() throws TelegramApiException {
        List<Task> taskList = taskServiceImpl.getAllByIdUserTelegram(idUserTelegram);
        menu.editMenu(callbackQuery, menuText.getDeleteTasksMenuText(!taskList.isEmpty()), keyboard.getDeleteTasksKeyboard(taskList));
    }

    private void deleteTask() throws TelegramApiException {
        taskServiceImpl.delete(Long.parseLong(callback[1]));
        deleteTaskMenu();
    }

    private void startTrackingTask() throws TelegramApiException{
        TimeTable timeTable = new TimeTable(Long.parseLong(callback[1]), Timestamp.from(Instant.now()));
        timeTableServiceImpl.save(timeTable);
        menu.editMenu(callbackQuery, menuText.getAfterStartTaskMenu(), keyboard.getAfterStartTaskKeyboard(timeTable.getId()));
    }

    private void stopTrackingTask() throws TelegramApiException{
        //не проверяем на наличие записи, потому что мы останавливаем(редактируем) уже существующую запись
        TimeTable timeTable = timeTableServiceImpl.getById(Long.parseLong(callback[1])).get();
        timeTable.setStoppedAt(Timestamp.from(Instant.now()));
        timeTableServiceImpl.save(timeTable);
        menu.editMenu(callbackQuery, menuText.getAfterStopTaskMenu(), keyboard.getBackToManageTaskKeyboard());
    }

    private void statsFromPeriod() throws TelegramApiException {
        List<Task> taskList = taskServiceImpl.getAllByIdUserTelegram(idUserTelegram);
        List<TimeTable> timeTables;
        LocalDateTime localDateTimeQuery = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        switch(callback[1]){
            case "day":
                break;
            case "week":
                    localDateTimeQuery = localDateTimeQuery.with(DayOfWeek.MONDAY);
                break;
            case "month":
                    localDateTimeQuery = localDateTimeQuery.withDayOfMonth(1);
                break;
        }

        Map<String, Long> stats = new LinkedHashMap<>();
        for(Task task : taskList){
            timeTables = timeTableServiceImpl.getByIdTaskAndStartedAtAfter(task.getId(), Timestamp.valueOf(localDateTimeQuery));
            long totalTime = 0;
            for(TimeTable timeTable : timeTables){
                totalTime += timeTable.getStoppedAt().getTime() - timeTable.getStartedAt().getTime();
            }
            if (totalTime != 0)
                stats.put(task.getName(), totalTime);
        }
        menu.editMenu(callbackQuery, menuText.getWithStatsMenuText(stats), keyboard.getBackToAllStatsMenuKeyboard());
    }
}
