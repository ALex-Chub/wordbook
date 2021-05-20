package com.alexch.newwordlearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Statsadapter  extends ArrayAdapter<Stats> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Stats> statpool;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String userid;



    public Statsadapter(@NonNull Context context, int resource, @NonNull ArrayList<Stats> stats) {
        super(context, resource, stats);
        this.statpool = stats;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference();

    }

    private static class ViewHolder {
        final TextView staname, statnumber ;

        ViewHolder(View view){
            staname = (TextView) view.findViewById(R.id.stat);
            statnumber= (TextView) view.findViewById(R.id.statvalue);

        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Statsadapter.ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new Statsadapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (Statsadapter.ViewHolder) convertView.getTag();
        }
        final Stats stats = statpool.get(position);
        viewHolder.staname.setText(stats.getName());
        viewHolder.statnumber.setText(stats.getNumber());
        return convertView;
    }
}
