package info.timestat.Keyboards.Inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {
    public static List<List<InlineKeyboardButton>> getStartMenuKeyboard(){
        String[] captionOnButton = new String[] {"Управление задачами", "Статистика", "О боте"};
        String[] callbackOnButton = new String[] {"tasks", "stats_tasks", "about"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    private static List<List<InlineKeyboardButton>> keyboardGenerator(int buttonsOnLine, String[] captionOnButton,
                                                                      String[] callbackOnButton){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for(int i = 0; i < captionOnButton.length;){
            List<InlineKeyboardButton> b = new ArrayList<>();
            for(int j = 0; j < buttonsOnLine && i < captionOnButton.length; j++){
                if(callbackOnButton[i]!=null && !callbackOnButton[i].isBlank()) {
                    b.add(InlineKeyboardButton
                            .builder()
                            .text(captionOnButton[i])
                            .callbackData(callbackOnButton[i])
                            .build());
                }
                i++;
            }
            buttons.add(b);
        }
        return buttons;
    }
}
