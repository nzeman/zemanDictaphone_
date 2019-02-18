package zeman.diktafonmev.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import zeman.diktafonmev.DividerItemDecoration;
import zeman.diktafonmev.Item;
import zeman.diktafonmev.R;
import zeman.diktafonmev.RecycleViewAdapter;

public class RecyclerMainActivity extends AppCompatActivity implements RecycleViewAdapter.OnItemClickListener {

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    //private Spinner spinner;


    boolean kontrola = false;
    boolean isFiltered = false;
    boolean reproducira = false;


    Activity activity;
    TextView tvname;

    SeekBar sbmusic;
    Button btnplaypause;
    Button btnrewind, btnforward;
    TextView tvctime, tvttime;
    int position = 0;

    MediaPlayer mediaPlayer;
    double timeElapsed = 0, finalTime = 0;
    int forwardTime = 2000, backwardTime = 2000;
    Handler durationHandler = new Handler();

    Boolean isPlayClicked = false;

    ArrayList<String> list;


    private List<Item> items;


    @Override
    protected void onStop() {
        super.onStop();
//        onSupportNavigateUp();

    }

    @Override
    protected void onResume(){
        super.onResume();
        kontrola = true;
        //mediaPlayer.reset();

    }

    @Override
    protected void onPause(){
        super.onPause();

        try{
            if(mediaPlayer !=null && mediaPlayer.isPlaying()){
                Log.d("TAG------->", "player is running");
                mediaPlayer.stop();
                Log.d("Tag------->", "player is stopped");
                mediaPlayer.release();
                Log.d("TAG------->", "player is released");
            }
        }catch(Exception e){
        }


/*
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer.reset();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        durationHandler.removeCallbacks(updateSeekBarTime);

/*
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {

            reproducira = true;
        }
        else{

            reproducira = false;
        }


        if(reproducira == true) {
            durationHandler.removeCallbacks(updateSeekBarTime);

        }else{
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }

*/
    }




    @Override
    protected void onRestart() {
        super.onRestart();
        mRecycleView.setAdapter(null);
        items = Item.createItemList(10);
        mAdapter = new RecycleViewAdapter(getApplicationContext(), items, RecyclerMainActivity.this);
        // Setup divider item decoration
        mRecycleView.addItemDecoration(new DividerItemDecoration(RecyclerMainActivity.this, LinearLayoutManager.VERTICAL));
        mRecycleView.setAdapter(mAdapter);
        // Auto-Scroll to a specific position
        mLayoutManager.smoothScrollToPosition(mRecycleView, null, 20);    // Set auto-scroll to that position
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();



      /*
       if (isFiltered == false) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();

            }
        } else {

            mRecycleView.setAdapter(null);
            items = Item.createItemList(10);
            mAdapter = new RecycleViewAdapter(getApplicationContext(), items, RecyclerMainActivity.this);
            // Setup divider item decoration
            mRecycleView.addItemDecoration(new DividerItemDecoration(RecyclerMainActivity.this, LinearLayoutManager.VERTICAL));
            mRecycleView.setAdapter(mAdapter);
            // Auto-Scroll to a specific position
            mLayoutManager.smoothScrollToPosition(mRecycleView, null, 20);    // Set auto-scroll to that position


        }*/
    }


    // strelica za nazad
    @Override
    public boolean onSupportNavigateUp() {

        if (isFiltered == false) {
            onBackPressed();
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                //mediaPlayer.release();


                //stop();

            }
        } else {

            mRecycleView.setAdapter(null);
            items = Item.createItemList(10);
            mAdapter = new RecycleViewAdapter(getApplicationContext(), items, RecyclerMainActivity.this);
            // Setup divider item decoration
            mRecycleView.addItemDecoration(new DividerItemDecoration(RecyclerMainActivity.this, LinearLayoutManager.VERTICAL));
            mRecycleView.setAdapter(mAdapter);
            // Auto-Scroll to a specific position
            mLayoutManager.smoothScrollToPosition(mRecycleView, null, 20);    // Set auto-scroll to that position
            isFiltered = false;


        }

