package edu.csun.campusharvest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Karanvir on 4/18/2015.
 */

public class MapFragment extends Fragment {

    private View mapFragmentView;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mapFragmentView = inflater.inflate(R.layout.fragment_map, container, false);
        return mapFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView mapView = (ImageView)mapFragmentView.findViewById(R.id.iv_map);
        mapView.setImageResource(R.drawable.map_campus_harvest);
    }
}