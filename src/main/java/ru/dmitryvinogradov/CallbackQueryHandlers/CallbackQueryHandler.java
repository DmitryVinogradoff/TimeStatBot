package ru.dmitryvinogradov.CallbackQueryHandlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static ru.dmitryvinogradov.GlobalConfig.BOT;

public class CallbackQueryHandler {
    private CallbackQuery callbackQuery;
    public void callbackMessage(CallbackQuery cbQ) throws TelegramApiException {
        this.callbackQuery = cbQ;
        String callback = callbackQuery.getData();
        switch (callback){
            case "but1":
                BOT.execute(
                        SendMessage
                                .builder()
                                .text("Нажата кнопка 1")
                                .chatId(callbackQuery.getMessage().getChatId().toString())
                                .build());
                break;
            case "but2":
                BOT.execute(
                        SendMessage
                                .builder()
                                .text("Нажата кнопка 2")
                                .chatId(callbackQuery.getMessage().getChatId().toString())
                                .build());
                break;
        }
    }
}
