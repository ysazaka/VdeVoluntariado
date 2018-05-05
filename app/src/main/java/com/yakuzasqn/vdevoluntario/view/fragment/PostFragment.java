package com.yakuzasqn.vdevoluntario.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.CustomFragmentPagerAdapter;
import com.yakuzasqn.vdevoluntario.adapter.PostAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private PagerAdapter adapter;
    private ArrayList<CustomFragmentPagerAdapter.Pager> pagers;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        /* Tabs "Contribuir" e "Oferecer" */
        ViewPager pager = v.findViewById(R.id.pager);
        PagerSlidingTabStrip tabs = v.findViewById(R.id.tabs);

        tabs.requestLayout();

        pagers = new ArrayList<>();

        pagers.add(new CustomFragmentPagerAdapter.Pager(getString(R.string.tab_contribute), TabContributeFragment.class));
        pagers.add(new CustomFragmentPagerAdapter.Pager(getString(R.string.tab_oferecer), TabOfferFragment.class));

        adapter = new CustomFragmentPagerAdapter(getChildFragmentManager(), pagers);

        pager.setAdapter(adapter);

        tabs.setViewPager(pager);

        pager.setOffscreenPageLimit(pagers.size());

        setHasOptionsMenu(true);
        /* */

        return v;
    }

}
