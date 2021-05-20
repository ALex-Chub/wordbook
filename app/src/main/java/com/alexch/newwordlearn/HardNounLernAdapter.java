package com.alexch.newwordlearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HardNounLernAdapter extends ArrayAdapter<Noun> {

    private final LayoutInflater inflater;
    private final int layout;
    private final ArrayList<Noun> nounpool;
    private final DatabaseReference myRef;
    String userid, a, r, d;
    int n;



    public HardNounLernAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Noun> noun) {
        super(context, resource, noun);

        this.nounpool = noun;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();




        myRef = FirebaseDatabase.getInstance().getReference();

    }

    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {

        final HardNounLernAdapter.ViewHolder viewHolder;

            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        final Noun noun = nounpool.get(position);


        if (noun.getCheck()==null) {

               viewHolder.rnoun.setText(noun.getRusnoun());

        }else{
            viewHolder.art.setText(noun.gethtArt());
            viewHolder.dnoun.setText(noun.gethtDenoun());
            viewHolder.rnoun.setText(noun.gethtRusnoun());
            if (!viewHolder.art.getText().toString().trim().equalsIgnoreCase(noun.getArt())||(!viewHolder.dnoun.getText().toString().trim().equalsIgnoreCase(noun.getDenoun()))){

                    if (!viewHolder.art.getText().toString().trim().equalsIgnoreCase(noun.getArt())){
                        viewHolder.art.setTextColor(Color.RED);
                    }
                    if (!viewHolder.dnoun.getText().toString().trim().equalsIgnoreCase(noun.getDenoun())){
                        viewHolder.dnoun.setTextColor(Color.RED);
                    }

            }
            else{
                viewHolder.art.setTextColor(Color.rgb(00,150,00));
                viewHolder.dnoun.setTextColor(Color.rgb(00,150,00));
                viewHolder.rnoun.setTextColor(Color.rgb(00,150,00));}
        }


        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (!viewHolder.art.getText().toString().trim().equals(noun.getArt())||(!viewHolder.dnoun.getText().toString().trim().equals(noun.getDenoun()))){
                    Toast toast = Toast.makeText(getContext(),"Неверно", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();;

                     if (!viewHolder.art.getText().toString().trim().equalsIgnoreCase(noun.getArt())){
                         viewHolder.art.setTextColor(Color.RED);
                     }
                     if (!viewHolder.dnoun.getText().toString().trim().equalsIgnoreCase(noun.getDenoun())){
                         viewHolder.dnoun.setTextColor(Color.RED);
                     }

                }
                else{Toast toast = Toast.makeText(getContext(),"Молодец", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();;
                viewHolder.art.setTextColor(Color.rgb(00,150,00));
                viewHolder.dnoun.setTextColor(Color.rgb(00,150,00));
                viewHolder.rnoun.setTextColor(Color.rgb(00,150,00));}
                noun.setCheck("lern");
                a = viewHolder.art.getText().toString().trim();
                noun.sethtArt(a);
                d = viewHolder.dnoun.getText().toString().trim();
                noun.sethtDenoun(d);
                r = viewHolder.rnoun.getText().toString().trim();
                noun.sethtRusnoun(r);
            }
        });

        viewHolder.droptolib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noun.chengestatus();
                myRef.child("Users").child(userid).child("Noun").child(noun.getDenoun()).child("status").setValue(noun.getStatus());
                if(noun.getStatus().equals("Complieted")){
                    remove(noun);
                    Toast toast = Toast.makeText(getContext(), "Слово добавлено в архив", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();}

            }
        });
        return convertView;
    }


    class ViewHolder {
        final EditText art, dnoun ;
       final TextView rnoun;
        final ImageButton droptolib ,check;
        ViewHolder(View view){
            art = (EditText) view.findViewById(R.id.articl);
            dnoun= (EditText) view.findViewById(R.id.nounw);
            rnoun= (TextView) view.findViewById(R.id.runoun);
            droptolib = (ImageButton) view.findViewById(R.id.tolib);
            check = (ImageButton)view.findViewById(R.id.chek);

        }
    }

}
