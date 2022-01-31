package ru.dmitryvinogradov.CallbackQueryHandlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitryvinogradov.Histograms.Histogram;
import ru.dmitryvinogradov.Keyboards.Inline.Keyboards;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Models.TimeTable;
import ru.dmitryvinogradov.Services.TasksService;
import ru.dmitryvinogradov.Services.TimeTableService;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.dmitryvinogradov.GlobalConfig.BOT;

public class CallbackQueryHandler {
    public void callbackMessage(CallbackQuery cbQ) throws TelegramApiException {
        String[] callback = cbQ.getData().split(":");
        //TODO вынести answer callback
        String chatId = cbQ.getMessage().getChatId().toString();
        Integer messageId = cbQ.getMessage().getMessageId();
        String messageText = "";
        List<List<InlineKeyboardButton>>  keyboard = new ArrayList<>();
        switch (callback[0]){
            case "start_menu": {
                messageText = "Данный бот предназначен для учета и анализа времени, потраченного на какие-либо задачи.";
                keyboard = Keyboards.getStartMenuKeyboard();
                break;
            }

            case "tasks": {
                messageText = "Меню управления задачами";
                keyboard = Keyboards.getManageTasksKeyboard();
                break;
            }
            case "stats_tasks":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    messageText = "Выберете период, за который Вы хотет просмотреть статистику";
                    keyboard = Keyboards.getChoicePeriodStatsKeyboard();
                } else {
                    messageText = "У Вас пока нет статистики по задачам";
                    keyboard = Keyboards.getBackToStartMenuKeyboard();
                }
                break;
            }
            case "about":{
                break;
            }

            case "tracking_task":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    messageText = "Ваши задачи для отслеживания";
                    keyboard = Keyboards.getAllTasksKeyboard( "tracking", tasks);
                } else {
                    messageText = "У Вас нет задач на отслеживание.\nДобавьте их в меню управлениями задачами, нажав \"Добавить задачу\"";
                    keyboard = Keyboards.getBackToManageTasksKeyboard();
                }
                break;
            }
            case "add_task_menu": {
                messageText = "Добавьте задачу, прислав боту сообщение с ее названием";
                keyboard = Keyboards.getBackToManageTasksKeyboard();
                break;
            }
            case "delete_task_menu":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks =  tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    messageText = "Для удаления задачи из списка доступных на отслеживание, нажимите на кнопку с названием задачи";
                    keyboard = Keyboards.getAllTasksKeyboard( "delete", tasks);
                } else {
                    messageText = "У Вас нет задач, которые можно удалить";
                    keyboard = Keyboards.getBackToManageTasksKeyboard();
                }

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
                    sb.append("<b>").append(nameOfPeriods.get(callback[1])).append("</b>\n\n");
                    Histogram histogram = new Histogram();
                    for (Tasks task : tasks) {
                        String taskStat = timeTableService.taskStat(task.getId(), callback[1]);
                        if ((taskStat != null)) {
                            String[] time = taskStat.split(":");
                            histogram.setNameTask(task.getName());
                            histogram.setTimeTask(Double.parseDouble(time[1]));
                            sb.append("На <b><i>").append(task.getName()).append("</i></b> потрачено: ");

                            if(!(Integer.parseInt(time[0]) == 0)) sb.append(time[0]).append(" часов ");

                            sb.append(time[1]).append(" минут\n");
                        }
                    }
                    //TODO Проверить чтобы не пустые данные
                    try {
                        histogram.generateHistogram();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        InputStream fileInputStream = new FileInputStream("histogram.png");
                        InputFile inputFile = new InputFile(fileInputStream, "histogram.png");
                        BOT.execute(SendPhoto
                                .builder()
                                .chatId(chatId)
                                .photo(inputFile)
                                .build());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    messageText = sb.toString();
                    keyboard = Keyboards.getBackToStatsTasksKeyboard();

                    BOT.execute(
                            DeleteMessage
                                    .builder()
                                    .messageId(messageId)
                                    .chatId(chatId)
                                    .build()
                    );


                    BOT.execute(
                            SendMessage
                                    .builder()
                                    .text(messageText).parseMode("HTML")
                                    .chatId(chatId)
                                    //TODO Добавить кнопку начать отслеживание добавленной задачи сейчас
                                    .replyMarkup(
                                            InlineKeyboardMarkup
                                                    .builder()
                                                    .keyboard(keyboard)
                                                    .build()
                                    )
                                    .build()
                    );


                    if (messageText.isBlank()) {
                        messageText = "У Вас нет статистики за этот период";
                    }
                    break;
                }
            }

            case "tracking":{
                //TODO https://stackoverflow.com/questions/6627289/what-is-the-most-recommended-way-to-store-time-in-postgresql-using-java
                TimeTableService timeTableService = new TimeTableService();
                long id = timeTableService.startTask(Integer.parseInt(callback[1]), Timestamp.from(Instant.now()));
                StringBuilder sb = new StringBuilder();
                sb.append("Начато отслеживание задачи <b><i>").append(callback[2]).append("</i></b>");
                messageText = sb.toString();
                sb.setLength(0);
                sb.append("stop:").append(id).append(":").append(callback[2]);
                keyboard = Keyboards.getStopTasksKeyboard("Остановить отслеживание", sb.toString());
                break;
            }
            case "delete":{
                TasksService tasksService = new TasksService();
                tasksService.deleteTask(Integer.parseInt(callback[1]));
                messageText = "Здача удалена! Вы можете продолжить удаление задач, или вернуться назад";

                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                keyboard = Keyboards.getAllTasksKeyboard("delete", tasks);
                break;
            }
            case "stop":{
                TimeTableService timeTableService = new TimeTableService();
                //TODO спрятать дату "под капот" метода
                timeTableService.stopTask(Integer.parseInt(callback[1]), Timestamp.from(Instant.now()));
                StringBuilder sb = new StringBuilder();
                sb.append("Отслеживание задачи <b><i>").append(callback[2]).append("</i></b> завершено");
                messageText = sb.toString();
                keyboard = Keyboards.getBackToManageTasksKeyboard("Мои задачи");
                break;
            }
        }

        BOT.execute(EditMessageText
                .builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText).parseMode("HTML")
                .build());
        BOT.execute(
                EditMessageReplyMarkup
                        .builder()
                        .replyMarkup(
                                InlineKeyboardMarkup
                                        .builder()
                                        .keyboard(keyboard)
                                        .build())
                        .chatId(cbQ.getMessage().getChatId().toString())
                        .messageId(cbQ.getMessage().getMessageId())
                        .build()
        );
        BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
    }
}
