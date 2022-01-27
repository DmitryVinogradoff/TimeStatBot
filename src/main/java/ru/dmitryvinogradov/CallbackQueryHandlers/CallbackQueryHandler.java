package ru.dmitryvinogradov.CallbackQueryHandlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitryvinogradov.Keyboards.Inline.Keyboards;
import ru.dmitryvinogradov.Models.TimeTable;
import ru.dmitryvinogradov.Services.TasksService;
import ru.dmitryvinogradov.Services.TimeTableService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static ru.dmitryvinogradov.GlobalConfig.BOT;

public class CallbackQueryHandler {
    private CallbackQuery callbackQuery;
    public void callbackMessage(CallbackQuery cbQ) throws TelegramApiException {
        this.callbackQuery = cbQ;
        String[] callback = callbackQuery.getData().split(":");
        //TODO вынести answer callback
        switch (callback[0]){
            case "start_menu": {
                BOT.execute(
                        EditMessageText
                                .builder()
                                .text("Данный бот предназначен для учета и анализа времени, потраченного на какие-либо задачи.")
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build());
                BOT.execute(
                        EditMessageReplyMarkup
                                .builder()
                                .replyMarkup(
                                        InlineKeyboardMarkup
                                                .builder()
                                                .keyboard(Keyboards.getStartMenuKeyboard())
                                                .build()
                                )
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build()
                );
              //  BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
                break;
            }

            case "tasks": {
                BOT.execute(
                        EditMessageText
                                .builder()
                                .text("Меню управления задачами")
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build()
                );

                BOT.execute(
                        EditMessageReplyMarkup
                                .builder()
                                .replyMarkup(
                                    InlineKeyboardMarkup
                                    .builder()
                                    .keyboard(Keyboards.getManageTasksKeyboard())
                                    .build()
                                )
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build()
                );

              //  BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());//убираем "часики"
                break;
            }
            case "stats_tasks":{
                if(false){//запрос в базу, какие задачи, и инлайн клавиатура с ними

                } else {
                    BOT.execute(
                            EditMessageText
                                    .builder()
                                    .text("У Вас пока нет статистики по задачам")
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build());
                    BOT.execute(
                            EditMessageReplyMarkup
                                    .builder()
                                    .replyMarkup(
                                            InlineKeyboardMarkup
                                                    .builder()
                                                    .keyboard(Keyboards.getBackToStartMenuKeyboard())
                                                    .build())
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );
                   // BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
                }
                break;
            }
            case "about":{
                break;
            }

            case "tracking_task":{
                List tasks = new ArrayList();
                TasksService tasksService = new TasksService();
                tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){
                    BOT.execute(
                            EditMessageText
                                    .builder()
                                    .text("Ваши задачи для отслеживания")
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );

                    BOT.execute(
                            EditMessageReplyMarkup
                                    .builder()
                                    .replyMarkup(
                                            InlineKeyboardMarkup
                                                    .builder()
                                                    .keyboard(Keyboards.getAllTasksKeyboard( "tracking", tasks))
                                                    .build())
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );
                } else {
                    BOT.execute(
                            EditMessageText
                                    .builder()
                                    .text("У Вас нет задач на отслеживание.\nДобавьте их в меню управлениями задачами, нажав \"Добавить задачу\"")
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );

                    BOT.execute(
                            EditMessageReplyMarkup
                                    .builder()
                                    .replyMarkup(
                                            InlineKeyboardMarkup
                                                    .builder()
                                                    .keyboard(Keyboards.getBackToManageTasksKeyboard())
                                                    .build())
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );
                }
               // BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
                break;
            }
            case "add_task_menu": {
                BOT.execute(
                        EditMessageText
                                .builder()
                                .text("Добавьте задачу, прислав боту сообщение с ее названием")
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build());
                BOT.execute(
                        EditMessageReplyMarkup
                                .builder()
                                .replyMarkup(
                                    InlineKeyboardMarkup
                                            .builder()
                                            .keyboard(Keyboards.getBackToManageTasksKeyboard())
                                            .build()
                                )
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build()
                );
               // BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
                break;
            }
            case "delete_task_menu":{
                List tasks = new ArrayList();
                TasksService tasksService = new TasksService();
                tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                if(!tasks.isEmpty()){ //запрос в базу, есть ли задачи
                    //если есть тут сообщение и задачи кнопками инлайн
                    BOT.execute(
                            EditMessageText
                                    .builder()
                                    .text("Для удаления задачи из списка доступных на отслеживание, нажимите на кнопку с названием задачи")
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );

                    BOT.execute(
                            EditMessageReplyMarkup
                                    .builder()
                                    .replyMarkup(
                                            InlineKeyboardMarkup
                                                    .builder()
                                                    .keyboard(Keyboards.getAllTasksKeyboard( "delete", tasks))
                                                    .build())
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );
                } else {
                    BOT.execute(
                            EditMessageText
                                    .builder()
                                    .text("У Вас нет задач, которые можно удалить")
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );

                    BOT.execute(
                            EditMessageReplyMarkup
                                    .builder()
                                    .replyMarkup(
                                            InlineKeyboardMarkup
                                                    .builder()
                                                    .keyboard(Keyboards.getBackToManageTasksKeyboard())
                                                    .build())
                                    .chatId(cbQ.getMessage().getChatId().toString())
                                    .messageId(cbQ.getMessage().getMessageId())
                                    .build()
                    );
                }
                BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
                break;
            }

            case "tracking":{
                //TODO https://stackoverflow.com/questions/6627289/what-is-the-most-recommended-way-to-store-time-in-postgresql-using-java
                TimeTableService timeTableService = new TimeTableService();
                long id = timeTableService.startTask(Integer.parseInt(callback[1]), Timestamp.from(Instant.now()));
                StringBuilder sb = new StringBuilder();
                sb.append("Начато отслеживание задачи <b><i>").append(callback[2]).append("</i></b>");
                BOT.execute(EditMessageText
                        .builder()
                        .chatId(cbQ.getMessage().getChatId().toString())
                        .messageId(cbQ.getMessage().getMessageId())
                        .text(sb.toString()).parseMode("HTML")
                        .build());
                sb.setLength(0);
                sb.append("stop:").append(id).append(":").append(callback[2]);
                BOT.execute(
                        EditMessageReplyMarkup
                                .builder()
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .replyMarkup(
                                        InlineKeyboardMarkup
                                                .builder()
                                                .keyboard(Keyboards.getStopTasksKeyboard("Остановить отслеживание", sb.toString()))
                                                .build()
                                )
                                .build()
                );
                break;
            }
            case "delete":{
                TasksService tasksService = new TasksService();
                tasksService.deleteTask(Integer.parseInt(callback[1]));
                BOT.execute(EditMessageText
                        .builder()
                        .chatId(cbQ.getMessage().getChatId().toString())
                        .messageId(cbQ.getMessage().getMessageId())
                        .text("Здача удалена! Вы можете продолжить удаление задач, или вернуться назад")
                        .build());
                List tasks = new ArrayList();
                tasks = tasksService.findByIdUserTelegram(cbQ.getFrom().getId());
                BOT.execute(
                        EditMessageReplyMarkup
                                .builder()
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .replyMarkup(
                                        InlineKeyboardMarkup
                                                .builder()
                                                .keyboard(Keyboards.getAllTasksKeyboard("delete", tasks))
                                                .build())
                                .build());
                break;
            }
            case "stop":{
                TimeTableService timeTableService = new TimeTableService();
                timeTableService.stopTask(Integer.parseInt(callback[1]), Timestamp.from(Instant.now()));
                StringBuilder sb = new StringBuilder();
                sb.append("Отслеживание задачи <b><i>").append(callback[2]).append("</i></b> завершено");
                BOT.execute(EditMessageText
                        .builder()
                        .chatId(cbQ.getMessage().getChatId().toString())
                        .messageId(cbQ.getMessage().getMessageId())
                        .text(sb.toString()).parseMode("HTML")
                        .build());
                BOT.execute(
                        EditMessageReplyMarkup
                                .builder()
                                .replyMarkup(
                                        InlineKeyboardMarkup
                                                .builder()
                                                .keyboard(Keyboards.getBackToManageTasksKeyboard("Мои задачи"))
                                                .build())
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build()
                );
                break;
            }
        }
        BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
    }
}
