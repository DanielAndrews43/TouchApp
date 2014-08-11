package com.twoshorts.touch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.twoshorts.sql.SQLiteHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;

public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        ((TouchApplication) getApplication()).getTracker(TouchApplication.TrackerName.APP_TRACKER);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_over, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static String SCORE = "score";
        private InterstitialAd ad;

        public PlaceholderFragment() {
        }

        public void setHighScore(){
            SQLiteHelper db = new SQLiteHelper(getActivity());

            TextView highScoreView = (TextView) getActivity().findViewById(R.id.over_high_score);
            int highScore = db.findHighScore();
            String currentText = (String) highScoreView.getText();

            highScoreView.setText(currentText + "\n" + highScore);
        }

        private void newGame(){
            //Display ad
            if(ad.isLoaded()){
                ad.show();
            }

            //end activity
            getActivity().finish();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            TextView scoreView = (TextView) getActivity().findViewById(R.id.over_score);

            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                String currentText = (String) scoreView.getText();
                String score = Integer.toString((Integer) bundle.get(SCORE));
                scoreView.setText(currentText + "\n" + (!TextUtils.isEmpty(score)? score:"0"));
            }

            setHighScore();

            Button retry = (Button) getActivity().findViewById(R.id.over_retry);
            retry.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    newGame();
                }
            });

            //create ad
            ad = new InterstitialAd(getActivity());
            ad.setAdUnitId("ca-app-pub-8227385973671320/3318418293");
            AdRequest adRequest = new AdRequest.Builder().build();
            ad.loadAd(adRequest);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_game_over, container, false);
            return rootView;
        }
    }
}
