package ru.dmitryvinogradov.MessageHandlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton
                        .builder()
                        .text("Кнопка 1")
                        .callbackData("but1")
                        .build(),
                InlineKeyboardButton
                        .builder()
                        .text("Кнопка 2")
                        .callbackData("but2")
                        .build()));
        switch (command){
            case "/start":
                    BOT.execute(SendMessage
                            .builder()
                            .text("Пришла команда /com1")
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
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
