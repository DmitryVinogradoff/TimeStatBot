package info.timestat.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="time_table")
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="id_task")
    private long idTask;

    @Column(name="started_at")
    private Timestamp startedAt;

    @Column(name="stopped_at")
    private Timestamp stoppedAt;

    public long getId() { return id; }

    public long getIdTask() {
        return idTask;
    }

    public void setIdTask(long id_task) {
        this.idTask = idTask;
    }

    public Timestamp getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Timestamp startedAt) {
        this.startedAt = startedAt;
    }

    public Timestamp getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(Timestamp stoppedAt) {
        this.stoppedAt = stoppedAt;
    }

    public TimeTable() { }

    public TimeTable(long idTask, Timestamp startedAt) {
        this.idTask = idTask;
        this.startedAt = startedAt;
    }
}
