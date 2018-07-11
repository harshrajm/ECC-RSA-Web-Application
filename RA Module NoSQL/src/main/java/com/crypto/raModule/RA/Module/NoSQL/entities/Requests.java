package com.crypto.raModule.RA.Module.NoSQL.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Requests")
public class Requests {

    @Id
    private String id;
    private String raId;
    @Indexed(unique = true)
    private String subEmail;
    private String otp;
    private Date mailSentOn;
    private Date firstInsert;
    //private boolean reqRaised;
    private boolean disable;

    private RaOffice raOffice;

    public Requests(){}

    public String getRaId() {
        return raId;
    }

    public void setRaId(String raId) {
        this.raId = raId;
    }

    public String getSubEmail() {
        return subEmail;
    }

    public void setSubEmail(String subEmail) {
        this.subEmail = subEmail;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getMailSentOn() {
        return mailSentOn;
    }

    public void setMailSentOn(Date mailSentOn) {
        this.mailSentOn = mailSentOn;
    }

    public Date getFirstInsert() {
        return firstInsert;
    }

    public void setFirstInsert(Date firstInsert) {
        this.firstInsert = firstInsert;
    }

    /*public boolean isReqRaised() {
        return reqRaised;
    }

    public void setReqRaised(boolean reqRaised) {
        this.reqRaised = reqRaised;
    }*/

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public RaOffice getRaOffice() {
        return raOffice;
    }

    public void setRaOffice(RaOffice raOffice) {
        this.raOffice = raOffice;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "id='" + id + '\'' +
                ", raId='" + raId + '\'' +
                ", subEmail='" + subEmail + '\'' +
                ", otp='" + otp + '\'' +
                ", mailSentOn=" + mailSentOn +
                ", firstInsert=" + firstInsert +
                //", reqRaised=" + reqRaised +
                ", disable=" + disable +
                '}';
    }
}
