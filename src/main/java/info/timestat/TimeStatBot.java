package info.timestat;

import info.timestat.callbackquery.handlers.CallbackQueryHandler;
import info.timestat.message.handlers.MessageHandler;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
public class TimeStatBot extends TelegramLongPollingBot {
    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private CallbackQueryHandler callbackQueryHandler;

    @Override
    public String getBotUsername() { return Dotenv.configure().filename("key.env").load().get("BOT_NAME"); }

    @Override
    public String getBotToken(){ return Dotenv.configure().filename("key.env").load().get("BOT_TOKEN"); }

    @Override
    public void onUpdateReceived(Update update) {
        new Thread(new Runnable() {
            public void run() {
                if (update.hasMessage()) {
                    try {
                        messageHandler.handleMessage(update.getMessage());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (update.hasCallbackQuery()) {
                   try {
                       callbackQueryHandler.handleCallbackQuery(update.getCallbackQuery());
                   } catch (TelegramApiException | IOException e) {
                       e.printStackTrace();
                   }
                }
            }
        }).start();
    }
}
