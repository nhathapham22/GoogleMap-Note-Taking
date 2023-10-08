package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.finalproject.databinding.ActivityMapsBinding;

// Maps Activity, containing 2 fragments
// JournalFormFragment - the form for location editing and
// MapFragment - the google map container with marker feature
public class MapsActivity extends AppCompatActivity {
    // declaration
    private JournalFormFragment journalFormFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // loading and displaying the fragments within the container view
        journalFormFragment = new JournalFormFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.top_fragment_container, journalFormFragment)
                .commit();
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map_fragment_container, mapFragment)
                .commit();

    }

    // menu management, 2 items - quit and view journal records
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_maps_view_journal_records:
                Intent intent = new Intent(this, JournalRecordsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_maps_quit:
                finishAffinity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public JournalFormFragment getJournalFormFragment() {
        return journalFormFragment;
    }
}