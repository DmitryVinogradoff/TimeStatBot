package ru.dmitryvinogradov.Services;

import ru.dmitryvinogradov.DAO.TasksDao;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Models.TimeTable;

import java.util.ArrayList;
import java.util.List;

public class TasksService {
    private TasksDao tasksDao = new TasksDao();

    public TasksService() {
    }

    public Tasks findById(long id){
        return tasksDao.findById(id);
    }

    public long saveTask(Tasks tasks){
        return tasksDao.save(tasks);
    }

    public void updateTask(Tasks tasks){
        tasksDao.update(tasks);
    }

    public void deleteTask(long id){
        tasksDao.delete(id);
    }

    public List<Tasks> findByIdUserTelegram(long idUserTelegram){
        return tasksDao.findByIdUserTelegram(idUserTelegram);
    }

    public List<Tasks> findAll(long idUserTelegram){ return tasksDao.findAll(idUserTelegram); }

    public void addTestData(long idUserTelegram){
        List<Tasks> testDataTasks = findByIdUserTelegram(999999999);
        List<TimeTable> testDataTimeTable;
        TimeTableService timeTableService = new TimeTableService();
        for(Tasks task : testDataTasks){
            testDataTimeTable = timeTableService.getTestData(task.getId());
            task.setIdUserTelegram(idUserTelegram);
            task.setTestData(true);
            timeTableService.setTestData(testDataTimeTable,  saveTask(task));
        }
    }

    public void deleteTestData(long idUserTelegram){
        tasksDao.deleteTestData(idUserTelegram);
    }

}
