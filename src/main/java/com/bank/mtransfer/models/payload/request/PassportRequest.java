package com.bank.mtransfer.models.payload.request;

public class PassportRequest {
    private String source;

    private String series;

    private String number;

    private Integer qc;

    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }

    public String getSeries() { return series; }

    public void setSeries(String series) { this.series = series; }

    public String getNumber() { return number; }

    public void setNumber(String number) { this.number = number; }

    public Integer getQc() { return qc; }

    public void setQc(Integer qc) { this.qc = qc; }
}