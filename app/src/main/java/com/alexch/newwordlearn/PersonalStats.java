package com.alexch.newwordlearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonalStats extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ArrayList<Stats> statsArrayList = new ArrayList<Stats>();
    private ArrayList<Word> learningwordarray = new ArrayList<Word>();
    private ArrayList<Noun> nounArrayList = new ArrayList<Noun>();
    private ArrayList<Verbs> verbsArrayList = new ArrayList<Verbs>();


    String userid, name;
    int wordnum, wordcomplieted=0, wordlearn, verbsnum, nounsnum, wordsnum;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();

        ListView statslist = (ListView)findViewById(R.id.statslist);

        Button exit = (Button)findViewById(R.id.exit);
        TextView welkome = (TextView)findViewById(R.id.welkome);
        LinearLayout line = (LinearLayout)findViewById(R.id.lerned);
        TextView numberslearned = (TextView)findViewById(R.id.learning);



        myRef.child("Users").child(userid).child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
                welkome.setText("Здравствуйте, "+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Statsadapter statsadapter = new Statsadapter(this, R.layout.statslistitem, statsArrayList);

        statslist.setAdapter(statsadapter);

        myRef.child("Users").child(userid).child("Word").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Word sword = dataSnapshot.getValue(Word.class);
                if(sword!=null) {
                    learningwordarray.add(sword);
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

        myRef.child("Users").child(userid).child("Noun").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Noun sword = dataSnapshot.getValue(Noun.class);

                nounArrayList.add(sword);
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

        myRef.child("Users").child(userid).child("Verbs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Verbs sword = dataSnapshot.getValue(Verbs.class);
                if(sword!=null) {
                    verbsArrayList.add(sword);
                }
                statsArrayList.clear();

                wordnum=0;
                wordsnum=0;
                wordcomplieted=0;
                wordlearn=0;
                verbsnum=0;
                nounsnum=0;



                for (int n = 0;n<verbsArrayList.size();n++){
                    Verbs verbs = verbsArrayList.get(n);
                    if (verbs.getStatus().equals("Complieted")){
                        wordcomplieted++;}else {wordlearn++;}
                }

                for (int n=0; n<nounArrayList.size();n++){
                    Noun noun = nounArrayList.get(n);
                    if (noun.getStatus().equals("Complieted")){
                        wordcomplieted++;}else{wordlearn++;}
                }

                for (int n =0;n<learningwordarray.size();n++){
                    Word w = learningwordarray.get(n);
                    if (w.getstatus().equals("Complieted")){
                        wordcomplieted++;}else{wordlearn++;}
                }


                wordnum = learningwordarray.size()+verbsArrayList.size()+nounArrayList.size();
                nounsnum= nounArrayList.size();
                verbsnum = verbsArrayList.size();
                wordsnum = learningwordarray.size();


                statsArrayList.add(new Stats("Добавлено слов:",String.valueOf(wordnum)));
                statsArrayList.add(new Stats("Сейчас учу слов:",String.valueOf(wordlearn)));
                statsArrayList.add(new Stats("Глаголов :",String.valueOf(verbsnum)));
                statsArrayList.add(new Stats("Существительных :",String.valueOf(nounsnum)));
                statsArrayList.add(new Stats("Прочих слов/фраз :",String.valueOf(wordsnum)));


                line.setVisibility(View.VISIBLE);
                statslist.setAdapter(statsadapter);
                statsadapter.notifyDataSetChanged();
               numberslearned.setText(String.valueOf(wordcomplieted));

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









        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Выйти из аккаунта?", Snackbar.LENGTH_LONG);
                snackbar.setAction("Выйти", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signOut();

                    }
                });
                snackbar.show();

            }
        });








    }

    public void gohard(View view){
        Intent intent = new Intent(PersonalStats.this, HardLearWords.class);startActivity(intent);
        finish();
    }

    public void golearn(View view){
        Intent intent = new Intent(PersonalStats.this, LearWords.class);startActivity(intent);finish();
    }

    public  void  golib(View view){
        Intent intent = new Intent(PersonalStats.this, LibraryWords.class);startActivity(intent);finish();
    }

    public void goadd(View view){
        Intent intent = new Intent(PersonalStats.this, AddWord.class);startActivity(intent);finish();
    }

}
