package net.myacxy.agsm.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.myacxy.agsm.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_server_details_parameter)
public class ItemServerDetailsParameterView extends LinearLayout
{

    @ViewById(R.id.server_details_parameter_item_title)
    public TextView parameterText;

    @ViewById(R.id.server_details_parameter_item_value)
    public TextView valueText;


    public ItemServerDetailsParameterView(Context context) {
        super(context);
    }

    public void bind(String parameter, String value)
    {
        parameterText.setText(parameter);
        valueText.setText(value);
    }
} // ItemServerDetailsParameterView
