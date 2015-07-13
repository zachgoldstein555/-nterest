package cs160.nterest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


public class CameraActivity extends Activity {

    private final static int PHOTO_CODE = 1;
    private final static int TWEET_CODE = 2;
    private Uri imageUri;
    private File photoLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        capturePicture();
    }

    private void capturePicture() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoLib = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
        File imageFile = new File(photoLib.getPath()
                + "\\" + "cs160excited"
                + Calendar.MONTH + "_"
                + Calendar.DATE + "_"
                + Calendar.YEAR + "_"
                + Calendar.HOUR_OF_DAY + "_"
                + Calendar.MINUTE + "_"
                + Calendar.SECOND + ".jpg");
        imageUri = Uri.fromFile(imageFile);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Log.i("Cam Check:", "Start Activity");
        startActivityForResult(captureIntent, PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Cam Check:", "Activity Result");
        if (requestCode == PHOTO_CODE) {
            if (resultCode == RESULT_OK) {
                TweetComposer.Builder tweetComp = new TweetComposer.Builder(this)
                        .text("Wooooo! I'm #cs160excited about this\nTaken with the !nterest App")
                        .image(imageUri);
                //tweetComp.show();
                Intent tweetCompIntent = tweetComp.createIntent();
                startActivityForResult(tweetCompIntent, TWEET_CODE);
            } else if (resultCode == RESULT_CANCELED) {
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            } else {
                Toast.makeText(getApplicationContext(), "We're sorry!\nYour pic didn't go through", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            }
        } else if (requestCode == TWEET_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Nice one!", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            } else if (resultCode == RESULT_CANCELED) {
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            } else {
                Toast.makeText(getApplicationContext(), "We're sorry!\nYour tweet didn't go through", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
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
