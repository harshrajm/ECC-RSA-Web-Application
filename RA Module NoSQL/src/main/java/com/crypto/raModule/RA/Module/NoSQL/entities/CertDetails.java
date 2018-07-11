package com.crypto.raModule.RA.Module.NoSQL.entities;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

public class CertDetails {

    @Indexed(unique = true)
    private  String certSerialNo;
    private  Date validFrm;
    private  Date validTo;

    public CertDetails(){}

    public CertDetails(String certSerialNo, Date validFrm, Date validTo) {
        this.certSerialNo = certSerialNo;
        this.validFrm = validFrm;
        this.validTo = validTo;
    }

    public String getCertSerialNo() {
        return certSerialNo;
    }

    public void setCertSerialNo(String certSerialNo) {
        this.certSerialNo = certSerialNo;
    }

    public Date getValidFrm() {
        return validFrm;
    }

    public void setValidFrm(Date validFrm) {
        this.validFrm = validFrm;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
}
