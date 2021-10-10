package com.example.myapplication.Bandak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.content.CursorLoader;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;


import com.example.myapplication.AboutDev;
import com.example.myapplication.R;
import com.example.myapplication.showallparty;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BandakMain extends AppCompatActivity {
    TabLayout bndkmntablayout;
    MaterialToolbar bndkmntoolbar;
    ViewPager2 bndkmnviewpager2;
    
    BottomNavigationView bndkmnbottomnavigation;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<String> partylist = new ArrayList<>();
    ArrayList<String> grouplist = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    BandakAppDatabaseAdapter DatabaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseAdapter = new BandakAppDatabaseAdapter(this);
        setTheme(R.style.Theme_MyApplication);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandak_main);
        setviewstoid();
        managedrawerlayout();
        bndkmnviewpager2.setAdapter(new BandakMainAdapter(this, this));
        managetablayoutmediator();
        bndkmnbottomnavigation = findViewById(R.id.bndkmnbottomnavigation);
        bndkmnbottomnavigation.inflateMenu(R.menu.bndknbottomnavigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.allparty: {
                        Intent i = new Intent(getApplicationContext(), showallparty.class);
                        startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                    case R.id.addnp: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i = new Intent(getApplicationContext(), bndkmnaddparty.class);
                        startActivity(i);

                        return true;
                    }
                    case R.id.addpi: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i = new Intent(getApplicationContext(), Payment_IN.class);
                        startActivity(i);
                        return true;
                    }
                    case R.id.addpo: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i = new Intent(getApplicationContext(), Payment_out.class);
                        startActivity(i);
                        return true;
                    }
                    case R.id.abt_dev: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i = new Intent(getApplicationContext(), AboutDev.class);
                        startActivity(i);
                        return true;
                    }
                    default: {
                        return true;
                    }
                }

            }
        });
        bndkmnbottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btmnvAP) {

                    Intent i = new Intent(getApplicationContext(), addpartyEXTRA.class);
                    startActivity(i);
//                   showBottomSheetDialog();

                } else if (item.getItemId() == R.id.btmnvPI) {

                    Intent i = new Intent(getApplicationContext(), Payment_IN.class);
                    startActivity(i);
                } else {

                    Intent i = new Intent(getApplicationContext(), Payment_out.class);
                    startActivity(i);
                }
                return true;
            }
        });
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.payment_bottom_sheet);
        bottomSheetDialog.show();
    }
public void setviewstoid()
{
    bndkmntablayout = findViewById(R.id.bndkmntablayout);
    bndkmntoolbar = findViewById(R.id.bdkmntoolbar);
    bndkmnbottomnavigation = findViewById(R.id.bndkmnbottomnavigation);
    drawerLayout = findViewById(R.id.drawerlayout);
    navigationView = findViewById(R.id.navmenu);
    bndkmnviewpager2 = findViewById(R.id.bndkmnviewpager);
}
public void managedrawerlayout()
{
    setSupportActionBar(bndkmntoolbar);


    //    navigationView.setNavigationItemSelectedListener();
    toggle = new ActionBarDrawerToggle(this, drawerLayout, bndkmntoolbar, R.string.OpenNavMenu, R.string.CLoseNavMenu);
    toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
    drawerLayout.addDrawerListener(toggle);

    toggle.syncState();
}
public void managetablayoutmediator()
{
     TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(bndkmntablayout, bndkmnviewpager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {

                        tab.setText("RECEIVED");

                        // TextView tabtextview=tab.getCustomView().findViewById(R.id.tablayouttext);
                        //tabtextview.setText("amount");
                        tab.setIcon(R.drawable.login_new);

                        break;
                    }
                    case 1: {
                        tab.setText("PAID");
                        tab.setIcon(R.drawable.ic_logout);
                        break;
                    }


                }
            }
        });
        tabLayoutMediator.attach();

}
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}