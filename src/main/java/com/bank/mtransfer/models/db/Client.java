package com.bank.mtransfer.models.db;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="client")
public class Client {
    @Id
    private String id;
    private Date dadd;

    private String surname;

    private String name;

    private String patronymic;

    private String userid;
    private Date dclose;

    public Client() {
    }

    public Client(String id, Date dadd, String surname, String name, String patronymic, String userid) {
        this.id = id;
        this.dadd = dadd;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDadd() {
        return dadd;
    }

    public void setDadd(Date dadd) {
        this.dadd = dadd;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getUserid() { return userid; }

    public void setUserid(String userid) { this.userid = userid; }

    public Date getDclose() {
        return dclose;
    }

    public void setDclose(Date dclose) {
        this.dclose = dclose;
    }
}
