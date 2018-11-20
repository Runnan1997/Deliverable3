package com.example.tianhao.seg2105project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.design.widget.TextInputLayout;
import android.media.MediaPlayer;


import com.example.tianhao.seg2105project.Model.Application;
import com.example.tianhao.seg2105project.Model.ServiceProvider;
import com.example.tianhao.seg2105project.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class SignIn extends AppCompatActivity {

    //sound
    MediaPlayer buttonSound1, buttonSound2;

    FirebaseDatabase database;
    DatabaseReference users;

    private Application application;

    private TextInputLayout editUsername;
    private TextInputLayout editPassword;

    Button buttonSubmit, buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //sound
        buttonSound1=MediaPlayer.create(SignIn.this,R.raw.button_sound);
        buttonSound2=MediaPlayer.create(SignIn.this,R.raw.button_sound);

        application = Application.getInstance(this);


        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");

        editUsername = findViewById((R.id.editUsername));
        editPassword = findViewById((R.id.editPassword));


        buttonSubmit = (Button)findViewById(R.id.buttonSubmit);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound1.start();
                Intent intentSignUp = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intentSignUp);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound2.start();
                signIn(editUsername.getEditText().getText().toString(),editPassword.getEditText().getText().toString());
            }
        });

    }

    private void signIn(final String username, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(username).exists() && !username.isEmpty()){
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if(login.getPassword().equals(password)){
                            Toast.makeText(SignIn.this, "Success Login", Toast.LENGTH_SHORT).show();
                            switch (login.getUserType()){
                                case "Service Provider":
                                    application.setUser(new ServiceProvider(getApplicationContext(),login));
                                    break;
                                default:
                                    application.setUser(login);
                                    break;
                            }
                            Intent intentWelcome = new Intent(getApplicationContext(), WelcomePage.class);
                            startActivity(intentWelcome);
                            finish();
                        }else{
                            Toast.makeText(SignIn.this, "Password is Wrong", Toast.LENGTH_SHORT).show();
                        }
                }else{
                    Toast.makeText(SignIn.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //write code here
            }
        });
    }


}
