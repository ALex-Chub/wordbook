package com.alexch.newwordlearn;

import android.content.Context;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class VerbLernAdapter extends ArrayAdapter<Verbs> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Verbs> verbspool;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String userid;

    public VerbLernAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Verbs> verbs) {
        super(context, resource, verbs);


        this.verbspool = verbs;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference();


    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final VerbLernAdapter.ViewHolder viewHolder;
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        final Verbs verbs = verbspool.get(position);



        if (verbs.getCheck()==null){
            viewHolder.ruv.setText(verbs.getRuverb());

            viewHolder.nomenat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.nomenat.setText(verbs.getNomenativ());
                    viewHolder.f2.setText(verbs.getForm2());
                    viewHolder.f3.setText(verbs.getForm3());
                    verbs.setCheck("chek");
                }
            });



        }else {

            viewHolder.nomenat.setText(verbs.getNomenativ());
            viewHolder.ruv.setText(verbs.getRuverb());
            viewHolder.f2.setText(verbs.getForm2());
            viewHolder.f3.setText(verbs.getForm3());
        }



        viewHolder.droptolib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verbs.chengestatus();
                myRef.child("Users").child(userid).child("Verbs").child(verbs.getNomenativ()).child("status").setValue(verbs.getStatus());
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



    class ViewHolder {
        final Button nomenat, f2, f3, ruv;
        final ImageButton  droptolib ;
        ViewHolder(View view){
           nomenat = (Button) view.findViewById(R.id.nomenativ);
            f2= (Button) view.findViewById(R.id.form2);
            f3= (Button) view.findViewById(R.id.form3);
            ruv= (Button) view.findViewById(R.id.ruv);
            droptolib = (ImageButton) view.findViewById(R.id.tolibrary);

        }
    }
}
