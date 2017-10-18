package anuja.project.finalproject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import anuja.project.finalproject.adapters.MyPagerAdapter;
import anuja.project.finalproject.fragment.FavouriteFragment;
import anuja.project.finalproject.fragment.SaleFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String ACCOUNT_TYPE = "com.anuja.finalproject";
    private static final String ACCOUNT_NAME = "Default Account";
    public static final String AUTHORITY = "com.anuja.finalproject";
    public static int SYNC_SECONDS = 15;
    Account account;

    @BindView(R.id.tabs)
     TabLayout mTabs;
    @BindView(R.id.toolbar)
     Toolbar mToolbar;
    @BindView(R.id.viewpager)
     ViewPager mViewPager;
    private  MyPagerAdapter mMyPagerAdapter;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setupMyPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);

        account = createSyncAccount();
        if (account == null) {
            Log.d(TAG, "<<<<Failed to create sync account.");
        } else {
            Log.d(TAG, "<<<<Adding periodic sync");
            ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY,
                    SYNC_SECONDS);
        }
    }

    private void setupMyPager(ViewPager viewPager){

        mFragmentList.add(new SaleFragment());
        mFragmentList.add(new FavouriteFragment());
        mFragmentTitleList.add("Sale");
        mFragmentTitleList.add("Favourites");
        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),mFragmentList,mFragmentTitleList);
        viewPager.setAdapter(mMyPagerAdapter);
    }

    public Account createSyncAccount() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts;

        try {
            accounts = am.getAccountsByType(ACCOUNT_TYPE);
        } catch (SecurityException e) { // This never should happen
            accounts = new Account[]{};
        }
        if (accounts.length > 0) { // already have an account defined
            Log.d(TAG, "<<<<Account already defined.");
            return accounts[0];
        }

        Account newAccount = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
        if (am.addAccountExplicitly(newAccount, "pass1", null)) {
            // Set this account as syncable.
            Log.d(TAG, "<< New account added. Setting syncable.");
            ContentResolver.setIsSyncable(newAccount, AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
        } else {// else The account exists or some other error occurred.
            Log.d(TAG, "<<<<Could not add new account.");
            newAccount = null;
        }
        return newAccount;
    }


}
