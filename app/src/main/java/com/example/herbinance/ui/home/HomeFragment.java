package com.example.herbinance.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herbinance.R;
import com.example.herbinance.model.User;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        ArrayList<User> liset = new ArrayList<>();
        User user = new User();
        user.setText1("test1");
        user.setText2("test2");
        user.setText3("test3");
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
        liset.add(user);
//
//        Log.d("TEST",""+liset.size());
//        homeAdapter = new HomeAdapter(liset);
//        recyclerView.setAdapter(homeAdapter);
    }

}