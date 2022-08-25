package info.timestat.CallbackQuertHandlers;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CallbackQueryHandler {
    String textCallbackQuery;
    String chatId;
    Integer messageId;

    public void handleCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        this.textCallbackQuery = callbackQuery.getData();
        this.chatId = callbackQuery.getMessage().getChatId().toString();
        this.messageId = callbackQuery.getMessage().getMessageId();
        switch (this.textCallbackQuery){
            case "tasks":
                break;
            case "stats_tasks":
                break;
            case "about":
                break;
        }
    }
}