        return true;

    }


    private void init() {
        activity = (Activity) RecyclerMainActivity.this;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });

        tvname = (TextView) findViewById(R.id.tvname);
        sbmusic = (SeekBar) findViewById(R.id.sbmusic);
        btnplaypause = (Button) findViewById(R.id.btnplaypause);
        //btnprevious = (Button) findViewById(R.id.btnprevious);
        //btnnext = (Button) findViewById(R.id.btnnext);
        btnrewind = (Button) findViewById(R.id.btnrewind);
        btnforward = (Button) findViewById(R.id.btnforward);
        tvctime = (TextView) findViewById(R.id.tvctime);
        tvttime = (TextView) findViewById(R.id.tvttime);


        btnplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();


            }
        });



        btnrewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewind();


            }
        });


        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forward();

            }
        });


        sbmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo((int) progress);
                }
            }
        });

        //fillList();
    }


    private void fillList(String pathDatoteke) {

        list = new ArrayList<String>();

        list.clear();
        String url = pathDatoteke;
        list.add(url);


        tvname.setText(list.get(position));
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {

            //get current position

try {
    if (mediaPlayer.isPlaying()) {
        timeElapsed = mediaPlayer.getCurrentPosition();
        reproducira = true;
    } else {

        reproducira = false;
    }
}catch(Exception e){

}
            //set seekbar progress
            if(reproducira == true) {
                sbmusic.setProgress((int) timeElapsed);

                //set time remaining
            /*double timeRemaining = finalTime - timeElapsed;
			tvctime.setText(String.format("%02d : %02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));*/

                tvctime.setText(String.format("%02d : %02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed),
                        TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));

                //repeat yourself that again in 100 miliseconds
                durationHandler.postDelayed(this, 100);
            }else{
                    durationHandler.removeCallbacks(this);
            }

        }
    };

    //moje

    private void play() {
        //mediaPlayer.stop();

        if (mediaPlayer != null && kontrola == true) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(list.get(position));
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }




        }


        // update view's values
        tvname.setText(list.get(position));

        //isPlayClicked = true;
        btnplaypause.setBackgroundResource(R.drawable.pause);
