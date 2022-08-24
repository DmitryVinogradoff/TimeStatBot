package info.timestat.MessageHandlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import static info.timestat.GlobalConfig.BOT;

public class MessageHandler {
    private Message message;


    public void handleMessage(Message message) throws TelegramApiException {
        this.message = message;
        if(message.hasText()){
            BOT.execute(SendMessage
                    .builder()
                    .chatId(message.getChatId().toString())
                    .text(message.getText())
                    .build());
        }
    }
}
