package info.timestat.keyboards.inline;

import info.timestat.entity.Task;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
public class Keyboards {
    Map<String, String> buttonsData = new LinkedHashMap<>();

    private List<List<InlineKeyboardButton>> keyboardGenerator(int buttonsOnLine){
        //TODO сделать кнопку Назад или какую либо другую одной на нижней строке
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
        buttonsData.put("manage_tasks_menu", "Управление задачами");
        buttonsData.put("stats_tasks_menu", "Статистика");
        buttonsData.put("about_menu", "О боте");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getBackToStartMenuKeyboard(){
        buttonsData.clear();
        buttonsData.put("start_menu", "Главное меню");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getManageTasksKeyboard(){
        buttonsData.clear();
        buttonsData.put("tracking_task_menu", "Отслеживание задачи");
        buttonsData.put("add_task_menu", "Добавить задачу");
        buttonsData.put("delete_task_menu", "Удалить задачу");
        buttonsData.put("start_menu", "Главное меню");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getBackToManageTaskKeyboard(){
        buttonsData.clear();
        buttonsData.put("manage_tasks_menu", "Назад");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getStatsTasksKeyboard(boolean data){
        buttonsData.clear();
        if (data){
            buttonsData.put("stats_day", "За день");
            buttonsData.put("stats_week", "За неделю");
            buttonsData.put("stats_month", "За месяц");
            buttonsData.put("manage_tasks_menu", "Назад");
            return keyboardGenerator(3);
        } else {
            buttonsData.put("start_menu", "Назад");
            return keyboardGenerator(1);
        }
    }

    public List<List<InlineKeyboardButton>> getTrackingTasksKeyboard(List<Task> taskList) {
        if (!taskList.isEmpty()){
            buttonsData.clear();
            for(Task task : taskList){
                buttonsData.put("tracking:" + task.getId(), task.getName());
            }
            buttonsData.put("manage_tasks_menu", "Управление задачами");
            return keyboardGenerator(3);
        } else {
            return getBackToManageTaskKeyboard();
        }
    }

    public List<List<InlineKeyboardButton>> getDeleteTasksKeyboard(List<Task> taskList) {
        if (!taskList.isEmpty()){
            buttonsData.clear();
            for(Task task : taskList){
                buttonsData.put("delete:" + task.getId(), task.getName());
            }
            buttonsData.put("manage_tasks_menu", "Управление задачами");
            return keyboardGenerator(3);
        } else {
            return getBackToManageTaskKeyboard();
        }
    }

    public List<List<InlineKeyboardButton>> getAfterAddingTaskKeyboard(String text, long id) {
        //TODO добавить кнопку исправить название задачи
        buttonsData.clear();
        buttonsData.put("start_task_menu:" + id, "Начать отслеживание");
        buttonsData.put("start_menu", "Главное меню");
        return keyboardGenerator(1);
    }
}
