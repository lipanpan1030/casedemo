package com.meizu.demo.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lipan on 16-5-20.
 */
public class IntroducePreference extends Preference {

    private Context mContext;

    public IntroducePreference(Context context) {
        super(context);
        this.mContext = context;
    }

    public IntroducePreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public IntroducePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }


    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);
    }


}
