package uk.ac.coventry.a260ct.orks.slopemanager.registration;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.database.User;

public class RegisteringActivity extends AppCompatActivity implements SendInfo {




    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private HashMap<User.ATTRIBUTES,String> masterInfo=new HashMap<>();
    private UpdateInfo callback1;
    private SendInfo callback2;
    private SendInfo sendConfirmationPaymentInfo;
    private SendInfo sendTypeUserCallback;
    private TabLayout tabLayout;
    private String userType;
    private int paid=-1;
    private SectionsPagerAdapterCostumer mSectionsPagerAdapterCostumer;
    private SectionsPagerAdapterStaff mSectionsPagerAdapterStaff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);
        Intent getType=getIntent();
        userType=getType.getStringExtra("type");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        if(!userType.matches("Customer")) {
            mSectionsPagerAdapterStaff = new SectionsPagerAdapterStaff(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapterStaff);
        }
        else if (userType.matches("Customer")){
            mSectionsPagerAdapterCostumer = new SectionsPagerAdapterCostumer(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapterCostumer);
        }
        // Set up the ViewPager with the sections adapter.


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment curFragment=null;
                if(userType.matches("Customerx")) {
                     curFragment = mSectionsPagerAdapterCostumer.getItem(tabLayout.getSelectedTabPosition());
                }
                else{
                     curFragment = mSectionsPagerAdapterStaff.getItem(tabLayout.getSelectedTabPosition());
                }
                if (curFragment.getArguments().getString("TAG").matches("LoginDetailsFragment")){
                    callback2 = (SendInfo) curFragment;
                    sendTypeUserCallback= (SendInfo) curFragment;
                    sendConfirmationPaymentInfo= (SendInfo) curFragment;
                    callback2.sendHashMap(masterInfo);
                    sendConfirmationPaymentInfo.sendConfPayment(paid);
                    sendTypeUserCallback.sendTypeOfUser(userType);


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment curFragment=null;
                if(userType.matches("Customer")) {
                    curFragment = mSectionsPagerAdapterCostumer.getItem(tabLayout.getSelectedTabPosition());
                }
                else{
                    curFragment = mSectionsPagerAdapterStaff.getItem(tabLayout.getSelectedTabPosition());
                }
                switch (curFragment.getArguments().getString("TAG")) {
                    case "AboutYouFragment":
                        callback1 = (UpdateInfo) curFragment;
                        callback1.setInfo("AboutYouFragment", masterInfo);
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }







    public class SectionsPagerAdapterCostumer extends FragmentPagerAdapter {
        ArrayList<Fragment> arrayOfFragments;

        public SectionsPagerAdapterCostumer(FragmentManager fm) {
            super(fm);
            arrayOfFragments=new ArrayList<>();
            arrayOfFragments.add(0,new AboutYouFragment());
            arrayOfFragments.add(1,new MembershipPayment());
            arrayOfFragments.add(2,new LoginDetailsFragment());

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragmentToReturn=arrayOfFragments.get(position);
            if (fragmentToReturn.getArguments()==null)  {
                Bundle bundle = new Bundle();
                bundle.putString("TAG", fragmentToReturn.getClass().getSimpleName());
                fragmentToReturn.setArguments(bundle);
            }
            return fragmentToReturn;
        }

        @Override
        public int getCount() {
            // Show  total pages.
            return arrayOfFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "About Me";
                case 1:
                    return "Membership Payment";
                case 2:
                    return "Login Details";
            }
            return null;
        }
    }
    public class SectionsPagerAdapterStaff extends FragmentPagerAdapter {
        ArrayList<Fragment> arrayOfFragments;

        public SectionsPagerAdapterStaff(FragmentManager fm) {
            super(fm);
            arrayOfFragments=new ArrayList<>();
            arrayOfFragments.add(0,new AboutYouFragment());
            arrayOfFragments.add(1,new LoginDetailsFragment());

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragmentToReturn=arrayOfFragments.get(position);
            if (fragmentToReturn.getArguments()==null)  {
                Bundle bundle = new Bundle();
                bundle.putString("TAG", fragmentToReturn.getClass().getSimpleName());
                fragmentToReturn.setArguments(bundle);
            }
            return fragmentToReturn;
        }

        @Override
        public int getCount() {
            // Show  total pages.
            return arrayOfFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "About Me";
                case 1:
                    return "Login Details";
            }
            return null;
        }
    }
    @Override
    public void sendHashMap(HashMap<User.ATTRIBUTES, String> masterInfo) {
        //just had to implement it
    }

    @Override
    public void sendConfPayment(int successful) {
        if(successful==1)
            paid=successful;
    }

    @Override
    public void sendTypeOfUser(String type) {
        //just had to implement it
    }
}
