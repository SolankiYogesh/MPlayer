package com.syinfotech.mplayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.syinfotech.mplayer.Fragments.LocalFragment;
import com.syinfotech.mplayer.Fragments.PlayistFragment;
import com.syinfotech.mplayer.Fragments.SongListFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    int tabscount;

    public PagerAdapter(@NonNull FragmentManager fm, int tabscount) {
        super(fm);
        this.tabscount = tabscount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SongListFragment();
            case 1:
                return new LocalFragment();
            case 2:
                return new PlayistFragment();
            default:
                return new SongListFragment();
        }
    }

    @Override
    public int getCount() {
        return tabscount;
    }
}
