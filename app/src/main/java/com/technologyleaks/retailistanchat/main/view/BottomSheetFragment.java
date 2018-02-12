package com.technologyleaks.retailistanchat.main.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.main.helpers.ActivityFragmentContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    /* Callback */
    private ActivityFragmentContract.FragmentToActivity fragmentToActivity;

    /* Views */
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.textView_title)
    protected TextView textView_title;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            fragmentToActivity = (ActivityFragmentContract.FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onPromotionsPricingFragmentInitializedListener");
        }
    }

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_online_users, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentToActivity.populateRecyclerView(recyclerView, this);

    }


    public void updateTitle(String title) {
        if (textView_title != null) {
            textView_title.setText(title);
        }
    }

}
