package com.example.tianhao.seg2105project;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tianhao.seg2105project.Model.Application;
import com.example.tianhao.seg2105project.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class usersFragment extends Fragment {


    public usersFragment() {
        // Required empty public constructor
    }

    User user;
    FirebaseDatabase database;
    DatabaseReference users;
    DatabaseReference providedServices;

    private Application application;

    TextView welcome;
    TextView usernameList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_num_account, container, false);

        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");
        providedServices=database.getReference("ProvidedServices");
        application = Application.getInstance(getActivity());
        user = application.getUser();
        usernameList=(TextView)view.findViewById(R.id.textUsers);

        if(user.getUserType().equals("Administrator")){//the admin can see the list of house owners and service providers
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userList="";
                    int counter=0;
                    if(application.getUser().getUserType().equals("Administrator")){
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            counter++;
                            userList+= counter+": "+data.child("username").getValue().toString()
                                    +" "+data.child("userType").getValue().toString()
                                    +"\n";
                        }
                    }
                    usernameList.setText(userList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else if (user.getUserType().equals("Service Provider")){//the Service Provider can see the timeSlot
            providedServices.addListenerForSingleValueEvent(new ValueEventListener() {  //may be a problem
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String timeSlot = "";
                    int counter=0;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if(data.child("serviceProviderName").getValue().toString().equals(application.getUser().getUsername())){
                            counter++;
                            timeSlot += counter+"."
                                    + data.child("service").child("name").getValue().toString()
                                    +": "+data.child("timeSlots").getValue().toString()+"\n";
                        }
                    }
                    usernameList.setText(timeSlot);//here username should be named as 'timeslotList'
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            usernameList.setVisibility(View.INVISIBLE);
        }

        // Inflate the layout for this fragment
        return view;
    }
}