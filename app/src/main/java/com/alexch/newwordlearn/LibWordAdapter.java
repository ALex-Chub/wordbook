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

import java.util.ArrayList;
import java.util.List;

public class LibWordAdapter extends ArrayAdapter<Word> {


    private Dialog dialog;
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Word> wordspool;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ArrayList<String> categorylist = new ArrayList<String>();
    String userid;
    Notyfidata notyfidata;


    public LibWordAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Word> words) {
        super(context, resource, words);

        this.wordspool=words;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference();


    }
    class ViewHolder {
        final TextView deutch, russian;
        final ImageButton droptolib, delword;
        ViewHolder(View view){
            deutch = (TextView) view.findViewById(R.id.deutchl);
            russian = (TextView) view.findViewById(R.id.russianl);
            delword = (ImageButton) view.findViewById(R.id.deletword);
            droptolib = (ImageButton) view.findViewById(R.id.tolib);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final LibWordAdapter.ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new LibWordAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (LibWordAdapter.ViewHolder) convertView.getTag();
        }
        final Word word = wordspool.get(position);


        viewHolder.russian.setText(word.getruschword());
        viewHolder.deutch.setText(word.getdeutschword());

            if (word.getstatus().equals("learn")) {
                viewHolder.droptolib.setImageResource(R.drawable.red);
            } else {
                viewHolder.droptolib.setImageResource(R.drawable.green);
            }


        viewHolder.droptolib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word.chengestatus();
                myRef.child("Users").child(userid).child("Word").child(word.getdeutschword()).child("status").setValue(word.getstatus());
                    if(word.getstatus().equals("learn")) {
                        viewHolder.droptolib.setImageResource(R.drawable.red);
                    }else{viewHolder.droptolib.setImageResource(R.drawable.green);}
            }
        });

        viewHolder.delword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialogwordedit);
                dialog.getWindow().setTitle("Подсказка");
                EditText denounch = (EditText) dialog.findViewById(R.id.denounchenge);
                EditText runounch = (EditText) dialog.findViewById(R.id.rusnounchenge);
                EditText catchenge = (EditText) dialog.findViewById(R.id.categorychanger);
                Spinner spincat = (Spinner) dialog.findViewById(R.id.categoryspin);
                ImageButton savech = (ImageButton) dialog.findViewById((R.id.savecheges));
                ImageButton delitword = (ImageButton) dialog.findViewById(R.id.deletword);
                runounch.setText(word.getruschword());
                denounch.setText(word.getdeutschword());



                ArrayAdapter spinadapter = new ArrayAdapter<String>(getContext(), R.layout.spinitem, categorylist);

                spincat.setAdapter(spinadapter);

                categorylist.clear();
                categorylist.add(word.getCatogory());




                myRef.child("Users").child(userid).child("Categories").child("Word").addChildEventListener(new ChildEventListener() {
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
                                myRef.child("Users").child(userid).child("Word").child(word.getdeutschword()).removeValue();
                                Toast.makeText(getContext(), "Слово удалено", Toast.LENGTH_LONG).show();
                                remove(word);
                                dialog.dismiss();
                            }
                        });
                        snackbar.show();
                    }
                });

                savech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        word.setCatogory(catchenge.getText().toString());
                        String dn = word.getdeutschword();
                        word.setDeutschword(denounch.getText().toString());
                        word.setRuschword(runounch.getText().toString());
                        notyfidata = (Notyfidata) getContext();

                        if (!categorylist.contains(word.getCatogory())){
                            myRef.child("Users").child(userid).child("Categories").child("Word").child(word.getCatogory()).setValue(word.getCatogory());
                        }

                        myRef.child("Users").child(userid).child("Word").child(word.getdeutschword()).setValue(word);
                        Toast.makeText(getContext(), "Слово изменено", Toast.LENGTH_LONG).show();

                        if (!word.getdeutschword().equals(dn)) {

                            myRef.child("Users").child(userid).child("Word").child(dn).removeValue();
                            remove(word);
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
