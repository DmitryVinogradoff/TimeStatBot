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

    public void saveTask(Tasks tasks){
        tasksDao.save(tasks);
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

}
