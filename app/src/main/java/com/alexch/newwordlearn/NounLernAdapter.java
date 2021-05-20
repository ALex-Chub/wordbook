package com.alexch.newwordlearn;

import android.annotation.SuppressLint;
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

public class NounLernAdapter extends ArrayAdapter<Noun> {

    private final LayoutInflater inflater;
    private final int layout;
    private final ArrayList<Noun> nounpool;
    private final DatabaseReference myRef;
    String userid;



    public NounLernAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Noun> noun) {
        super(context, resource, noun);

        this.nounpool = noun;
        this.layout = resource;
        this.inflater=LayoutInflater.from(context);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference();

    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {

        final NounLernAdapter.ViewHolder viewHolder;

            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        final Noun noun = nounpool.get(position);



if(noun.getCheck()==null) {
    viewHolder.art.setText(noun.getArt());
    viewHolder.dnoun.setText(noun.getDenoun());

    viewHolder.dnoun.setClickable(false);
    viewHolder.rnoun.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewHolder.rnoun.setText(noun.getRusnoun());
            noun.setCheck("learn");
        }
    });
}else {
    viewHolder.art.setText(noun.getArt());
    viewHolder.dnoun.setText(noun.getDenoun());
    viewHolder.rnoun.setText(noun.getRusnoun());
}

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
        final Button art, dnoun, rnoun;
        final ImageButton droptolib ;
        ViewHolder(View view){
            art = (Button) view.findViewById(R.id.articl);
            dnoun= (Button) view.findViewById(R.id.nounw);
            rnoun= (Button) view.findViewById(R.id.runoun);
            droptolib = (ImageButton) view.findViewById(R.id.tolib);

        }
    }

}
