package ru.dmitryvinogradov.Services;

import ru.dmitryvinogradov.DAO.TimeTableDao;
import ru.dmitryvinogradov.Models.TimeTable;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public class TimeTableService {
    public TimeTableService() {
    }

    TimeTableDao timeTableDao = new TimeTableDao();

    public TimeTable findById(long id){
        return timeTableDao.findById(id);
    }

    public long startTask(long idTask, Timestamp statedAt){
        TimeTable timeTable = new TimeTable(idTask, statedAt, true);
        return timeTableDao.save(timeTable);
    }

    public long stopTask(long id){
        Timestamp stoppedAt = Timestamp.from(Instant.now());
        TimeTable timeTable = timeTableDao.findById(id);
        timeTable.setStoppedAt(stoppedAt);
        timeTable.setStatus(false);
        timeTableDao.update(timeTable);
        return id;
    }

    public void updateTimeTable(TimeTable timeTable){ timeTableDao.update(timeTable); }

    public void deleteTimeTable(long id){
        timeTableDao.delete(id);
    }

    public String taskStat(long idTask, String period){
        Timestamp timestampNow = Timestamp.from(Instant.now());
        Timestamp timestampAgo =  Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        switch (period){
            case "day":
                timestampAgo =  Timestamp.valueOf(LocalDateTime.now().minusDays(1));
                break;
            case "week":
                timestampAgo =  Timestamp.valueOf(LocalDateTime.now().minusDays(7));
                break;
            case "month":
                timestampAgo =  Timestamp.valueOf(LocalDateTime.now().minusMonths(1));
                break;
        }
        return timeTableDao.taskStat(idTask, timestampNow, timestampAgo);
    }

}
