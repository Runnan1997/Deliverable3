package com.example.tianhao.seg2105project.Model;

public class Profile {

    private String serviceProviderName;
    private String address, phone, companyName, description;
    private boolean Licensed;

    public Profile() {
    }

    public Profile(String serviceProviderName, String address, String phone, String companyName, String description, boolean Licensed) {
        this.serviceProviderName = serviceProviderName;
        this.address = address;
        this.phone = phone;
        this.companyName = companyName;
        this.description = description;
        this.Licensed = Licensed;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLicensed() {
        return Licensed;
    }

    public void setLicensed(boolean licensed) {
        this.Licensed = licensed;
    }
}
