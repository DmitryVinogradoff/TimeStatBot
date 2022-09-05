package info.timestat.keyboards.inline;

import info.timestat.entity.Task;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
public class Keyboards {
    Map<String, String> buttonsData = new LinkedHashMap<>();

    private List<List<InlineKeyboardButton>> keyboardGenerator(int buttonsOnLine){
        Iterator<Map.Entry<String, String >> iterator = buttonsData.entrySet().iterator();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        while (iterator.hasNext()){
            List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
            for(int i = 0; i < buttonsOnLine && iterator.hasNext(); i++){
                Map.Entry<String, String> entry = iterator.next();
                buttonsLine.add(InlineKeyboardButton
                        .builder()
                        .text(entry.getValue())
                        .callbackData(entry.getKey())
                        .build());
            }
            buttons.add(buttonsLine);
        }
        return buttons;
    }

    public List<List<InlineKeyboardButton>> getStartMenuKeyboard(){
        buttonsData.clear();
        buttonsData.put("manage_tasks", "Управление задачами");
        buttonsData.put("stats_tasks", "Статистика");
        buttonsData.put("about", "О боте");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getBackToStartMenuKeyboard(){
        buttonsData.clear();
        buttonsData.put("start_menu", "Главное меню");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getManageTasksKeyboard(){
        buttonsData.clear();
        buttonsData.put("tracking_task", "Отслеживание задачи");
        buttonsData.put("add_task", "Добавить задачу");
        buttonsData.put("delete_task", "Удалить задачу");
        buttonsData.put("start_menu", "Главное меню");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getBackToManageTaskKeyboard(){
        buttonsData.clear();
        buttonsData.put("manage_tasks", "Назад");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getStatsTasksKeyboard(boolean data){
        buttonsData.clear();
        if (data){
            buttonsData.put("stats_day", "За день");
            buttonsData.put("stats_week", "За неделю");
            buttonsData.put("stats_month", "За месяц");
            buttonsData.put("manage_tasks", "Назад");
            return keyboardGenerator(3);
        } else {
            buttonsData.put("start_menu", "Назад");
            return keyboardGenerator(1);
        }
    }

    public List<List<InlineKeyboardButton>> getTrackingTasksKeyboard(boolean data) {

        if (data){
            //TODO создать клавиатуру со списком задач
            buttonsData.clear();
            return keyboardGenerator(3);
        } else {
            return getBackToManageTaskKeyboard();
        }
    }

    public List<List<InlineKeyboardButton>> getDeleteTasksKeyboard(List<Task> taskList) {
        if (!taskList.isEmpty()){
            List<String> captionOnButton = new LinkedList<>();
            List<String> callbackOnButton = new LinkedList<>();
            for(Task task : taskList){

            }
            //TODO создать клавиатуру со списком задач
            buttonsData.clear();
            return keyboardGenerator(3);
        } else {
            return getBackToManageTaskKeyboard();
        }
    }

    public List<List<InlineKeyboardButton>> getAfterAddingTaskKeyboard(String text, long id) {
        //TODO добавить кнопку исправить название задачи
        buttonsData.clear();
        buttonsData.put("start_task:" + id, "Начать отслеживание");
        buttonsData.put("start_menu", "Главное меню");
        return keyboardGenerator(1);
    }
}
