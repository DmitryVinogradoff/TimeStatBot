package info.timestat.menu;

import org.springframework.stereotype.Component;

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
            return "Статистика";
        } else {
            return "У Вас нет статистики по задачам. Добавьте задачу в меню \"Управление задачами\" и начните ее отслеживание";
        }
    }

    public String getTrackingTasksMenuText(boolean data) {
        if (data){
            return "Нажмите на названия задачи для начала отслеживания";
        } else {
            return "Список Ваших задач пуст";
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
        return "Задача <i><b>" + taskName + "</b></i> добавлена.\nЧтобы начать ее отслеживание нажмите кнопку \"Начать отслеживание\"";
    }
}
