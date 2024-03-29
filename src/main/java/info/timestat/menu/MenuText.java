package info.timestat.menu;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class MenuText {
    public String getStartMenu(String userName){
        StringBuilder sb = new StringBuilder();
        sb.append("Привет, ").append(userName).append("!\n");
        sb.append("Я TimeStatBot, предназначеный для учета и анализа времени, затраченного на выполнение каких-либо задач\n\n");
        sb.append("Я помогу понять, на что было потрачено время в течении дня, недели или месяца\n\n");
        return sb.toString();
    }


    public String getAboutBotMenuText() {
        return "© TimeStatBot 2022";
    }

    public String getControlTasksMenuText() { return "Управление задачами"; }

    public String getAddTaskMenuText() { return "Напишите боту название задачи"; }

    public String getStatsTasksMenuText(boolean data) {
        if (data){
            return "Выберите период, за которых хотите просмотреть статистику";
        } else {
            return "У Вас нет статистики по задачам. Добавьте задачу в меню <i><b>«Управление задачами»</b></i> и начните ее отслеживание";
        }
    }

    public String getTrackingTasksMenuText(boolean data) {
        if (data){
            return "Нажмите на названия задачи для начала отслеживания";
        } else {
            return "Список Ваших задач пуст. Добавьте задачи в меню <i><b>«Управление задачами»</b></i>";
        }
    }

    public String getDeleteTasksMenuText(boolean data) {
        if (data){
            return "Нажмите на названия задачи, которую хотите удалить";
        } else {
            return "Список Ваших задач пуст";
        }
    }

    public String getAfterAddingTaskMenu(String taskName){
        return "Задача <i><b>" + taskName + "</b></i> добавлена.\nЧтобы начать её отслеживание нажмите кнопку <i><b>«Начать отслеживание»</b></i>";
    }

    public String getAfterStartTaskMenu() {
        return "Начато отслеживание задачи. Чтобы остановить отслеживание нажмите кнопку <i><b>«Остановить отслеживание»</b></i>";
    }

    public String getAfterStopTaskMenu() {
        return "Отслеживание задачи остановлено";
    }

    public String getWithStatsMenuText(Map<String, Duration> stats, String period) {
        StringBuilder sb = new StringBuilder();
        sb.append("Статистика по задачам: \n");
        for (Map.Entry<String, Duration> entry : stats.entrySet()) {
            sb.append("<b><i>").append(entry.getKey()).append(":</i></b> ");

            if (!period.equals("day")) sb.append(entry.getValue().toDaysPart()).append(" дней ");

            sb.append(entry.getValue().toHoursPart()).append(" часов ")
                    .append(entry.getValue().toMinutesPart()).append(" минут")
                    .append("\n");
        }
        return sb.toString();
    }

    public String getTaskIsAlreadyPresentMenu(String taskName) {
        return "Задача <i><b>" + taskName + "</b></i> уже существует.\nЧтобы начать её отслеживание нажмите кнопку <i><b>«Начать отслеживание»</b></i>";
    }

    public String getChangeTaskNameText() {
        return "Введите новое название задачи";
    }

    public String getAfterRenameTaskMenu(String oldTaskName, String newTaskName) {
        return "Задача <i><b>" + oldTaskName + "</b></i> переименована в <i><b>" +newTaskName + "</b></i>.\nЧтобы начать её отслеживание нажмите кнопку <i><b>«Начать отслеживание»</b></i>";
    }
}
