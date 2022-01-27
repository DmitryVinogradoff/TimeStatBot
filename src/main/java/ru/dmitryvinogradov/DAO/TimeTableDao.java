package ru.dmitryvinogradov.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Models.TimeTable;
import ru.dmitryvinogradov.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class TimeTableDao {
    public TimeTable findById(long id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        TimeTable timeTable = session.get(TimeTable.class, id);
        session.close();
        return timeTable;
    }
    //TODO попробовать убрать дублирование кода и эти функции вынести в общий файл
    public long save(TimeTable timeTable){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        long id = (long) session.save(timeTable);
        transaction.commit();
        session.close();
        return id;
    }

    public void update(TimeTable timeTable){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(timeTable);
        transaction.commit();
        session.close();
    }

    public void delete(long id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query =
                session.createQuery("DELETE FROM TimeTable WHERE id = :id")
                        .setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
        session.close();
    }
}
