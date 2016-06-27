package com.meizu.demo.resources;

import android.content.Context;
import android.util.Log;

import com.meizu.demo.R;

/**
 * Created by lipan on 16-6-21.
 */
public class ResourceUtils {

    public static int testDp(Context context) {
        int mRippleDiameter = context.getResources()
                .getDimensionPixelOffset(R.dimen.mz_dialpad_key_button_ripple_diameter);
        Log.d("lipan", "size:"+mRippleDiameter);
        return mRippleDiameter;
    }
}
