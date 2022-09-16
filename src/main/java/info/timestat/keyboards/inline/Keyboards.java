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
        int j = 0;
        while (iterator.hasNext()){
            List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
            for(int i = 0; i < buttonsOnLine && iterator.hasNext(); i++){
                Map.Entry<String, String> entry = iterator.next();
                buttonsLine.add(InlineKeyboardButton
                        .builder()
                        .text(entry.getValue())
                        .callbackData(entry.getKey())
                        .build());
                j++;
                if (j >= buttonsData.size() - 1) break;
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
        buttonsData.put("manage_tasks_menu", "Управление задачами");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getStatsTasksKeyboard(boolean data){
        buttonsData.clear();
        if (data){
            buttonsData.put("stats:day", "За день");
            buttonsData.put("stats:week", "За неделю");
            buttonsData.put("stats:month", "За месяц");
            buttonsData.put("start_menu", "Главное меню");
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
            return keyboardGenerator(2);
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
            return keyboardGenerator(2);
        } else {
            return getBackToManageTaskKeyboard();
        }
    }

    public List<List<InlineKeyboardButton>> getAfterAddingTaskKeyboard(String text, long id) {
        buttonsData.clear();
        buttonsData.put("tracking:" + id, "Начать отслеживание");
        buttonsData.put("start_menu", "Главное меню");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getAfterStartTaskKeyboard(long id) {
        buttonsData.clear();
        buttonsData.put("stop:" + id, "Остановить отслеживание");
        return keyboardGenerator(1);
    }

    public List<List<InlineKeyboardButton>> getBackToAllStatsMenuKeyboard() {
        buttonsData.clear();
        buttonsData.put("stats_tasks_menu", "Назад");
        return keyboardGenerator(1);
    }
}
