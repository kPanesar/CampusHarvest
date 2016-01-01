package edu.csun.campusharvest;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Karanvir on 4/18/2015.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private View settingsFragmentView;
    private String selectedOptions;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button saveBtn = (Button) settingsFragmentView.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        loadSettings();

        return settingsFragmentView;
    }

    private boolean loadSettings(){
        boolean loaded = false;
        String fileName = getResources().getString(R.string.file_settings);
        String[] settingsData = new String[4];

        try{

            FileInputStream fis = getActivity().openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            int index = 0;
            while ((line = bufferedReader.readLine()) != null) {
                settingsData[index] = line;
                index++;
            }

            for(index = 0; index < settingsData.length; index++){
                int buttonID = Integer.parseInt(settingsData[index]);
                RadioButton button = (RadioButton)settingsFragmentView.findViewById(buttonID);
                generateItemCombo(index,buttonID);
                button.setChecked(true);
            }
            //Toast.makeText(getActivity(), selectedOptions, Toast.LENGTH_SHORT).show();

            loaded = true;
            bufferedReader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return loaded;
    }

    private void generateItemCombo(int question, int buttonID) {
        if(question == 0){
            if(buttonID == R.id.radio_budget_level1) selectedOptions = "1";
            else if (buttonID == R.id.radio_budget_level2) selectedOptions = "2";
            else if (buttonID == R.id.radio_budget_level3) selectedOptions = "3";
        }
        else{
            if(buttonID == R.id.radio_emphasis1_choice1) selectedOptions += "L";
            else if(buttonID == R.id.radio_emphasis1_choice2) selectedOptions += "P";
            else  if(buttonID == R.id.radio_emphasis1_choice3) selectedOptions += "B";

            if(buttonID == R.id.radio_emphasis2_choice1) selectedOptions += "W";
            else if(buttonID == R.id.radio_emphasis2_choice2) selectedOptions += "E";
            else  if(buttonID == R.id.radio_emphasis2_choice3) selectedOptions += "A";

            if(buttonID == R.id.radio_emphasis3_choice1) selectedOptions += "S";
            else if(buttonID == R.id.radio_emphasis3_choice2) selectedOptions += "I";
            else  if(buttonID == R.id.radio_emphasis3_choice3) selectedOptions += "C";
        }
    }

    private void saveBtnClick(View view){
        view = settingsFragmentView;

        Button saveBtn = (Button)view.findViewById(R.id.save_btn);
        saveBtn.setEnabled(false);

        RadioGroup budgetGroup = (RadioGroup)view.findViewById(R.id.rg_budget);
        int budgetID = budgetGroup.getCheckedRadioButtonId();

        RadioGroup emphasis1Group = (RadioGroup)view.findViewById(R.id.rg_emphasis1);
        int emphasis1ID = emphasis1Group.getCheckedRadioButtonId();

        RadioGroup emphasis2Group = (RadioGroup)view.findViewById(R.id.rg_emphasis2);
        int emphasis2ID = emphasis2Group.getCheckedRadioButtonId();

        RadioGroup emphasis3Group = (RadioGroup)view.findViewById(R.id.rg_emphasis3);
        int emphasis3ID = emphasis3Group.getCheckedRadioButtonId();

        int[] answers = {budgetID, emphasis1ID, emphasis2ID, emphasis3ID};

        String settingsFile = getResources().getString(R.string.file_settings);
        FileOutputStream outputStream;

        //Remove old settings file
        File dir = getActivity().getFilesDir();
        File file = new File(dir, settingsFile);
        boolean deleted = file.delete();

        try {
            outputStream = getActivity().openFileOutput(settingsFile, Context.MODE_PRIVATE);
            String data = "";
            for(int index = 0; index < answers.length; index++){
                data = (String.valueOf(answers[index]) + "\n");
                outputStream.write(data.getBytes());
            }
            outputStream.close();

            //Remove saved checkbox states
            dir = getActivity().getFilesDir();
            file = new File(dir, getResources().getString(R.string.file_checklist));
            deleted = file.delete();

            saveBtn.setEnabled(true);

            Toast.makeText(getActivity(),getResources().getString(R.string.toast_settings_saved),Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public String getSelectedOptions() {
        return selectedOptions;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save_btn){
            saveBtnClick(v);
        }
    }
}