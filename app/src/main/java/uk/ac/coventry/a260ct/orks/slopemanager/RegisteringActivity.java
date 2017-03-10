package uk.ac.coventry.a260ct.orks.slopemanager;

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

public class RegisteringActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private HashMap<String,String> masterInfo=new HashMap<>();
    private UpdateInfo callback1;
    private SendInfo callback2;
    private TabLayout tabLayout;
    private String userType;
    private int paid=-1;
    enum hashAttribtues{
        FIRST_NAME,
        SURNAME,
        PHONE,
        EMAIL,

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);
        Intent getType=getIntent();
        userType=getType.getStringExtra("type");
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
                if (curFragment.getArguments().getString("TAG").matches("LoginDetailsFragment")){
                    callback2 = (SendInfo) curFragment;
                    callback2.sendHashMap(masterInfo);


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment curFragment=mSectionsPagerAdapter.getItem(tabLayout.getSelectedTabPosition());
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







    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> arrayOfFragments;

        public SectionsPagerAdapter(FragmentManager fm,String typeOfUser) {
            super(fm);
            arrayOfFragments=new ArrayList<>();
            arrayOfFragments.add(0,new AboutYouFragment());
            arrayOfFragments.add(1,new MembershipPayment());
            arrayOfFragments.add(2,new LoginDetailsFragment());

             if(typeOfUser.matches("Operator") ||typeOfUser.matches("Manager") || typeOfUser.matches("Instructor")){
                arrayOfFragments.remove(1);
            }

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragmentToReturn=arrayOfFragments.get(position);
            if(!fragmentToReturn.isAdded()) {
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
}
