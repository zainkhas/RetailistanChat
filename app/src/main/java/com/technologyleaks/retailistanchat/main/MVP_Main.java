package com.technologyleaks.retailistanchat.main;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Holder interface that contains all interfaces
 * responsible to maintain communication between
 * Model View Presenter layers.
 * Each layer implements its respective interface:
 * View implements RequiredViewOps
 * Presenter implements ProvidedPresenterOps, RequiredPresenterOps
 * Model implements ProvidedModelOps
 */
public interface MVP_Main {
    /**
     * Required View methods available to Presenter.
     * A passive layer, responsible to show data
     * and receive user interactions
     * Presenter to View
     */
    interface PresenterToView {
        Context getAppContext();

        Context getActivityContext();

        void showToast(Toast toast);

        void clearMessage();


    }

    /**
     * Operations offered to View to communicate with Presenter.
     * Process user interaction, sends data requests to Model, etc.
     * View to Presenter
     */
    interface ViewToPresenter {
        void onDestroy(boolean isChangingConfiguration);

        void setView(PresenterToView view);

        void checkLogin();

        void onButtonSendClicked(EditText editText_message);

        void populateRecyclerView(RecyclerView recyclerView, LifecycleOwner lifecycleOwner);

        void onMenuLogoutSelected();

        void takeOnline();

        void takeOffline();

    }

    /**
     * Required Presenter methods available to Model.
     * Model to Presenter
     */
    interface ModelToPresenter {

        Context getAppContext();

        Context getActivityContext();

        void onMessageSend();
    }

    /**
     * Operations offered to Model to communicate with Presenter
     * Handles all data business logic.
     * Presenter to Model
     */
    interface PresenterToModel {

        void onDestroy(boolean isChangingConfiguration);

        void sendMessage(String message);

        void takeOnline();

        void takeOffline();

    }
}
