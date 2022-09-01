package info.timestat.entity;

import javax.persistence.*;

@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "id_user_telegram")
    private long idUserTelegram;

    public Task() { }

    public Task(String name, long idUserTelegram) {
        this.name = name;
        this.idUserTelegram = idUserTelegram;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdUserTelegram(long idUserTelegram) {
        this.idUserTelegram = idUserTelegram;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getIdUserTelegram() {
        return idUserTelegram;
    }
}
