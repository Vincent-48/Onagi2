package com.example.onagi2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


public class MessagesFragment extends Fragment {

    RecyclerView recyclerView;

    View view;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
        setupRecyclerView();
    }

    void setupRecyclerView(){

    }
}