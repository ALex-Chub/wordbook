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
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LearnWordAdapter extends ArrayAdapter<Word> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Word> wordspool;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String userid;


    public LearnWordAdapter( Context context, int resource, ArrayList<Word> words) {
        super(context, resource, words);
        this.wordspool=words;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference();


    }


    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);


        final Word word = wordspool.get(position);


        viewHolder.deutch.setText(word.getdeutschword());

        if (word.getCheck()==null) {

            viewHolder.russian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.russian.setText(word.getruschword());
                    word.setCheck("answered");
                }
            });
        }else{  viewHolder.russian.setText(word.getruschword());}

           viewHolder.droptolib.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               word.chengestatus();
               myRef.child("Users").child(userid).child("Word").child(word.getdeutschword()).child("status").setValue(word.getstatus());
               if(word.getstatus().equals("Complieted")){
                   remove(word);
                   Toast toast = Toast.makeText(getContext(), "Слово добавлено в архив", Toast.LENGTH_LONG);
                   toast.setGravity(Gravity.CENTER,0,0);
                   toast.show();;
               }
           }
       });
        return convertView;
    }
    class ViewHolder {
        final Button deutch, russian;
        final ImageButton droptolib;
        ViewHolder(View view){
            deutch = (Button) view.findViewById(R.id.deutch);
            russian = (Button) view.findViewById(R.id.russian);
            droptolib = (ImageButton) view.findViewById(R.id.tolib);

        }
    }



}
