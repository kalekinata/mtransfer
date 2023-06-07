package com.bank.mtransfer.models.payload.response;

import java.util.Date;

public class TranCheck {
    public String id;
    public Date dadd;
    public String trid;
    public String status_tr;

    public TranCheck() {
    }

    public TranCheck(String id, Date dadd, String trid, String status_tr) {
        this.id = id;
        this.dadd = dadd;
        this.trid = trid;
        this.status_tr = status_tr;
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

    public String getTrid() {
        return trid;
    }

    public void setTrid(String trid) {
        this.trid = trid;
    }

    public String getStatus_tr() {
        return status_tr;
    }

    public void setStatus_tr(String status_tr) {
        this.status_tr = status_tr;
    }
}
