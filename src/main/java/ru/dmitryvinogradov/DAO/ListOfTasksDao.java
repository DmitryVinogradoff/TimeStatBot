package ru.dmitryvinogradov.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.dmitryvinogradov.Models.ListOfTasks;
import ru.dmitryvinogradov.Models.Users;
import ru.dmitryvinogradov.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class ListOfTasksDao {
    public ListOfTasks findById(int id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        ListOfTasks listOfTasks = session.get(ListOfTasks.class, id);
        session.close();
        return listOfTasks;
    }

    public void save(ListOfTasks listOfTasks){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(listOfTasks);
        transaction.commit();
        session.close();
    }

    public void update(ListOfTasks listOfTasks){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(listOfTasks);
        transaction.commit();
        session.close();
    }

    public void delete(ListOfTasks listOfTasks){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(listOfTasks);
        transaction.commit();
        session.close();
    }

    public List<ListOfTasks> findByIdUserTelegram(int idUserTelegram){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query =
                session.createQuery("FROM ListOfTasks WHERE idUserTelegram = :idUserTelegram")
                        .setParameter("idUserTelegram", idUserTelegram);

        List<ListOfTasks> listOfTasks = query.list();
        session.close();
        return listOfTasks;
    }
}
