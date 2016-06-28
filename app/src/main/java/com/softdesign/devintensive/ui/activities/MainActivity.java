package com.softdesign.devintensive.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private int mCurrentEditMode = 0;
    private List<EditText> mUserInfo;

    private DataManager mDataManager;
    NavigationView mNavigationView;
    private EditText mUserPhone, mUserEmail, mUserGit, mUserVk, mUserBio;
    private ImageView mMakeCall_img;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mDataManager = DataManager.getInstance();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawerLayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserEmail = (EditText) findViewById(R.id.email_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.github_et);
        mUserBio = (EditText) findViewById(R.id.bio_et);


        mMakeCall_img = (ImageView) findViewById(R.id.makeCall_img);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mUserInfo = new ArrayList<>();
        mUserInfo.add(mUserPhone);
        mUserInfo.add(mUserEmail);
        mUserInfo.add(mUserVk);
        mUserInfo.add(mUserGit);
        mUserInfo.add(mUserBio);

        Collections.addAll(mUserInfo, mUserPhone, mUserEmail,
                mUserVk, mUserGit, mUserBio);

        mFab.setOnClickListener(this);
        setupToolbar();
        setupDrawer();
        loadUserInfoValue();

//

        if (savedInstanceState == null) {

            //activity запущено впервые
        } else {
            //activity уже запускалось
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (mCurrentEditMode == 1) {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                } else {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");

        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserInfoValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupDrawer() {
        Log.d(TAG, "setupDrawer");

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            updateDrawerItems();
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    showSnackbar(item.getTitle().toString());
                    item.setChecked(true);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }
    }

    public void updateDrawerItems(){  //redraw navigation view items
        Log.d(TAG, "updateDrawerItems");

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            TextView mTextView_menuUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.menu_userEmail_txt);
            mTextView_menuUserEmail.setText(mUserEmail.getText()); //drawer menu email change
            setupMenuAvatar();
        }
    }

    public void setupMenuAvatar() {    //setup menu avatar with rounded corners from picture
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            ImageView mRoundedAvatar_img = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.rounded_avatar);
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.woman0041);
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), src);
            dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
            mRoundedAvatar_img.setImageDrawable(dr);
        }
    }

    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
            for (EditText e : mUserInfo) {
                e.setEnabled(true);
                e.setFocusable(true);
                e.setFocusableInTouchMode(true);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            for (EditText e : mUserInfo) {
                e.setEnabled(false);
                e.setFocusable(false);
                e.setFocusableInTouchMode(false);
            }
            saveUserInfoValue();
        }
    }

    private void loadUserInfoValue() {
        Log.d(TAG, "loadUserInfoValue");

        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();

        for (int i = 0; i < userData.size(); i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        Log.d(TAG, "saveUserInfoValue");

        List<String> userData = new ArrayList<>();
        for (int i = 0; i < mUserInfo.size(); i++) {
            userData.add(mUserInfo.get(i).getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
        updateDrawerItems();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");

        if (mNavigationView != null && mNavigationView.isShown()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}