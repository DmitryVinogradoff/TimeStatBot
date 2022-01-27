package ru.dmitryvinogradov.Services;

import ru.dmitryvinogradov.DAO.TimeTableDao;
import ru.dmitryvinogradov.Models.TimeTable;

import java.sql.Timestamp;

public class TimeTableService {
    public TimeTableService() {
    }

    TimeTableDao timeTableDao = new TimeTableDao();

    public TimeTable findById(long id){
        return timeTableDao.findById(id);
    }

    public long startTask(long idTask, Timestamp statedAt){
        TimeTable timeTable = new TimeTable(idTask, statedAt, true);
        long id = timeTableDao.save(timeTable);
        return id;
    }

    public long stopTask(long id, Timestamp stoppedAt){
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

}
