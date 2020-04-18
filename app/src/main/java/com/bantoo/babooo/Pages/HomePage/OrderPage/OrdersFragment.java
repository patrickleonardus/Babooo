package com.bantoo.babooo.Pages.HomePage.OrderPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    List<ServiceSchedule> serviceScheduleList = new ArrayList<ServiceSchedule>();

    RecyclerView historyRV;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    private LinearLayoutManager historyLayoutManager;

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_orders, container, false);
        historyRV = rootView.findViewById(R.id.history_RV);

        addDummyData();
        setupRecyclerView();

        return rootView;
    }

    private void addDummyData() {
        ServiceSchedule dummy1 = new ServiceSchedule("10","Cuci Setrika Baju","Ningsih","Jan","Berlangsung","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2", "01239139123");
        ServiceSchedule dummy2 = new ServiceSchedule("12","Membersihkan Kamar Mandi","Inem","Feb","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2", "10239123");
        ServiceSchedule dummy3 = new ServiceSchedule("9","Setrika Baju","Purwati","Feb","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2", "10239193");
        ServiceSchedule dummy4 = new ServiceSchedule("2","Menyapu dan Mengepel","Angel","Mar","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2", "102391239");

        serviceScheduleList.add(dummy1);
        serviceScheduleList.add(dummy2);
        serviceScheduleList.add(dummy3);
        serviceScheduleList.add(dummy4);
    }

    private void setupRecyclerView() {
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(getContext(), serviceScheduleList);
        historyLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        historyRV.setLayoutManager(historyLayoutManager);
        historyRV.setAdapter(historyRecyclerViewAdapter);
    }

}
