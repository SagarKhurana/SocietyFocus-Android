package com.zircon.app.ui.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.zircon.app.BuildConfig;
import com.zircon.app.R;
import com.zircon.app.ui.common.activity.AbsBaseActivity;
import com.zircon.app.ui.common.fragment.AbsFragment;
import com.zircon.app.utils.SessionManager;

public class HomeFragment extends AbsFragment {

    private ImageView mSocietyImgView;
    private TextView mSocietyNameView;
    private TextView mSocietyAddressView;
    private ImageView mSocietyCallView;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

   /* InterstitialAd mInterstitialAd;

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E272D0E02F9646D08082058A0B814575")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }*/

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSocietyImgView = (ImageView) view.findViewById(R.id.society_img);
        mSocietyNameView = (TextView) view.findViewById(R.id.society_name);
        mSocietyAddressView = (TextView) view.findViewById(R.id.society_address);
        mSocietyCallView = (ImageView) view.findViewById(R.id.society_call);

   /*     mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        requestNewInterstitial();*/

        Picasso.with(getActivity()).load(SessionManager.getLoggedInSociety().societypic).placeholder(R.drawable.ic_1_2).into(mSocietyImgView);

        mSocietyNameView.setText(SessionManager.getLoggedInSociety().name);
        mSocietyAddressView.setText(SessionManager.getLoggedInSociety().address);

        mSocietyCallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AbsBaseActivity) getActivity()).call(SessionManager.getLoggedInSociety().contactDetail);
            }
        });
    }
}
