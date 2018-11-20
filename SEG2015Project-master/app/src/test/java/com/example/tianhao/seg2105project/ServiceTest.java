package com.example.tianhao.seg2105project;

import com.example.tianhao.seg2105project.Model.Service;
import com.example.tianhao.seg2105project.Model.User;

import static org.junit.Assert.*;
import org.junit.Test;


public class ServiceTest{
    @Test
    public void checkServiceName(){
        Service service = new Service("id", "name", 12.5);
        assertEquals("check the name of service", "name", service.getName());
    }


    @Test
    public void checkhourlyRate(){
        Service service = new Service("id", "name", 12.5);
        assertEquals("check the hourlyrate of service", 12.5, service.getHourlyRate(), 0.001);
    }


}
