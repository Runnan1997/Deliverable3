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
import android.widget.Toast;

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
public class homeFragment extends Fragment {


    public homeFragment() {
        // Required empty public constructor
    }

    private Application application = Application.getInstance(getActivity());

    private RecyclerView recyclerView;
    private Button buttonAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        buttonAdd =(Button)view.findViewById(R.id.buttonAdd);//button to add service, visible to admin only
        if(!application.getUser().getUserType().equals("Administrator")) {
            buttonAdd.setVisibility(View.INVISIBLE);
        }
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditService.class);
                intent.putExtra("id","");
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();//refresh the recylerview every time it is brought to the front
        application = Application.getInstance(getActivity());
        recyclerView.setAdapter(new ServiceViewAdapter(getActivity(),application.getServiceArrayList()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
