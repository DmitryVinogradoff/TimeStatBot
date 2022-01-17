package ru.dmitryvinogradov;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dmitryvinogradov.CallbackQueryHandlers.CallbackQueryHandler;
import ru.dmitryvinogradov.MessageHandlers.MessageHandler;


public class TimeStatBot extends TelegramLongPollingBot {
    public String getBotToken () {return BotConfig.BOT_TOKEN;}
    public String getBotUsername () {return BotConfig.USERNAME;}

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            try {
                new MessageHandler().handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            try {
                new CallbackQueryHandler().callbackMessage(update.getMessage());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

}
