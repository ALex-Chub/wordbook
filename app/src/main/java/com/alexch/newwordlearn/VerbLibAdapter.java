package com.alexch.newwordlearn;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class VerbLibAdapter extends ArrayAdapter<Verbs> {

    private ArrayList<String> categorylist = new ArrayList<String>();
    private Dialog dialog;
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Verbs> verbspool;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String userid;
    Notyfidata notyfidata;

    public VerbLibAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Verbs> verbs) {
        super(context, resource, verbs);

        this.verbspool = verbs;
        this.layout = resource;
        this.inflater= LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference();
    }

    class ViewHolder {
        final TextView nomenat, f2, f3, ruv;
        final ImageButton stat, delword;
        ViewHolder(View view){
            nomenat = (TextView) view.findViewById(R.id.nom);
            f2= (TextView) view.findViewById(R.id.for2m);
            f3= (TextView) view.findViewById(R.id.for3m);
            ruv= (TextView) view.findViewById(R.id.ruverb);
            stat=(ImageButton) view.findViewById(R.id.stats);
            delword = (ImageButton) view.findViewById(R.id.deletword);


        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final VerbLibAdapter.ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new VerbLibAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (VerbLibAdapter.ViewHolder) convertView.getTag();
        }
        final Verbs verbs = verbspool.get(position);

        viewHolder.f2.setText(verbs.getForm2());
        viewHolder.f3.setText(verbs.getForm3());
        viewHolder.ruv.setText(verbs.getRuverb());
        viewHolder.nomenat.setText(verbs.getNomenativ());
        if(verbs.getStatus().equals("learn")) {
            viewHolder.stat.setImageResource(R.drawable.red);
        }else{viewHolder.stat.setImageResource(R.drawable.green);}

        viewHolder.stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verbs.chengestatus();
                myRef.child("Users").child(userid).child("Verbs").child(verbs.getNomenativ()).child("status").setValue(verbs.getStatus());
                if(verbs.getStatus().equals("learn")) {
                    viewHolder.stat.setImageResource(R.drawable.red);
                }else{viewHolder.stat.setImageResource(R.drawable.green);}
            }
        });

        viewHolder.delword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialogverbedit);
                dialog.getWindow().setTitle("Подсказка");
                EditText artch = (EditText) dialog.findViewById(R.id.artchange);
                EditText denounch = (EditText) dialog.findViewById(R.id.denounchenge);
                EditText runounch = (EditText) dialog.findViewById(R.id.rusnounchenge);
                EditText catchenge = (EditText) dialog.findViewById(R.id.categorychanger);
                EditText form3ch = (EditText)dialog.findViewById(R.id.driformch);
                Spinner spincat = (Spinner) dialog.findViewById(R.id.categoryspin);
                ImageButton savech = (ImageButton) dialog.findViewById((R.id.savecheges));
                ImageButton delitword = (ImageButton) dialog.findViewById(R.id.deletword);
                artch.setText(verbs.getForm2());
                runounch.setText(verbs.getRuverb());
                denounch.setText(verbs.getNomenativ());
                form3ch.setText(verbs.getForm3());




                ArrayAdapter spinadapter = new ArrayAdapter<String>(getContext(), R.layout.spinitem, categorylist);

                spincat.setAdapter(spinadapter);

                categorylist.clear();
                categorylist.add(verbs.getCategory());




                myRef.child("Users").child(userid).child("Categories").child("Verbs").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                        String c = dataSnapshot.getValue(String.class);
                        if (!categorylist.contains(c)) {
                            categorylist.add(c);
                            spinadapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String ca = String.valueOf(spinadapter.getItem(position));

                        catchenge.setText(ca);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                delitword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar = Snackbar.make(v, "Удалить слово?", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Удалить", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myRef.child("Users").child(userid).child("Verbs").child(verbs.getNomenativ()).removeValue();
                                Toast.makeText(getContext(), "Слово удалено", Toast.LENGTH_LONG).show();
                                remove(verbs);
                                dialog.dismiss();
                            }
                        });
                        snackbar.show();
                    }
                });

                savech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        verbs.setForm2(artch.getText().toString());
                        verbs.setCategory(catchenge.getText().toString());
                        String dn = verbs.getNomenativ();
                        verbs.setNomenativ(denounch.getText().toString());
                        verbs.setRuverb(runounch.getText().toString());
                        verbs.setForm3(form3ch.getText().toString());
                        notyfidata = (Notyfidata) getContext();
                        if (!categorylist.contains(verbs.getCategory())){
                             myRef.child("Users").child(userid).child("Categories").child("Verbs").child(verbs.getCategory()).setValue(verbs.getCategory());
                        }

                        myRef.child("Users").child(userid).child("Verbs").child(verbs.getNomenativ()).setValue(verbs);
                        Toast.makeText(getContext(), "Слово изменено", Toast.LENGTH_LONG).show();
                        if(!verbs.getNomenativ().equals(dn)){
                            myRef.child("Users").child(userid).child("Verbs").child(dn).removeValue();
                            remove(verbs);
                        }

                        notyfidata.notifyDataSetChanged();
                        dialog.dismiss();


                    }
                });


                dialog.show();

            }
        });

        return convertView;
    }


}
