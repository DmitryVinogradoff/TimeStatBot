package ru.dmitryvinogradov.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.dmitryvinogradov.Models.TimeTable;
import ru.dmitryvinogradov.utils.HibernateSessionFactoryUtil;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class TimeTableDao {
    public TimeTable findById(long id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        TimeTable timeTable = session.get(TimeTable.class, id);
        session.close();
        return timeTable;
    }

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

    public List<String> taskStat (long idTask, Timestamp timestampNow, Timestamp timestampAgo){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<String> result = new LinkedList<>();
        Query firstPartTime = session
                .createQuery(
                "SELECT TO_CHAR(SUM(stoppedAt-startedAt), 'HH24:MI') " +
                "FROM TimeTable WHERE idTask = :idTask " +
                "AND startedAt BETWEEN TO_TIMESTAMP(:timestampAgo, 'YYYY-MM-DD HH24:MI:SS.US') " +
                "AND TO_TIMESTAMP(:timestampNow, 'YYYY-MM-DD HH24:MI:SS.US') " +
                "GROUP BY idTask")
                .setParameter("idTask", idTask)
                .setParameter("timestampNow", timestampNow)
                .setParameter("timestampAgo", timestampAgo);

        Query secondPartTime = session
                .createQuery(
                        "SELECT TO_CHAR(SUM(stoppedAt-:timestampAgo), 'HH24:MI') " +
                                "FROM TimeTable WHERE idTask = :idTask " +
                                "AND stoppedAt BETWEEN TO_TIMESTAMP(:timestampAgo, 'YYYY-MM-DD HH24:MI:SS.US') " +
                                "AND TO_TIMESTAMP(:timestampNow, 'YYYY-MM-DD HH24:MI:SS.US') " +
                                "AND startedAt NOT BETWEEN TO_TIMESTAMP(:timestampAgo, 'YYYY-MM-DD HH24:MI:SS.US') " +
                                "AND TO_TIMESTAMP(:timestampNow, 'YYYY-MM-DD HH24:MI:SS.US') "+
                                "GROUP BY idTask")
                .setParameter("idTask", idTask)
                .setParameter("timestampNow", timestampNow)
                .setParameter("timestampAgo", timestampAgo);
        if(!firstPartTime.list().isEmpty()) result.add(firstPartTime.list().get(0).toString());
        if(!secondPartTime.list().isEmpty()) result.add(secondPartTime.list().get(0).toString());
        session.close();
        if(!result.isEmpty()){
            return result;
        } else {
            return null;
        }
    }

    public void deleteByIdTask(long idTask){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("DELETE FROM TimeTable WHERE idTask = :idTask")
                .setParameter("idTask", idTask);
        query.executeUpdate();
        transaction.commit();
        session.close();
    }
}
