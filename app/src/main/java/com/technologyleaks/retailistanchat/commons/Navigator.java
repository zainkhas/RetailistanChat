package com.technologyleaks.retailistanchat.commons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.technologyleaks.retailistanchat.login.view.LoginActivity;
import com.technologyleaks.retailistanchat.main.view.MainActivity;


/**
 * Created by Zain on 06-Mar-17.
 */

public class Navigator {

    //Can be called from Activity as well as Presenter.
    public static void navigate(Context context, SCREEN screen) {

        Class activity = getActivityClass(screen);
        Intent i = new Intent(context, activity);
        context.startActivity(i);
    }

    //Can only be called from Activity, because it requires an "Activity" instance, and Presenter only has Context.
    public static void navigateWithoutAnimation(Context context, SCREEN screen, boolean newTask, Activity theActivity) {

        Class activity = getActivityClass(screen);

        Intent i = new Intent(context, activity);


        context.startActivity(i);
        theActivity.overridePendingTransition(0, 0);
        if (newTask) {
            theActivity.finish();
        }

    }

    public static void navigate(Context context, SCREEN screen, boolean newTask) {

        Class activity = getActivityClass(screen);

        Intent i = new Intent(context, activity);

        if (newTask) {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(i);
    }


    public static Class getActivityClass(SCREEN screen) {

        Class activity = null;
        switch (screen) {
            case MAIN:
                activity = MainActivity.class;
                break;
            case LOGIN:
                activity = LoginActivity.class;
                break;
        }

        return activity;
    }

    //Lis of Screens
    public enum SCREEN {
        MAIN,
        LOGIN
    }


//        try {
//            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            if (showAnimation) {
//                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
//            }
//            if (screen != SCREEN.MAIN) {
//                ft.addToBackStack(null);
//            }
//
//            ft.replace(R.id.frame_layout, (Fragment) fragment.newInstance());
//
//            ft.commit();
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


//    public static void navigate(Context context, SCREEN screen) {
//
//        Class fragment = null;
//        switch (screen) {
////            case MAIN:
////                fragment = MainFragment.class;
////                break;
////            case LOCATOR:
////                fragment = LocatorFragment.class;
////                break;
////            case PHONE_FINDER:
////                fragment = PhoneFinderFragment.class;
////                break;
////            case FLASH:
////                fragment = FlashFragment.class;
////                break;
////            case SMART_VOLUME:
////                fragment = SmartVolumeFragment.class;
////                break;
//        }
//
////        try {
////            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
////            FragmentTransaction ft = fragmentManager.beginTransaction();
////            if (showAnimation) {
////                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
////            }
////            if (screen != SCREEN.MAIN) {
////                ft.addToBackStack(null);
////            }
////
////            ft.replace(R.id.frame_layout, (Fragment) fragment.newInstance());
////
////            ft.commit();
////
////
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
//    }


}
