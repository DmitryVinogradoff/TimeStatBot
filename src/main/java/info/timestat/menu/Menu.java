package info.timestat.menu;

import info.timestat.TimeStatBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class Menu {
    @Autowired
    private TimeStatBot timeStatBot;

    private String messageText;
    private CallbackQuery callbackQuery;

    public void editMenu(CallbackQuery callbackQuery, String messageText,
                         List<List<InlineKeyboardButton>> keyboard) throws TelegramApiException {
        this.callbackQuery = callbackQuery;
        this.messageText = messageText;

        editMessageText();
        timeStatBot.execute(EditMessageReplyMarkup
                .builder()
                .replyMarkup(
                        InlineKeyboardMarkup
                                .builder()
                                .keyboard(keyboard)
                                .build()
                )
                .chatId(callbackQuery.getMessage().getChatId().toString())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build());
        removeClock();
    }

    public void editMenu(Message message, String messageText,
                         List<List<InlineKeyboardButton>> keyboard) throws TelegramApiException {
        timeStatBot.execute(SendMessage.builder()
                .text(messageText).parseMode("HTML")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboard(keyboard)
                        .build())
                .build());
    }


    private void editMessageText() throws TelegramApiException{
        timeStatBot.execute(EditMessageText
                .builder()
                .chatId(callbackQuery.getMessage().getChatId().toString())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(messageText).parseMode("HTML")
                .build());
    }

    private void removeClock() throws TelegramApiException {
        timeStatBot.execute(AnswerCallbackQuery
                .builder()
                .callbackQueryId(callbackQuery.getId())
                .build()
        );
    }

    public void deleteMessage(Message message) throws TelegramApiException {
        timeStatBot.execute(DeleteMessage
                .builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build());
    }
}
