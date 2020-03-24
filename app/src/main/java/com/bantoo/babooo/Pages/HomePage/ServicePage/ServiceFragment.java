package com.bantoo.babooo.Pages.HomePage.ServicePage;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bantoo.babooo.Model.ScheduleData;
import com.bantoo.babooo.Pages.ScheduleAdapter;
import com.bantoo.babooo.R;

import java.util.Vector;

public class ServiceFragment extends Fragment {

    private RecyclerView scheduleRV;
    private Vector<ScheduleData> sd = new Vector<ScheduleData>();

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_service, container, false);
        scheduleRV = rootView.findViewById(R.id.rv_schedule);
        scheduleRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScheduleData sd1 = new ScheduleData();
        sd1.setAddress("Jl. aaaaa");
        sd1.setMaid("inem");
        sd1.setOrderDate("10");
        sd1.setOrderMonth("Jan");
        sd1.setOrderTime("10.00 - 12.00");
        sd1.setServiceType("Cuci aja");
        sd1.setStatus("completed");
        sd.add(sd1);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(sd);
        scheduleRV.setAdapter(scheduleAdapter);
        scheduleRV.setItemAnimator(new DefaultItemAnimator());
        return rootView;
        //inflater.inflate(R.layout.fragment_service, container, false)
    }
}
