package com.example.tianhao.seg2105project;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tianhao.seg2105project.Model.Application;
import com.example.tianhao.seg2105project.Model.Profile;
import com.example.tianhao.seg2105project.Model.ServiceProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceProviderProfile extends AppCompatActivity {

    private ServiceProvider user;
    private Application application;

    TextInputLayout editAddress;
    TextInputLayout editPhone;
    TextInputLayout editCompany;
    TextInputLayout editDescription;
    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton radioButtonYes;
    RadioButton radioButtonNo;
    Button ButtonSubmit;
    MediaPlayer buttonSound;

    //firebase init
    FirebaseDatabase database;
    DatabaseReference profile_firebase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);

        application = Application.getInstance(this);
        user = (ServiceProvider) application.getUser();

        editAddress = findViewById((R.id.editAddress));
        editPhone = findViewById((R.id.editPhone));
        editCompany = findViewById((R.id.editCompany));
        editDescription = findViewById((R.id.editDescription));
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonYes = findViewById(R.id.radioButtonYes);
        radioButtonNo = findViewById(R.id.radioButtonNo);
        ButtonSubmit = (Button)findViewById(R.id.buttonSubmit2);
        buttonSound=MediaPlayer.create(ServiceProviderProfile.this,R.raw.button_sound);

        //about firebase
        database = FirebaseDatabase.getInstance();
        profile_firebase = database.getReference("Profile");

        if(user.getProfile()!=null){
            editAddress.getEditText().setText(user.getProfile().getAddress());
            editPhone.getEditText().setText(user.getProfile().getPhone());
            editCompany.getEditText().setText(user.getProfile().getCompanyName());
            editDescription.getEditText().setText(user.getProfile().getDescription());
            radioButtonYes.setChecked(user.getProfile().isLicensed());
            radioButtonNo.setChecked(!user.getProfile().isLicensed());
        }


        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound.start();
                if(validateAddress()&&validatePhone()&&validateCompany()) {
                    int radioButtonId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(radioButtonId);

                    final Profile profile = new Profile(
                            user.getUsername(),
                            editAddress.getEditText().getText().toString(),
                            editPhone.getEditText().getText().toString(),
                            editCompany.getEditText().getText().toString(),
                            editDescription.getEditText().getText().toString(),
                            Boolean.parseBoolean(radioButton.getText().toString()));

                    user.setProfile(profile);

                    profile_firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean flag=true;//flag set false when profile is not valid
                            profile_firebase.child(user.getUsername()).setValue(profile);
                            Toast.makeText(ServiceProviderProfile.this, "Success set profile", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }//if end
            }
        });

    }

    public void checkButton(View v){
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioButtonId);
        Toast.makeText(this,"Selected Radio Button: "+radioButton.getText(),Toast.LENGTH_SHORT).show();
    }

    public boolean validateAddress(){
        String addressInput = editAddress.getEditText().getText().toString().trim();
        if (addressInput.isEmpty()) {
            editAddress.setError("Please enter an address");
            return false;
        }else {
            editAddress.setError(null);
            return true;
        }
    }

    public boolean validatePhone(){
        String phoneInput = editPhone.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()) {
            editPhone.setError("Please enter a phone number");
            return false;
        } else if (phoneInput.length() != 10) {
            editPhone.setError("Please enter a correct phone number");
            return false;
        }else {
            editPhone.setError(null);
            return true;
        }
    }

    public boolean validateCompany(){
        String companyInput = editCompany.getEditText().getText().toString().trim();
        if (companyInput.isEmpty()) {
            editPhone.setError("Please enter a company");
            return false;
        }else {
            editCompany.setError(null);
            return true;
        }
    }
}
