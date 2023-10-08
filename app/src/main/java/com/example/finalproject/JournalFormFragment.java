package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class JournalFormFragment extends Fragment {


    // var declaration
    private EditText editLocationName;
    private EditText editLocationAddress;
    private EditText editLatitude;
    private EditText editLongitude;
    private EditText editNotes;
    private Button btnSave;

    private DataManager db;

    //
    // may want to switch to binding later for consistency with MapFragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflating the layout for the journal form fragment
        View view = inflater.inflate(R.layout.fragment_journal_form, container, false);
        // Initializing and linking UI elements
        editLocationName = view.findViewById(R.id.edit_location_name);
        editLocationAddress = view.findViewById(R.id.edit_location_address);
        editLatitude = view.findViewById(R.id.edit_latitude);
        editLongitude = view.findViewById(R.id.edit_longitude);
        editNotes = view.findViewById(R.id.edit_notes);
        btnSave = view.findViewById(R.id.btn_save);
        // Creating a new instance of the DataManager to manage data
        db = new DataManager(getActivity().getApplicationContext());

        // Set onClickListener for the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle save button click event
                JournalRecord journalRecord = new JournalRecord(0,
                        editLocationName.getText().toString(),
                        editLocationAddress.getText().toString(),
                        Double.parseDouble(String.valueOf(editLatitude.getText())),
                        Double.parseDouble(String.valueOf(editLongitude.getText())),
                        editNotes.getText().toString()
                        );
                db.insert(journalRecord);
                Toast.makeText(getContext(), "added new journal record, switch view to see", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    // this is used by MapFragment to updateData whenever marker change location
    public void updateData(Double latitude, Double longitude, String locationAddress) {
        // Update the corresponding EditText fields with the received data
        editLatitude.setText(String.valueOf(latitude));
        editLongitude.setText(String.valueOf(longitude));
        editLocationAddress.setText(locationAddress);
    }

}
