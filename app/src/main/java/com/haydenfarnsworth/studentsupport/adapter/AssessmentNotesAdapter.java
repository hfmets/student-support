package com.haydenfarnsworth.studentsupport.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.List;

public class AssessmentNotesAdapter extends RecyclerView.Adapter<AssessmentNotesAdapter.NotesViewHolder> {
    private List<String> data;
    private Context context;
    private int assessmentId;

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        CardView noteCardView;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            noteCardView = itemView.findViewById(R.id.card_view_note);
        }
    }

    public AssessmentNotesAdapter(Context context, int assessmentId) {
        this.context = context;
        this.assessmentId = assessmentId;
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
        AppRepository appRepository = new AppRepository(((Activity) context).getApplication());
        holder.noteCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.new_note_dialog);

                EditText noteText = dialog.findViewById(R.id.new_note_text);
                noteText.setText(data.get(position));
                dialog.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!noteText.getText().toString().isEmpty()) {
                            data.set(position, noteText.getText().toString());
                            ((TextView) holder.noteCardView.findViewById(R.id.note_text)).setText(data.get(position));
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        holder.noteCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, data.get(position));

                context.startActivity(Intent.createChooser(intent, "Choose an email app."));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}
