package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisteringActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private HashMap<String,String> masterInfo=new HashMap<>();
    private UpdateInfo callback1;
    private UpdateInfo callback2;
    private UpdateInfo callback3;
    private SendInfo callback4;
    private TabLayout tabLayout;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);
        Intent getType=getIntent();
        userType=getType.getStringExtra("TYPE");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),userType);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment curFragment=mSectionsPagerAdapter.getItem(tabLayout.getSelectedTabPosition());
                if (curFragment.getArguments().getString("TAG").matches("AboutYouFragment")){
                    callback4 = (SendInfo) curFragment;
                    callback4.sendHashMap(masterInfo);


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment curFragment=mSectionsPagerAdapter.getItem(tabLayout.getSelectedTabPosition());
                switch (curFragment.getArguments().getString("TAG")) {
                    case "AboutYouFragment":
                        callback1 = (UpdateInfo) curFragment;
                        callback1.sendHashMap("AboutYouFragment", masterInfo);
                        callback1.showInfo(masterInfo);
                        break;
                    case "MembershipPayment":
                       // callback2 = (UpdateInfo) mSectionsPagerAdapter.getItem(tabLayout.getSelectedTabPosition());
                      //  callback2.setInfo(tab.getPosition(), allergensInfo, foodTypePref);
                        break;
                    case "LoginDetailsFragment":
                        callback3 = (UpdateInfo) mSectionsPagerAdapter.getItem(tabLayout.getSelectedTabPosition());
                        callback3.setInfo("LoginDetailsFragment", masterInfo);
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }







    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> arrayOfFragments;

        public SectionsPagerAdapter(FragmentManager fm,String typeOfUser) {
            super(fm);
            arrayOfFragments=new ArrayList<>();
            if(typeOfUser.matches("User")){
                arrayOfFragments.add(new AboutYouFragment());
                arrayOfFragments.add(new LoginDetailsFragment());
            }
            else if(typeOfUser.matches("Member")) {

                arrayOfFragments.add(new AboutYouFragment());
                arrayOfFragments.add(new MembershipPayment());
                arrayOfFragments.add(new LoginDetailsFragment());
            }
            else if(typeOfUser.matches("Operator")){
                arrayOfFragments.add(new AboutYouFragment());
                arrayOfFragments.add(new LoginDetailsFragment());
            }

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragmentToReturn=arrayOfFragments.get(position);
            Bundle bundle=new Bundle();
            bundle.putString("TAG",fragmentToReturn.getClass().getSimpleName());
            fragmentToReturn.setArguments(bundle);
            return fragmentToReturn;
        }

        @Override
        public int getCount() {
            // Show  total pages.
            return arrayOfFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arrayOfFragments.get(position).getTag();
        }
    }
}
