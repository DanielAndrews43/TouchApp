package com.twoshorts.touch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twoshorts.sql.SQLiteHelper;
import com.twoshorts.sql.Score;
import com.google.android.gms.analytics.GoogleAnalytics;

public class TouchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TouchApplication) getApplication()).getTracker(TouchApplication.TrackerName.APP_TRACKER);

        setContentView(R.layout.activity_touch);
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
        getMenuInflater().inflate(R.menu.touch, menu);
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
        private SQLiteHelper db;
        private boolean running = false;


        public PlaceholderFragment() {
        }

        public void addNumber(){
            TextView number = (TextView) getActivity().findViewById(R.id.touch_number);
            int old = Integer.parseInt(number.getText().toString());
            number.setText(Integer.toString(old+1));
        }

        private void timeUp(Score score){
            db = new SQLiteHelper(getActivity());
            db.addScore(score);

            Intent intent = new Intent(getActivity(), GameOverActivity.class);
            intent.putExtra(GameOverActivity.PlaceholderFragment.SCORE, score.getScore());
            startActivity(intent);

            //Restart activity when new game starts
            getActivity().recreate();
        }

        public void startGame() {
            final int TIMER_TIME = 15000; //15 seconds
            running = true;

            //Set up timer
            final TextView timerView = (TextView) getActivity().findViewById(R.id.touch_timer);
            CountDownTimer timer = new CountDownTimer(TIMER_TIME, 100) {

                @Override
                public void onTick(long millisUntilFinished) {
                    timerView.setText((millisUntilFinished / 1000) + "." + ((millisUntilFinished / 100) % 10));
                }

                @Override
                public void onFinish() {
                    running = false;
                    //gets score from the textView
                    TextView number = (TextView) getActivity().findViewById(R.id.touch_number);
                    Score score = new Score(Integer.parseInt((String) number.getText()));

                    timerView.setText("Time's Up!");
                    timeUp(score);
                }
            };
            timer.start();

            RelativeLayout screen = (RelativeLayout) getActivity().findViewById(R.id.screen);
            screen.setOnTouchListener(new RelativeLayout.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m) {
                    if (running && (m.getActionMasked() == MotionEvent.ACTION_DOWN || m.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN)) {
                        addNumber();
                    }
                    return true;
                }
            });
        }

        public void numberVisibility(boolean visible){
            TextView number = (TextView) getActivity().findViewById(R.id.touch_number);
            if(visible){
                number.setVisibility(TextView.VISIBLE);
            } else {
                number.setVisibility(TextView.INVISIBLE);
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            numberVisibility(false);

            //Starts game when anything is clicked
            RelativeLayout screen = (RelativeLayout) getActivity().findViewById(R.id.screen);
            screen.setOnClickListener(new RelativeLayout.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView introText = (TextView) getActivity().findViewById(R.id.touch_intro);
                    introText.setVisibility(TextView.INVISIBLE);
                    numberVisibility(true);

                    startGame();
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_touch, container, false);
            return rootView;
        }
    }
}
