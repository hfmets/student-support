package com.haydenfarnsworth.studentsupport.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.activity.EditAssessmentActivity;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Assessment;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<String> data;

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        CardView noteCardView;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            noteCardView = itemView.findViewById(R.id.card_view_note);
        }
    }

    public void setNotes(List<String> notes) {
        data = notes;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_note, parent, false);
        NotesViewHolder vh = new NotesViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        ((TextView) holder.noteCardView.findViewById(R.id.note_text)).setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}
