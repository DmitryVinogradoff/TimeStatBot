package info.timestat.Keyboards.Inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {
    private String[] captionOnButton;
    private String[] callbackOnButton;

    private void setData(String[] captionOnButton, String[] callbackOnButton) {
        this.captionOnButton = captionOnButton;
        this.callbackOnButton = callbackOnButton;
    }

    private List<List<InlineKeyboardButton>> keyboardGenerator(int buttonsOnLine){
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

    public List<List<InlineKeyboardButton>> getStartMenuKeyboard(){
        setData(new String[] {"Управление задачами", "Статистика", "О боте"}, new String[] {"tasks", "stats_tasks", "about"});
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getBackToStartMenuKeyboard(){
        setData(new String[] {"Назад"}, new String[] {"start_menu"});
        return keyboardGenerator(1);
    }

}
