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
        file=findViewById(R.id.addfile);
        folder=findViewById(R.id.addfolder);
        gridView=findViewById(R.id.grid);

        names=new ArrayList<>();
        image=new ArrayList<>();
        arrayAdapter=new GridAdapter(this,names,image);
        gridView.setAdapter(arrayAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.addfile).setVisibility(View.VISIBLE);
                findViewById(R.id.addfolder).setVisibility(View.VISIBLE);

            }
        });

        file.setOnClickListener(new View.OnClickListener() {
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
                        startActivity(intent);
                        String result = input.getText().toString();
                        names.add(result);
                        image.add(R.drawable.file);
//                        arrayList.add(resullt);

                        arrayAdapter.notifyDataSetChanged();

                    }
                });
                alertdialog.show();

                file.setVisibility(View.INVISIBLE);
                folder.setVisibility(View.INVISIBLE);
            }
        });

        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialog=new AlertDialog.Builder(MainActivity.this);
                alertdialog.setTitle("Create New Folder");
                alertdialog.setMessage("Enter Folder Name");



                final EditText input=new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertdialog.setView(input);
                alertdialog.setIcon(R.drawable.ic_create_new_folder);
                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertdialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result=input.getText().toString();
                        names.add(result);
                        image.add(R.drawable.folder);
//                        arrayList.add(resullt);

                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                alertdialog.show();
                file.setVisibility(View.INVISIBLE);
                folder.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent=new Intent(MainActivity.this,LoginOptions.class);
            startActivity(intent);
        }
        else{
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

            case R.id.btn:
                b++;
                if(b==5){
                    //Intent intent=new Intent(MainActivity.this,Folder.class);
                    b=0;
//                    FingerprintManager fingerprintManager=(FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                    FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(MainActivity.this);
//                    if(!fingerprintManager.isHardwareDetected()){
//                        Toast.makeText(MainActivity.this,"Fingerprint Scanner Is not Present",Toast.LENGTH_SHORT).show();
//                    }
                    if (!fingerprintManagerCompat.isHardwareDetected()) {
                        // Device doesn't support fingerprint authentication
                        Toast.makeText(MainActivity.this,"Fingerprint Scanner Is not Present",Toast.LENGTH_SHORT).show();
                    }
                    else{
                    startActivity(new Intent(MainActivity.this,FingerprintAuth.class));
                   // Toast.makeText(MainActivity.this,"Welcome To hidden Folder",Toast.LENGTH_SHORT).show();}
                }}
                return true;
            case R.id.darkmode:
                constraintLayout=findViewById(R.id.main);
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));
                return true;
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

//    public void dispfiles(){
//        names=new ArrayList<>();
//        image=new ArrayList<>();
//        arrayAdapter=new GridAdapter(this,names,image);
//        gridView.setAdapter(arrayAdapter);
//        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
//        final String userid=currentuser.getUid();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User").child(userid);//.child("note");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DataSnapshot file=dataSnapshot.child(userid);
//                Iterable<DataSnapshot> files=file.getChildren();
//                for (DataSnapshot contact : files){
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
