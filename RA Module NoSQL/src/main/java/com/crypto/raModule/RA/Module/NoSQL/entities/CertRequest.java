package com.crypto.raModule.RA.Module.NoSQL.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "CertRequest")
public class CertRequest {

    @Id
    private String id;

    @Indexed(unique = true)
    private int sNo;

    private Csr csr;

    private int certClass;

    private int validity;

    private String pan;

    private String other;

    private boolean isEcc;

    private String subEmail;

    private RaOffice raOffice;

    private Date reqDate;

    private String verifiedByRaEmail;
    private Date verifiedByRaOn;
    private String authByRaEmail;
    private Date authByRaOn;
    private String verifiedByCaEmail;
    private Date verifiedByCaOn;
    private String authByCaEmail;
    private Date authByCaOn;
    private Date certGeneratedOn;

    //private String processByCaEmail;
    //private Date processByCaOn;
    private String certRejReason;
    private String certRejBy;
    private Date certRejOn;

    private int status;

    @Transient
    @JsonProperty("isRaOfficer")
    private boolean isRaOfficer;
    @Transient
    @JsonProperty("isRaAdmin")
    private boolean isRaAdmin;
    @Transient
    @JsonProperty("isCaOfficer")
    private boolean isCaOfficer;
    @Transient
    @JsonProperty("isCaAdmin")
    private boolean isCaAdmin;
    @Transient
    @JsonProperty("statusOfReq")
    private String statusOfReq;
    @Transient
    @JsonProperty("allowVerif")
    private boolean allowVerif;
    @Transient
    @JsonProperty("allowAuth")
    private boolean allowAuth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getsNo() {
        return sNo;
    }

    public void setsNo(int sNo) {
        this.sNo = sNo;
    }

    public Csr getCsr() {
        return csr;
    }

    public void setCsr(Csr csr) {
        this.csr = csr;
    }

    public int getCertClass() {
        return certClass;
    }

    public void setCertClass(int certClass) {
        this.certClass = certClass;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public boolean isEcc() {
        return isEcc;
    }

    public void setEcc(boolean ecc) {
        isEcc = ecc;
    }

    public String getSubEmail() {
        return subEmail;
    }

    public void setSubEmail(String subEmail) {
        this.subEmail = subEmail;
    }

    public RaOffice getRaOffice() {
        return raOffice;
    }

    public void setRaOffice(RaOffice raOffice) {
        this.raOffice = raOffice;
    }

    public Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }

    public String getVerifiedByRaEmail() {
        return verifiedByRaEmail;
    }

    public void setVerifiedByRaEmail(String verifiedByRaEmail) {
        this.verifiedByRaEmail = verifiedByRaEmail;
    }

    public Date getVerifiedByRaOn() {
        return verifiedByRaOn;
    }

    public void setVerifiedByRaOn(Date verifiedByRaOn) {
        this.verifiedByRaOn = verifiedByRaOn;
    }

    public String getAuthByRaEmail() {
        return authByRaEmail;
    }

    public void setAuthByRaEmail(String authByRaEmail) {
        this.authByRaEmail = authByRaEmail;
    }

    public Date getAuthByRaOn() {
        return authByRaOn;
    }

    public void setAuthByRaOn(Date authByRaOn) {
        this.authByRaOn = authByRaOn;
    }

    public String getVerifiedByCaEmail() {
        return verifiedByCaEmail;
    }

    public void setVerifiedByCaEmail(String verifiedByCaEmail) {
        this.verifiedByCaEmail = verifiedByCaEmail;
    }

    public Date getVerifiedByCaOn() {
        return verifiedByCaOn;
    }

    public void setVerifiedByCaOn(Date verifiedByCaOn) {
        this.verifiedByCaOn = verifiedByCaOn;
    }

    public String getAuthByCaEmail() {
        return authByCaEmail;
    }

    public void setAuthByCaEmail(String authByCaEmail) {
        this.authByCaEmail = authByCaEmail;
    }

    public Date getAuthByCaOn() {
        return authByCaOn;
    }

    public void setAuthByCaOn(Date authByCaOn) {
        this.authByCaOn = authByCaOn;
    }

    public Date getCertGeneratedOn() {
        return certGeneratedOn;
    }

    public void setCertGeneratedOn(Date certGeneratedOn) {
        this.certGeneratedOn = certGeneratedOn;
    }

    public String getCertRejReason() {
        return certRejReason;
    }

    public void setCertRejReason(String certRejReason) {
        this.certRejReason = certRejReason;
    }

    public String getCertRejBy() {
        return certRejBy;
    }

    public void setCertRejBy(String certRejBy) {
        this.certRejBy = certRejBy;
    }

    public Date getCertRejOn() {
        return certRejOn;
    }

    public void setCertRejOn(Date certRejOn) {
        this.certRejOn = certRejOn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isRaOfficer() {
        return isRaOfficer;
    }

    public void setRaOfficer(boolean raOfficer) {
        isRaOfficer = raOfficer;
    }

    public boolean isRaAdmin() {
        return isRaAdmin;
    }

    public void setRaAdmin(boolean raAdmin) {
        isRaAdmin = raAdmin;
    }

    public boolean isCaOfficer() {
        return isCaOfficer;
    }

    public void setCaOfficer(boolean caOfficer) {
        isCaOfficer = caOfficer;
    }

    public boolean isCaAdmin() {
        return isCaAdmin;
    }

    public void setCaAdmin(boolean caAdmin) {
        isCaAdmin = caAdmin;
    }

    public String getStatusOfReq() {
        return statusOfReq;
    }

    public void setStatusOfReq(String statusOfReq) {
        this.statusOfReq = statusOfReq;
    }

    public boolean isAllowVerif() {
        return allowVerif;
    }

    public void setAllowVerif(boolean allowVerif) {
        this.allowVerif = allowVerif;
    }

    public boolean isAllowAuth() {
        return allowAuth;
    }

    public void setAllowAuth(boolean allowAuth) {
        this.allowAuth = allowAuth;
    }

    @Override
    public String toString() {
        return "CertRequest{" +
                "id='" + id + '\'' +
                ", sNo=" + sNo +
                ", csr=" + csr +
                ", certClass=" + certClass +
                ", validity=" + validity +
                ", pan='" + pan + '\'' +
                ", other='" + other + '\'' +
                ", isEcc=" + isEcc +
                ", subEmail='" + subEmail + '\'' +
                ", raOffice=" + raOffice +
                ", reqDate=" + reqDate +
                ", verifiedByRaEmail='" + verifiedByRaEmail + '\'' +
                ", verifiedByRaOn=" + verifiedByRaOn +
                ", AuthByRaEmail='" + authByRaEmail + '\'' +
                ", AuthByRaOn=" + authByRaOn +
                ", verifiedByCaEmail='" + verifiedByCaEmail + '\'' +
                ", verifiedByCaOn=" + verifiedByCaOn +
                ", AuthByCaEmail='" + authByCaEmail + '\'' +
                ", AuthByCaOn=" + authByCaOn +
                ", certGeneratedOn=" + certGeneratedOn +
                ", certRejReason='" + certRejReason + '\'' +
                ", certRejBy='" + certRejBy + '\'' +
                ", certRejOn=" + certRejOn +
                ", status=" + status +
                ", isRaOfficer=" + isRaOfficer +
                ", isRaAdmin=" + isRaAdmin +
                ", isCaOfficer=" + isCaOfficer +
                ", isCaAdmin=" + isCaAdmin +
                ", statusOfReq='" + statusOfReq + '\'' +
                ", allowVerif=" + allowVerif +
                ", allowAuth=" + allowAuth +
                '}';
    }
}
