package lol.challenge.magnet.android.lol;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import net.yanzm.mth.MaterialTabHost;

import java.util.Locale;

import lol.challenge.magnet.android.lol.constants.Constant;
import lol.challenge.magnet.android.lol.adapter.ChatAdapter;
/**
 * According to facebook documentation,
 * the facebook api has to be initialized first before call to setContentView.
 * I called the initFacebook() method which I defined in the MainActivity class.
 * In the initFacebook() method, I configured the third-party library which
 * wraps up the Facebook sdk. Check link to know more about this library
 *https://github.com/sromku/android-simple-facebook/wiki/Configuration
 * And take a look at the Facebook api documentation(not compulsory since we are using a third-party)
 * https://developers.facebook.com/docs/android
 *
 */
public class MainActivity extends ActionBarActivity {

    private SimpleFacebook mSimpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFacebook();
        setContentView(R.layout.activity_main);
        // setup facebook api
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        /**
         * Used a third-party library for the tabs. You can check the
         * documentation here. Based on the instruction there I wrote
         * the remaining codes. https://github.com/yanzm/MaterialTabHost
         */
        MaterialTabHost tabHost = (MaterialTabHost) findViewById(android.R.id.tabhost);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(pagerAdapter.getPageTitle(i));
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(tabHost);
        viewPager.setCurrentItem(1); // Added this line myself so that the tab is set to chat tab when run.

        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
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


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initFacebook() {
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(getString(R.string.app_id))
                .setNamespace(getString(R.string.app_name_space))
                .setPermissions(Constant.permissions)
                .build();
        SimpleFacebook.setConfiguration(configuration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_facebook).toUpperCase(l);
                case 1:
                    return getString(R.string.title_chats).toUpperCase(l);
                case 2:
                    return getString(R.string.title_find_friends).toUpperCase(l);
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            if (getArguments().getInt(ARG_SECTION_NUMBER)== 1) {
                // Facebook Fragment
                rootView = inflater.inflate(R.layout.pager_facebook, container, false);
                return rootView;
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER)== 2) {
                // Chat Fragment
                rootView = inflater.inflate(R.layout.pager_chats, container, false);
                ListView chat_listView = (ListView) rootView.findViewById(R.id.chat_list);
                chat_listView.setAdapter(new ChatAdapter(getActivity()));
                return rootView;
            }
            else {
                rootView = inflater.inflate(R.layout.pager_fragment, container, false);
                return rootView;
            }
        }

            }
}
