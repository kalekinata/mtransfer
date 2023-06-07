package com.bank.mtransfer.models.payload;

import java.util.Date;

public class TransInfoBank {
    private String id;
    private Date dadd;
    private String clid_send;
    private String clid_recip;
    private String region;
    private String type;
    private Float sum;
    private Float commission;
    private String status;
    private String status_check;

    public TransInfoBank(){};

    public TransInfoBank(String id, Date dadd, String clid_send,
                         String clid_recip, String region, String type,
                         Float sum, Float commission, String status, String status_check) {
        this.id = id;
        this.dadd = dadd;
        this.clid_send = clid_send;
        this.clid_recip = clid_recip;
        this.region = region;
        this.type = type;
        this.sum = sum;
        this.commission = commission;
        this.status = status;
        this.status_check = status_check;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Date getDadd() { return dadd; }

    public void setDadd(Date dadd) { this.dadd = dadd; }

    public String getClid_send() { return clid_send; }

    public void setClid_send(String clid_send) {  this.clid_send = clid_send; }

    public String getClid_recip() { return clid_recip; }

    public void setClid_recip(String clid_recip) { this.clid_recip = clid_recip; }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public Float getSum() { return sum; }

    public void setSum(Float sum) { this.sum = sum; }

    public Float getCommission() { return commission; }

    public void setCommission(Float commission) { this.commission = commission; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getStatus_check() { return status_check; }

    public void setStatus_check(String status_check) { this.status_check = status_check; }
}