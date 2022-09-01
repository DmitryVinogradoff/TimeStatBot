package info.timestat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TimeStatApplication {
    private static TimeStatBot timeStatBot;

    @Autowired
    public void setTimeStatBot(TimeStatBot timeStatBot){
        this.timeStatBot = timeStatBot;
    }
    public static void main(String[] args) {
        SpringApplication.run(TimeStatApplication.class);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(timeStatBot);
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
