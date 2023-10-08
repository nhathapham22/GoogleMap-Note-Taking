package com.example.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JournalRecordAdapter extends RecyclerView.Adapter<JournalRecordAdapter.ViewHolder> {
    private ArrayList<JournalRecord> journalRecords;

    /**
     * Sets the dataset for the adapter with a new ArrayList of `JournalRecord` objects.
     * This method is used to update the data displayed in the RecyclerView.
     */
    public void setJournalRecords(ArrayList<JournalRecord> journalRecords) {
        this.journalRecords = journalRecords;
        notifyDataSetChanged();
    }


    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item. This method inflates the layout for the item view and returns a new
     * instance of the `ViewHolder` class.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal_record, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * updates the contents of the `ViewHolder` to reflect the item at the given position.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JournalRecord journalRecord = journalRecords.get(position);

        holder.textName.setText(journalRecord.getLocationName());
        holder.textAddress.setText(journalRecord.getLocationAddress());
        holder.textLatLng.setText(journalRecord.getLatitude() + ", " + journalRecord.getLongitude());
        holder.textNotes.setText(journalRecord.getJournalNotes());
    }
    /**
     * Returns the total number of items in the dataset held by the adapter.
     */
    @Override
    public int getItemCount() {
        return journalRecords != null ? journalRecords.size() : 0;
    }

    /**
     * The ViewHolder class represents each item view in the RecyclerView. It holds
     * references to the views contained within the item layout.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public TextView textAddress;
        public TextView textLatLng;
        public TextView textNotes;

        /**
         * Constructs a new instance of the ViewHolder class.
         *
         * @param itemView The item view for the ViewHolder.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.journal_item_name);
            textAddress = itemView.findViewById(R.id.journal_item_address);
            textLatLng = itemView.findViewById(R.id.journal_item_lat_lng);
            textNotes = itemView.findViewById(R.id.journal_item_notes);
        }
    }
}
