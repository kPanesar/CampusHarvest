package edu.csun.campusharvest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

/**
 * Created by Karanvir on 4/18/2015.
 */

public class HealthTipFragment extends Fragment {

    private TextView tvTip;

    public HealthTipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View healthTipView = inflater.inflate(R.layout.fragment_health_tip, container, false);

        tvTip = (TextView)healthTipView.findViewById(R.id.tv_tip);
        Button refreshBtn = (Button)healthTipView.findViewById(R.id.nextTipBtn);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTip(tvTip);
            }
        });

        updateTip(tvTip);


        return healthTipView;
    }

    private void updateTip(TextView tipView){
        Random rand = new Random();
        String[] tipList = getResources().getStringArray(R.array.health_tips);
        int maxIndex = tipList.length - 1;

        int tipIndex = rand.nextInt(maxIndex - 0);
        tipView.setText(tipList[tipIndex]);
    }


}