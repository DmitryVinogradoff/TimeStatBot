package ru.dmitryvinogradov.CallbackQueryHandlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitryvinogradov.Histograms.Histogram;
import ru.dmitryvinogradov.Keyboards.Inline.Keyboards;
import ru.dmitryvinogradov.Menu;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Services.TasksService;
import ru.dmitryvinogradov.Services.TimeTableService;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static ru.dmitryvinogradov.GlobalConfig.BOT;
import static ru.dmitryvinogradov.GlobalConfig.NOWSTATE;

public class CallbackQueryHandler {
    public void callbackMessage(CallbackQuery cbQ) throws TelegramApiException {
        String[] callback = cbQ.getData().split(":");
        String chatId = cbQ.getMessage().getChatId().toString();
        Integer messageId = cbQ.getMessage().getMessageId();
        String messageText = "";
        List<List<InlineKeyboardButton>>  keyboard;
        NOWSTATE.setDefaultState();
        switch (callback[0]){
            case "start_menu": {
                StringBuilder sb = new StringBuilder();
                sb.append("Привет, <b><i>").append(cbQ.getFrom().getFirstName()).append("!</i></b>\n");
                sb.append("Я TimeStatBot предназначеный для учета и анализа времени, потраченного на выполнение каких-либо задач\n\n");
                sb.append("Я помогу тебе понять, распределение твоего времени в течении дня");

                keyboard = Keyboards.getStartMenuKeyboard();
                Menu.editMenu(chatId, messageId, sb.toString(), keyboard);
                break;
            }

            case "tasks": {
                messageText = "<b>Меню управления задачами</b>";
                keyboard = Keyboards.getManageTasksKeyboard();
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
            case "stats_tasks":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    messageText = "Выбери период, за который ты хочешь просмотреть статистику";
                    keyboard = Keyboards.getChoicePeriodStatsKeyboard();
                } else {
                    messageText = "У тебя пока нет статистики по задачам";
                    keyboard = Keyboards.getBackToStartMenuKeyboard();
                }
                if((callback.length > 1) && (callback[1].equals("save"))){
                    String oldMessageText = cbQ.getMessage().getText();
                    Menu.editMenuWithStatsSave(chatId, messageId, messageText, keyboard, oldMessageText);
                } else {
                    Menu.editMenu(chatId, messageId, messageText, keyboard);
                }

                break;
            }
            case "about":{
                //menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }

            case "tracking_task":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    messageText = "<b>Твои задачи для отслеживания</b>\n<i>Нажми на название задачи, чтобы начать её отслеживание</i>";
                    keyboard = Keyboards.getAllTasksKeyboard( "tracking", tasks);
                } else {
                    messageText = "У тебя нет задач на отслеживание\nДобавь их в меню управлениями задачами, <i>нажав \"Добавить задачу\"</i>";
                    keyboard = Keyboards.getBackToManageTasksKeyboard();
                }
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
            case "add_task_menu": {
                NOWSTATE.setAddingTaskState();
                messageText = "Добавь задачу, прислав боту сообщение с ее названием";
                keyboard = Keyboards.getBackToManageTasksKeyboard();
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
            case "delete_task_menu":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks =  tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    messageText = "<b>Список твоих задач</b>\n<i>Нажми на название задачи, чтобы её удалить</i>";
                    keyboard = Keyboards.getAllTasksKeyboard( "delete", tasks);
                } else {
                    messageText = "У тебя нет задач, которые можно удалить";
                    keyboard = Keyboards.getBackToManageTasksKeyboard();
                }
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }

            case "period_of_stats": {
                TasksService tasksService = new TasksService();
                List<Tasks> tasks = tasksService.findAll(cbQ.getFrom().getId());
                Map<String, String> nameOfPeriods= new HashMap<>();
                nameOfPeriods.put("day", "Статистика за день");
                nameOfPeriods.put("week", "Статистика за неделю");
                nameOfPeriods.put("month", "Статистика за месяц");
                if(!tasks.isEmpty()) {
                    TimeTableService timeTableService = new TimeTableService();
                    StringBuilder sb = new StringBuilder();
                    Histogram histogram = new Histogram();
                    for (Tasks task : tasks) {
                        String taskStat = timeTableService.taskStat(task.getId(), callback[1]);
                        if ((taskStat != null)) {
                            String[] time = taskStat.split(":");
                            Integer hours = Integer.parseInt(time[0]);
                            Integer minutes = Integer.parseInt(time[1]);
                            Integer totalTime = hours * 60 + minutes;
                            if(totalTime != 0){
                                histogram.setNameTask(task.getName());
                                histogram.setTimeTask(totalTime);
                                sb.append("На \"").append(task.getName()).append("\" потрачено: ");
                                if(hours != 0) sb.append(hours).append(" часов ");

                                sb.append(minutes).append(" минут\n");
                            }
                        }
                    }
                    messageText = sb.toString();
                    if (messageText.isBlank()) {
                        messageText = "У тебя нет статистики за этот период";
                        keyboard = Keyboards.getBackToStatsTasksKeyboard();
                        Menu.editMenu(chatId, messageId, messageText, keyboard);
                    } else {
                        InputStream fileInputStream = null;
                        try {
                            fileInputStream = histogram.generateHistogram(nameOfPeriods.get(callback[1]));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        InputFile inputFile = new InputFile(fileInputStream, "histogram");
                        keyboard = Keyboards.getBackToStatsTasksKeyboardWithSave();
                        Menu.editMenuWithStats(chatId, messageId, messageText, keyboard, inputFile);
                    }
                    break;
                }
            }

            case "tracking":{
                TimeTableService timeTableService = new TimeTableService();
                long id = timeTableService.startTask(Integer.parseInt(callback[1]), Timestamp.from(Instant.now()));
                StringBuilder sb = new StringBuilder();
                sb.append("Начато отслеживание задачи \"").append(callback[2]).append("\"");
                messageText = sb.toString();
                sb.setLength(0);
                sb.append("stop:").append(id).append(":").append(callback[2]);
                keyboard = Keyboards.getStopTasksKeyboard("Остановить отслеживание", sb.toString());
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
            case "delete":{
                TasksService tasksService = new TasksService();
                tasksService.deleteTask(Integer.parseInt(callback[1]));
                messageText = "Здача удалена! Ты можешь продолжить удаление задач, или вернуться в меню управления задачами";

                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                keyboard = Keyboards.getAllTasksKeyboard("delete", tasks);
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
            case "stop":{
                TimeTableService timeTableService = new TimeTableService();
                timeTableService.stopTask(Integer.parseInt(callback[1]));
                StringBuilder sb = new StringBuilder();
                sb.append("Отслеживание задачи \"").append(callback[2]).append("\" завершено");
                messageText = sb.toString();
                keyboard = Keyboards.getBackToManageTasksKeyboard("Мои задачи");
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
        }

        BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
    }
}
