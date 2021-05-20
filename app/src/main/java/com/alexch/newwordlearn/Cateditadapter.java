package com.alexch.newwordlearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cateditadapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<String> catlist;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String userid, type;
    Notyfidata notyfidata;

    public Cateditadapter(@NonNull Context context, int resource, @NonNull List<String> cat) {

        super(context, resource, cat);
        this.catlist = (ArrayList<String>) cat;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();
        switch (catlist.get(0)){
            case "Noun":
                type="Noun";
                break;
            case "Verbs":
                type="Verbs";
                break;
            case "Word":
                type ="Word";
                break;
        }
        catlist.remove(0);
        catlist.remove(0);
        catlist.remove(0);
        catlist.remove(0);
    }



    class ViewHolder {
        final EditText cat;
        final ImageButton delete, change;
        ViewHolder(View view){
            delete = (ImageButton) view.findViewById(R.id.delcat);
            change = (ImageButton) view.findViewById(R.id.savecat);
            cat = (EditText) view.findViewById(R.id.cats);

        }
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        final Cateditadapter.ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (Cateditadapter.ViewHolder) convertView.getTag();
        }
        final  String word = catlist.get(position);





        viewHolder.cat.setText(word);


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               Toast.makeText(getContext(),""+type, Toast.LENGTH_LONG).show();
                myRef.child("Users").child(userid).child("Categories").child(type).child(word).removeValue();
                remove(word);
            }
        });

        viewHolder.change.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Users").child(userid).child("Categories").child(type).child(word).removeValue();
                String newcat = viewHolder.cat.getText().toString();
                myRef.child("Users").child(userid).child("Categories").child(type).child(newcat).setValue(newcat);
                remove(word);
            }
        }));


        return convertView;
    }

}
