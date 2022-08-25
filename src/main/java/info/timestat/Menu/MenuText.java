package info.timestat.Menu;

public class MenuText {
    public String getStartMenu(String userName){
        StringBuilder sb = new StringBuilder();
        sb.append("Привет, ").append(userName).append("!\n");
        sb.append("Я TimeStatBot, предназначеный для учета и анализа времени, затраченного на выполнение каких-либо задач\n\n");
        sb.append("Я помогу понять, на что было потрачено время в течении дня, недели или месяца\n\n");
        return sb.toString();
    }


    public String aboutBotMenuText() {
        return "© TimeStatBot 2022";
    }
}
