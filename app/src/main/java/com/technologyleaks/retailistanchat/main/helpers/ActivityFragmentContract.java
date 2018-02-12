package com.technologyleaks.retailistanchat.main.helpers;

import android.arch.lifecycle.LifecycleOwner;
import android.support.v7.widget.RecyclerView;

/**
 * Created by zainulabideen on 11/02/2018.
 */

public interface ActivityFragmentContract {

    /* Fragment to Activity */

    interface FragmentToActivity {
        void populateRecyclerView(RecyclerView recyclerView, LifecycleOwner lifecycleOwner);
    }

}
