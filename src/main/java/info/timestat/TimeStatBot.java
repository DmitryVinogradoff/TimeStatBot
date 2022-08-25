package info.timestat;

import info.timestat.CallbackQuertHandlers.CallbackQueryHandler;
import info.timestat.MessageHandlers.MessageHandler;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TimeStatBot extends TelegramLongPollingBot {
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
                        new MessageHandler().handleMessage(update.getMessage());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (update.hasCallbackQuery()) {
                   try {
                       new CallbackQueryHandler().handleCallbackQuery(update.getCallbackQuery());
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                }
            }
        }).start();
    }
}
