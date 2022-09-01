package info.timestat.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static info.timestat.GlobalConfig.BOT;

public class Menu {
    private String chatId;
    private Integer messageId;
    private String messageText;
    private List<List<InlineKeyboardButton>> keyboard;

    public void editMenu(String chatId, Integer messageId, String messageText,
                         List<List<InlineKeyboardButton>> keyboard) throws TelegramApiException {
        this.chatId = chatId;
        this.messageId = messageId;
        this.messageText = messageText;
        this.keyboard = keyboard;

        editMessageText();
        BOT.execute(EditMessageReplyMarkup
                .builder()
                .replyMarkup(
                        InlineKeyboardMarkup
                                .builder()
                                .keyboard(keyboard)
                                .build()
                )
                .chatId(chatId)
                .messageId(messageId)
                .build());
    }

    private void editMessageText() throws TelegramApiException{
        BOT.execute(EditMessageText
                .builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText).parseMode("HTML")
                .build());
    }
}
