package com.example.tianhao.seg2105project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.example.tianhao.seg2105project.Model.Application;
import com.example.tianhao.seg2105project.Model.User;

public class WelcomePage extends AppCompatActivity {

    User user;
    NavigationView navigationView;

    TextView welcome;

    Button buttonSignOut;
    Button buttonProfile;
    MediaPlayer buttonSound;


    private Application application;

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    //for navigation bar
    private NavigationView mMainNav;
    private FrameLayout mMainFrame;
    private homeFragment homeFragment;//home page fragment linking to fragment_home.xml
    private usersFragment usersFragment;//users fragment linking to fragment_num_account.xml
    private profileFragment profileFragment;//profile fragment linking to fragment_profile.xml


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        buttonSound=MediaPlayer.create(WelcomePage.this,R.raw.button_sound);
        application = Application.getInstance(this);
        user = application.getUser();
        hideProfileItem();

        //title
        welcome=(TextView)findViewById(R.id.textWelcome);
        welcome.setText("Hello, "+user.getUsername()+
        "！ You are logged as a "+ user.getUserType());

        mDrawerlayout=(DrawerLayout)findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //for navigation draw
        mMainFrame=(FrameLayout) findViewById(R.id.main_fragment);
        mMainNav=(NavigationView) findViewById(R.id.nav_draw);
        homeFragment=new homeFragment();
        usersFragment=new usersFragment();
        profileFragment =new profileFragment();
        setFragment(homeFragment);

        mMainNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        welcome.setText("Hello, "+user.getUsername()+
                                "！ You are logged as a "+ user.getUserType());
                        return true;
                    case R.id.nav_users:
                        welcome.setText("This is the list of all users");
                        setFragment(usersFragment);
                        return true;
                    case R.id.nav_timeslot:
                        welcome.setText("This is the list of your available time");
                        setFragment(usersFragment);//here should be as know as timeslotFragment, but ok to use this name
                        return true;
                    case R.id.nav_profile:
                        setFragment(profileFragment);
                        welcome.setText("This is the list of services you provide");
                        return true;
                        default:
                            return false;
                }
            }
        });

        //profile for service provider only
        buttonProfile = (Button)findViewById(R.id.buttonProviderProfile);
        if(!user.getUserType().equals("Service Provider")){
            buttonProfile.setVisibility(View.INVISIBLE);
        }
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound.start();
                openProfileActivity();
            }
        });


        //signOut
        buttonSignOut=(Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound.start();
                Intent intentSignUp = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intentSignUp);
                finish();
            }
        });
    }

    public void openProfileActivity(){
        Intent intentProfile = new Intent(WelcomePage.this, ServiceProviderProfile.class);
        startActivity(intentProfile);
    }

    //hide profile in navigation draw
    private void hideProfileItem(){
        navigationView =findViewById(R.id.nav_draw);
        Menu nav_Menu = navigationView.getMenu();
        if(!application.getUser().getUserType().equals("Service Provider")){
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
        }
        if(!application.getUser().getUserType().equals("Administrator")){
            nav_Menu.findItem(R.id.nav_users).setVisible(false);
        }
        if(!application.getUser().getUserType().equals("Service Provider")){
            nav_Menu.findItem(R.id.nav_timeslot).setVisible(false);
        }
    }

    //menuItem for draw in admin
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //setNavigation Fragment
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }


}
