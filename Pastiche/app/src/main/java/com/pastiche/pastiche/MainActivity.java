package com.pastiche.pastiche;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.pastiche.pastiche.register.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final String ACTIVITY_TAG = "MainActivity";
    private static final float DISASBLE_ALPHA = (float) 0.4;
    private static final float ENABLE_ALPHA = 1;
    private Toolbar main_toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //make Navigation bar transparent with bg color
        //set status bar color
        if ( Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.windowBackground));
        }

        //set up the app bar
        main_toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(main_toolbar);
        TextView app_name = (TextView) findViewById(R.id.toolbar_app_name);

        //set up app name font
        Typeface title_font = Typeface.createFromAsset(getAssets(), "fonts/GreatVibes-Regular.ttf");
        app_name.setTypeface(title_font);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //authentication
        String id = new String();
        if(!registered(id)) {
            startAuthentications();
        } else {
            Log.d(ACTIVITY_TAG, "Not logged_in");
        }

    }

    /**
     * Check private storage for user info
     * if nothing found, user is not registered
     * @return true if user already logged in, false otherwise
     */
    private boolean registered(String _id) {
        SharedPreferences pref = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        String id = pref.getString("id", "");
        boolean logged_in = pref.getBoolean("logged_in", false);

        Log.d(ACTIVITY_TAG, "id: " + id + " is logged in? " + logged_in);
        Toast.makeText(this, "id: " + id + " is logged in? " + logged_in, Toast.LENGTH_LONG).show();

        _id = id;
        return logged_in;
    }

    /**        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

     * Disables the button and sets the disable transparency
     * @param button
     */
    public static void disableButton(Button button){
        button.setEnabled(false);
        button.setAlpha(DISASBLE_ALPHA);
    }

    /**
     * Enables the button and sets the enable transparency
     * @param button
     */
    public static void enableButton(Button button){
        button.setEnabled(true);
        button.setAlpha(ENABLE_ALPHA);
    }

    //    //TODO need to add SplashActivity and move this method to it
    private void startAuthentications() {

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the option main_menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noInspection SimplifiableIfStatement
        if ( id == R.id.action_settings ){
            String _id = new String();
            registered(_id);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
