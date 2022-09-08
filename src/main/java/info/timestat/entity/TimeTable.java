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
    private long id_task;

    @Column(name="started_at")
    private Timestamp startedAt;

    @Column(name="stopped_at")
    private Timestamp stoppedAt;

    public long getId() { return id; }

    public long getId_task() {
        return id_task;
    }

    public void setId_task(long id_task) {
        this.id_task = id_task;
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

    public TimeTable(long id_task, Timestamp startedAt) {
        this.id_task = id_task;
        this.startedAt = startedAt;
    }
}
