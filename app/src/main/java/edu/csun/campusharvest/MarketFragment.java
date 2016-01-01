package edu.csun.campusharvest;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karanvir on 4/18/2015.
 */

public class MarketFragment extends Fragment {

    private ListView mMarketList;
    private MarketListItemAdapter mMarketListAdapter;

    public MarketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View marketListView = inflater.inflate(R.layout.fragment_market, container, false);

        mMarketList = (ListView) marketListView.findViewById(R.id.lv_market_list);
        List<MarketListItem> listItems = generateMarketListItems();

        mMarketList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                TextView tv = (TextView)arg1.findViewById(R.id.market_list_item_name);
                String searchEntry = (String)tv.getText();

                Intent webSearch = new Intent(Intent.ACTION_WEB_SEARCH);
                webSearch.putExtra(SearchManager.QUERY,searchEntry);
                startActivity(webSearch);
            }
        });

        //getActivity can return null if called before onAttach()
        if(getActivity() != null) {
            mMarketListAdapter = new MarketListItemAdapter(getActivity(), listItems);
        }

        mMarketList.setAdapter(mMarketListAdapter);

        // Inflate the layout for this fragment
        return marketListView;
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

        List<MarketListItem> result = new ArrayList<MarketListItem>();

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

}