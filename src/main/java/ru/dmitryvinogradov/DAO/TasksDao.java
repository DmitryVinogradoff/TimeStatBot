package ru.dmitryvinogradov.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class TasksDao {
    public Tasks findById(long id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Tasks tasks = session.get(Tasks.class, id);
        session.close();
        return tasks;
    }

    public long save(Tasks tasks){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(tasks);
        long id = tasks.getId();
        transaction.commit();
        session.close();
        return id;
    }

    public void update(Tasks tasks){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(tasks);
        transaction.commit();
        session.close();
    }

    public void delete(long id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("DELETE FROM Tasks WHERE id = :id").setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    public List<Tasks> findByIdUserTelegram(long idUserTelegram){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query = session.createQuery("FROM Tasks WHERE idUserTelegram = :idUserTelegram")
                .setParameter("idUserTelegram", idUserTelegram);

        List<Tasks> tasks = query.list();
        session.close();
        return tasks;
    }

    public List<Tasks> findAll(long idUserTelegram){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Tasks> tasks = session.createQuery("FROM Tasks WHERE idUserTelegram = :idUserTelegram")
                .setParameter("idUserTelegram", idUserTelegram).list();
        session.close();
        return tasks;
    }
}
