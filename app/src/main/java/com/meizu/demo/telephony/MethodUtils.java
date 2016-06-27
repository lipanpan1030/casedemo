package com.meizu.demo.telephony;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by lipan on 16-6-21.
 */
public class MethodUtils {
    private static MethodUtils sMethodUtils;

    public static MethodUtils getInstance() {
        if (sMethodUtils == null) {
            sMethodUtils = new MethodUtils();
        }
        return sMethodUtils;
    }

    public String getPhoneNumber(Context context) {
        String phoneNumber = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm != null){
            boolean isSim1Ready = (tm.getSimState() == TelephonyManager.SIM_STATE_READY);
            if (isSim1Ready) {
                phoneNumber = tm.getLine1Number();
                return phoneNumber;
            }
        }
        Log.d("lipan", phoneNumber);
        return phoneNumber;
    }


}
