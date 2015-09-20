package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.TextView;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import citu.teknoybuyandselladmin.fragments.DonationsFragment;
import citu.teknoybuyandselladmin.fragments.ItemsQueueFragment;
import citu.teknoybuyandselladmin.fragments.NotificationsFragment;
import citu.teknoybuyandselladmin.fragments.ReservedItemsFragment;
import citu.teknoybuyandselladmin.fragments.TransactionsFragment;

public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        String admin = intent.getStringExtra("admin");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        TextView txtUser = (TextView) findViewById(R.id.txtName);
        txtUser.setText(admin);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.sandwich);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    public void setActionBarTitle(String title){
        mToolbar.setTitle(title);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    public void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //menuItem.setChecked(true);
                        selectDrawerItem(menuItem);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;

        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_notifications:
                fragmentClass = NotificationsFragment.class;
                break;
            case R.id.nav_items_queue:
                fragmentClass = ItemsQueueFragment.class;
                break;
            case R.id.nav_reserved_items:
                fragmentClass = ReservedItemsFragment.class;
                break;
            case R.id.nav_donations:
                fragmentClass = DonationsFragment.class;
                break;
            case R.id.nav_transactions:
                fragmentClass = TransactionsFragment.class;
                break;
            default:
                fragmentClass = NotificationsFragment.class;

        }
        if(menuItem.getItemId() != (R.id.nav_logout)) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Intent intent;
            intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
