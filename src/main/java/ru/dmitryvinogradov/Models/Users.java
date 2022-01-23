package ru.dmitryvinogradov.Models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "id_user_telegram")
    private int id_user_telegram;

    public Users() {
    }

    public Users(int id_user_telegram) {
        this.id = id;
        this.id_user_telegram = id_user_telegram;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user_telegram() {
        return id_user_telegram;
    }

    public void setId_user_telegram(int id_user_telegram) {
        this.id_user_telegram = id_user_telegram;
    }
}
