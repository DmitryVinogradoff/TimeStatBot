package ru.dmitryvinogradov.Keyboards.Inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {
    public static List<List<InlineKeyboardButton>> getStartMenuKeyboard(){
        String[] captionOnButton = new String[] {"Мои задачи", "Статистика", "О боте"};
        String[] callbackOnButton = new String[] {"tasks", "stats_tasks", "about"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getManageTasksKeyboard(){
        //сюда данные будт приходить из базы
        String[] captionOnButton = new String[] {"Отслеживание задач", "Добавить задачу", "Удалить задачу", "Назад"};
        String[] callbackOnButton = new String[] {"tracking_task", "add_task_menu", "delete_task_menu", "start_menu"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getBackToStartMenuKeyboard(){
        String[] captionOnButton = new String[] {"Назад"};
        String[] callbackOnButton = new String[] {"start_menu"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getBackToManageTasksKeyboard(){
        String[] captionOnButton = new String[] {"Назад"};
        String[] callbackOnButton = new String[] {"tasks"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }
    //метод для клавиатуры возврата из добавления задач
    public static List<List<InlineKeyboardButton>> getBackToManageTasksKeyboard(String text){
        String[] captionOnButton = new String[] {text};
        String[] callbackOnButton = new String[] {"tasks"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    private static List<List<InlineKeyboardButton>> keyboardGenerator(int buttonsOnLine, String[] captionOnButton, String[] callbackOnButton){
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
