package ru.dmitryvinogradov.CallbackQueryHandlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitryvinogradov.Keyboards.Inline.Keyboards;

import static ru.dmitryvinogradov.GlobalConfig.BOT;

public class CallbackQueryHandler {
    private CallbackQuery callbackQuery;
    public void callbackMessage(CallbackQuery cbQ) throws TelegramApiException {
        this.callbackQuery = cbQ;
        String callback = callbackQuery.getData();
        switch (callback){
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
                BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
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

                BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());//убираем "часики"
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
                    BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
                }
                break;
            }
            case "about":{
                break;
            }

            case "tracking_task":{
                if(false){ //запрос в базу, есть ли задачи
                    //если есть тут сообщение и задачи кнопками инлайн
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
                BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
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
                BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
                break;
            }
            case "delete_task_menu":{
                if(false) { //тут запрос в базу, есть ли какие либо задачи
                    // тут инлайн клавиатура с кнопками с задачами и свое сообщение
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


        }
    }
}
