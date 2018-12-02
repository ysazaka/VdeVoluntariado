package com.yakuzasqn.vdevoluntario.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

    protected List<Pager> pagers;

    public CustomFragmentPagerAdapter(FragmentManager fm, List<Pager> pagers) {
        super(fm);

        this.pagers = pagers;
    }

    @Override
    public Fragment getItem(int i) {
        return pagers.get(i).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int i) {
        return pagers.get(i).getTitle();
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    public static class Pager implements IPager {

        private String title;
        private Class newFragment;
        private Fragment fragmentGo;
        private int icon;
        private String iconS;

        public Pager(String title, Class fragment) {
            this.title = title;
            this.newFragment = fragment;
        }

        public Pager(int icon, Class fragment) {
            this.icon = icon;
            this.newFragment = fragment;
        }

        public Pager(int icon, Class fragment, String title) {
            this.icon = icon;
            this.newFragment = fragment;
            this.title = title;
        }

        public Pager(String title, Fragment fragmentGo) {
            this.title = title;
            this.fragmentGo = fragmentGo;
        }

        public Pager(int icon, Fragment fragmentGo) {
            this.icon = icon;
            this.fragmentGo = fragmentGo;
        }

        public Pager(int icon, String title, Class newFragment) {
            this.title = title;
            this.newFragment = newFragment;
            this.icon = icon;
        }

        public Pager(int icon, String title, Fragment fragmentGo) {
            this.title = title;
            this.fragmentGo = fragmentGo;
            this.icon = icon;
        }

        /* -- */

        public Pager(Class fragment, String icon, String title) {
            this.iconS = icon;
            this.newFragment = fragment;
            this.title = title;
        }

        public Pager(Fragment fragmentGo, String icon, String title) {
            this.iconS = icon;
            this.fragmentGo = fragmentGo;
            this.title = title;
        }

        @Override
        public int getIcon() {
            return icon;
        }

        @Override
        public String getIcons() {
            return iconS;
        }

        @Override
        public String getTitle() {
            return title;
        }

        public Fragment getFragment() {

            if( newFragment == null ) return fragmentGo;

            Fragment fragment = new Fragment();
            try {
                fragment = (Fragment) newFragment.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fragment;
        }

    }
}