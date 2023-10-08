package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.ActivityJournalRecordsBinding;

import java.util.ArrayList;


// showing records, pretty straight forward - using recyclerView
public class JournalRecordsActivity extends AppCompatActivity {
    // declaration
    private DataManager db;
    private RecyclerView recyclerView;
    private JournalRecordAdapter journalRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityJournalRecordsBinding binding = ActivityJournalRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DataManager(getApplicationContext());

        recyclerView = findViewById(R.id.journal_records_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        journalRecordAdapter = new JournalRecordAdapter();


        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(journalRecordAdapter);

        // Fetch records from the SQLite database
        ArrayList<JournalRecord> records = db.listAllRecords();

        // Update the adapter with the retrieved records
        journalRecordAdapter.setJournalRecords(records);
    }

    // menu management, 3 items - quit, delete all records and go back to adding new records
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_journal_records, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        //  case for option item selected
        switch (itemId) {
            case R.id.menu_journal_records_view_enter_record:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_journal_records_delete_records:
                db.clearAllRecords();
                Toast.makeText(getBaseContext(), "records cleared, refresh to see changes", Toast.LENGTH_SHORT).show();
                // this should refresh the view to show all records have been deleted, not sure why not working
                journalRecordAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_journal_records_quit:
                finishAffinity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
