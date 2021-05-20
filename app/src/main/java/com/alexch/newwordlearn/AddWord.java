package com.alexch.newwordlearn;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class AddWord extends AppCompatActivity {

    private Dialog dialog;

    String deutchword, rusword, categoryword;

    int var;
    String type="Empty";
    private ArrayList<Word> learningwordarray = new ArrayList<Word>();
    private ArrayList<Noun> nounArrayList = new ArrayList<Noun>();
    private ArrayList<Verbs> verbsArrayList = new ArrayList<Verbs>();
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private ArrayList<String> categorylist = new ArrayList<String>();
    private ArrayAdapter spinadapter;
    String userid;
    String secform, form3verb;
    String art1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addword);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();

        EditText category = (EditText) findViewById(R.id.category);
        ImageButton sevecat = (ImageButton) findViewById(R.id.addcategory);
        EditText newdeutch = (EditText) findViewById(R.id.newdeutch);
        EditText newruss = (EditText)findViewById(R.id.newrus);
        Button saveword = (Button)findViewById(R.id.createword);
        Spinner spincat = (Spinner) findViewById(R.id.categoryspin);
        EditText form2art = (EditText) findViewById(R.id.artform2);
        EditText art = (EditText)findViewById(R.id.art);
        RadioGroup selector = (RadioGroup) findViewById(R.id.selectorwordetyp);
        RadioButton selV = (RadioButton) findViewById(R.id.selectv);
        RadioButton selN = (RadioButton) findViewById(R.id.selectn);
        RadioButton selw = (RadioButton) findViewById(R.id.selectw);


        selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int Id) {
                switch (Id){
                    case R.id.selectv:
                        art.setVisibility(View.VISIBLE);

                        newdeutch.setVisibility(View.VISIBLE);

                        newruss.setVisibility(View.VISIBLE);

                        form2art.setVisibility(View.VISIBLE);

                        art.setHint("Infinitiv");
                        newdeutch.setHint("Präteritum");
                        newruss.setHint("Partizip II");
                        form2art.setHint("Перевод");
                        var=1;
                        type="Verbs";
                        break;
                    case R.id.selectn:
                        art.setVisibility(View.VISIBLE);
                        art.setHint("Артикль");
                        newdeutch.setHint("Существительное");
                        form2art.setVisibility(View.INVISIBLE);
                        newruss.setVisibility(View.VISIBLE);
                        newdeutch.setVisibility(View.VISIBLE);
                        newruss.setHint("Перевод");

                        var=2;
                        type="Noun";
                        break;
                    case R.id.selectw:
                        newdeutch.setHint("Cлово или фраза");
                        newdeutch.setVisibility(View.VISIBLE);
                        newruss.setVisibility(View.VISIBLE);
                        form2art.setVisibility(View.INVISIBLE);
                        form2art.setHint("");
                        newruss.setHint("Перевод");
                        art.setVisibility(View.INVISIBLE);

                        var=3;
                        type="Word";
                        break;
                    default:
                        newdeutch.setHint("Cлово или фраза");
                        form2art.setVisibility(View.INVISIBLE);
                        form2art.setHint("");

                        var=3;
                        type="Word";
                        break;
                }
                spinrenovator();
            }
        });


        spinadapter = new ArrayAdapter<String>(this, R.layout.spinitem, categorylist);
        spincat.setAdapter(spinadapter);

        spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ca = String.valueOf(spinadapter.getItem(position));
                category.setText(ca);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sevecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = category.getText().toString().trim();
                if (cat.isEmpty()||type=="Empty"||cat.equals("Категории")||categorylist.contains(cat)){
                    showtoast("Ошибка");
                }else{
                myRef.child("Users").child(userid).child("Categories").child(type).child(cat).setValue(cat);
                    showtoast("Фильтр добавлен");}
            }
        });

        saveword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deutchword = newdeutch.getText().toString().trim();
                if (deutchword.contains(".")||deutchword.contains("#")||deutchword.contains("$")||deutchword.contains("[")||deutchword.contains("]")){

                    showtoast("Нельзя добавлять выражения с '.', '#', '$', '[' или ']'");


                }else {


                    switch (var) {
                        case 3:
                            deutchword = newdeutch.getText().toString().trim();
                            rusword = newruss.getText().toString().trim();
                            categoryword = category.getText().toString().trim();
                            if (deutchword.isEmpty() || rusword.isEmpty() || categoryword.isEmpty()) {
                                showtoast("Ошибка: заполните все поля");
                            } else {
                                Word newword = new Word(deutchword, rusword, categoryword, userid);
                                myRef.child("Users").child(userid).child("Word").child(deutchword).setValue(newword);
                                showtoast("Слово добавлено");
                                if (!categorylist.contains(categoryword)) {
                                    myRef.child("Users").child(userid).child("Categories").child(type).child(categoryword).setValue(categoryword);
                                }
                                newdeutch.setText("");
                                newruss.setText("");
                                newdeutch.setHint("Cлово или фраза");
                                newruss.setHint("перевод");
                            }
                            break;
                        case 1:
                            deutchword = art.getText().toString().trim();
                            rusword = form2art.getText().toString().trim();
                            categoryword = category.getText().toString().trim();
                            secform = newdeutch.getText().toString().trim();
                            form3verb = newruss.getText().toString().trim();
                            Verbs newverb = new Verbs(deutchword, secform, form3verb, rusword, categoryword);
                            if (deutchword.isEmpty() || rusword.isEmpty() || secform.isEmpty() || form3verb.isEmpty() || categoryword.isEmpty()) {
                                String t = "Ошибка: заполните все поля";
                                showtoast(t);

                            } else {
                                myRef.child("Users").child(userid).child("Verbs").child(deutchword).setValue(newverb);
                                showtoast("Слово добавлено");
                                if (!categorylist.contains(categoryword)) {
                                    myRef.child("Users").child(userid).child("Categories").child(type).child(categoryword).setValue(categoryword);
                                }
                                newdeutch.setText("");
                                newruss.setText("");
                                form2art.setText("");
                                art.setText("");
                                art.setHint("Infinitiv");
                                newdeutch.setHint("Präteritum");
                                newruss.setHint("Partizip II");
                                form2art.setHint("Перевод");
                            }
                            break;
                        case 2:
                            deutchword = newdeutch.getText().toString().trim();
                            rusword = newruss.getText().toString().trim();
                            categoryword = category.getText().toString().trim();
                            art1 = art.getText().toString().trim();
                            Noun newnoun = new Noun(art1, deutchword, rusword, categoryword);
                            if (deutchword.isEmpty() || rusword.isEmpty() || art1.isEmpty() || categoryword.isEmpty()) {
                                showtoast("Ошибка: заполните все поля");
                            } else {
                                myRef.child("Users").child(userid).child("Noun").child(deutchword).setValue(newnoun);
                                showtoast("Слово добавлено");
                                if (!categorylist.contains(categoryword)) {
                                    myRef.child("Users").child(userid).child("Categories").child(type).child(categoryword).setValue(categoryword);
                                }
                                newdeutch.setText("");
                                newruss.setText("");
                                art.setText("");
                                newdeutch.setHint("Существительное");
                                newruss.setHint("перевод");
                                art.setHint("Артикль");
                            }
                            break;

                    }
                }
            }
        });


    }
    private void spinrenovator(){
        categorylist.clear();
        myRef.child("Users").child(userid).child("Categories").child(type).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                String c = dataSnapshot.getValue(String.class);
                if (c!=null){
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

    }


    public void goback (View view){
     finish();
    }


    public void showhelp(View view){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.faqveiw);
        dialog.getWindow().setTitle("Подсказка");
        dialog.setCancelable(false);
        TextView textView = (TextView)dialog.findViewById(R.id.faqtext);
        textView.setText(R.string.helpadd);
        dialog.show();

        Button closehelp= (Button)dialog.findViewById(R.id.closefaq);
        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void showtoast(String tex){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(tex);
        Toast toast = new Toast(AddWord.this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
