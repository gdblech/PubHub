package me.lgbt.pubhub.Utils;

import android.content.Context;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;


/**
 * @author Linh Tran
 */
public class Keyboard {
        public static void hideKeyboard (View view){
            InputMethodManager inputManager = (InputMethodManager) view
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            IBinder binder = view.getWindowToken();
            inputManager.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }