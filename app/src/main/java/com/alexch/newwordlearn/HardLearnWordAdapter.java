package com.alexch.newwordlearn;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HardLearnWordAdapter extends ArrayAdapter<Word> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Word> wordspool;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String userid, d, r;


    public HardLearnWordAdapter(Context context, int resource, ArrayList<Word> words) {
        super(context, resource, words);
        this.wordspool=words;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference();


    }
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        final Word word = wordspool.get(position);

       if(word.getCheck()==null){
           viewHolder.russian.setText(word.getruschword());}
       else{
           viewHolder.deutch.setText(word.gethtdeutschword());
           viewHolder.russian.setText(word.gethtruschword());
           if ((!viewHolder.deutch.getText().toString().trim().equalsIgnoreCase(word.getdeutschword()))||!viewHolder.russian.getText().toString().trim().equalsIgnoreCase(word.getruschword())){

               if(!viewHolder.deutch.getText().toString().trim().equalsIgnoreCase(word.getdeutschword())){
                   viewHolder.deutch.setTextColor(Color.RED);
               }if (!viewHolder.russian.getText().toString().trim().equalsIgnoreCase(word.getruschword())){
                   viewHolder.russian.setTextColor(Color.RED);
               }
           }
           else{
               viewHolder.russian.setTextColor(Color.rgb(00,150,00));
               viewHolder.deutch.setTextColor(Color.rgb(00,150,00));

           }
       }

        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if ((!viewHolder.deutch.getText().toString().trim().equalsIgnoreCase(word.getdeutschword()))||!viewHolder.russian.getText().toString().trim().equalsIgnoreCase(word.getruschword())){
                    Toast toast = Toast.makeText(getContext(),"Неверно", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();;
                    if(!viewHolder.deutch.getText().toString().trim().equalsIgnoreCase(word.getdeutschword())){
                        viewHolder.deutch.setTextColor(Color.RED);
                    }
                    if (!viewHolder.russian.getText().toString().trim().equalsIgnoreCase(word.getruschword())){
                        viewHolder.russian.setTextColor(Color.RED);
                    }
                }
                else{Toast toast = Toast.makeText(getContext(),"Молодец", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();;
                    viewHolder.russian.setTextColor(Color.rgb(00,150,00));
                    viewHolder.deutch.setTextColor(Color.rgb(00,150,00));

                }

                r=viewHolder.russian.getText().toString().trim();
                word.settRuschword(r);
                d=viewHolder.deutch.getText().toString().trim();
                word.settDeutschword(d);
                word.setCheck("learn");
            }

        });

       viewHolder.droptolib.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               word.chengestatus();
               myRef.child("Users").child(userid).child("Word").child(word.getdeutschword()).child("status").setValue(word.getstatus());
               if(word.getstatus().equals("Complieted")){
                   remove(word);
                   Toast toast = Toast.makeText(getContext(),"Слово добавлено в архив", Toast.LENGTH_LONG);
                   toast.setGravity(Gravity.CENTER,0,0);
                   toast.show();
               }
           }
       });
        return convertView;
    }



    class ViewHolder {
        final EditText deutch;
        final TextView russian;
        final ImageButton droptolib, check;
        ViewHolder(View view){
            deutch = (EditText) view.findViewById(R.id.deutch);
            russian = (TextView) view.findViewById(R.id.russian);
            droptolib = (ImageButton) view.findViewById(R.id.tolib);
            check = (ImageButton) view.findViewById(R.id.chek);
        }
    }
}
