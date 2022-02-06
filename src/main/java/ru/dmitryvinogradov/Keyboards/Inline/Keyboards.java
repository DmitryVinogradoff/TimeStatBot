package ru.dmitryvinogradov.Keyboards.Inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.dmitryvinogradov.Models.Tasks;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {
    public static List<List<InlineKeyboardButton>> getStartMenuKeyboard(){
        String[] captionOnButton = new String[] {"Управление задачами", "Статистика", "О боте", "Тестовые данные"};
        String[] callbackOnButton = new String[] {"tasks", "stats_tasks", "about", "test_data"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getTestDatasetMenuKeyboard(){
        String[] captionOnButton = new String[] {"Добавить тестовые данные", "Удалить тестовые данные", "Назад"};
        String[] callbackOnButton = new String[] {"add_test_data", "delete_test_data", "start_menu"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getManageTasksKeyboard(){
        String[] captionOnButton = new String[] {"Отслеживание задач", "Добавить задачу", "Удалить задачу", "Главное меню"};
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

    public static List<List<InlineKeyboardButton>> getBackToStatsTasksKeyboard(){
        String[] captionOnButton = new String[] {"Назад"};
        String[] callbackOnButton = new String[] {"stats_tasks"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getBackToStatsTasksKeyboardWithSave(){
        String[] captionOnButton = new String[] {"Назад"};
        String[] callbackOnButton = new String[] {"stats_tasks:save"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }
    //метод для клавиатуры возврата из добавления задач
    public static List<List<InlineKeyboardButton>> getBackToManageTasksKeyboard(String text){
        String[] captionOnButton = new String[] {text};
        String[] callbackOnButton = new String[] {"tasks"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getBackToManageTasksKeyboard(long idTask, String taskName){
        StringBuilder sb = new StringBuilder();
        sb.append("tracking:").append(idTask).append(":").append(taskName);
        String[] captionOnButton = new String[] {"Начать отслеживание", "Управление задачами"};
        String[] callbackOnButton = new String[] {sb.toString(),"tasks"};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getStopTasksKeyboard(String text, String callback){
        String[] captionOnButton = new String[] {text};
        String[] callbackOnButton = new String[] {callback};
        return keyboardGenerator(1, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getAllTasksKeyboard(String type, List<Tasks> tasks){
        int count = tasks.size();
        int i = 0;
        String[] captionOnButton = new String[count + 1];
        String[] callbackOnButton = new String[count + 1];
        StringBuilder sb = new StringBuilder();
        for(Tasks task : tasks){
            captionOnButton[i] = task.getName();
            sb.append(type).append(":").append(task.getId()).append(":").append(task.getName());
            callbackOnButton[i] = sb.toString();
            i++;
            sb.setLength(0);
        }
        captionOnButton[i] = "Управление задачами";
        callbackOnButton[i] = "tasks";
        return keyboardGenerator(2, captionOnButton, callbackOnButton);
    }

    public static List<List<InlineKeyboardButton>> getChoicePeriodStatsKeyboard(){
        String[] captionOnButton = new String[] {"День", "Неделя", "Месяц", "Назад"};
        String[] callbackOnButton = new String[] {"period_of_stats:day", "period_of_stats:week", "period_of_stats:month", "start_menu"};
        return keyboardGenerator(3, captionOnButton, callbackOnButton);
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
