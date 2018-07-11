package com.crypto.raModule.RA.Module.NoSQL.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Users")
public class User {
    @Id
    private String id;
    private String name;
    private String contactNo;
    @Indexed(unique = true)
    private String email;
    private String password;
    private int role;
    private boolean admin;
    private List<CertDetails> certDetails;
    private RaOffice raOffice;

    public User(){}

    public User(String name, String contactNo, String email, String password, int role,
                boolean admin, List<CertDetails> certDetails, /*List<ReqRaised> reqRaised,*/
                RaOffice raOffice) {
        this.name = name;
        this.contactNo = contactNo;
        this.email = email;
        this.password = password;
        this.role = role;
        this.admin = admin;
        this.certDetails = certDetails;
        //this.reqRaised = reqRaised;
        this.raOffice = raOffice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<CertDetails> getCertDetails() {
        return certDetails;
    }

    public void setCertDetails(List<CertDetails> certDetails) {
        this.certDetails = certDetails;
    }

    public RaOffice getRaOffice() {
        return raOffice;
    }

    public void setRaOffice(RaOffice raOffice) {
        this.raOffice = raOffice;
    }

    public User(User user){

        this.id = user.id;
        this.name = user.name;
        this.contactNo = user.contactNo;
        this.email = user.email;
        this.password = user.password;
        this.role = user.role;
        this.admin = user.admin;
        this.certDetails = user.certDetails;
        this.raOffice = user.raOffice;

    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", admin=" + admin +
                ", certDetails=" + certDetails +
                ", raOffice=" + raOffice +
                '}';
    }
}
