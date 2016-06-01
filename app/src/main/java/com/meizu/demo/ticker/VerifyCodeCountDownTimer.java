package com.meizu.demo.ticker;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by lipan on 16-5-20.
 */
public class VerifyCodeCountDownTimer extends CountDownTimer {
    private TextView tv;

    public VerifyCodeCountDownTimer(long millisInFuture, long countDownInterval, TextView tv) {
        super(millisInFuture, countDownInterval);
        this.tv = tv;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        tv.setText(millisUntilFinished / 1000 + " " + "秒");
    }

    @Override
    public void onFinish() {
        tv.setEnabled(true);
        tv.setText("获取验证码");
    }
}
