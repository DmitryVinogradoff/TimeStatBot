package ru.dmitryvinogradov.Models;

import javax.persistence.*;

@Entity
@Table(name = "list_of_tasks")
public class ListOfTasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "name")
    private String name;

    @Column (name = "id_user_telegram")
    private int idUserTelegram;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdUserTelegram() {
        return idUserTelegram;
    }

    public void setIdUserTelegram(int idUserTelegram) {
        this.idUserTelegram = idUserTelegram;
    }

    public ListOfTasks() {
    }

    public ListOfTasks(String name, int idUserTelegram) {
        this.id = id;
        this.name = name;
        this.idUserTelegram = idUserTelegram;
    }
}
