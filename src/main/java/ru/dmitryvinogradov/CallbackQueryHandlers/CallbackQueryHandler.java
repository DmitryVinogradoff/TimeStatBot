package ru.dmitryvinogradov.CallbackQueryHandlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitryvinogradov.DataSet.DataSetDeleter;
import ru.dmitryvinogradov.DataSet.DataSetGenerator;
import ru.dmitryvinogradov.Histograms.Histogram;
import ru.dmitryvinogradov.Keyboards.Inline.Keyboards;
import ru.dmitryvinogradov.Menu;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Services.TasksService;
import ru.dmitryvinogradov.Services.TimeTableService;

import java.io.*;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
            case "test_data":{
                keyboard = Keyboards.getTestDatasetMenuKeyboard();
                Locale locale = new Locale("ru", "RU");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM", locale);
                LocalDate localDate = LocalDate.now();
                LocalDate minusMonthLocalDate = localDate.minusMonths(1);
                StringBuilder sb = new StringBuilder();
                sb.append("Нажмите кнопку <i>\"Добавить тестовые данные\"</i>, чтобы заполнить базу случайно сгенерированными данными за период c <i><b>")
                        .append(minusMonthLocalDate.getDayOfMonth()).append(" ").append(minusMonthLocalDate.format(dateTimeFormatter)).append(" ").append(minusMonthLocalDate.getYear())
                        .append("</b></i> по <i><b>")
                        .append(localDate.getDayOfMonth()).append(" ").append(localDate.format(dateTimeFormatter)).append(" ").append(localDate.getYear())
                        .append("</b></i> и оценить весь функционал бота (заполнение займет около 5 секунд)\n\nЧтобы удалить тестовые данные из базы, и начать работу только со своими данными, нажмите кнопку <i>\"Удалить тестовые данные\"</i>");
                messageText = sb.toString();
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }

            case "add_test_data":{
                if(DataSetGenerator.generateDataSetOnMonth(cbQ.getFrom().getId())) {
                    messageText = "Тестовые данные успешно добавлены!\nТеперь можно вернуться в главное меню и оценить все функции бота";
                } else {
                    messageText = "Тестовые данные уже добавлены!\nМожно вернуться в главное меню и оценить все функции бота";
                }
                keyboard = Keyboards.getStopTasksKeyboard("Главное меню", "start_menu");
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }

            case "delete_test_data":{
//                TasksService tasksService = new TasksService();
//                tasksService.deleteTestData(cbQ.getFrom().getId());
                DataSetDeleter.deleteDataSet(cbQ.getFrom().getId());
                messageText = "Тестовые данные успешно удалены!";
                keyboard = Keyboards.getStopTasksKeyboard("Главное меню", "start_menu");
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }

            case "start_menu": {
                StringBuilder sb = new StringBuilder();
                sb.append("Привет, <b><i>").append(cbQ.getFrom().getFirstName()).append("!</i></b>\n");
                sb.append("Я TimeStatBot, предназначеный для учета и анализа времени, затраченного на выполнение каких-либо задач\n\n");
                sb.append("Я помогу понять, на что было потрачено время в течении дня, недели или месяца\n\n");
                sb.append("Бот выводит статистику только по тем задачам, которые суммарно выполнялись больше 1 минуты, округляя при этом время в меньшую сторону\n\n");
                sb.append("<i>Чтобы оценить возможности бота сразу, без отслеживания личной статистики, можно заполнить базу <b>тестовыми данными</b>, нажав на соответствующую кнопку</i>");

                keyboard = Keyboards.getStartMenuKeyboard();
                Menu.editMenu(chatId, messageId, sb.toString(), keyboard);
                break;
            }

            case "tasks": {
                messageText = "Управление задачами";
                keyboard = Keyboards.getManageTasksKeyboard();
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
            case "stats_tasks":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                messageText = "Нажмите на кнопку с названием периода, чтобы просмотреть статистику";
                keyboard = Keyboards.getChoicePeriodStatsKeyboard();

                if((callback.length > 1) && (callback[1].equals("save"))){
                    String oldMessageText = cbQ.getMessage().getText();
                    Menu.editMenuWithStatsSave(chatId, messageId, messageText, keyboard, oldMessageText);
                } else {
                    Menu.editMenu(chatId, messageId, messageText, keyboard);
                }

                break;
            }
            case "about":{
                messageText = "Стек технологий, используемых в боте:\nJava\nTelegram Bot Java Library\nHibernate\nJFreeChart\n\nБаза данных: PostgreSQL\n\nGitHub: https://github.com/DmitryVinogradoff/TimeStatBot\n\n\nTimeStatBot © Dmitry Vinogradov, 2022 \nhttps://t.me/dmitry_vinogradov";
                keyboard = Keyboards.getBackToStartMenuKeyboard();
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }

            case "tracking_task":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    messageText = "Нажмите на название задачи, чтобы начать её отслеживание";
                    keyboard = Keyboards.getAllTasksKeyboard( "tracking", tasks);
                } else {
                    messageText = "Нет задач на отслеживание\n\nДобавьте их в меню управления задачами, нажав <i>\"Добавить задачу\"</i>";
                    keyboard = Keyboards.getBackToManageTasksKeyboard();
                }
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
            case "add_task_menu": {
                NOWSTATE.setAddingTaskState();
                messageText = "Добавьте задачу, прислав боту сообщение с ее названием, например: <i>Работа</i>";
                keyboard = Keyboards.getBackToManageTasksKeyboard();
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }
            case "delete_task_menu":{
                TasksService tasksService = new TasksService();
                List<Tasks> tasks =  tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    messageText = "Нажмите на название задачи, чтобы её удалить";
                    keyboard = Keyboards.getAllTasksKeyboard( "delete", tasks);
                } else {
                    messageText = "Нет задач, которые можно удалить";
                    keyboard = Keyboards.getBackToManageTasksKeyboard();
                }
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
            }

            case "period_of_stats": {
                TasksService tasksService = new TasksService();
                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                Map<String, String> nameOfPeriods= new HashMap<>();
                nameOfPeriods.put("day", "Статистика за день");
                nameOfPeriods.put("week", "Статистика за неделю");
                nameOfPeriods.put("month", "Статистика за месяц");
                if(!tasks.isEmpty()) {
                    TimeTableService timeTableService = new TimeTableService();
                    StringBuilder sb = new StringBuilder();
                    Histogram histogram = new Histogram();
                    for (Tasks task : tasks) {
                        List<String> taskStats = timeTableService.taskStat(task.getId(), callback[1]);
                        if ((taskStats != null)) {
                            Integer hours = 0;
                            Integer minutes = 0;
                            Integer totalTime = 0;
                            for(String taskStat : taskStats) {
                                String[] time = taskStat.split(":");
                                hours += Integer.parseInt(time[0]);
                                minutes += Integer.parseInt(time[1]);
                                totalTime += hours * 60 + minutes;
                            }
                            if(totalTime != 0){
                                histogram.setNameTask(task.getName());
                                histogram.setTimeTask(totalTime);
                                sb.append("На \"").append(task.getName()).append("\" затрачено: ");
                                if(hours != 0) sb.append(hours).append(" часов ");

                                sb.append(minutes).append(" минут\n");
                            }
                        }
                    }
                    messageText = sb.toString();
                    if (!messageText.isBlank()) {
                        InputStream fileInputStream = null;
                        try {
                            fileInputStream = histogram.generateHistogram(nameOfPeriods.get(callback[1]));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        InputFile inputFile = new InputFile(fileInputStream, "histogram");
                        keyboard = Keyboards.getBackToStatsTasksKeyboardWithSave();
                        Menu.editMenuWithStats(chatId, messageId, messageText, keyboard, inputFile);
                        break;
                    }
                }
                messageText = "Нет статистики за указанный период";
                keyboard = Keyboards.getBackToStatsTasksKeyboard();
                Menu.editMenu(chatId, messageId, messageText, keyboard);
                break;
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
                StringBuilder sb = new StringBuilder();
                sb.append("Здача \"").append(callback[2]).append("\" удалена!");

                List<Tasks> tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());

                if(tasks.isEmpty()) sb.append("\n\nНет задач, которые можно удалить");
                else sb.append("\n\nМожно продолжить удаление задач, нажатием на кнопку с название задачи, либо вернуться в меню управления");

                keyboard = Keyboards.getAllTasksKeyboard("delete", tasks);
                messageText = sb.toString();
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
