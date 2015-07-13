package cs160.nterest;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class AccelMonitorService extends Service {

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private final float accelThresh = 2.7f;
    private long lastNotif;
    private long timeDiff;
    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            timeDiff = System.currentTimeMillis() - lastNotif;

            if (mAccel > accelThresh && timeDiff > 5000) {
                signalPhone();
                lastNotif = timeDiff + lastNotif;
                timeDiff = 0;
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        lastNotif = 0;

        return START_STICKY;
    }

    public void signalPhone() {
        int notificationId = 001;

        Intent appIntent = new Intent(this, MainWearActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(this, 0, appIntent, 0);

        Intent commIntent = new Intent(this, DeviceCommunicationActivity.class);
        PendingIntent commPendingIntent = PendingIntent.getActivity(this, 0, commIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.card_background)
                .setContentTitle("Woah!")
                .setContentText("Something has sparked your\n!nterest")
                .setContentIntent(appPendingIntent)
                .setAutoCancel(true)
                .addAction(new NotificationCompat.Action.Builder(
                        R.drawable.cam,
                        "Snap a pic!",
                        commPendingIntent).build());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, notificationBuilder.build());
        Toast.makeText(getApplicationContext(), "Hey Look!\nThere's something \n!nteresting in your notifications", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}