package anuja.project.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import anuja.project.finalproject.adapters.SaleAdapter;
import anuja.project.finalproject.data.ProductContract;
import anuja.project.finalproject.fragment.DetailFragment;
import anuja.project.finalproject.fragment.FavouriteFragment;
import anuja.project.finalproject.fragment.SaleFragment;
import anuja.project.finalproject.sync.SyncAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SaleAdapter.CallBack {

    private static final String TAG = "MainActivity";
    public static boolean isTablet = false;



//    public static int SYNC_SECONDS = 15;
//    Account account;

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

        System.out.println("---------------------------- MainActivity ---------------------------------");
        Cursor countCursor =this.getContentResolver().query(ProductContract.ProductEntry.CONTENT_URI,
                new String[] {"count(*) AS count"},
                null,
                null,
                null);
        countCursor.moveToFirst();
        int count = countCursor.getInt(0);
        Log.e(TAG, "Counting db size = "+count);
        System.out.println("---------------------------- Counting columns in db =  ---------------------------------"+count);
        if(count == 0){
            Log.e(TAG, "Calling Sync");
            SyncAdapter.Sync(this);
        }else {
            Log.e(TAG, "Calling initializeAdapter");
            SyncAdapter.initializeAdapter(this);
        }

//        account = createSyncAccount();
//        if (account == null) {
//            Log.d(TAG, "<<<<Failed to create sync account.");
//        } else {
//            Log.d(TAG, "<<<<Adding periodic sync");
//            ContentResolver.addPeriodicSync(account, ProductContract.AUTHORITY, Bundle.EMPTY,
//                    SYNC_SECONDS);
//        }
    }

    private void setupMyPager(ViewPager viewPager){

        mFragmentList.add(new SaleFragment());
        mFragmentList.add(new FavouriteFragment());
        mFragmentTitleList.add("Sale");
        mFragmentTitleList.add("Favourites");
        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),mFragmentList,mFragmentTitleList);
        viewPager.setAdapter(mMyPagerAdapter);
    }

    @Override
    public void ItemSelected(Uri selectedUri) {
        Log.e(TAG,"This is phone");
        startActivity(new Intent(MainActivity.this, DetailFragment.class).
                setData(selectedUri));
    }

    //    public Account createSyncAccount() {
//        AccountManager am = AccountManager.get(this);
//        Account[] accounts;
//
//        try {
//            accounts = am.getAccountsByType(ACCOUNT_TYPE);
//        } catch (SecurityException e) { // This never should happen
//            accounts = new Account[]{};
//        }
//        if (accounts.length > 0) { // already have an account defined
//            Log.d(TAG, "<<<<Account already defined.");
//            return accounts[0];
//        }
//
//        Account newAccount = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
//        if (am.addAccountExplicitly(newAccount, "pass1", null)) {
//            // Set this account as syncable.
//            Log.d(TAG, "<< New account added. Setting syncable.");
//            ContentResolver.setIsSyncable(newAccount, ProductContract.AUTHORITY, 1);
//            ContentResolver.setSyncAutomatically(newAccount,ProductContract.AUTHORITY, true);
//        } else {// else The account exists or some other error occurred.
//            Log.d(TAG, "<<<<Could not add new account.");
//            newAccount = null;
//        }
//        return newAccount;
//    }


}
