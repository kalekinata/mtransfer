package com.bank.mtransfer.models.db;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "fraud_tran")
public class FraudTran {
    @Id
    @Column(name = "id")
    private String checkid;
    private Date dadd;
    private String trid;
    private String status_check;
    private String marker;
    private String description;
    public FraudTran(){}

    public FraudTran(String checkid, Date dadd, String trid, String status_check, String marker, String description) {
        this.checkid = checkid;
        this.dadd = dadd;
        this.trid = trid;
        this.status_check = status_check;
        this.marker = marker;
        this.description = description;
    }

    public String getCheckid() {
        return checkid;
    }

    public void setCheckid(String checkid) {
        this.checkid = checkid;
    }

    public Date getDadd() {
        return dadd;
    }

    public void setDadd(Date dadd) {
        this.dadd = dadd;
    }

    public String getTrid() {
        return trid;
    }

    public void setTrid(String trid) {
        this.trid = trid;
    }

    public String getStatus_check() {
        return status_check;
    }

    public void setStatus_check(String status_check) {
        this.status_check = status_check;
    }

    public String getMarker() { return marker; }

    public void setMarker(String marker) { this.marker = marker; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
