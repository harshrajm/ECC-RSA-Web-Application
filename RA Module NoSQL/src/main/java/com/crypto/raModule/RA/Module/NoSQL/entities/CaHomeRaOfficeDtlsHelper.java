package com.crypto.raModule.RA.Module.NoSQL.entities;

public class CaHomeRaOfficeDtlsHelper {

    private int totalRequests;
    private int pendingRaVerification;
    private int pendingRaAuthorization;
    private int pendingCaVerification;
    private int pendingCaAuthorization;
    private int readyToGenerate;
    private int certificateReadyToDownload;
    private int cancelled;

    public int getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public int getPendingRaVerification() {
        return pendingRaVerification;
    }

    public void setPendingRaVerification(int pendingRaVerification) {
        this.pendingRaVerification = pendingRaVerification;
    }

    public int getPendingRaAuthorization() {
        return pendingRaAuthorization;
    }

    public void setPendingRaAuthorization(int pendingRaAuthorization) {
        this.pendingRaAuthorization = pendingRaAuthorization;
    }

    public int getPendingCaVerification() {
        return pendingCaVerification;
    }

    public void setPendingCaVerification(int pendingCaVerification) {
        this.pendingCaVerification = pendingCaVerification;
    }

    public int getPendingCaAuthorization() {
        return pendingCaAuthorization;
    }

    public void setPendingCaAuthorization(int pendingCaAuthorization) {
        this.pendingCaAuthorization = pendingCaAuthorization;
    }

    public int getReadyToGenerate() {
        return readyToGenerate;
    }

    public void setReadyToGenerate(int readyToGenerate) {
        this.readyToGenerate = readyToGenerate;
    }

    public int getCertificateReadyToDownload() {
        return certificateReadyToDownload;
    }

    public void setCertificateReadyToDownload(int certificateReadyToDownload) {
        this.certificateReadyToDownload = certificateReadyToDownload;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }
}
