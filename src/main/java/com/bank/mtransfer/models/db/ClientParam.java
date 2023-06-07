package com.bank.mtransfer.models.db;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="cl_param")
public class ClientParam {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private Date dadd;

    private String clid;

    private String title;

    private String value;

    public ClientParam() { }

    public ClientParam(Date dadd, String clid, String title, String value){
        this.dadd = dadd;
        this.clid = clid;
        this.title = title;
        this.value = value;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Date getDadd() { return dadd; }

    public void setDadd(Date dadd) { this.dadd = dadd; }

    public String getClid() { return clid; }

    public void setClid(String clid) { this.clid = clid; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
}
