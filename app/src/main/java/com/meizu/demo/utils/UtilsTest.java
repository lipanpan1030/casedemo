package com.meizu.demo.utils;

import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipan on 16-6-6.
 */
public class UtilsTest {
    private static final String TAG = "UtilsTest";
    private List<String> mArmyWhiteList = new ArrayList<>();

    public boolean needBlockCallForGreenArmy(String number) {
        mArmyWhiteList.add("18575605124");
        mArmyWhiteList.add("+18575606124");
        boolean needBlock = false;
        number = PhoneNumberUtils.stripSeparators(number);
        Log.d(TAG, number);
        String num = "+8618575606124";
        if (mArmyWhiteList.contains(num)) {
            Log.d(TAG, "true");
        }
        return needBlock;
    }
}
