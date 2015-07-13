package cs160.nterest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;


public class DeviceCommunicationActivity extends Activity {

    private Node handheld;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_communication);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        setupApiClient();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        finish();
    }


    public void setupApiClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CapabilityApi.GetCapabilityResult commResult =
                        Wearable.CapabilityApi.getCapability(mGoogleApiClient,
                                "tweet_pic", CapabilityApi.FILTER_REACHABLE).await();

                if (commResult.getCapability().getNodes().size() > 0) {
                    handheld = commResult.getCapability().getNodes().iterator().next();
                }

                if (handheld != null) {
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, handheld.getId(),
                            "tweet_pic", null).await();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_communication, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
