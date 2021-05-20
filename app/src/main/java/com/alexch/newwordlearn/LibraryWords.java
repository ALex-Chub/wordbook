package com.alexch.newwordlearn;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class LibraryWords extends AppCompatActivity implements Notyfidata {

    private Dialog dialog , editcatdialog;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ArrayList<Word> learningwordarray = new ArrayList<Word>();
    private ArrayList<Noun> nounArrayList = new ArrayList<Noun>();
    private ArrayList<Verbs> verbsArrayList = new ArrayList<Verbs>();
    private ArrayList<Word> filterlearningwordarray = new ArrayList<Word>();
    private ArrayList<Noun> filternounArrayList = new ArrayList<Noun>();
    private ArrayList<Verbs> filterverbsArrayList = new ArrayList<Verbs>();
    private ArrayList<String> catarray = new ArrayList<String>();
    ArrayAdapter catspinAdapter;
    LinearLayout fi;
    String userid, type = "Word";
    String searchword;
    ListView wordlist;
    VerbLibAdapter filterverbLernAdapter, verbLernAdapter;
    LibWordAdapter adapter, filtrword;
    NounLibAdapter nounLernAdapter, filtrnounibAdapter;
    EditText search;
    ImageButton editcats;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libraryview);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();

        wordlist = (ListView)findViewById(R.id.wordslist);
        Button showverbws = (Button)findViewById(R.id.verbs);
        Button shownouns = (Button) findViewById(R.id.nouns);
        Button showwords = (Button) findViewById(R.id.words);
        search = (EditText) findViewById(R.id.search);
        fi= (LinearLayout) findViewById(R.id.filterpanel);
        ImageButton findru = (ImageButton) findViewById(R.id.gosearch);
        Spinner catspin = (Spinner) findViewById(R.id.catselector);
        editcats = (ImageButton)findViewById(R.id.editcats);

        catarray.add("Все категории");


        adapter = new LibWordAdapter(this, R.layout.wordlibitem, learningwordarray);
        nounLernAdapter = new NounLibAdapter( this, R.layout.nounlibitem, nounArrayList);
        verbLernAdapter = new VerbLibAdapter(this,R.layout.verblibitem, verbsArrayList);
        filtrword = new LibWordAdapter(this, R.layout.wordlibitem, filterlearningwordarray);
        filtrnounibAdapter = new NounLibAdapter( this, R.layout.nounlibitem, filternounArrayList);
        filterverbLernAdapter = new VerbLibAdapter(this,R.layout.verblibitem, filterverbsArrayList);
        catspinAdapter = new ArrayAdapter(this, R.layout.spinitem, catarray);


        catspin.setAdapter(catspinAdapter);


