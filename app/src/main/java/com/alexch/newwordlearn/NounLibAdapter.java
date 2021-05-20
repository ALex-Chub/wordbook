package com.alexch.newwordlearn;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.util.ArrayList;
import java.util.List;

public class NounLibAdapter extends ArrayAdapter<Noun> {

    private ArrayList<String> categorylist = new ArrayList<String>();
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Noun> nounpool;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String userid;
    private Dialog dialog;
    private Notyfidata notyfidata;

    public NounLibAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Noun> noun) {

        super(context, resource, noun);
        this.nounpool = noun;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference();


    }

    static class ViewHolder {
        final TextView art, dnoun, rnoun ;
        final ImageButton stat, delword;
        ViewHolder(View view){
            art = (TextView) view.findViewById(R.id.artl);
            dnoun= (TextView) view.findViewById(R.id.deutchl);
            rnoun= (TextView) view.findViewById(R.id.russianl);
            stat =(ImageButton) view.findViewById(R.id.statl);
            delword = (ImageButton) view.findViewById(R.id.deletword);


        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final NounLibAdapter.ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new NounLibAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (NounLibAdapter.ViewHolder) convertView.getTag();
        }
        final Noun noun = nounpool.get(position);
        viewHolder.art.setText(noun.getArt());
        viewHolder.dnoun.setText(noun.getDenoun());
        viewHolder.rnoun.setText(noun.getRusnoun());
        if(noun.getStatus().equals("learn")) {
            viewHolder.stat.setImageResource(R.drawable.red);
        }else{viewHolder.stat.setImageResource(R.drawable.green);}


        viewHolder.stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noun.chengestatus();
                myRef.child("Users").child(userid).child("Noun").child(noun.getDenoun()).child("status").setValue(noun.getStatus());
                if(noun.getStatus().equals("learn")) {
                    viewHolder.stat.setImageResource(R.drawable.red);
                }else{viewHolder.stat.setImageResource(R.drawable.green);}
            }
        });


        viewHolder.delword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialognounedit);
                dialog.getWindow().setTitle("Подсказка");
                EditText artch = (EditText) dialog.findViewById(R.id.artchange);
                EditText denounch = (EditText) dialog.findViewById(R.id.denounchenge);
                EditText runounch = (EditText) dialog.findViewById(R.id.rusnounchenge);
                EditText catchenge = (EditText) dialog.findViewById(R.id.categorychanger);
                Spinner spincat = (Spinner) dialog.findViewById(R.id.categoryspin);
                ImageButton savech = (ImageButton) dialog.findViewById((R.id.savecheges));
                ImageButton delitword = (ImageButton) dialog.findViewById(R.id.deletword);
                artch.setText(noun.getArt());
                runounch.setText(noun.getRusnoun());
                denounch.setText(noun.getDenoun());



                ArrayAdapter spinadapter = new ArrayAdapter<String>(getContext(), R.layout.spinitem, categorylist);

                spincat.setAdapter(spinadapter);

                categorylist.clear();
                categorylist.add(noun.getCategory());




                myRef.child("Users").child(userid).child("Categories").child("Noun").addChildEventListener(new ChildEventListener() {
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
                                myRef.child("Users").child(userid).child("Noun").child(noun.getDenoun()).removeValue();
                                Toast.makeText(getContext(), "Слово удалено", Toast.LENGTH_LONG).show();
                                remove(noun);
                                dialog.dismiss();
                            }
                        });
                        snackbar.show();
                    }
                });

                savech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noun.setArt(artch.getText().toString());
                        noun.setCategory(catchenge.getText().toString());
                        String dn = noun.getDenoun();
                        noun.setDenoun(denounch.getText().toString());
                        noun.setRusnoun(runounch.getText().toString());
                        notyfidata = (Notyfidata) getContext();

                        if (!categorylist.contains(noun.getCategory())){
                            String cat = catchenge.getText().toString();
                            myRef.child("Users").child(userid).child("Categories").child("Noun").child(cat).setValue(cat);
                        }

                        myRef.child("Users").child(userid).child("Noun").child(noun.getDenoun()).setValue(noun);
                        Toast.makeText(getContext(), "Слово изменено", Toast.LENGTH_LONG).show();

                        if (!noun.getDenoun().equals(dn)) {

                              myRef.child("Users").child(userid).child("Noun").child(dn).removeValue();
                              remove(noun);
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
