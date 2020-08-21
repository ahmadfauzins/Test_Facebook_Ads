package com.fauzi.testfacebookads;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;

public class Ad_Interstitial extends AppCompatActivity {

    TextView txStatus;
    ProgressBar progress;

    boolean canShowFullscreenAd;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_interstitial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txStatus = findViewById(R.id.interstitial_status);
        progress = findViewById(R.id.interstitial_progress);

        interstitialAd = new InterstitialAd(this, ActivityConfig.FB_INTERSTITIAL);
        interstitialAd.loadAd();
        interstitialAd.setAdListener(new AbstractAdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);
                Log.d("adInterLog", error.getErrorMessage());
                progress.setVisibility(View.GONE);
                txStatus.setText("Ad Failed to load");
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
                progress.setVisibility(View.GONE);
                txStatus.setText("Ad Displayed");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
                progress.setVisibility(View.GONE);
                txStatus.setText("Ad Closed");
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                txStatus.setText("Ad Loaded");
                if (canShowFullscreenAd) {
                    // Jika Langsung Tampil tanpa Nunggu Watu Delayed
                    //interstitialAd.show();
                    // Jika  Tampil dengan Nunggu Watu Delayed
                    showAdWithDelay();
                }
            }
        });
    }

    private void showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
         Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Check if interstitialAd has been loaded successfully
                if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
                    return;
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if(interstitialAd.isAdInvalidated()) {
                    return;
                }
                // Show the ad
                interstitialAd.show();
            }
        }, 1000 * 60 * 1); // Show the ad after 15 minutes
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        canShowFullscreenAd = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        canShowFullscreenAd = true;
    }
}