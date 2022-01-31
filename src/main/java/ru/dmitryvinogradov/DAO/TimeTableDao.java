package ru.dmitryvinogradov.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.dmitryvinogradov.Models.TimeTable;
import ru.dmitryvinogradov.utils.HibernateSessionFactoryUtil;

import java.sql.Timestamp;
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
        Query query = session.createQuery("DELETE FROM TimeTable WHERE id = :id").setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    public String taskStat (long idTask, Timestamp timestampNow, Timestamp timestampAgo){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query sumQuery = session
                .createQuery(
                "SELECT TO_CHAR(SUM(stoppedAt-startedAt), 'HH24:MI') " +
                "FROM TimeTable WHERE idTask = :idTask " +
                "AND startedAt BETWEEN TO_TIMESTAMP(:timestampAgo, 'YYYY-MM-DD HH24:MI:SS.US') " +
                "AND TO_TIMESTAMP(:timestampNow, 'YYYY-MM-DD HH24:MI:SS.US') " +
                "GROUP BY idTask")
                .setParameter("idTask", idTask)
                .setParameter("timestampNow", timestampNow)
                .setParameter("timestampAgo", timestampAgo);
        List result = sumQuery.list();
        session.close();
        if(!result.isEmpty()){
            return result.get(0).toString();
        } else {
            return null;
        }
    }
}
