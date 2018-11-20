package com.example.tianhao.seg2105project;

import android.media.MediaPlayer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tianhao.seg2105project.Model.Application;
import com.example.tianhao.seg2105project.Model.Service;

import java.util.regex.Pattern;

public class EditService extends AppCompatActivity {
    private static final Pattern HOURLY_RATE =
            Pattern.compile(
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])"+ "(?=\\S+$)"+"(?=.*[@#$~`!*()_{}|?/>,<%^&+=])");
    private Application application = Application.getInstance(this);
    private TextInputLayout createHourlyRate;
    private TextInputLayout createServiceType;
    MediaPlayer buttonSound1,buttonSound2,buttonSound3;
    private String id;


    Button buttonDelete, buttonGOBACK, buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        buttonSound1=MediaPlayer.create(EditService.this,R.raw.button_sound);
        buttonSound2=MediaPlayer.create(EditService.this,R.raw.button_sound);
        buttonSound3=MediaPlayer.create(EditService.this,R.raw.button_sound);

//      id of current service;
        id = getIntent().getStringExtra("id");
        createServiceType = findViewById((R.id.save_serviceType));
        createHourlyRate = findViewById((R.id.save_hourlyRate));

        buttonDelete = (Button)findViewById(R.id.delete_service);
        buttonSave = (Button) findViewById(R.id.save_button);
        buttonGOBACK = (Button) findViewById(R.id.cancel);

        if(!application.getUser().getUserType().equals("Administrator")){//only available to admin
            buttonDelete.setVisibility(View.INVISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
        }

        if(id.equals("")){//delete is not shown when the user add a service instead of change the service
            buttonDelete.setVisibility(View.INVISIBLE);
        }else{
            createServiceType.getEditText().setText(getIntent().getStringExtra("name"));
            createHourlyRate.getEditText().setText(getIntent().getStringExtra("hourlyRate"));
        }

        //for deletion
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound1.start();
                application.removeService(id);
                finish();
            }
        });

        //for go back button
        buttonGOBACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound2.start();
                finish();
            }
        });


        //for save button-two functions(edit and post new serv
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSound3.start();
                //identify which function to be used
                if(id.equals("")) {
                    if(validationHourlyRate()&&validationServiceName()){
                        application.addService(createServiceType.getEditText().getText().toString(),
                                Double.parseDouble(createHourlyRate.getEditText().getText().toString()));
                        finish();
                    }else if(!validationHourlyRate()){
                        Toast.makeText(EditService.this, "invalid Hourly Rate", Toast.LENGTH_SHORT).show();
                    }else if(!validationServiceName()){
                        Toast.makeText(EditService.this, "invalid Service Name", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(validationHourlyRate()&&validationServiceName()) {
                        application.editService(id, createServiceType.getEditText().getText().toString(),
                                Double.parseDouble(createHourlyRate.getEditText().getText().toString()));
                        finish();
                    }else if(!validationHourlyRate()){
                        Toast.makeText(EditService.this, "invalid Hourly Rate", Toast.LENGTH_SHORT).show();
                    }else if(!validationServiceName()){
                        Toast.makeText(EditService.this, "invalid Service Name", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean validationServiceName(){
        String ServiceName = createServiceType.getEditText().getText().toString().trim();
        if(ServiceName.isEmpty()){
            createServiceType.setError("Please Enter Service type");
            return false;
        }else if(!validationNameNotBeNumber(ServiceName)){
            createServiceType.setError("At least one letter");
            return false;
        }else{
            createServiceType.setError(null);
            return true;
        }
    }

    public boolean validationNameNotBeNumber(String name){
        boolean result=false;
        try{
            name = name.replaceAll("\\s","");
            Double.parseDouble(name);
        }catch(NumberFormatException e){
            result=true;
        }
        return result;
    }

    public boolean validationHourlyRate() {
        String hourlyRate = createHourlyRate.getEditText().getText().toString().trim();
        if (hourlyRate.isEmpty()) {
            createHourlyRate.setError("Please Enter Hourly rate here");
            return false;
        } else if (HOURLY_RATE.matcher(hourlyRate).matches()) {
            createHourlyRate.setError("HOURLY RATE NOT VALID,SHOULD ONLY NUMBER");
            return false;
        } else {
            createHourlyRate.setError(null);
            return true;
        }
    }
}



