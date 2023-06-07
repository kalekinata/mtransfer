package com.bank.mtransfer.models.db;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    private String id;
    private Date dadd;
    private String clid;
    private String accid_send;
    private String accid_recip;
    private String type;
    private float sum;
    private float commission;
    private String region;
    private String status;
    private String status_check;

    public Transaction( String id, Date dadd, String clid, String accid_send, String accid_recip, String type, float sum, float commission, String region, String status) {
        this.id = id;
        this.dadd = dadd;
        this.clid = clid;
        this.accid_send = accid_send;
        this.accid_recip = accid_recip;
        this.type = type;
        this.sum = sum;
        this.commission = commission;
        this.region = region;
        this.status = status;
    }

    public Transaction() {

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

    public String getAccid_send() {
        return accid_send;
    }

    public void setAccid_send(String accid_send) {
        this.accid_send = accid_send;
    }

    public String getAccid_recip() {
        return accid_recip;
    }

    public void setAccid_recip(String accid_recip) {
        this.accid_recip = accid_recip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_check() {
        return status_check;
    }

    public void setStatus_check(String status_check) {
        this.status_check = status_check;
    }
}
