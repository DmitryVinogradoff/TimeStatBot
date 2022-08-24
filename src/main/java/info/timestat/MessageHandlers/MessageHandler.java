package info.timestat.MessageHandlers;

import info.timestat.Keyboards.Inline.Keyboards;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static info.timestat.GlobalConfig.BOT;

public class MessageHandler {
    private Message message;


    public void handleMessage(Message message) throws TelegramApiException {
        this.message = message;
        if (message.hasText()) {
            if (message.hasEntities()) {
                Optional<MessageEntity> commandEntity =
                        message.getEntities().stream().filter(e -> "bot_command" .equals(e.getType())).findFirst();
                if (commandEntity.isPresent()) {
                    String command = message
                            .getText()
                            .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                    commandManager(command);
                }
            } else {
                messageManager(message.getText());
            }
        }
    }

    private void commandManager(String command) throws TelegramApiException {
        switch (command) {
            case "/start":
                StringBuilder sb = new StringBuilder();
                sb.append("Привет, ").append(message.getFrom().getFirstName()).append("!\n");
                sb.append("Я TimeStatBot, предназначеный для учета и анализа времени, затраченного на выполнение каких-либо задач\n\n");
                sb.append("Я помогу понять, на что было потрачено время в течении дня, недели или месяца\n\n");
                BOT.execute(SendMessage.builder().
                        text(sb.toString())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Keyboards.getStartMenuKeyboard()).build())
                        .build());
                break;
        }
    }

    private void messageManager(String text) {

    }
}
