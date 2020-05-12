package com.example.notes2;

//import androidx.annotation.RequiresApi;
import androidx.annotation.NonNull;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import android.content.DialogInterface;
import android.content.Intent;
//import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    Bundle a;
    ConstraintLayout constraintLayout;
    FloatingActionButton add,file,folder;
    int b=0;
    GridView gridView;
//    FloatingActionButton add,file,folder;
    List<String> names;
    GridAdapter arrayAdapter;
    List<Integer> image;
    List<String> notes;
    List<String> title;
    List<String> nsize;
    List<String> bcolor,tcolor;
    List<String> B,u,i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a=getIntent().getExtras();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        add=findViewById(R.id.addfilefolder);
//        file=findViewById(R.id.addfile);
//        folder=findViewById(R.id.addfolder);
        gridView=findViewById(R.id.grid);

        names=new ArrayList<>();
        image=new ArrayList<>();
        notes=new ArrayList<>();
        title=new ArrayList<>();
        nsize=new ArrayList<>();
        bcolor=new ArrayList<>();
        tcolor=new ArrayList<>();
        B=new ArrayList<>();
        i=new ArrayList<>();
        u=new ArrayList<>();
        arrayAdapter=new GridAdapter(this,names,image);
        gridView.setAdapter(arrayAdapter);

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                findViewById(R.id.addfile).setVisibility(View.VISIBLE);
//                findViewById(R.id.addfolder).setVisibility(View.VISIBLE);
//
//            }
//        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
                alertdialog.setTitle("Create New File");
                alertdialog.setMessage("Enter File Name");


                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertdialog.setView(input);
                alertdialog.setIcon(R.drawable.ic_notefile);
                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertdialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(MainActivity.this,Editor.class);
                        intent.putExtra("Title",input.getText().toString());
                        intent.putExtra("fname",input.getText().toString());
                        startActivity(intent);
                        String result = input.getText().toString();
                        names.add(result);
                        image.add(R.drawable.file);
//                        arrayList.add(resullt);

                        arrayAdapter.notifyDataSetChanged();

                    }
                });
                alertdialog.show();

//                file.setVisibility(View.INVISIBLE);
//                folder.setVisibility(View.INVISIBLE);
            }
        });

//        folder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertdialog=new AlertDialog.Builder(MainActivity.this);
//                alertdialog.setTitle("Create New Folder");
//                alertdialog.setMessage("Enter Folder Name");
//
//
//
//                final EditText input=new EditText(MainActivity.this);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT);
//                input.setLayoutParams(lp);
//                alertdialog.setView(input);
//                alertdialog.setIcon(R.drawable.ic_create_new_folder);
//                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                alertdialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String result=input.getText().toString();
//                        names.add(result);
//                        image.add(R.drawable.folder);
////                        arrayList.add(resullt);
//
//                        arrayAdapter.notifyDataSetChanged();
//                    }
//                });
//                alertdialog.show();
//                file.setVisibility(View.INVISIBLE);
//                folder.setVisibility(View.INVISIBLE);
//            }
//        });
        boolean verify=check();
        if(verify){
        disp();}

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
                alertdialog.setTitle("Do you want to delete this file?");

                alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    DatabaseReference ref;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ref =FirebaseDatabase.getInstance().getReference().child("User").child(userid).child(names.get(position));
                        ref.setValue(null);
                        names.remove(position);
                        notes.remove(position);
                        nsize.remove(position);
                        bcolor.remove(position);
                        tcolor.remove(position);
                        B.remove(position);
                        i.remove(position);
                        u.remove(position);
                        title.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                alertdialog.show();
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference ref;
                Intent intent=new Intent(MainActivity.this,Editor.class);
//                String tlt=title
                intent.putExtra("Title",title.get(position));
                intent.putExtra("note",notes.get(position));
                intent.putExtra("fname",names.get(position));
                intent.putExtra("nsize",nsize.get(position));
                intent.putExtra("bcolor",bcolor.get(position));
                intent.putExtra("tcolor",tcolor.get(position));
                intent.putExtra("bold",B.get(position));
                intent.putExtra("itallic",i.get(position));
                intent.putExtra("uline",u.get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this,"Successfully Signed Out",Toast.LENGTH_LONG).show();
                        Intent lip=new Intent(MainActivity.this,LoginOptions.class);
                        startActivity(lip);
                        finish();
                    }
                });
    }

//    @Override
    protected boolean check() {
//        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent=new Intent(MainActivity.this,LoginOptions.class);
            startActivity(intent);
            return  false;
        }
        else{
//            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            Toast.makeText(this, "Welcome :-)", Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

//            case R.id.btn:
//                b++;
//                if(b==5){
//                    //Intent intent=new Intent(MainActivity.this,Folder.class);
//                    b=0;
////                    FingerprintManager fingerprintManager=(FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
//                    FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(MainActivity.this);
////                    if(!fingerprintManager.isHardwareDetected()){
////                        Toast.makeText(MainActivity.this,"Fingerprint Scanner Is not Present",Toast.LENGTH_SHORT).show();
////                    }
//                    if (!fingerprintManagerCompat.isHardwareDetected()) {
//                        // Device doesn't support fingerprint authentication
//                        Toast.makeText(MainActivity.this,"Fingerprint Scanner Is not Present",Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                    startActivity(new Intent(MainActivity.this,FingerprintAuth.class));
//                   // Toast.makeText(MainActivity.this,"Welcome To hidden Folder",Toast.LENGTH_SHORT).show();}
//                }}
//                return true;
//            case R.id.darkmode:
//                constraintLayout=findViewById(R.id.main);
//                constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));
//                return true;
            case R.id.signout:
                switch (item.getItemId()) {
                    // ...
                    case R.id.signout:
                        signOut();
                        break;
                }

                FirebaseAuth.getInstance().signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void disp(){
        DatabaseReference ref;
//        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
        String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref=FirebaseDatabase.getInstance().getReference().child("User").child(userid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items=dataSnapshot.getChildren().iterator();
                while(items.hasNext()){
                    DataSnapshot item=items.next();
                    String fname=item.child("fname").getValue().toString();
                    String note=item.child("note").getValue().toString();
                    String Title=item.child("Title").getValue().toString();
                    String Nsize=item.child("nsize").getValue().toString();
                    String Bcolor=item.child("bcolor").getValue().toString();
                    String Tcolor=item.child("tcolor").getValue().toString();
                    String bold=item.child("b").getValue().toString();
                    String itallic=item.child("i").getValue().toString();
                    String uline=item.child("u").getValue().toString();
                    if(fname != null){
                        names.add(fname);
                        notes.add(note);
                        title.add(Title);
                        nsize.add(Nsize);
                        bcolor.add(Bcolor);
                        tcolor.add(Tcolor);
                        B.add(bold);
                        i.add(itallic);
                        u.add(uline);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Not Changed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
