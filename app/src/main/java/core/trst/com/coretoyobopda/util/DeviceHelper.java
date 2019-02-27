package core.trst.com.coretoyobopda.util;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

public class DeviceHelper{
    @SuppressLint("MissingPermission")
    public void vibrateDevice(int timeVibrate, AppCompatActivity apps){
        Vibrator v = (Vibrator) apps.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(timeVibrate, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(timeVibrate);
        }
    }
}
