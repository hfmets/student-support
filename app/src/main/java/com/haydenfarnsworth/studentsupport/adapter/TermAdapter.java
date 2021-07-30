package com.haydenfarnsworth.studentsupport.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Term;
import com.haydenfarnsworth.studentsupport.fragment.TermWithCoursesFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private List<Term> data;
    AppRepository appRepository;

    public static class TermViewHolder extends RecyclerView.ViewHolder {
        public CardView termCardView;
        public TermViewHolder(View view) {
            super(view);
            termCardView = view.findViewById(R.id.card_view_term);
        }
    }

    public void setTerms(List<Term> terms) {
        data = terms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        appRepository = new AppRepository(((FragmentActivity)parent.getContext()).getApplication());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_term, parent, false);
        TermViewHolder vh = new TermViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        DateFormat df = new SimpleDateFormat("M/d/yyyy");

        TextView tName = (TextView) holder.termCardView.findViewById(R.id.card_view_term_name);
        TextView tDates = (TextView) holder.termCardView.findViewById(R.id.card_view_term_dates);
        tName.setText(data.get(position).term_name);
        tDates.setText(df.format(data.get(position).term_start) + " - " + df.format(data.get(position).term_end));
        holder.termCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new TermWithCoursesFragment(data.get(position))).addToBackStack(null).commit();
            }
        });

        holder.termCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.confirm_delete_dialog);
                dialog.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (appRepository.getNumberOfCoursesInTerm(data.get(position).term_id) == 0) {
                                appRepository.deleteTerm(data.get(position).term_id);
                                dialog.dismiss();
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.show();
                return false;
            }
        });
        Log.d(TAG, "onBindViewHolder: " + data.get(position).term_name);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }




}
