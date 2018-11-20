package com.example.tianhao.seg2105project.Model;


import java.util.ArrayList;

public class ProvidedService {

    private String id;
    private Service service;
    private String serviceProviderName;
    private String TimeSlots;


    public ProvidedService() {
    }

    public ProvidedService(String id, Service service, String serviceProviderName, String timeSlots) {
        this.id = id;
        this.service = service;
        this.serviceProviderName = serviceProviderName;
        TimeSlots = timeSlots;
    }


    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getTimeSlots() {
        return TimeSlots;
    }

    public void setTimeSlots(String timeSlots) {
        TimeSlots = timeSlots;
    }
}
