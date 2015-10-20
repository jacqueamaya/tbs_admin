package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jacquelyn on 9/24/2015.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private SharedPreferences mSharedPreferences;
    private ImageView mImgUser;

    public static final int INDEX_USER_IMAGE = (int) (Math.random()*10);
    public static final int USER_IMAGES[] =
            {
                    R.drawable.user_1,
                    R.drawable.user_2,
                    R.drawable.user_3,
                    R.drawable.user_4,
                    R.drawable.user_5,
                    R.drawable.user_6,
                    R.drawable.user_7,
                    R.drawable.user_8,
                    R.drawable.user_9,
                    R.drawable.user_10
            };

    protected void setupUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mImgUser = (ImageView) findViewById(R.id.imgUser);
        mImgUser.setImageResource(USER_IMAGES[INDEX_USER_IMAGE]);

        if(null == toolbar) {
            throw new RuntimeException("No toolbar found");
        }

        if(null == mDrawerLayout) {
            throw new RuntimeException("No drawer layout found");
        }

        if(null == mNavigationView) {
            throw new RuntimeException("No navigation view found");
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle.syncState();

        TextView txtUser = (TextView) findViewById(R.id.txtName);
        txtUser.setText(getUserPreferences());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Intent intent = null;

        if(checkItemClicked(menuItem)) {
            switch(menuItem.getItemId()) {
                case R.id.nav_notifications:
                    intent = new Intent(this, NotificationsActivity.class);
                    break;
                case R.id.nav_items_queue:
                    intent = new Intent(this, ItemsOnQueueActivity.class);
                    break;
                case R.id.nav_reserved_items:
                    intent = new Intent(this, ReservedItemsActivity.class);
                    break;
                case R.id.nav_donations:
                    intent = new Intent(this, DonationsActivity.class);
                    break;
                case R.id.nav_transactions:
                    intent = new Intent(this, TransactionsActivity.class);
                    break;
                case R.id.nav_logout:
                    mSharedPreferences.edit().clear().apply();
                    intent = new Intent(this, LoginActivity.class);
                    break;
                default:
                    intent = new Intent(this, NotificationsActivity.class);

            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }

        mDrawerLayout.closeDrawers();

        if(intent != null) {
            startActivity(intent);
            this.finish();
            overridePendingTransition(0, 0);
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    public String getUserPreferences() {
        mSharedPreferences = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        return mSharedPreferences.getString("username", "No Username");
    }

    public abstract boolean checkItemClicked(MenuItem menuItem);
}
