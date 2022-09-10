package com.gisdemo.app.mygisapplication.spinnerpicker;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gisdemo.app.mygisapplication.R;

import java.util.List;

public class ListDropDownAdapter extends ArrayAdapter<DropDownListDataModel> {
    private Context context;
    private List<DropDownListDataModel> items;


    public ListDropDownAdapter(Context context, int resource, int textViewResourceId, List<DropDownListDataModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_spinner_list, parent, false);
        }
        DropDownListDataModel dataModel = items.get(position);
        setData(dataModel,view);
        return view;
    }

    private void setData(DropDownListDataModel dataModel, View view){
        TextView lblName = view.findViewById(R.id.lbl_spinner_item);
        lblName.setText(dataModel.getName());

        if (dataModel.isSelected()){
            lblName.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            lblName.setTextColor(ContextCompat.getColor(context,R.color.white));
        } else {
            lblName.setBackgroundColor(Color.TRANSPARENT);
            lblName.setTextColor(ContextCompat.getColor(context,R.color.black));
        }
    }

}