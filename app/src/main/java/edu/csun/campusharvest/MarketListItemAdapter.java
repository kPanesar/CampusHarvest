package edu.csun.campusharvest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Karanvir on 4/18/2015.
 */
public class MarketListItemAdapter extends BaseAdapter {

    List<MarketListItem> listItems;
    Context mContext;

    public MarketListItemAdapter(Context context,List<MarketListItem> listItems) {
        this.listItems = listItems;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.market_list_item, null);
        }

        TextView tvName     = (TextView) convertView.findViewById(R.id.market_list_item_name);
        TextView tvPrice    = (TextView) convertView.findViewById(R.id.market_list_item_price);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.market_list_item_category);

        MarketListItem item = listItems.get(position);

        tvName.setText(item.getName());
        tvPrice.setText(item.getPrice());
        tvCategory.setText(item.getCategory());

        return convertView;
    }
}