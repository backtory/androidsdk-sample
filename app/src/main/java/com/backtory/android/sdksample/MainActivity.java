package com.backtory.android.sdksample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;


public class MainActivity extends AppCompatActivity {

    static String lastGenEmail = "";
    static String lastGenUsername = "";
    static String lastGenPassword = "";

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment f = fragmentManager.findFragmentByTag(
                "android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (f != null && f instanceof InAppPurchaseFragment)
            f.onActivityResult(requestCode, resultCode, data);
        else if(f != null && f instanceof AuthFragment)
            f.onActivityResult(requestCode, resultCode, data);
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    //-----------------------------------------------------------------------------

    static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    static String generateEmail(boolean random) {
        String s = random ? randomAlphabetic(3) + "@" + randomAlphabetic(3) + ".com" : "ar.d.farahani@gmail.com";
        lastGenEmail = s;
        return s;
    }

    static String generateUsername(boolean random) {
        String s = random ? randomAlphabetic(6) : "hamze";
        lastGenUsername = s;
        return s;
    }

    static String generatePassword(boolean random) {
        String s = random ? randomAlphabetic(6) : "1234";
        lastGenPassword = s;
        return s;
    }

    //-----------------------------------------------------------------------------
    public abstract static class AbsFragment extends Fragment implements View.OnClickListener {
        TextView textView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(getLayoutRes(), container, false);
            textView = (TextView) v.findViewById(R.id.textview);
            for (int id : getButtonsId()) {
                v.findViewById(id).setOnClickListener(this);
            }
            return v;
        }

        /**
         * Setting enclosing class as click listener for all the layout buttons
         *
         * @return list of this fragment's buttons ids. Order doesn't matter
         */
        protected abstract int[] getButtonsId();

        protected abstract
        @LayoutRes
        int getLayoutRes();

        protected <T> BacktoryCallBack<T> printCallBack() {
            return new BacktoryCallBack<T>() {
                @Override
                public void onResponse(BacktoryResponse<T> response) {
                    if (response.isSuccessful())
                        textView.setText(response.body() != null ? gson.toJson(response.body()) : "successful");
                    else
                        textView.setText(response.message());
                }
            };
        }

        void toast(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

    //---------------------------------------------------------------------------


    public static class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[]{"Auth", "CloudCode", "Game", "Storage", "Database",
                                                    "Matchmaking", "Challenge", "Realtime", "Chat", "InApp Purchase"};

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AuthFragment();
                case 1:
                    return new CloudCodeFragment();
                case 2:
                    return new GameFragment();
                case 3:
                    return new FileStorageFragment();
                case 4:
                    return new DatabaseFragment();
                case 5:
                    return new MatchmakingFragment();
                case 6:
                    return new ChallengeFragment();
                case 7:
                    return RealtimeFragment.getInstance();
                case 8:
                    return new ChatFragment();
                case 9:
                    return new InAppPurchaseFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
