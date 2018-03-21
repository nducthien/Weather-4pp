package com.tnd.weather4pp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.tnd.weather4pp.adapter.ViewPaperAdapter;
import com.tnd.weather4pp.fragment.FragmentForecast;
import com.tnd.weather4pp.fragment.FragmentMain;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ViewPager viewPager;
    InkPageIndicator indicator;
    RelativeLayout background;
    String TabFragmentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.my_viewpaper);
        indicator = (InkPageIndicator) findViewById(R.id.indicator);
        background = (RelativeLayout) findViewById(R.id.background);

        ViewPaperAdapter adapter = new ViewPaperAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMain());
        adapter.addFragment(new FragmentForecast());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        background.setBackgroundResource(R.drawable.bg_01);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setTabFragmentB(String t) {
        TabFragmentB = t;
    }

    public String getTabFragmentB() {
        return TabFragmentB;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.pop_one:
                background.setBackgroundResource(R.drawable.bg_01);
                break;
            case R.id.pop_two:
                background.setBackgroundResource(R.drawable.bg_02);
                break;
            case R.id.pop_three:
                background.setBackgroundResource(R.drawable.bg_03);
                break;
            case R.id.pop_four:
                background.setBackgroundResource(R.drawable.bg_04);
                break;
            case R.id.pop_five:
                background.setBackgroundResource(R.drawable.bg_05);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent myWebLink = new Intent();
            myWebLink.setData(Uri.parse("https://github.com/nducthien/ProjectFinal"));
            startActivity(myWebLink);

        } else if (id == R.id.nav_manage) {
            Intent myWebLink1 = new Intent();
            myWebLink1.setData(Uri
                    .parse("https://play.google.com/store/apps/details?id=com.bvl.weatherapp&hl=vi"));
            startActivity(myWebLink1);

        } else if (id == R.id.nav_share) {
            Intent shareInten = new Intent();
            shareInten.setAction(Intent.ACTION_SEND);
            shareInten.putExtra(Intent.EXTRA_TEXT, "Hey, download this app!");
            shareInten.setType("text/plain");
            startActivity(shareInten);

        } else if (id == R.id.nav_send) {
            Intent about = new Intent(getBaseContext(), About.class);
            startActivity(about);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
