package com.sand_corporation.www.uthao.CustomPlaceAutoComplete;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by HP on 12/28/2017.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
        //We have returned false from here to solve this below problem
        //Exception java.lang.IndexOutOfBoundsException: Inconsistency detected.
        //Invalid item position 3(offset:3).state:5
    }

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
