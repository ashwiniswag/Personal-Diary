package com.example.notes2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class Editor extends AppCompatActivity {

    Bundle a;
    EditText title,editor;
    FloatingActionButton save;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        a=getIntent().getExtras();
        title=findViewById(R.id.Title);
        editor=findViewById(R.id.edtor);
        title.setText(a.getString("Title"));
        save=findViewById(R.id.save);
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentuser=mAuth.getCurrentUser();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid=currentuser.getUid();
                ref=database.getReference("User").child(userid).child(a.getString("Title"));//.child("note");
                String Title=title.getText().toString();
                String note=editor.getText().toString();
                Content content=new Content(Title,note);
                ref.setValue(content);
                startActivity(new Intent(Editor.this,MainActivity.class));
//                JSONObject obj=new JSONObject();
//                try{
//                    obj.put("Title",title.getText().toString());
//                    obj.put("note",editor.getText().toString());
//                    ref.setValue(obj);
//                    startActivity(new Intent(Editor.this,MainActivity.class));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        });
    }
}
