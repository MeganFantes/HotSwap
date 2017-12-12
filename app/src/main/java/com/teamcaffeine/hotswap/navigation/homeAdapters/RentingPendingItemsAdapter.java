package com.teamcaffeine.hotswap.navigation.homeAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamcaffeine.hotswap.R;
import com.teamcaffeine.hotswap.swap.ActiveTransactionInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class RentingPendingItemsAdapter extends BaseAdapter {

    private Context context;
    private HashMap<String, ActiveTransactionInfo> items;
    private ArrayList<ActiveTransactionInfo> itemList;
    private String currentUserID;

    public RentingPendingItemsAdapter(Context aContext, String currentUserID) {
        context = aContext;
        items = new HashMap<>();
        itemList = new ArrayList<ActiveTransactionInfo> (items.values());
        this.currentUserID = currentUserID;
    }

    public void putItems(HashMap<String, ActiveTransactionInfo> items) {
        this.items = items;
        this.itemList = new ArrayList<>(items.values());
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listview_renting_pending_row, parent, false);
        } else {
            row = convertView;
        }

        TextView itemName = (TextView) row.findViewById(R.id.txtItemName);
        TextView swapDates = (TextView) row.findViewById(R.id.txtSwapDates);
        TextView SwapInfo = (TextView) row.findViewById(R.id.txtSwapInfo);

        itemName.setText(itemList.get(position).getItem().getName());
        swapDates.setText(itemList.get(position).getDate().toString());

        String ownerID = itemList.get(position).getItem().getOwnerID();
        if (ownerID.equals(currentUserID)) {
            SwapInfo.setText(context.getString(R.string.lending_to) + " " + itemList.get(position).getRenterId());
        } else {
            SwapInfo.setText(context.getString(R.string.renting_from) + " " + itemList.get(position).getItem().getOwnerID());
        }
        return row;
    }

    public ActiveTransactionInfo getActiveTransactionInfoAtPosition(int position) {
        return itemList.get(position);
    }
}
