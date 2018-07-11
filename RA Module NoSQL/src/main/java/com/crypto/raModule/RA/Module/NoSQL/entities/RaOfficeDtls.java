package com.crypto.raModule.RA.Module.NoSQL.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "RaOfficeDetails")
public class RaOfficeDtls {

    @Id
    private String id;

    @Indexed(unique = true)
    private String raOfficeCode;
    private String Address;
    private String contactNo;

    public RaOfficeDtls(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRaOfficeCode() {
        return raOfficeCode;
    }

    public void setRaOfficeCode(String raOfficeCode) {
        this.raOfficeCode = raOfficeCode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
