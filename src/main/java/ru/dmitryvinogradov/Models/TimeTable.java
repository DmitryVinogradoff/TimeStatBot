package ru.dmitryvinogradov.Models;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "time_table")
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_task")
    private long idTask;

    @Column(name = "started_at")
    private Timestamp startedAt;

    @Column(name = "stopped_at")
    private Timestamp stoppedAt;

    @Column(name = "status")
    private boolean status;

    public long getIdTask() {
        return idTask;
    }

    public void setIdTask(long idTask) {
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TimeTable() {
    }

    public TimeTable(long idTask, Timestamp startedAt, boolean status) {
        this.idTask = idTask;
        this.startedAt = startedAt;
        this.status = status;
    }
}
