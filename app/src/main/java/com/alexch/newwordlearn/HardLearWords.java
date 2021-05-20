package com.alexch.newwordlearn;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HardLearWords extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ArrayList<Word> learningwordarray = new ArrayList<Word>();
    private ArrayList<Noun> nounArrayList = new ArrayList<Noun>();
    private ArrayList<Verbs> verbsArrayList = new ArrayList<Verbs>();
    private Dialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lerningwords);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userid = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();


        ListView wordlist = (ListView)findViewById(R.id.wordslist);
        Button showverbws = (Button)findViewById(R.id.verbs);
        Button shownouns = (Button) findViewById(R.id.nouns);
        Button showwords = (Button) findViewById(R.id.words);


        HardLearnWordAdapter adapter = new HardLearnWordAdapter(this, R.layout.hardlistlearnitem, learningwordarray);
        HardVerbLernAdapter verbLernAdapter = new HardVerbLernAdapter(this,R.layout.hardlearnverbitem, verbsArrayList);
        HardNounLernAdapter nounLernAdapter1 = new HardNounLernAdapter(this,R.layout.hardneunlernitem, nounArrayList);



        showwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                learningwordarray.clear();

                wordlist.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                myRef.child("Users").child(userid).child("Word").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                        Word word = dataSnapshot.getValue(Word.class);
                        if (word != null) {
                            String stat = word.getstatus();
                            if (stat.equals("learn")||word.getdeutschword().length()<15){
                                learningwordarray.add(word);
                                adapter.notifyDataSetChanged();}
                        } else {
                            Toast.makeText(HardLearWords.this, "Ошибка", Toast.LENGTH_SHORT).show();
                            learningwordarray.add(new Word("не загружает","слова", ""));
                            adapter.notifyDataSetChanged();
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
            }
        });



        shownouns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nounArrayList.clear();

                wordlist.setAdapter(nounLernAdapter1);

                nounLernAdapter1.notifyDataSetChanged();

                myRef.child("Users").child(userid).child("Noun").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                        Noun noun = dataSnapshot.getValue(Noun.class);
                        if (noun != null) {
                            String stat = noun.getStatus();
                            if (stat.equals("learn")){
                                nounArrayList.add(noun);
                                nounLernAdapter1.notifyDataSetChanged();}
                        } else {
                            Toast.makeText(HardLearWords.this, "Ошибка", Toast.LENGTH_SHORT).show();

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
            }
        });



        showverbws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verbsArrayList.clear();

                wordlist.setAdapter(verbLernAdapter);

                verbLernAdapter.notifyDataSetChanged();

                myRef.child("Users").child(userid).child("Verbs").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                        Verbs verb = dataSnapshot.getValue(Verbs.class);
                        if (verb != null) {
                            String stat = verb.getStatus();
                            if (stat.equals("learn")){
                                verbsArrayList.add(verb);
                                verbLernAdapter.notifyDataSetChanged();}
                        } else {
                            Toast.makeText(HardLearWords.this, "Ошибка", Toast.LENGTH_SHORT).show();
                            learningwordarray.add(new Word("не загружает","слова", ""));
                            adapter.notifyDataSetChanged();
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
            }
        });


    }

    public void gostats(View view){
        Intent intent = new Intent(HardLearWords.this, PersonalStats.class);startActivity(intent);
    }

    public void gotoaddword(View view){
        Intent intent = new Intent(HardLearWords.this, AddWord.class); startActivity(intent);
    }

    public  void gotolib(View view){
        Intent intent = new Intent(HardLearWords.this, LibraryWords.class); startActivity(intent);
    }

    public void showhelp(View view){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.faqveiw);
        dialog.getWindow().setTitle("Подсказка");

        dialog.setCancelable(false);
        TextView textView = (TextView)dialog.findViewById(R.id.faqtext);
        textView.setText(R.string.helplearn);
        dialog.show();

        Button closehelp= (Button)dialog.findViewById(R.id.closefaq);
        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }
}
