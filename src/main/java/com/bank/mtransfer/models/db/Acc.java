package com.bank.mtransfer.models.db;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="account")
public class Acc {
    @Id
    private String id;

    private Date dadd;

    private String clid;

    private String type;

    private String value;

    private String a;

    public Acc(){}

    public Acc(String id, Date dadd, String clid, String type, String value, String a) {
        this.id = id;
        this.dadd = dadd;
        this.clid = clid;
        this.type = type;
        this.value = value;
        this.a = a;
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

    public String getClid() {
        return clid;
    }

    public void setClid(String clid) {
        this.clid = clid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