if (kontrola == false) {
    try {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(list.get(position));
        mediaPlayer.prepare();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
        finalTime = mediaPlayer.getDuration();
        sbmusic.setMax((int) finalTime);

        tvttime.setText(String.format("%02d : %02d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

        mediaPlayer.start();
        timeElapsed = mediaPlayer.getCurrentPosition();
        sbmusic.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);

    }


    private void playPause() {

        if (isPlayClicked) {
            isPlayClicked = false;
            btnplaypause.setBackgroundResource(R.drawable.play0);

            mediaPlayer.pause();

        } else {
            isPlayClicked = true;
            btnplaypause.setBackgroundResource(R.drawable.pause);

            try {
                mediaPlayer.setDataSource(list.get(position));
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finalTime = mediaPlayer.getDuration();
            sbmusic.setMax((int) finalTime);

            tvttime.setText(String.format("%02d : %02d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

            mediaPlayer.start();
            timeElapsed = mediaPlayer.getCurrentPosition();
            sbmusic.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }
    }

    public void rewind() {
        if ((timeElapsed - backwardTime) > 0) {
            timeElapsed = timeElapsed - backwardTime;

            mediaPlayer.seekTo((int) timeElapsed);
        }
    }

    public void forward() {
        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed = timeElapsed + forwardTime;

            mediaPlayer.seekTo((int) timeElapsed);
        }
    }

    private void previous() {
        mediaPlayer.stop();
        position -= 1;

        if (position < 0) {
            position = 0;

            finalTime = 0;
            sbmusic.setMax((int) finalTime);

            timeElapsed = 0;
            sbmusic.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateSeekBarTime, 100);

        } else {

            // update view's values
            tvname.setText(list.get(position));

            isPlayClicked = true;
            btnplaypause.setBackgroundResource(R.drawable.pause);

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(list.get(position));
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finalTime = mediaPlayer.getDuration();
            sbmusic.setMax((int) finalTime);

            tvttime.setText(String.format("%02d : %02d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

            mediaPlayer.start();
            timeElapsed = mediaPlayer.getCurrentPosition();
            sbmusic.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }
    }

    private void next() {
        mediaPlayer.stop();
        position += 1;

        if (position >= list.size()) {
            position = (list.size() - 1);

            finalTime = 0;
            sbmusic.setMax((int) finalTime);

            timeElapsed = 0;
            sbmusic.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateSeekBarTime, 100);

        } else {

            // update view's values
            tvname.setText(list.get(position));

            isPlayClicked = true;
            btnplaypause.setBackgroundResource(R.drawable.pause);

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(list.get(position));
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finalTime = mediaPlayer.getDuration();
            sbmusic.setMax((int) finalTime);

            tvttime.setText(String.format("%02d : %02d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

            mediaPlayer.start();
            timeElapsed = mediaPlayer.getCurrentPosition();
            sbmusic.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }
    }

    private void stop() {

        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        finalTime = 0;
        sbmusic.setMax((int) finalTime);

        timeElapsed = 0;
        sbmusic.setProgress((int) timeElapsed);
        //durationHandler.postDelayed(updateSeekBarTime, 100);


    }


    //ENDREGION


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main_activity);


        init();


        //  za toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        //spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.animators, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);

        items = Item.createItemList(10);
        //items = Item.createItemListSearch(10, "uu");

        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);
        mRecycleView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new RecycleViewAdapter(this, items, this);
        // Setup divider item decoration
        mRecycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecycleView.setAdapter(mAdapter);

        // Auto-Scroll to a specific position
        mLayoutManager.smoothScrollToPosition(mRecycleView, null, 20);    // Set auto-scroll to that position

        isFiltered = false;
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(view.getContext(), "You got a call from ELEMENT at position "+position+"\n"+items.get(position).getName(), Toast.LENGTH_SHORT).show();
        String path = items.get(position).getFilePath();


        //File file = new File(path);
        //String test = Environment.getExternalStorageDirectory() + "/ZemanRecording/" + path;
        Intent i = new Intent(RecyclerMainActivity.this, SongDetailsActivity.class);
        i.putExtra("path", path);


        startActivity(i);

        stop();

        //playPause();



    }

    @Override
    public void onButtonClick(View view, int position) {
        String path = items.get(position).getFilePath();
        fillList(path);
        play();
    }

    @Override
    public void onLongClick(View view, int position) {
        String path = items.get(position).getFilePath();
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(path);

        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("audio/mpeg");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, items.get(position).getName());
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Slanje datoteke...");

            startActivity(Intent.createChooser(intentShareFile, "Slanje zapisa"));
        }
    }

    //region MENU OPTIONS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popis, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {


            // get prompts.xml view
            LayoutInflater layoutInflater = LayoutInflater.from(RecyclerMainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.input_dialog_search, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecyclerMainActivity.this);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(R.id.edittext);

            editText.setText("");
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mRecycleView.setAdapter(null);
                            items = Item.createItemListSearch(10, editText.getText().toString());
                            mAdapter = new RecycleViewAdapter(getApplicationContext(), items, RecyclerMainActivity.this);
                            // Setup divider item decoration
                            mRecycleView.addItemDecoration(new DividerItemDecoration(RecyclerMainActivity.this, LinearLayoutManager.VERTICAL));
                            mRecycleView.setAdapter(mAdapter);
                            // Auto-Scroll to a specific position
                            mLayoutManager.smoothScrollToPosition(mRecycleView, null, 20);    // Set auto-scroll to that position
                            // treba ako želimo da se nakon pritiska back vrati popis svih zapisa
                            isFiltered = true;


                        }
                    })
                    .setNegativeButton("Odustani",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //dialog.cancel();
                                    dialog.dismiss();
                                }
                            });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



//endregion






}
