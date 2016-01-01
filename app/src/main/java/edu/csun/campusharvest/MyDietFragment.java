package edu.csun.campusharvest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.csun.campusharvest.common.view.SlidingTabLayout;

/**
 * Created by Karanvir on 4/18/2015.
 * This class show the My Diet Section with information on the Grocery List
 * as well as a bar graph to visualize the Grocery List
 */

public class MyDietFragment extends Fragment {

    private ListView mGroceryList;
    private List<GroceryListItem> listItems;
    private GroceryListItemAdapter mGroceryListItemAdapter;
    private String selectedOptions = "";
    private float spentAmt;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    public MyDietFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dietFragmentView = inflater.inflate(R.layout.fragment_my_diet, container, false);

        return dietFragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyDietPagerAdapter());

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*TextView tvSpent = (TextView)(getActivity().findViewById(R.id.tv_spent));
        tvSpent.setText("Spent: $" + String.format("%.2f", spentAmt));*/
    }

    @Override
    public void onPause() {
        super.onPause();
        int[] checkedStates = new int[listItems.size()];

        for(int index = 0; index < listItems.size(); index++){
            checkedStates[index] = (listItems.get(index).isChecked()) ? 1 : 0;
        }

        //Toast.makeText(getActivity(), checkedStates.toString(), Toast.LENGTH_SHORT).show();

        String fileName = getResources().getString(R.string.file_checklist);
        FileOutputStream outputStream;

        //Remove saved checkbox states
        File dir = getActivity().getFilesDir();
        File file = new File(dir, fileName);
        boolean deleted = file.delete();

        try {
            outputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            for(int index = 0; index < checkedStates.length; index++){
                outputStream.write((checkedStates[index] + "\n").getBytes());
            }
            outputStream.close();

            //Toast.makeText(getActivity(),getResources().getString(R.string.toast_settings_saved),Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void updateTotal(View view) {
        float sum = 0;

        for(GroceryListItem item: listItems){
            if (item != null){
                sum += item.getPrice();
            }
        }

        TextView textView = (TextView)view.findViewById(R.id.tv_total);
        textView.setText("Total: $" + String.format("%.2f", sum));
    }

    private List<GroceryListItem> generateGroceryListItems() {
        List<GroceryListItem> result = new ArrayList<GroceryListItem>();
        int index = 0;
        String[] data;
        String rawData, rawList="";

        List<MarketListItem> marketItems = generateMarketListItems();

        data = readGroceryList();
        loadSettings(); //Sets the selected options
        //selectedOptions = "1PES";

        ArrayList<Integer> checklistStates = readCheckedStates();

        boolean found = false;
        while(!found && index < data.length){
            rawData = data[index];
            if(rawData.startsWith(selectedOptions)){
                rawList = rawData.substring(rawData.indexOf(":") + 1,rawData.indexOf(";"));
                found = true;
            }
            index++;
        }
        String[] rawItems = rawList.split(",");
        int indexCheckbox = 0;
        for(index = 0; index < rawItems.length-1; index += 2){
            int quantity = Integer.parseInt(rawItems[index]);
            MarketListItem item = findMarketItem(marketItems, rawItems[index + 1]);
            GroceryListItem groceryItem = new GroceryListItem();

            groceryItem.setQuantity(quantity);
            groceryItem.setMarketItem(item);

            if(checklistStates.size() > 0 && indexCheckbox < checklistStates.size()){
                if(checklistStates.get(indexCheckbox) == 1){
                    groceryItem.setChecked(true);
                    spentAmt += groceryItem.getPrice();
                }
                else{
                    groceryItem.setChecked(false);
                }
            }

            result.add(groceryItem);
            indexCheckbox++;
        }
        return result;
    }

    private MarketListItem findMarketItem(List<MarketListItem> marketItems, String rawItem) {
        MarketListItem result=null;

        for (int index = 0; index < marketItems.size(); index++){
            if(marketItems.get(index).getName().equals(rawItem)){
                result = marketItems.get(index);
            }
        }
        return result;
    }

    private ArrayList<Integer> readCheckedStates(){
        ArrayList<Integer> result = new ArrayList<>();

        BufferedReader reader;
        String fileName = getResources().getString(R.string.file_checklist);

        try {
            FileInputStream fis = getActivity().openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            reader = new BufferedReader(isr);

            String line = reader.readLine();
            while(line != null){
                result.add(Integer.parseInt(line));
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }

    private String[] readGroceryList() {
        BufferedReader reader;
        String fileName = getResources().getString(R.string.file_grocery_list);

        String[] data = new String[81];

        try {
            InputStreamReader inputStream = new InputStreamReader(getActivity().getAssets().open(fileName));
            reader = new BufferedReader(inputStream);

            int index = 0;
            String line = reader.readLine();

            while(line != null){
                data[index] = line;
                line = reader.readLine();
                index++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return data;
    }

    public List<MarketListItem> generateMarketListItems() {
        String[] grainsNames = getResources().getStringArray(R.array.grains_list_names);
        String[] grainsPrices = getResources().getStringArray(R.array.grains_list_prices);

        String[] vegetableNames = getResources().getStringArray(R.array.vegetable_list_names);
        String[] vegetablePrices = getResources().getStringArray(R.array.vegetable_list_prices);

        String[] fruitsNames = getResources().getStringArray(R.array.fruit_list_names);
        String[] fruitsPrices = getResources().getStringArray(R.array.fruit_list_prices);

        String[] dairyNames = getResources().getStringArray(R.array.dairy_list_names);
        String[] dairyPrices = getResources().getStringArray(R.array.dairy_list_prices);

        String[] proteinsNames = getResources().getStringArray(R.array.proteins_list_names);
        String[] proteinsPrices = getResources().getStringArray(R.array.proteins_list_prices);

        String[] caffeineNames = getResources().getStringArray(R.array.caffeine_list_name);
        String[] caffeinePrices = getResources().getStringArray(R.array.caffeine_list_prices);

        String[] spiceNames = getResources().getStringArray(R.array.spice_list_names);
        String[] spicePrices = getResources().getStringArray(R.array.spice_list_prices);

        List<MarketListItem> result = new ArrayList<>();

        for (int i = 0; i < grainsNames.length; i++) {
            MarketListItem item = new MarketListItem();
            item.setName(grainsNames[i]);
            item.setPrice(grainsPrices[i]);
            item.setCategory("Grains");
            result.add(item);
        }

        for (int i = 0; i < vegetableNames.length; i++) {
            MarketListItem item = new MarketListItem();
            item.setName(vegetableNames[i]);
            item.setPrice(vegetablePrices[i]);
            item.setCategory("Vegetables");
            result.add(item);
        }

        for (int i = 0; i < fruitsNames.length; i++) {
            MarketListItem item = new MarketListItem();
            item.setName(fruitsNames[i]);
            item.setPrice(fruitsPrices[i]);
            item.setCategory("Fruits");
            result.add(item);
        }

        for (int i = 0; i < dairyNames.length; i++) {
            MarketListItem item = new MarketListItem();
            item.setName(dairyNames[i]);
            item.setPrice(dairyPrices[i]);
            item.setCategory("Dairy");
            result.add(item);
        }

        for (int i = 0; i < proteinsNames.length; i++) {
            MarketListItem item = new MarketListItem();
            item.setName(proteinsNames[i]);
            item.setPrice(proteinsPrices[i]);
            item.setCategory("Proteins");
            result.add(item);
        }

        for (int i = 0; i < caffeineNames.length; i++) {
            MarketListItem item = new MarketListItem();
            item.setName(caffeineNames[i]);
            item.setPrice(caffeinePrices[i]);
            item.setCategory("Caffeine");
            result.add(item);
        }

        for (int i = 0; i < spiceNames.length; i++) {
            MarketListItem item = new MarketListItem();
            item.setName(spiceNames[i]);
            item.setPrice(spicePrices[i]);
            item.setCategory("Spices");
            result.add(item);
        }
        return result;
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
                generateItemCombo(index,buttonID);
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

    class MyDietPagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Grocery List";
                case 1:
                    return "Nutrition Chart";
                default:
                    return "Invalid";
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view;
            if(position == 0){ //My Diet
                view = getActivity().getLayoutInflater().inflate(R.layout.item_my_diet,
                        container, false);

                mGroceryList = (ListView) view.findViewById(R.id.lv_grocery_list);
                listItems = generateGroceryListItems();
                updateTotal(view);

                //getActivity can return null if called before onAttach()
                if(getActivity() != null) {
                    mGroceryListItemAdapter = new GroceryListItemAdapter(getActivity(), listItems, spentAmt);
                }

                mGroceryList.setAdapter(mGroceryListItemAdapter);
                container.addView(view);

            }
            else{
                view = getActivity().getLayoutInflater().inflate(R.layout.item_nutrition_chart,
                        container, false);

                // Add the newly created View to the ViewPager
                container.addView(view);

                float[] yValues = getYValues();

                GraphView graph = (GraphView) view.findViewById(R.id.graph);
                BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(1, yValues[0]),
                        new DataPoint(2, yValues[1]),
                        new DataPoint(3, yValues[2]),
                        new DataPoint(4, yValues[3]),
                        new DataPoint(5, yValues[4])
                });

                series.setSpacing(10);
                graph.getViewport().setMinX(1);
                graph.getViewport().setMaxX(5.2);
                graph.getViewport().setXAxisBoundsManual(true);
                //graph.getGridLabelRenderer().setNumHorizontalLabels(5);
                series.setDrawValuesOnTop(true);
                series.setValuesOnTopColor(Color.GRAY);

                series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        switch((int)data.getX()){
                            case 1:
                                return Color.rgb(240,102,20);
                            case 2:
                                return Color.rgb(10,190,110);
                            case 3:
                                return Color.rgb(150,5,40);
                            case 4:
                                return Color.rgb(5,150,230);
                            case 5:
                                return Color.rgb(4,60,120);
                            default:
                                return Color.rgb(168,5,50);
                        }
                    }
                });

                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            // show normal x values
                            switch ((int)value) {
                                case 1:
                                    return "Grains";
                                case 2:
                                    return "Veg.";
                                case 3:
                                    return "Fruits";
                                case 4:
                                    return "Dairy";
                                case 5:
                                    return "Protein";
                                default:
                                    return super.formatLabel(value, isValueX);
                            }
                        } else {
                            // show currency for y values
                            return super.formatLabel(value, isValueX) + "%";
                        }
                    }
                });

                graph.addSeries(series);
                //graph.getGridLabelRenderer().setHorizontalAxisTitle("Food Category");
                graph.getGridLabelRenderer().setVerticalAxisTitle("% of Diet");
                graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            }

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public float[] getYValues(){
            float[] result = new float[5];

            for(int index = 0; index < listItems.size(); index++){
                String category = listItems.get(index).getCategory();

                switch (category){
                    case "Grains":
                        result[0]++;
                        break;
                    case "Vegetables":
                        result[1]++;
                        break;
                    case "Fruits":
                        result[2]++;
                        break;
                    case "Dairy":
                        result[3]++;
                        break;
                    case "Proteins":
                        result[4]++;
                        break;
                    default:
                }
            }

            for(int index = 0; index < result.length; index++){
                result[index] = (result[index]/listItems.size())*100;
            }

            return result;
        }
    }

}