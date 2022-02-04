package ru.dmitryvinogradov.Models;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "id_user_telegram")
    private long idUserTelegram;

    @Column (name = "test_data")
    private boolean testData;

    public long getId() {
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

    public void setIdUserTelegram(long idUserTelegram) {
        this.idUserTelegram = idUserTelegram;
    }

    public Tasks() {
    }

    public Tasks(String name, long idUserTelegram, boolean testData) {
        this.id = id;
        this.name = name;
        this.idUserTelegram = idUserTelegram;
        this.testData = testData;
    }

    public boolean isTestData() {
        return testData;
    }

    public void setTestData(boolean testData) {
        this.testData = testData;
    }
}
