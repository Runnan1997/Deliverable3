package com.example.tianhao.seg2105project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tianhao.seg2105project.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class SignUp extends AppCompatActivity {
    public static final Pattern EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+"\\@"+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+"("+
                    "\\."+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+"
    );

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private TextInputLayout editEmail;
    private TextInputLayout editUsername;
    private TextInputLayout editPassword;
    private TextInputLayout editpasswordConfirm;
    MediaPlayer buttonSound;


    FirebaseDatabase database;
    DatabaseReference users;

    Spinner dropdownmenu;

    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //firebase path
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        //assign id
        buttonSound=MediaPlayer.create(SignUp.this,R.raw.button_sound);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editpasswordConfirm = findViewById(R.id.editPasswordConfirm);
        editEmail = findViewById(R.id.editEmail);
        buttonSubmit = (Button)findViewById(R.id.buttonSubmit);
        dropdownmenu = findViewById(R.id.spinner);


        List<String> list = new ArrayList<>();
        list.add("Home Owner");
        list.add("Administrator");
        list.add("Service Provider");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownmenu.setAdapter(adapter);

        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View view){
                    buttonSound.start();
                    if(validateUser()&&validateEmail()&&validatePassword()&& validatePasswordConfirm()){

                        final User user = new User(
                                editUsername.getEditText().getText().toString(),
                                editEmail.getEditText().getText().toString(),
                                editPassword.getEditText().getText().toString(),
                                dropdownmenu.getSelectedItem().toString());


                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean flag=true;//flag set false when registration is not valid
                                if(user.getUserType().equals("Administrator")){//ensure only one admin
                                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                                        String userType = data.child("userType").getValue().toString();
                                        if(userType.equals("Administrator")){
                                            flag=false;
                                            Toast.makeText(SignUp.this, "Admin Already Exists", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }
                                for(DataSnapshot data: dataSnapshot.getChildren()) {//ensure no duplicate email
                                    String userType = data.child("email").getValue().toString();
                                    if(userType.equals(editEmail.getEditText().getText().toString())){
                                        flag=false;
                                        Toast.makeText(SignUp.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                if (dataSnapshot.child(user.getUsername()).exists()) {//no duplicate username
                                    Toast.makeText(SignUp.this, "This Username Exists", Toast.LENGTH_SHORT).show();
                                    flag=false;
                                }
                                if(flag){
                                    users.child(user.getUsername()).setValue(user);
                                    Toast.makeText(SignUp.this, "Success Register", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
        });//onclick listener end
    }//oncreate end


    public boolean validateEmail(){
        String emailInput = editEmail.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            editEmail.setError("Please Enter Email here");
            return false;
        }else if(!EMAIL_ADDRESS.matcher(emailInput).matches()){
            editEmail.setError("Invalid email address");
            return false;
        }else{
            editEmail.setError(null);
            return true;
        }
    }

    public boolean validateUser(){
        String usernameInput = editUsername.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            editUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 10) {
            editUsername.setError("Username too long");
            return false;
        }else {
            editUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = editPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            editPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            editPassword.setError("Password too weak");
            return false;
        } else {
            editPassword.setError(null);
            return true;
        }
    }

    private boolean validatePasswordConfirm(){
        String passwordInput = editPassword.getEditText().getText().toString().trim();
        String passwordConfirmInput = editpasswordConfirm.getEditText().getText().toString().trim();

        if(!passwordInput.equals(passwordConfirmInput)){
            editpasswordConfirm.setError("Passwords are different");
            return false;
        }else{
            editpasswordConfirm.setError(null);
            return true;
        }
    }


}
