package com.roque.tinder;

import android.app.Application;

/**
 * Created by Roque on 11/12/2017.
 */

public class GlobalesRinder extends Application {
    private int global_id;

    public int getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(int global_id) {
        this.global_id = global_id;
    }
}
