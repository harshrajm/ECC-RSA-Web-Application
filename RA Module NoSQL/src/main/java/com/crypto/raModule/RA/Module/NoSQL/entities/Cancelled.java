package com.crypto.raModule.RA.Module.NoSQL.entities;

import java.util.Date;

public class Cancelled {


    private String RaEmail;

    private Date date;

    private String reason;

    public String getRaEmail() {
        return RaEmail;
    }

    public void setRaEmail(String raEmail) {
        RaEmail = raEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
