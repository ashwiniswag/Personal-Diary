package com.example.notes2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.StringTokenizer;

public class Editor extends AppCompatActivity {

    Bundle a;
    EditText title,editor;
    FloatingActionButton save;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref;
    ConstraintLayout constraintLayout;
    String nsize;
    String bcolor;
    String tcolor,otext,replace;
    TextToSpeech textToSpeech;
//    String b,i,u;
    int b,u,i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        i=0;
        b=0;
        u=0;
        nsize="20";
        bcolor=String.valueOf(Color.WHITE);
        tcolor=String.valueOf(Color.BLACK);
        constraintLayout=findViewById(R.id.layout);
        a=getIntent().getExtras();
        title=findViewById(R.id.Title);
//        title.setTextSize(30);
        editor=findViewById(R.id.edtor);
        title.setText(a.getString("Title"));
        if(a.getString("bold")!=null){
            b=Integer.parseInt(a.getString("bold"));
            i=Integer.parseInt(a.getString("itallic"));
            u=Integer.parseInt(a.getString("uline"));
            System.out.println("Mein kaam kar ragha hu "+ b + " " + i +" "+u);
        }
        if(a.getString("nsize")!=null)
        nsize=a.getString("nsize");
        if(nsize!=null)
        editor.setTextSize(Integer.parseInt(nsize));
        if(a.getString("bcolor")!=null && a.getString("tcolor")!=null){
            bcolor=a.getString("bcolor");
            tcolor=a.getString("tcolor");
        }
        if(a.getString("note")!=null){
            editor.setText(a.getString("note"));
        }
        if(bcolor!=null && tcolor!=null){
            constraintLayout.setBackgroundColor(Integer.parseInt(bcolor));
            editor.setBackgroundColor(Integer.parseInt(bcolor));
            title.setBackgroundColor(Integer.parseInt(bcolor));
            editor.setTextColor(Integer.parseInt(tcolor));
            title.setTextColor(Integer.parseInt(tcolor));
        }

        style(b,i,u);

        save=findViewById(R.id.save);
        final String fname=a.getString("fname");
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentuser=mAuth.getCurrentUser();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid=currentuser.getUid();
                ref=database.getReference("User").child(userid).child(a.getString("fname"));//.child("note");
                String Title=title.getText().toString();
                String note=editor.getText().toString();
                Content content=new Content(Title,note,fname,nsize,bcolor,tcolor,String.valueOf(b),String.valueOf(i),String.valueOf(u));
                ref.setValue(content);
                startActivity(new Intent(Editor.this,MainActivity.class));
            }
        });

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        Log.e("TTS", "The Language is not supported!");
                    } else {
//                        Log.i("TTS", "Language Supported.");
//                                Toast.makeText(Editor.this,"LAnguage Support",Toast.LENGTH_SHORT).show();
                    }
