package ru.dmitryvinogradov;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static ru.dmitryvinogradov.GlobalConfig.BOT;

public class Menu {

    public static void editMenu(String chatId, Integer messageId, String messageText, List<List<InlineKeyboardButton>> keyboard) throws TelegramApiException {
        BOT.execute(EditMessageText
                .builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText).parseMode("HTML")
                .build());
        BOT.execute(
                EditMessageReplyMarkup
                        .builder()
                        .replyMarkup(
                                InlineKeyboardMarkup
                                        .builder()
                                        .keyboard(keyboard)
                                        .build())
                        .chatId(chatId)
                        .messageId(messageId)
                        .build()
        );
    }

    public static void editMenuWithStatsSave(String chatId, Integer messageId, String messageText, List<List<InlineKeyboardButton>> keyboard, String oldMessageText) throws TelegramApiException {
        BOT.execute(
                EditMessageText
                .builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(oldMessageText).parseMode("HTML")
                .build());
        BOT.execute(
                SendMessage
                        .builder()
                        .text(messageText).parseMode("HTML")
                        .replyMarkup(
                                InlineKeyboardMarkup
                                        .builder()
                                        .keyboard(keyboard)
                                        .build())
                        .chatId(chatId)
                        .build()
        );
    }

    public static void editMenuWithStats(String chatId, Integer messageId, String messageText,
                                  List<List<InlineKeyboardButton>> keyboard, InputFile inputFile)
                                                        throws TelegramApiException {
        BOT.execute(DeleteMessage
                        .builder()
                        .messageId(messageId)
                        .chatId(chatId)
                        .build()
        );

        BOT.execute(SendPhoto
                .builder()
                .chatId(chatId)
                .photo(inputFile)
                .build()
        );

        BOT.execute(
                SendMessage
                        .builder()
                        .text(messageText).parseMode("HTML")
                        .replyMarkup(
                                InlineKeyboardMarkup
                                        .builder()
                                        .keyboard(keyboard)
                                        .build())
                        .chatId(chatId)
                        .build()
        );

    }
}
