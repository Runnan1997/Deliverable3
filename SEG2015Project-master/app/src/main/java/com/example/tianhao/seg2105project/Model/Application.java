package com.example.tianhao.seg2105project.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.tianhao.seg2105project.ServiceViewAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class Application {

    private Context mContext;

    private ArrayList<Service> serviceArrayList = new ArrayList<>();

    private static Application instance;

    private User user;

    FirebaseDatabase database;
    DatabaseReference services;

    private Application(Context context) {
        mContext=context;
        database=FirebaseDatabase.getInstance();
        services=database.getReference("Services");
        services.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceArrayList = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    serviceArrayList.add(data.getValue(Service.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Application getInstance(Context context) {
        if(instance == null){
            instance = new Application(context);
        }
        return instance;
    }

    public void addService(String name, double hourlyRate){
        if(!user.getUserType().equals("Administrator")) {return;}
        String id = services.push().getKey();
        services.child(id).setValue(new Service(id,name,hourlyRate));
        Toast.makeText(mContext, "New Service Added", Toast.LENGTH_SHORT).show();
    }

    public void editService(String id, String name, double hourlyRate){
        if(!user.getUserType().equals("Administrator")) {return;}
        DatabaseReference originalService = services.child(id);
        originalService.setValue(new Service(id, name, hourlyRate));
        Toast.makeText(mContext, "Service Updated", Toast.LENGTH_SHORT).show();
    }


    public void removeService(String id){
        if(!user.getUserType().equals("Administrator")) {return;}
        if(id.equals(""))return;
        DatabaseReference originalService = services.child(id);
        originalService.removeValue();
        Toast.makeText(mContext, "Service Removed", Toast.LENGTH_SHORT).show();
    }


    public ArrayList<Service> getServiceArrayList() {
        return serviceArrayList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
