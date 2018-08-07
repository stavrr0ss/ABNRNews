package ro.atoming.abnrnews.ui.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.ui.fragments.BusinessFragment;
import ro.atoming.abnrnews.ui.fragments.EntertainmentFragment;
import ro.atoming.abnrnews.ui.fragments.HeadlineFragment;
import ro.atoming.abnrnews.ui.fragments.HealthFragment;
import ro.atoming.abnrnews.ui.fragments.MyNewsFragment;
import ro.atoming.abnrnews.ui.fragments.ScienceFragment;
import ro.atoming.abnrnews.ui.fragments.SportsFragment;
import ro.atoming.abnrnews.ui.fragments.TechnologyFragment;

public class CategoryAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public CategoryAdapter(FragmentManager fm,Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return new MyNewsFragment();
            case 2: return new EntertainmentFragment();
            case 3: return new SportsFragment();
            case 4: return new BusinessFragment();
            case 5: return new TechnologyFragment();
            case 6: return new ScienceFragment();
            case 7: return new HealthFragment();
            default: return new HeadlineFragment();
        }
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return mContext.getString(R.string.fragment_title_headline);
        }else if (position == 1){
            return mContext.getString(R.string.fragment_title_mynews);
        }else if (position == 2){
            return mContext.getString(R.string.fragment_title_entertainment);
        }else if (position == 3){
            return mContext.getString(R.string.fragment_title_sports);
        }else if (position == 4){
            return mContext.getString(R.string.fragment_title_business);
        }else if (position == 5){
            return mContext.getString(R.string.fragment_title_technology);
        }else if (position == 6){
            return mContext.getString(R.string.fragment_title_science);
        }else {
            return mContext.getString(R.string.fragment_title_health);
        }
    }

}
