package ru.dmitryvinogradov.Services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.dmitryvinogradov.DAO.ListOfTasksDao;
import ru.dmitryvinogradov.Models.ListOfTasks;
import ru.dmitryvinogradov.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class ListOfTasksService {
    private ListOfTasksDao listOfTasksDao = new ListOfTasksDao();

    public ListOfTasksService() {
    }

    public ListOfTasks findById(int id){
        return listOfTasksDao.findById(id);
    }

    public void saveListOfTasks(ListOfTasks listOfTasks){
        listOfTasksDao.save(listOfTasks);
    }

    public void updateListOfTasks(ListOfTasks listOfTasks){
        listOfTasksDao.update(listOfTasks);
    }

    public void deleteListOfTask(ListOfTasks listOfTasks){
        listOfTasksDao.delete(listOfTasks);
    }

    public List<ListOfTasks> findByIdUserTelegram(int idUserTelegram){
        return listOfTasksDao.findByIdUserTelegram(idUserTelegram);
    }
}
