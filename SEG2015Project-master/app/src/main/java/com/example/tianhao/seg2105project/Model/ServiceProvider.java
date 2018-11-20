package com.example.tianhao.seg2105project.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceProvider extends User {

    private Profile profile;

    FirebaseDatabase database;
    DatabaseReference ProvidedServices;

    private Context mContext;


    private ArrayList<ProvidedService> serviceArrayList = new ArrayList<>();

    public ServiceProvider(Context context, User user) {
        mContext=context;
        if(user.getUserType().equals("Service Provider")){
            this.setUsername(user.getUsername());
            this.setEmail(user.getEmail());
            this.setPassword(user.getPassword());
            this.setUserType(user.getUserType());
        }

        database=FirebaseDatabase.getInstance();
        ProvidedServices=database.getReference("ProvidedServices");

        //get profile from database if it exist
        database.getReference("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(ServiceProvider.this.getUsername()).exists()){
                    profile = dataSnapshot.child(ServiceProvider.this.getUsername()).getValue(Profile.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //get all the provided services from database
        ProvidedServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceArrayList = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("serviceProviderName").getValue().toString().
                            equals(ServiceProvider.this.getUsername())) {
                        serviceArrayList.add(data.getValue(ProvidedService.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void saveServiceToProfile(Service service,String timeSlots){
        String id = service.getId()+this.getUsername();
        ProvidedServices.child(id).setValue(new ProvidedService(id,service,this.getUsername(),timeSlots));
        Toast.makeText(mContext, "The Service is saved to the profile", Toast.LENGTH_SHORT).show();
    }


    public void removeServiceFromProfile(String serviceId){
        String id = serviceId+this.getUsername();
        ProvidedServices.child(id).removeValue();
        Toast.makeText(mContext, "Service Removed from profile", Toast.LENGTH_SHORT).show();
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        database.getReference("Profile").child(getUsername()).setValue(profile);
    }

    public ArrayList<ProvidedService> getServiceArrayList() {
        return serviceArrayList;
    }
}
