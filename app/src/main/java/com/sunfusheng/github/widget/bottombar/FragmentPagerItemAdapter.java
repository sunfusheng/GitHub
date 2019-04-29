package com.sunfusheng.github.widget.bottombar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class FragmentPagerItemAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<FragmentPagerItem> items;
    private SparseArray<Fragment> fragments = new SparseArray<>();
    private OnInstantiateFragmentListener listener;

    public FragmentPagerItemAdapter(Context context, FragmentManager fragmentManager, List<FragmentPagerItem> items) {
        super(fragmentManager);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        if (listener != null) {
            listener.onInstantiate(position, fragment, items.get(position).getArgs());
        }
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position).newInstance(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public String getPageTitle(int position) {
        return items.get(position).getPageTitle();
    }

    public void setOnInstantiateFragmentListener(OnInstantiateFragmentListener listener) {
        this.listener = listener;
    }

    public interface OnInstantiateFragmentListener {
        void onInstantiate(int position, Fragment fragment, Bundle args);
    }

    public static class Builder {

        private Context context;
        private FragmentManager fragmentManager;
        private List<FragmentPagerItem> items = new ArrayList<>();

        public Builder(Context context, FragmentManager fragmentManager) {
            this.context = context;
            this.fragmentManager = fragmentManager;
        }

        public Builder add(FragmentPagerItem item) {
            items.add(item);
            return this;
        }

        public Builder add(int resId, Fragment fragment) {
            return add(context.getString(resId), fragment);
        }

        public Builder add(int resId, Class<? extends Fragment> clazz) {
            return add(context.getString(resId), clazz);
        }

        public Builder add(int resId, Class<? extends Fragment> clazz, Bundle args) {
            return add(context.getString(resId), clazz, args);
        }

        public Builder add(String title, Fragment fragment) {
            return add(FragmentPagerItem.create(title, fragment));
        }

        public Builder add(String title, Class<? extends Fragment> clazz) {
            return add(FragmentPagerItem.create(title, clazz));
        }

        public Builder add(String title, Class<? extends Fragment> clazz, Bundle args) {
            return add(FragmentPagerItem.create(title, clazz, args));
        }

        public FragmentPagerItemAdapter build() {
            return new FragmentPagerItemAdapter(context, fragmentManager, items);
        }
    }
}
