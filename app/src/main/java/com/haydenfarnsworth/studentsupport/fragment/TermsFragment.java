package com.haydenfarnsworth.studentsupport.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.ViewModel.TermViewModel;
import com.haydenfarnsworth.studentsupport.adapter.TermAdapter;
import com.haydenfarnsworth.studentsupport.entity.Term;

import java.util.List;


public class TermsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TermViewModel termViewModel;

    public TermsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_terms, container, false);

        Toolbar topToolbar = (Toolbar) root.findViewById(R.id.top_toolbar);
        topToolbar.setTitle("Terms");

        root.findViewById(R.id.floatingActionButton).setOnClickListener((View view) -> {
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewTermFragment()).addToBackStack(null).commit();
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.terms_recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = new ViewModelProvider(requireActivity()).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(getViewLifecycleOwner(), new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                adapter.setTerms(terms);
            }
        });

        return root;
    }
}

