package ru.dmitryvinogradov.MessageHandlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static ru.dmitryvinogradov.GlobalConfig.BOT;

public class MessageHandler {
    private Message message;

    public void handleMessage(Message msg) throws TelegramApiException {
        this.message = msg;
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message
                        .getText()
                        .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                commandManager(command);
            } else {
                try {
                    BOT.execute(SendMessage
                            .builder()
                            .text("Я получил от тебя текст: " + message.getText())
                            .chatId(message.getChatId().toString())
                            .build()
                    );
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void commandManager (String command) throws TelegramApiException {
        switch (command){
            case "/com1":
                    BOT.execute(SendMessage
                            .builder()
                            .text("Пришла команда /com1")
                            .chatId(message.getChatId().toString())
                            .build()
                    );
                break;
            case "/com2":
                    BOT.execute(SendMessage
                            .builder()
                            .text("Пришла команда /com2")
                            .chatId(message.getChatId().toString())
                            .build()
                    );
                break;
        }
    }
}
