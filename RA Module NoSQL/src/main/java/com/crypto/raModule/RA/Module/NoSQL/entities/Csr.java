package com.crypto.raModule.RA.Module.NoSQL.entities;

public class Csr {

    private String distinguishedName;
    private String organization;
    private String organizationalUnit;
    private String city;
    private String state;
    private String country;
    private String email;

    public Csr(){}

    public Csr(String distinguishedName, String organization, String organizationalUnit,
               String city, String state, String country, String email) {
        this.distinguishedName = distinguishedName;
        this.organization = organization;
        this.organizationalUnit = organizationalUnit;
        this.city = city;
        this.state = state;
        this.country = country;
        this.email = email;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(String organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Csr{" +
                "distinguishedName='" + distinguishedName + '\'' +
                ", organization='" + organization + '\'' +
                ", organizationalUnit='" + organizationalUnit + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
