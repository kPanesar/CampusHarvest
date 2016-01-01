package edu.csun.campusharvest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Karanvir on 4/18/2015.
 */
public class GroceryListItemAdapter extends BaseAdapter {

    List<GroceryListItem> mItems;
    Context mContext;
    private float spentAmount;

    public GroceryListItemAdapter(Context context,List<GroceryListItem> mItems, float spentAmount) {
        this.mItems = mItems;
        this.mContext = context;
        this.spentAmount = spentAmount;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
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
            convertView = mInflater.inflate(R.layout.grocery_list_item, null);
        }

        final CheckBox chkBox   = (CheckBox) convertView.findViewById(R.id.checkbox_grocery_list_item);
        TextView tvPrice        = (TextView) convertView.findViewById(R.id.tv_grocery_list_item_price);

        final GroceryListItem item = mItems.get(position);

        if (item != null) {
            chkBox.setText(item.getQuantity() + " " + item.getUnits() + " " + item.getName());
        }

        if(item.isChecked()){
            //spentAmount += item.getPrice();
            chkBox.setChecked(true);
        }
        else{
            chkBox.setChecked(false);
        }
        TextView tvSpent = (TextView)((Activity)mContext).findViewById(R.id.tv_spent);
        tvSpent.setText("Spent: $" + String.format("%.2f", spentAmount));
        tvPrice.setText("$" + String.format("%.2f", item.getPrice()));

        chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TextView tvSpent = (TextView)((Activity)mContext).findViewById(R.id.tv_spent);
                String spentStr = (String)tvSpent.getText();
                final boolean isChecked = chkBox.isChecked();

                //float spent = Float.parseFloat(spentStr.substring((spentStr.indexOf("$") + 1),spentStr.length()));
                float itemPrice = item.getPrice();

                if(isChecked){
                    spentAmount += itemPrice;
                    item.setChecked(true);
                }
                else{
                    spentAmount -=itemPrice;
                    item.setChecked(false);
                }

                tvSpent.setText("Spent: $" + String.format("%.2f", spentAmount));
                //Toast.makeText(mContext, "Clicked on Row: " + item.getName(),Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }
}