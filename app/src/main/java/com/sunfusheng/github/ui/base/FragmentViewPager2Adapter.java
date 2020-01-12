package com.sunfusheng.github.ui.base;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * @author sunfusheng
 * @since 2020-01-12
 */
public class FragmentViewPager2Adapter extends FragmentStateAdapter {
    private SparseArray<Fragment> fragments;

    public FragmentViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public FragmentViewPager2Adapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public FragmentViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setFragments(SparseArray<Fragment> fragments) {
        this.fragments = fragments;
    }

    public SparseArray<Fragment> getFragments() {
        return fragments;
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
