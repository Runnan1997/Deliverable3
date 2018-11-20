package com.example.tianhao.seg2105project;

import com.example.tianhao.seg2105project.Model.Profile;


import static org.junit.Assert.*;
import org.junit.Test;

public class ProfileTest {
    @Test
    public void checkAddress(){
        Profile profile = new Profile("serviceProviderName", "1235 Laurier dr", "6137170111", "companyName", "description", true);
        assertEquals("check the address of profile", "1235 Laurier dr", profile.getAddress());

    }
    @Test
    public void checkPhone(){
        Profile profile = new Profile("serviceProviderName", "1235 Laurier dr", "6137170111", "companyName", "description", true);
        assertEquals("check the phone of profile", "6137170111", profile.getPhone());

    }
}
