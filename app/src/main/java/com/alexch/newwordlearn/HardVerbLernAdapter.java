package com.alexch.newwordlearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class HardVerbLernAdapter extends ArrayAdapter<Verbs> {

    private final LayoutInflater inflater;
    private final int layout;
    private final ArrayList<Verbs> verbspool;
    private final DatabaseReference myRef;
    String userid, n, ff, fff, r;

    public HardVerbLernAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Verbs> verbs) {
        super(context, resource, verbs);
        this.verbspool = verbs;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();

    }
    class ViewHolder {
        final EditText nom, f2, f3;
        final TextView r;
        final ImageButton droptolib ,check;
        ViewHolder(View view){
            nom = (EditText) view.findViewById(R.id.hnomenativ);
            f2= (EditText) view.findViewById(R.id.hform2);
            f3= (EditText) view.findViewById(R.id.hform3);
            r= (TextView) view.findViewById(R.id.hruv);
            droptolib = (ImageButton) view.findViewById(R.id.tolibrary);
            check = (ImageButton)view.findViewById(R.id.chek);

        }
    }
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {

        final HardVerbLernAdapter.ViewHolder viewHolder;

        convertView = inflater.inflate(this.layout, parent, false);
        viewHolder = new HardVerbLernAdapter.ViewHolder(convertView);
        convertView.setTag(viewHolder);

        final Verbs verbs = verbspool.get(position);


        if (verbs.getCheck()==null) {


            viewHolder.r.setText(verbs.getRuverb());

        }else{
            viewHolder.nom.setText(verbs.gethtNomenativ());
            viewHolder.f2.setText(verbs.gethtForm2());
            viewHolder.f3.setText(verbs.gethtForm3());
            viewHolder.r.setText(verbs.getRuverb());
            if (!viewHolder.nom.getText().toString().trim().equalsIgnoreCase(verbs.getNomenativ())||(!viewHolder.f2.getText().toString().trim().equalsIgnoreCase(verbs.getForm2()))||!viewHolder.f3.getText().toString().trim().equalsIgnoreCase(verbs.getForm3())){

                if (!viewHolder.nom.getText().toString().trim().equalsIgnoreCase(verbs.getNomenativ())){
                    viewHolder.nom.setTextColor(Color.RED);
                }
                    if (!viewHolder.f2.getText().toString().trim().equalsIgnoreCase(verbs.getForm2())){
                        viewHolder.f2.setTextColor(Color.RED);
                    }
                    if (!viewHolder.f3.getText().toString().trim().equalsIgnoreCase(verbs.getForm3())){
                        viewHolder.f3.setTextColor(Color.RED);
                    }

            }
            else{
                viewHolder.nom.setTextColor(Color.rgb(00,150,00));
                viewHolder.f2.setTextColor(Color.rgb(00,150,00));
                viewHolder.r.setTextColor(Color.rgb(00,150,00));
                viewHolder.f3.setTextColor(Color.rgb(00,150,00));
            }
        }


        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (!viewHolder.nom.getText().toString().trim().equalsIgnoreCase(verbs.getNomenativ())||(!viewHolder.f2.getText().toString().trim().equalsIgnoreCase(verbs.getForm2()))||!viewHolder.f3.getText().toString().trim().equalsIgnoreCase(verbs.getForm3())){
                    Toast toast = Toast.makeText(getContext(),"Неверно", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    if (!viewHolder.nom.getText().toString().trim().equalsIgnoreCase(verbs.getNomenativ())){
                        viewHolder.nom.setTextColor(Color.RED);
                    }
                        if (!viewHolder.f2.getText().toString().trim().equalsIgnoreCase(verbs.getForm2())){
                            viewHolder.f2.setTextColor(Color.RED);
                        }
                        if (!viewHolder.f3.getText().toString().trim().equalsIgnoreCase(verbs.getForm3())){
                            viewHolder.f3.setTextColor(Color.RED);
                        }

                }
                else{Toast toast = Toast.makeText(getContext(),"Молодец", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    viewHolder.nom.setTextColor(Color.rgb(00,150,00));
                    viewHolder.f2.setTextColor(Color.rgb(00,150,00));
                    viewHolder.r.setTextColor(Color.rgb(00,150,00));
                    viewHolder.f3.setTextColor(Color.rgb(00,150,00));
                }
                verbs.setCheck("lern");
                n = viewHolder.nom.getText().toString().trim();
                verbs.sethtNomenativ(n);
               ff = viewHolder.f2.getText().toString().trim();
               verbs.sethtForm2(ff);
                fff = viewHolder.f3.getText().toString().trim();
                verbs.sethtForm3(fff);
                r = viewHolder.r.getText().toString().trim();
                verbs.sethtRuverb(r);

            }
        });

        viewHolder.droptolib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verbs.chengestatus();
                myRef.child("Users").child(userid).child("Noun").child(verbs.getNomenativ()).child("status").setValue(verbs.getStatus());
                if(verbs.getStatus().equals("Complieted")){
                    remove(verbs);
                  Toast toast = Toast.makeText(getContext(), "Слово добавлено в архив", Toast.LENGTH_LONG);
                  toast.setGravity(Gravity.CENTER,0,0);
                 toast.show();
                }

            }
        });
        return convertView;
    }



}
