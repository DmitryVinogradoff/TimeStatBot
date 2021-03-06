package ru.dmitryvinogradov.Services;

import ru.dmitryvinogradov.DAO.TasksDao;
import ru.dmitryvinogradov.Models.Tasks;

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

    public List<Tasks> findTestData(long idUserTelegram){ return tasksDao.findTestData(idUserTelegram); }

    public void deleteTestData(long idUserTelegram){
        tasksDao.deleteTestData(idUserTelegram);
    }

}
