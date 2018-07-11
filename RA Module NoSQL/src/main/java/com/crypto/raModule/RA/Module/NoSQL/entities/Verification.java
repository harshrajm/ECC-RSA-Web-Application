package com.crypto.raModule.RA.Module.NoSQL.entities;

import java.util.Date;

public class Verification {

    private String RaEmail;

    private Date date;

    public Verification(){}

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
}
