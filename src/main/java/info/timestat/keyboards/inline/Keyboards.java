package info.timestat.keyboards.inline;

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
        setData(new String[] {"Управление задачами", "Статистика", "О боте"},
                new String[] {"manage_tasks", "stats_tasks", "about"});
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getBackToStartMenuKeyboard(){
        setData(new String[] {"Главное меню"}, new String[] {"start_menu"});
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getManageTasksKeyboard(){
        setData(new String[] {"Отслеживание задачи", "Добавить задачу", "Удалить задачу", "Главное меню"},
                new String[]{"tracking_task", "add_task", "delete_task", "start_menu"});
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getBackToManageTaskKeyboard(){
        setData(new String[] {"Назад"}, new String[] {"manage_tasks"});
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getStatsTasksKeyboard(boolean data){
        if (data){
            setData(new String[] {"За день", "За неделю", "За месяц", "Назад"},
                    new String[] {"stats_day", "stats_week", "stats_month","manage_tasks"});
            return keyboardGenerator(3);
        } else {
            setData(new String[] {"Назад"}, new String[] {"start_menu"});
            return keyboardGenerator(1);
        }
    }

    public List<List<InlineKeyboardButton>> getTrackingTasksKeyboard(boolean data) {
        if (data){
            //TODO создать клавиатуру со списком задач
            return keyboardGenerator(3);
        } else {
            setData(new String[] {"Назад"}, new String[] {"manage_tasks"});
            return keyboardGenerator(1);
        }
    }

    public List<List<InlineKeyboardButton>> getDeleteTasksKeyboard(boolean data) {
        if (data){
            //TODO создать клавиатуру со списком задач
            return keyboardGenerator(3);
        } else {
            setData(new String[] {"Назад"}, new String[] {"manage_tasks"});
            return keyboardGenerator(1);
        }
    }
}
