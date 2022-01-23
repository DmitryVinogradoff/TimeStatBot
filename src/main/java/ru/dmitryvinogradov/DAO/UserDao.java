package ru.dmitryvinogradov.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.dmitryvinogradov.Models.Users;
import ru.dmitryvinogradov.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class UserDao {
    public Users findById(int id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Users users = session.get(Users.class, id);
        session.close();
        return users;
    }

    public void save(Users users){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(users);
        transaction.commit();
        session.close();
    }

    public void update(Users users){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(users);
        transaction.commit();
        session.close();
    }

    public void delete(Users users){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(users);
        transaction.commit();
        session.close();
    }

    public List<Users> findAll(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Users> users = (List<Users>) session.createQuery("FROM Users").list();
        session.close();
        return users;
    }
}
