package legut.djplayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends AppCompatActivity implements SensorEventListener {
    @BindView(R.id.album_image) ImageView albumImage;
    @BindView(R.id.song_title_and_band) TextView songTitleAndBand;
    @BindView(R.id.rewind_back) ImageButton rewindBack;
    @BindView(R.id.start_or_pause) ImageButton startOrPause;
    @BindView(R.id.rewind_forward) ImageButton rewindForward;

    Song song;
    MediaPlayer mediaPlayer;
    MediaPlayer samplesPlayer;
    int startDrawableId;
    int pauseDrawableId;
    SeekBar seekBar;
    Handler handler;
    Runnable runnable;
    int soundId;
    Menu myMenu;
    Switch menuSwitch;
    SensorManager sensorManager;
    Sensor accelerometer;
    long lastUpdate = 0;
    float last_x, last_y, last_z;
    static final int SHAKE_THRESHOLD = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        startDrawableId = getResources().getIdentifier("@android:drawable/ic_media_play", null, null);
        pauseDrawableId = getResources().getIdentifier("@android:drawable/ic_media_pause", null, null);
        song = MainActivity.songsList.get(getIntent().getIntExtra("position", 0));
        albumImage.setImageResource(song.getAlbumImage());
        songTitleAndBand.setText(song.getSongTitle()+System.lineSeparator()+song.getBand());
        mediaPlayer = MediaPlayer.create(getBaseContext(), song.getSong());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.seekTo(0);
                seekBar.setProgress(0);
                startOrPause.setImageResource(startDrawableId);
            }
        });
        handler = new Handler();
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        };
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    if(samplesPlayer!=null) samplesPlayer.start();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu=menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activate, menu);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.activate_menu);
        menuSwitch = (Switch) item.getActionView().findViewById(R.id.switch_item);
        menuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (soundId != 0) {
                        myMenu.findItem(R.id.main_menu).setEnabled(false);
                        samplesPlayer = MediaPlayer.create(getBaseContext(), soundId);
                        samplesPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                samplesPlayer.seekTo(0);
                            }
                        });
                        mediaPlayer.setVolume(0.5f, 0.5f);
                    }
                    else {
                        Toast.makeText(PlayerActivity.this , "Choose sound first!", Toast.LENGTH_LONG).show();
                        menuSwitch.setChecked(false);
                    }
                }
                else {
                    samplesPlayer.reset();
                    soundId = 0;
                    myMenu.findItem(R.id.main_menu).setEnabled(true);
                    myMenu.findItem(R.id.main_menu).setTitle("Choose Sound");
                    mediaPlayer.setVolume(1f, 1f);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drum_1:
                soundId = getResources().getIdentifier("drum_1", "raw", this.getPackageName());
                myMenu.findItem(R.id.main_menu).setTitle("Drum 1");
                return true;
            case R.id.drum_2:
                soundId = getResources().getIdentifier("drum_2", "raw", this.getPackageName());
                myMenu.findItem(R.id.main_menu).setTitle("Drum 2");
                return true;
            case R.id.scratch_1:
                soundId = getResources().getIdentifier("scratch_1", "raw", this.getPackageName());
                myMenu.findItem(R.id.main_menu).setTitle("Scratch 1");
                return true;
            case R.id.scratch_2:
                soundId = getResources().getIdentifier("scratch_2", "raw", this.getPackageName());
                myMenu.findItem(R.id.main_menu).setTitle("Scratch 2");
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.release();
        if(samplesPlayer!=null) samplesPlayer.release();
        handler.removeCallbacks(runnable);
        sensorManager.unregisterListener(this);
        super.onBackPressed();
    }

    @OnClick(R.id.rewind_back)
    public void rewindBack() {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
    }

    @OnClick(R.id.start_or_pause)
    public void startOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            startOrPause.setImageResource(startDrawableId);
        }
        else {
            mediaPlayer.start();
            startOrPause.setImageResource(pauseDrawableId);
        }
    }

    @OnClick(R.id.rewind_forward)
    public void rewindForward() {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
    }
}