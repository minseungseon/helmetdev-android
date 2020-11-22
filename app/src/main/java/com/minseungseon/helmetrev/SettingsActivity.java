package com.minseungseon.helmetrev;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();


        Toolbar toolbar2 = (Toolbar)findViewById(R.id.toolbar2);
        toolbar2.setTitle("설정");
        setSupportActionBar(toolbar2);
        ActionBar actionBar2 = getSupportActionBar();
        if (actionBar2 != null) {
            actionBar2.setDisplayShowCustomEnabled(true);
            actionBar2.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                Intent intent = new Intent( SettingsActivity.this, HomeActivity.class );
                startActivity( intent );

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}