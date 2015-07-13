package cs160.nterest;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class ReceiveMessageService extends WearableListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private static final String RECEIVER_SERVICE_PATH = "tweet_pic_too";

    public void onMessageReceived(MessageEvent messageEvent) {
            Intent camIntent = new Intent(getApplicationContext(), CameraActivity.class);
            camIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(camIntent);
    }
}
