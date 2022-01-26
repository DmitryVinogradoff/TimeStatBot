package ru.dmitryvinogradov.Services;

import ru.dmitryvinogradov.DAO.TimeTableDao;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Models.TimeTable;

import java.util.List;

public class TimeTableService {
    public TimeTableService() {
    }

    TimeTableDao timeTableDao = new TimeTableDao();

    public TimeTable findById(long id){
        return timeTableDao.findById(id);
    }

    public void saveTimeTable(TimeTable timeTable){
        timeTableDao.save(timeTable);
    }

    public void updateTimeTable(TimeTable timeTable){
        timeTableDao.update(timeTable);
    }

    public void deleteTimeTable(long id){
        timeTableDao.delete(id);
    }

}
