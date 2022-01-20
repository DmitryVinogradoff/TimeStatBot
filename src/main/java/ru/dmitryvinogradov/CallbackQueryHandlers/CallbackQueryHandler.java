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
            case "tasks":
                BOT.execute(EditMessageText
                        .builder()
                        .text("Меню управления задачами")
                        .chatId(cbQ.getMessage().getChatId().toString())
                        .messageId(cbQ.getMessage().getMessageId())
                        .build());
                BOT.execute(
                        EditMessageReplyMarkup
                                .builder()
                                .replyMarkup(
                                        InlineKeyboardMarkup
                                        .builder()
                                        .keyboard(Keyboards.getManageTaskKeyboard())
                                        .build()
                                )
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build()
                );
                BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
                break;
            case "start_menu":
                BOT.execute(EditMessageText
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
                                                .keyboard(Keyboards.getStartKeyboard())
                                                .build()
                                )
                                .chatId(cbQ.getMessage().getChatId().toString())
                                .messageId(cbQ.getMessage().getMessageId())
                                .build()
                );
                BOT.execute(AnswerCallbackQuery.builder().callbackQueryId(cbQ.getId()).build());
        }
    }
}
