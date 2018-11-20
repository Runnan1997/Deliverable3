package com.example.tianhao.seg2105project;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tianhao.seg2105project.Model.Application;
import com.example.tianhao.seg2105project.Model.ProvidedService;
import com.example.tianhao.seg2105project.Model.Service;
import com.example.tianhao.seg2105project.Model.ServiceProvider;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {


    public profileFragment() {
        // Required empty public constructor
    }
    private Application application = Application.getInstance(getActivity());
    private ServiceProvider serviceProvider;

    private ArrayList<Service> services;
    private ArrayList<ProvidedService> providedServices;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        if(application.getUser().getUserType().equals("Service Provider")){
            serviceProvider = (ServiceProvider)application.getUser();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapter();
    }

    private void initAdapter(){
        services = new ArrayList<>();
        providedServices = serviceProvider.getServiceArrayList();
        for(ProvidedService providedService : providedServices){
            services.add(providedService.getService());
        }

        application = Application.getInstance(getActivity());
        recyclerView.setAdapter(new ServiceViewAdapter(getActivity(),services));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