//берём фильтр тут
        catspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String filter = String.valueOf(catspinAdapter.getItem(position));
                switch (position){
                    case 0:
                        if (type.equals("Word")){
                            wordlist.setAdapter(adapter);
                        }

                        if (type.equals("Noun")){
                        wordlist.setAdapter(nounLernAdapter);
                        }else{
                            wordlist.setAdapter(verbLernAdapter);
                        }
                        break;
                    case 1:
                        filterstat("Complieted",type);
                        break;
                    case 2:
                        filterstat("learn",type);
                        break;
                    default:
                        filteer(filter);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//берём фильтр
        showwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Word";spinrenovator();
                learningwordarray.clear();
                wordlist.setAdapter(adapter);

                fi.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();

                myRef.child("Users").child(userid).child("Word").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                        Word word = dataSnapshot.getValue(Word.class);
                        if(word.getstatus()==null){
                            word.setStatus("Complieted");
                        }
                        learningwordarray.add(word);
                        adapter.notifyDataSetChanged();
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

                type = "Noun";spinrenovator();
                fi.setVisibility(View.VISIBLE);
                nounArrayList.clear();

                wordlist.setAdapter(nounLernAdapter);

                nounLernAdapter.notifyDataSetChanged();

                myRef.child("Users").child(userid).child("Noun").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                        Noun noun = dataSnapshot.getValue(Noun.class);

                        if(noun.getStatus()==null){noun.setStatus("Complieted");}

                        nounArrayList.add(noun);
                        nounLernAdapter.notifyDataSetChanged();

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

                type = "Verbs";spinrenovator();
                fi.setVisibility(View.VISIBLE);
                verbsArrayList.clear();

                wordlist.setAdapter(verbLernAdapter);

                verbLernAdapter.notifyDataSetChanged();

                myRef.child("Users").child(userid).child("Verbs").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                        Verbs verb = dataSnapshot.getValue(Verbs.class);
                        if (verb.getStatus()==null) {
                           verb.setStatus("Complieted");

                        }
                              verbsArrayList.add(verb);
                         verbLernAdapter.notifyDataSetChanged();
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

        editcats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(LibraryWords.this);
                catarray.add(0,type);
                dialog.setContentView(R.layout.dialogcatedit);
                dialog.setCancelable(false);
                ListView listView = (ListView)dialog.findViewById(R.id.catlist);
                Cateditadapter catadapt = new Cateditadapter(LibraryWords.this,R.layout.editcats,catarray);
                listView.setAdapter(catadapt);
                ImageButton ext = (ImageButton)dialog.findViewById((R.id.close));
                ext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        catarray.add(0,"Все");
                        catarray.add(1, "Выученные");
                        catarray.add(2, "Невыученные");

                        spinrenovator();
                        dialog.dismiss();


                    }
                });







                dialog.show();

            }
        });




    }

    private  void  filteer (String filter) {


        switch (type) {
            case "Verbs":
                filterverbsArrayList.clear();
                for (int n = 0; n < verbsArrayList.size(); n++) {
                    Verbs fverb = verbsArrayList.get(n);
                    if (fverb.getCategory().equals(filter)) {
                        filterverbsArrayList.add(fverb);
                    }
                }
                wordlist.setAdapter(filterverbLernAdapter);
                filterverbLernAdapter.notifyDataSetChanged();
                break;
            case "Word":
                filterlearningwordarray.clear();
                for (int n = 0; n < learningwordarray.size(); n++) {
                    Word fword = learningwordarray.get(n);
                    if (fword.getCatogory().equals(filter)){
                        filterlearningwordarray.add(fword);
                    }
                }
                wordlist.setAdapter(filtrword);
                filtrword.notifyDataSetChanged();
                break;
            case "Noun":
                filternounArrayList.clear();
                for (int n = 0; n < nounArrayList.size(); n++) {
                    Noun fnoun = nounArrayList.get(n);
                    if (fnoun.getCategory().equals(filter)){
                        filternounArrayList.add(fnoun);
                    }
                }
                wordlist.setAdapter(filtrnounibAdapter);
                filtrnounibAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void spinrenovator(){

        catarray.clear();

        catarray.add("Все");
        catarray.add("Выученные");
        catarray.add("Невыученные");

        myRef.child("Users").child(userid).child("Categories").child(type).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                String c = dataSnapshot.getValue(String.class);
                if (c!=null){
                    catarray.add(c);
                    catspinAdapter.notifyDataSetChanged();
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

    public void gotoaddword(View view){
        Intent intent = new Intent(LibraryWords.this, AddWord.class); startActivity(intent);
    }

    public  void  search(View view){
        searchword = search.getText().toString();
        if( searchword.equals("")){
        Toast.makeText(this,"Введите слово",Toast.LENGTH_SHORT).show();}
        switch (type) {
            case "Verbs":
                filterverbsArrayList.clear();
                for (int n = 0; n < verbsArrayList.size(); n++) {
                    Verbs fverb = verbsArrayList.get(n);
                    if (fverb.getRuverb().contains(searchword)||fverb.getNomenativ().contains(searchword)) {
                        filterverbsArrayList.add(fverb);
                    }
                }
                wordlist.setAdapter(filterverbLernAdapter);
                filterverbLernAdapter.notifyDataSetChanged();
                break;
            case "Word":
                filterlearningwordarray.clear();
                for (int n = 0; n < learningwordarray.size(); n++) {
                    Word fword = learningwordarray.get(n);
                    if (fword.getruschword().contains(searchword)||fword.getdeutschword().contains(searchword)){
                        filterlearningwordarray.add(fword);
                    }
                }
                wordlist.setAdapter(filtrword);
                filtrword.notifyDataSetChanged();
                break;
            case "Noun":
                filternounArrayList.clear();
                for (int n = 0; n < nounArrayList.size(); n++) {
                    Noun fnoun = nounArrayList.get(n);
                    if (fnoun.getRusnoun().contains(searchword)||fnoun.getDenoun().contains(searchword)){
                        filternounArrayList.add(fnoun);
                    }
                }
                wordlist.setAdapter(filtrnounibAdapter);
                filtrnounibAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
            search.setText("");
        }

        public void gostats(View view){

        Intent intent = new Intent(LibraryWords.this, PersonalStats.class);startActivity(intent);
}

    public void showhelp(View view){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.faqveiw);
        dialog.getWindow().setTitle("Подсказка");
        dialog.setCancelable(false);
        TextView textView = (TextView)dialog.findViewById(R.id.faqtext);
        textView.setText(R.string.helplib);
        dialog.show();

        Button closehelp= (Button)dialog.findViewById(R.id.closefaq);
        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void filterstat(String st, String t){
        switch (type) {
            case "Verbs":
                filterverbsArrayList.clear();
                for (int n = 0; n < verbsArrayList.size(); n++) {
                    Verbs fverb = verbsArrayList.get(n);
                    if (fverb.getStatus().equals(st)) {
                        filterverbsArrayList.add(fverb);
                    }
                }
                wordlist.setAdapter(filterverbLernAdapter);
                filterverbLernAdapter.notifyDataSetChanged();
                break;
            case "Word":
                filterlearningwordarray.clear();
                for (int n = 0; n < learningwordarray.size(); n++) {
                    Word fword = learningwordarray.get(n);
                    if (fword.getstatus().equals(st)){
                        filterlearningwordarray.add(fword);
                    }
                }
                wordlist.setAdapter(filtrword);
                filtrword.notifyDataSetChanged();
                break;
            case "Noun":
                filternounArrayList.clear();
                for (int n = 0; n < nounArrayList.size(); n++) {
                    Noun fnoun = nounArrayList.get(n);
                    if (fnoun.getStatus().equals(st)){
                        filternounArrayList.add(fnoun);
                    }
                }
                wordlist.setAdapter(filtrnounibAdapter);
                filtrnounibAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

    }


    @Override
    public void notifyDataSetChanged() {
        nounLernAdapter.notifyDataSetChanged();
    }

    @Override
    public String get_type() {
        return type;
    }
}

