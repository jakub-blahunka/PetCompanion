package org.jaku8ka.petcompanion;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SlidePagerInfoAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SlidePagerInfoAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new SlidePageCatFragment();
            case 1:
                return new SlidePageDogFragment();
            case 2:
                return new SlidePageRabbitFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.cat);
            case 1:
                return mContext.getString(R.string.dog);
            case 2:
                return mContext.getString(R.string.rabbit);
            default:
                return null;
        }
    }

}