//                    Log.i("TTS", "Initialization success.");
//                            Toast.makeText(Editor.this,"Initialization success.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.editor_option,menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        constraintLayout=findViewById(R.id.layout);
        EditText note=findViewById(R.id.edtor);
        EditText tnote=findViewById(R.id.Title);
        otext=editor.getText().toString();
        switch(item.getItemId()){
            case R.id.yellow:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                note.setTextColor(getResources().getColor(R.color.colorPrimary));
                note.setBackgroundColor(getResources().getColor(R.color.yellow));
                tnote.setTextColor(getResources().getColor(R.color.colorPrimary));
                tnote.setBackgroundColor(getResources().getColor(R.color.yellow));
                bcolor=String.valueOf(getResources().getColor(R.color.yellow));
                tcolor=String.valueOf(getResources().getColor(R.color.colorPrimary));
                return true;
            case R.id.blue:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                note.setTextColor(getResources().getColor(R.color.darkb));
                note.setBackgroundColor(getResources().getColor(R.color.blue));
                tnote.setTextColor(getResources().getColor(R.color.darkb));
                tnote.setBackgroundColor(getResources().getColor(R.color.blue));
                bcolor=String.valueOf(getResources().getColor(R.color.blue));
                tcolor=String.valueOf(getResources().getColor(R.color.darkb));
                return true;
            case R.id.green:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.lgreen));
                note.setTextColor(getResources().getColor(R.color.dblue));
                note.setBackgroundColor(getResources().getColor(R.color.lgreen));
                tnote.setTextColor(getResources().getColor(R.color.dblue));
                tnote.setBackgroundColor(getResources().getColor(R.color.lgreen));
                bcolor=String.valueOf(getResources().getColor(R.color.lgreen));
                tcolor=String.valueOf(getResources().getColor(R.color.dblue));
                return true;
            case R.id.red:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.red));
                note.setTextColor(getResources().getColor(R.color.blue));
                note.setBackgroundColor(getResources().getColor(R.color.red));
                tnote.setTextColor(getResources().getColor(R.color.blue));
                tnote.setBackgroundColor(getResources().getColor(R.color.red));
                bcolor=String.valueOf(getResources().getColor(R.color.red));
                tcolor=String.valueOf(getResources().getColor(R.color.blue));
                return true;
            case R.id.extrasmall:
                note.setTextSize(10);
                nsize="10";
                return true;
            case R.id.small:
                note.setTextSize(15);
                nsize="15";
                return true;
            case R.id.medium:
                note.setTextSize(25);
                nsize="25";
                return true;
            case R.id.large:
                note.setTextSize(30);
                nsize="30";
                return true;
            case R.id.extralarge:
                note.setTextSize(45);
                nsize="45";
                return true;
            case R.id.defaul:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
                note.setBackgroundColor(getResources().getColor(R.color.white));
                note.setTextColor(getResources().getColor(R.color.black));
                tnote.setBackgroundColor(getResources().getColor(R.color.white));
                tnote.setTextColor(getResources().getColor(R.color.black));
                bcolor=String.valueOf(getResources().getColor(R.color.white));
                tcolor=String.valueOf(getResources().getColor(R.color.black));
                return true;
            case R.id.bold:
                b=1;
                style(b,i,u);
//                replace="<b>"+ otext+"</b>";
//                editor.setText(Html.fromHtml(replace));
                return true;
            case R.id.itallic:
                i=1;
                style(b,i,u);
//                replace="<i>"+ otext+"</i>";
//                editor.setText(Html.fromHtml(replace));
                return true;
            case R.id.Underline:
                u=1;
                style(b,i,u);
//                replace="<u>"+ otext+"</u>";
//                editor.setText(Html.fromHtml(replace));
                return true;
            case R.id.normal:
                b=0;
                i=0;
                u=0;
                otext=editor.getText().toString();
                editor.setText(otext);
                return true;
            case R.id.read:
                String data=editor.getText().toString();
                int speechstatus=textToSpeech.speak(data,TextToSpeech.QUEUE_FLUSH,null);
                if (speechstatus == TextToSpeech.ERROR) {
                    Toast.makeText(Editor.this,"Error",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.find:
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(Editor.this);
                alertdialog.setTitle("FInd");
                alertdialog.setMessage("Enter the word");

                final EditText input = new EditText(Editor.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertdialog.setView(input);
                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });


                alertdialog.setPositiveButton("Find", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String result = input.getText().toString();
                        String replace="<span style='background-color:green'>"+ result+ "</span>";
                        String otext=editor.getText().toString();
                        String mtext=otext.replaceAll(result,replace);
                        editor.setText(Html.fromHtml(mtext));
                    }
                });
                alertdialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void style(int bo,int it,int ul){
        System.out.println("Chal toh mein bhi raha hu "+bo+ " "+it+ " "+ul);
        String text=editor.getText().toString();

        String pre = "",post = "";
        if(bo==1){
            pre=pre + "<b>";
            post=post+ "</b>";
        }
        if(it==1){
            pre=pre + "<i>";
            post="</i>" + post;
        }
        if(ul==1){
            pre=pre +"<u>";
            post="</u>" + post;
        }
        System.out.println(text + " " + pre+" "+ post);
        String replace=pre + text + post;
        System.out.println(replace);
        editor.setText(Html.fromHtml(replace));
    }
}
