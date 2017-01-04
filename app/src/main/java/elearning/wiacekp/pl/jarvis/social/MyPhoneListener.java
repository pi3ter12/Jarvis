package elearning.wiacekp.pl.jarvis.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MyPhoneListener extends PhoneStateListener {
    private Context ctx;
    private Activity act;

    public MyPhoneListener(Context ctx, Activity act){
        this.ctx = ctx;
        this.act = act;
    }
    private boolean onCall = false;

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                onCall = true;
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                if (onCall == true) {
                    Intent restart = act.getBaseContext().getPackageManager().
                            getLaunchIntentForPackage(act.getBaseContext().getPackageName());
                    restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(restart);

                    onCall = false;
                }
                break;
            default:
                break;
        }

    }
}

