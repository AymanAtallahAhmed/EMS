package com.work.ammar.ems;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.realm.Realm;

public class Main extends AppCompatActivity {

    String mail = null;
    String pass = null;

    static FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if(getIntent().getExtras() != null) {
            mail = getIntent().getExtras().getString("mail");
            pass = getIntent().getExtras().getString("pass");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Realm realm = Realm.getInstance(getBaseContext());
        if(realm.allObjects(User.class).isEmpty()) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainFragment mainFragment = new MainFragment();
            Bundle args = new Bundle();
            args.putString("mail",mail);
            args.putString("pass",pass);
            mainFragment.setArguments(args);
            fragmentTransaction.replace(R.id.fragment, mainFragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragment, homeFragment, "home");
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        HomeFragment mainFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
        if(mainFragment == null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragment, homeFragment, "home");
            fragmentTransaction.commit();
        }
    }
}
