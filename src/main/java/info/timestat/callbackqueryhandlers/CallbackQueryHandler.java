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
import java.time.LocalTime;
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
            case "stats":
                statsFromPeriod();
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
        //TODO сделать в таблице пометку "удаленное", но не удалять
        taskServiceImpl.delete(Long.parseLong(callback[1]));
        deleteTaskMenu();
    }

    private void startTrackingTask() throws TelegramApiException{
        TimeTable timeTable = new TimeTable(Long.parseLong(callback[1]), Timestamp.from(Instant.now()));
        timeTableServiceImpl.save(timeTable);
        menu.editMenu(callbackQuery, menuText.getAfterStartTaskMenu(), keyboard.getAfterStartTaskKeyboard(timeTable.getId()));
    }

    private void stopTrackingTask() throws TelegramApiException{
        TimeTable timeTable = timeTableServiceImpl.getById(Long.parseLong(callback[1])).get();
        timeTable.setStoppedAt(Timestamp.from(Instant.now()));
        timeTableServiceImpl.save(timeTable);
        menu.editMenu(callbackQuery, menuText.getAfterStopTaskMenu(), keyboard.getBackToManageTaskKeyboard());
    }

    private void statsFromPeriod() throws TelegramApiException {
        List<Task> taskList = taskServiceImpl.getAllByIdUserTelegram(idUserTelegram);
        List<TimeTable> timeTables;
        //TODO пока что выдаю имеющуюся статистику, сдлеать за день, месяц, год
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeQuery = localDateTimeNow.with(LocalTime.of(0,0,0));
        List<TimeTable> list = new ArrayList<>();
        switch(callback[1]){
            case "day":
                //localDateTimeQuery = localDateTimeNow.with(LocalTime.of(0,0,0));
                break;
            case "week":
                if (!localDateTimeNow.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                    localDateTimeQuery = localDateTimeQuery.with(DayOfWeek.MONDAY);
                    list = timeTableServiceImpl.getByIdTaskAndStartedAtAfter((long)77, Timestamp.valueOf(localDateTimeQuery));
                }
                break;
            case "month":
                break;
        }

        Map<String, Long> stats = new LinkedHashMap<String, Long>();
        for(Task task : taskList){
            timeTables = timeTableServiceImpl.getByIdTask(task.getId());
            long totalTime = 0;
            for(TimeTable timeTable : timeTables){
                totalTime += timeTable.getStoppedAt().getTime() - timeTable.getStartedAt().getTime();

            }
            stats.put(task.getName(), totalTime);
        }
        menu.editMenu(callbackQuery, menuText.getWithStatsMenuText(stats), keyboard.getBackToAllStatsMenuKeyboard());
    }
}
