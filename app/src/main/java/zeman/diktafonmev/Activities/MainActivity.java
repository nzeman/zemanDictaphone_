package zeman.diktafonmev.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import android.media.AudioRecord;
import android.media.AudioFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import zeman.diktafonmev.Helpers.FreeSpaceOnSD;
import zeman.diktafonmev.Helpers.FolderCreation;
import zeman.diktafonmev.Helpers.ZemanAnimation;
import zeman.diktafonmev.R;

public class MainActivity extends AppCompatActivity {



    // DIO ZA PCM
    private static final int RECORDER_BPP = 16;
    //private static final int RECORDER_BPP = 24;
    //private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static int frequency = 22050;
    private static int channelConfiguration = android.media.AudioFormat.CHANNEL_IN_STEREO;
    private static int EncodingBitRate = android.media.AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord audioRecord = null;
    private int recBufSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    // END DIO ZA PCM



    public static int AudioEncodingBitrate = 128000;
    public static int AudioSamplingRate = 44100;
    public static int AudioOutputFormat = 2;
    public static int AudioEncoder = 2;
    public static int AudioFormat = 2;

    public static double slobodanProstorKontrola = 99.25;
    public static double slobodanProstorUpozorenje = 85.0;

    public String simpleDateFormat;
    public String savePath;


    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private TextView timerValue;
    private TextView freeSpace;
    private TextView outputFileTextView;
    private TextView fileNameTextView;
    private TextView audioEncodingBitrateTextView;
    private TextView audioSamplingRateTextView;
    private TextView filePath;
    private TextView trenutnePostavkeTv;

    private boolean recording = false;


    boolean checkBoxFilePath;
    boolean checkBoxEncoding;
    boolean checkBoxSampling;
    boolean checkBoxMB;
    boolean checkBoxRenameDialog;

    boolean snimamWAV = false;


    private Toolbar toolbar;


    @Override
    public void onStop() {
        super.onStop();
        RefreshajProgressBar();
        PostaviUIPremaPostavkama();
        LoadajTextViewZaB();
        PostaviPremaPostavkama();

    }

    @Override
    public void onStart() {
        super.onStart();
        RefreshajProgressBar();
        PostaviUIPremaPostavkama();
        LoadajTextViewZaB();
        PostaviPremaPostavkama();

    }


    @Override
    public void onResume() {
        super.onResume();
        // refresh
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        RefreshajProgressBar();
        PostaviPremaPostavkama();
        PostaviUIPremaPostavkama();
        LoadajTextViewZaB();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        RefreshajProgressBar();
        PostaviUIPremaPostavkama();
        LoadajTextViewZaB();
        PostaviPremaPostavkama();

    }


    public float VratiPostotakZauzetogProstora() {

        float a = FreeSpaceOnSD.getSizeInMB(); //11781
        float b = FreeSpaceOnSD.getAvailableSpaceInMB(); //2258
        float c = b / a; // 0.19
        float progress = 100 - (c * 100);

        return progress;
    }

    public void UpozorenjeFreeSpace() {

        if (VratiPostotakZauzetogProstora() >= slobodanProstorUpozorenje) {
            Toast.makeText(getApplicationContext(), "Upozorenje! Vaš uređaj ima malo slobodnog prostora! Da bi izbjegli neugodne situacije, osigurajte više slobodnog prostora!", Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), VratiPostotakZauzetogProstora() + ">=90", Toast.LENGTH_LONG).show();
        }
    }

    public void PostaviSavePath() {

        Date datum = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //hh-mm-ss__dd-MM-yyyy
        String DateToStr = format.format(datum);

        savePath = Environment.getExternalStorageDirectory() + File.separator + "ZemanRecording" + File.separator + DateToStr + File.separator;
    }

    public void RefreshajProgressBar() {

        freeSpace.setText(Long.toString(FreeSpaceOnSD.getAvailableSpaceInMB()) + "MB / " + Long.toString(FreeSpaceOnSD.getSizeInMB()) + " MB slobodnog");

        ProgressBar spaceProgressBar = (ProgressBar) findViewById(R.id.spaceProgressBar);


        spaceProgressBar.setProgress(Math.round(VratiPostotakZauzetogProstora()));
        spaceProgressBar.getProgressDrawable().setColorFilter(Color.parseColor("#FF5722"), PorterDuff.Mode.SRC_IN);


    }

    public void PostaviIzgledZapisa() {
        Context context = getApplicationContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        String listZapis = pref.getString("listpref4", "");

        if (listZapis != "") {
            simpleDateFormat = listZapis;
        } else {
            simpleDateFormat = "yyyy-MM-dd__hh-mm-ss";

        }


    }

    public void LoadajTextViewZaB() {

        audioEncodingBitrateTextView.setText(Integer.toString(AudioEncodingBitrate) + "bit/s");
        audioSamplingRateTextView.setText(Integer.toString(AudioSamplingRate) + "Hz");
    }

    public void PostaviDefaultPostavke() {


    }

    public void PostaviPremaPostavkama() {


        Context context = getApplicationContext();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String listEncodingBitrate = pref.getString("listpref", "");
        String listSamplingRate = pref.getString("listpref2", "");
        String listFormat = pref.getString("listpref3", "");

        if (listEncodingBitrate != "" || listSamplingRate != "" || listFormat != "") {
            AudioEncodingBitrate = Integer.parseInt(listEncodingBitrate);
            AudioSamplingRate = Integer.parseInt(listSamplingRate);
            AudioFormat = Integer.parseInt(listFormat);
        } else {
            AudioEncodingBitrate = 128000;
            AudioSamplingRate = 44100;
            AudioFormat = 2;
        }

    }

    public boolean PostaviRecordera(int outputFormat, int audioEncoder) {

        boolean success = false;


        try

        {
            myAudioRecorder.setOutputFormat(outputFormat);
            myAudioRecorder.setOutputFile(outputFile);
            myAudioRecorder.setAudioEncodingBitRate(AudioEncodingBitrate);
            myAudioRecorder.setAudioSamplingRate(AudioSamplingRate);
            myAudioRecorder.setAudioEncoder(audioEncoder);

            if (AudioSamplingRate <= 22050){
                frequency = AudioSamplingRate;
            }else{
                frequency = 22050;
            }


            success = true;
        } catch (Exception e) {

            success = false;
        }


        return success;
    }


    public void PostaviUIPremaPostavkama() {

        final Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);


        audioEncodingBitrateTextView = (TextView) findViewById(R.id.textView7);
        audioSamplingRateTextView = (TextView) findViewById(R.id.textView8);
        outputFileTextView = (TextView) findViewById(R.id.textView6);
        freeSpace = (TextView) findViewById(R.id.textView2);
        trenutnePostavkeTv = (TextView) findViewById(R.id.textView25);


        ProgressBar spaceProgressBar = (ProgressBar) findViewById(R.id.spaceProgressBar);
        Button hint = (Button) findViewById(R.id.buttonHint);


        audioEncodingBitrateTextView.setVisibility(View.INVISIBLE);
        audioSamplingRateTextView.setVisibility(View.INVISIBLE);
        outputFileTextView.setVisibility(View.INVISIBLE);
        freeSpace.setVisibility(View.INVISIBLE);
        spaceProgressBar.setVisibility(View.INVISIBLE);
        trenutnePostavkeTv.setVisibility(View.INVISIBLE);

        checkBoxEncoding = preferences.getBoolean("checkBoxEncoding", false);
        checkBoxFilePath = preferences.getBoolean("checkBoxFilePath", false);
        checkBoxMB = preferences.getBoolean("checkBoxMB", false);
        checkBoxSampling = preferences.getBoolean("checkBoxSampling", false);

        //Toast.makeText(getApplicationContext(), "checkBoxEncoding=" + checkBoxEncoding, Toast.LENGTH_SHORT).show();


        if (checkBoxEncoding && checkBoxSampling) {
            audioEncodingBitrateTextView.setVisibility(View.VISIBLE);
            audioSamplingRateTextView.setVisibility(View.VISIBLE);
            hint.setVisibility(View.VISIBLE);
            trenutnePostavkeTv.setVisibility(View.VISIBLE);
        } else {

            if (checkBoxEncoding == true && checkBoxSampling == false) {
                audioEncodingBitrateTextView.setVisibility(View.VISIBLE);
                hint.setVisibility(View.VISIBLE);
                trenutnePostavkeTv.setVisibility(View.VISIBLE);

                audioSamplingRateTextView.setVisibility(View.INVISIBLE);
            }

            if (checkBoxEncoding == false && checkBoxSampling == true) {
                audioEncodingBitrateTextView.setVisibility(View.INVISIBLE);
                hint.setVisibility(View.VISIBLE);
                trenutnePostavkeTv.setVisibility(View.VISIBLE);

                audioSamplingRateTextView.setVisibility(View.VISIBLE);

            }

            if (checkBoxEncoding == false && checkBoxSampling == false) {

                audioEncodingBitrateTextView.setVisibility(View.INVISIBLE);

                hint.setVisibility(View.INVISIBLE);
                trenutnePostavkeTv.setVisibility(View.INVISIBLE);
                audioSamplingRateTextView.setVisibility(View.INVISIBLE);
            }
        }

       /*
        } else {

        }
*/


        /*

        if (checkBoxSampling) {
            audioSamplingRateTextView.setVisibility(View.VISIBLE);
            hint.setVisibility(View.VISIBLE);
            trenutnePostavkeTv.setVisibility(View.VISIBLE);

        } else {
            audioSamplingRateTextView.setVisibility(View.INVISIBLE);
            hint.setVisibility(View.INVISIBLE);
            trenutnePostavkeTv.setVisibility(View.INVISIBLE);

        }

*/


        if (checkBoxFilePath) outputFileTextView.setVisibility(View.VISIBLE);
        else outputFileTextView.setVisibility(View.INVISIBLE);

        if (checkBoxMB) {
            freeSpace.setVisibility(View.VISIBLE);
            spaceProgressBar.setVisibility(View.VISIBLE);

        } else {
            freeSpace.setVisibility(View.INVISIBLE);
            spaceProgressBar.setVisibility(View.INVISIBLE);

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        // ZA ANIMACIJU BLINKANJA
        final Chronometer myText = (Chronometer) findViewById(R.id.chronometer);
        final Animation anim = new AlphaAnimation(0.5f, 1.0f);
        anim.setDuration(1500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        //  za toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        final Button record = (Button) findViewById(R.id.button);
        final Button hint = (Button) findViewById(R.id.buttonHint);
        final Button lastSavedRecord = (Button) findViewById(R.id.buttonLastSaved);

        freeSpace = (TextView) findViewById(R.id.textView2);
        outputFileTextView = (TextView) findViewById(R.id.textView6);
        audioEncodingBitrateTextView = (TextView) findViewById(R.id.textView7);
        audioSamplingRateTextView = (TextView) findViewById(R.id.textView8);
        filePath = (TextView) findViewById(R.id.textView6);
        trenutnePostavkeTv = (TextView) findViewById(R.id.textView25);

        final RippleView rippleView = (RippleView) findViewById(R.id.more);
        final Chronometer ch = (Chronometer) findViewById(R.id.chronometer);


        outputFileTextView.setVisibility(View.INVISIBLE);
        filePath.setVisibility(View.INVISIBLE);
        lastSavedRecord.setVisibility(View.INVISIBLE);//REDO

        //PostaviPremaPostavkama();



        RefreshajProgressBar();
//        PostaviPremaPostavkama();


        PostaviUIPremaPostavkama();
        RefreshajProgressBar();
        PostaviSavePath();
        PostaviIzgledZapisa();
        LoadajTextViewZaB();
        PostaviPremaPostavkama();



        //lastSavedRecord.setVisibility(View.INVISIBLE);
        //filePath.setVisibility(View.INVISIBLE);


        if (FolderCreation.CreateDateFolder()) {
            //Toast.makeText(this, "Kreiran folder za spremanje snimci ILI već postoji", Toast.LENGTH_SHORT).show();
        } else

        {

            outputFile = Environment.DIRECTORY_MUSIC;
            //Toast.makeText(this, "Postavljeni je drugi direktorij za spremanje snimci!!", Toast.LENGTH_SHORT).show();
        }


        // Snimaj button
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean dovoljnoSlobodnogProstora = true;

                if (VratiPostotakZauzetogProstora() >= slobodanProstorKontrola) {
                    dovoljnoSlobodnogProstora = false;
                    //Toast.makeText(getApplicationContext(), VratiPostotakZauzetogProstora()+"", Toast.LENGTH_SHORT).show();
                }


                if (recording == false && dovoljnoSlobodnogProstora == true) {
                    try {

                        // dok krene nanovo snimat da glatko makne
                        ZemanAnimation.AlphaAnim(outputFileTextView, 1.0f, 0.0f, 180, 100);
                        ZemanAnimation.AlphaAnim(lastSavedRecord, 1.0f, 0.0f, 180, 100);


                        ch.setBase(SystemClock.elapsedRealtime());
                        ch.start();


                        //

                        // ne utječe na refreshanje onih textviewva
                        PostaviPremaPostavkama();
                        PostaviSavePath();
                        PostaviIzgledZapisa();


                        // ovo doduse utjece ali kasno
                        LoadajTextViewZaB();


                        // kreiranje novog datuma za korištenje u nazivu datoteke kod snimanja novog zapisa
                        Date newDate = new Date(System.currentTimeMillis());
                        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                        String DateToStr = format.format(newDate);

                        //Toast.makeText(MainActivity.this, "datetostr = " + DateToStr, Toast.LENGTH_LONG ).show();

                        myAudioRecorder = new MediaRecorder();
                        try {
                            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        }catch(Exception e){

                            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                        }

                        PostaviPremaPostavkama();

                        //PostaviRecordera(2, 2); // .mp3
                        //PostaviRecordera(1, 3); // .3gp
/*
                        if (AudioFormat == 0) {
                            if (PostaviRecordera(MediaRecorder.OutputFormat.WEBM, MediaRecorder.AudioEncoder.VORBIS) == true) {

                                outputFile = savePath + "zapis3GP_" + DateToStr + ".3gp";

                            }
                        }
*/

                        if (AudioFormat == 1) {
                            if (PostaviRecordera(MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.AudioEncoder.AAC)) {
                                outputFile = savePath + "zapis3GP_" + DateToStr + ".3gp";
                            }
                        }

                        if (AudioFormat == 2) {
                            if (PostaviRecordera(MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC)) {
                                outputFile = savePath + "zapisAAC_" + DateToStr + ".aac";
                            }
                        }

                        if (AudioFormat == 3) {
                            if (PostaviRecordera(MediaRecorder.OutputFormat.AMR_WB, MediaRecorder.AudioEncoder.AMR_WB)) {
                                outputFile = savePath + "zapisAWB_" + DateToStr + ".awb";
                            }
                        }

                        if (AudioFormat == 4) {
                            outputFile = savePath + "zapisWAV_" + DateToStr + ".wav";
                            startRecord();
                            snimamWAV = true;

                            // 512

                        }





    /*
                        if (AudioFormat == 2) {
                            if (PostaviRecordera(2, 2) == true) {

                                outputFile = savePath + "zapisMP3_" + DateToStr + ".mp3";

                            }
                        }
*/
                        myAudioRecorder.setAudioChannels(2);


                        // VORBIS = .OGG
                        //myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.VORBIS);
                        //myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.WEBM);


                        UpozorenjeFreeSpace();


                        if (snimamWAV == false) {


                            myAudioRecorder.setAudioEncodingBitRate(AudioEncodingBitrate);
                            myAudioRecorder.setAudioSamplingRate(AudioSamplingRate);
                            myAudioRecorder.setOutputFile(outputFile);


                            myAudioRecorder.prepare();
                            myAudioRecorder.start();
                        }

                    } catch (IllegalStateException e) {

                        e.printStackTrace();
                    } catch (IOException e) {

                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // TESTIRANJE BLINKING

                    myText.startAnimation(anim);


                    //record.setEnabled(false);
                    //stop.setEnabled(true);

                    //Toast.makeText(getActivity(), "Snimam...", Toast.LENGTH_SHORT).show();
                    record.setBackgroundColor(Color.parseColor("#FFCCBC"));
                    //record.setBackgroundColor(Color.parseColor("#D47A6A"));

                    record.setText(R.string.stop_recording);

                    //my_button.setBackgroundResource(R.drawable.icon);


                    recording = true;


                } else if (recording == true && dovoljnoSlobodnogProstora == true) {


                    ch.stop();
                    anim.cancel();

                    if (snimamWAV == true) {
                        stopRecord();
                    }

                    if (snimamWAV == false) {

                        try {
                            myAudioRecorder.stop();
                            myAudioRecorder.reset();
                            myAudioRecorder.release();
                            myAudioRecorder = null;


                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    recording = false;  // prestali smo snimati


                    record.setBackgroundColor(Color.parseColor("#FF5722"));
                    record.setText("Snimaj!");

                    final Context context = getApplicationContext();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                    checkBoxRenameDialog = preferences.getBoolean("checkBoxRenameDialog", false);

                    //Toast.makeText(context, "boolean checkboxrenamedialog = " + checkBoxRenameDialog, Toast.LENGTH_SHORT).show();


                    if (checkBoxRenameDialog == true) {
                        showInputDialog();
                    }

                    //File file = new File(outputFile);
                    //File file = new File();
                    //Toast.makeText(MainActivity.this, "file--  " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();


                    outputFileTextView.setText("Spremljeno u " + outputFile);

                    ZemanAnimation.AlphaAnim(lastSavedRecord, 0.0f, 1.0f, 800, 0);
                    ZemanAnimation.AlphaAnim(outputFileTextView, 0.0f, 1.0f, 800, 0);


                }
                if (dovoljnoSlobodnogProstora == false) {
                    record.setText("Nema dovoljno prostora!");
                    record.setTextSize(22);
                    Toast.makeText(getApplicationContext(), "Zbog jako malo slobodnog prostora molim Vas da oslobodite više prostora da se možete vratiti snimanju!", Toast.LENGTH_LONG).show();
                    ch.setText("00:00");

                }
            }


        });


        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, MyPreferenceActivity.class);
                startActivity(i);

            }
        });


        lastSavedRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, SongDetailsActivity.class);
                i.putExtra("path", outputFile);
                startActivity(i);

            }
        });

    }


    protected void showInputDialog() {


        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);


        File cijeli = new File(outputFile);

            editText.setText(cijeli.getName().substring(0, cijeli.getName().length() - 4));
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Spremi", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //resultText.setText("Hello, " + editText.getText());
                            //Toast.make(this, "Hello " + editText.getText(), Toast.LENGTH_SHORT);

                            //outputFile = editText.getText().toString();

                            File f = new File(outputFile);
                            File renamedFile = new File(editText.getText().toString());

                            final int[] ext = {0};
                            //ext[0] = Integer.parseInt("0");


                            if (f.getName().endsWith(".3gp")) {
                                //editText.setText(f.getName().substring(0, f.getName().length() - 4));
                                ext[0] = Integer.parseInt("0");
                            }

                            if (f.getName().endsWith(".aac")) {
                                //editText.setText(f.getName().substring(0, f.getName().length() - 4));
                                ext[0] = Integer.parseInt("1");
                            }

                            if (f.getName().endsWith(".awb")) {
                                //editText.setText(f.getName().substring(0, f.getName().length() - 4));
                                ext[0] = Integer.parseInt("2");
                            }

                            if (f.getName().endsWith(".wav")) {
                                //editText.setText(f.getName().substring(0, f.getName().length() - 4));
                                ext[0] = Integer.parseInt("3");
                            }



                            File f1 = new File(outputFile);
                            f1 = new File(f1.getAbsolutePath());
                            String dir = f1.getParent();

                            //File renamedFile = new File(outputFile);

                            if (ext[0] == 0) {
                                renamedFile = new File(dir + "//" + editText.getText().toString() + ".3gp");
                            }
                            if (ext[0] == 1) {
                                renamedFile = new File(dir + "//" + editText.getText().toString() + ".aac");

                            }
                            if (ext[0] == 2) {
                                renamedFile = new File(dir + "//" + editText.getText().toString() + ".awb");

                            }
                            if (ext[0] == 3) {
                                renamedFile = new File(dir + "//" + editText.getText().toString() + ".wav");

                            }

                            if (!renamedFile.exists()) {
                                if (f1.renameTo(renamedFile)) {
                                    System.out.println("Succes! Name changed to: " + renamedFile.getName());


                                } else {
                                    System.out.println("failed");
                                    Toast.makeText(MainActivity.this, "Greška kod preimenovanja!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //Toast.makeText(MainActivity.this, "Već postoji zapis sa istim imenom!", Toast.LENGTH_SHORT).show();
                            }
                            //System.out.println("Succes! Name changed to: " + renamedFile.getName());
                            outputFileTextView.setText(renamedFile.getAbsolutePath());

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

    }


    //region MENU OPTIONS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Toast.makeText(this, "KREIRAM OPTIONS MENU", Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_popis) {

            Intent i = new Intent(this, RecyclerMainActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_preferences) {

            Intent i = new Intent(this, MyPreferenceActivity.class);
            startActivity(i);
            //return true;
        }


        return super.onOptionsItemSelected(item);
    }
//endregion

    //region WAV


    private String getFilename() {
        //    outputFile = savePath + "zapisWAV_" + DateToStr + ".wav";


        String filepath = outputFile;
        File file = new File(filepath);

        /*
        if(file.exists()){
            file.delete();
        }*/

        return file.getAbsolutePath();
    }

    private String getTempFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);

        if (tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void startRecord() {

        createAudioRecord();
        audioRecord.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }

    private void writeAudioDataToFile() {
        byte data[] = new byte[recBufSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int read = 0;

        if (null != os) {
            while (isRecording) {
                read = audioRecord.read(data, 0, recBufSize);

                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecord() {
        if (null != audioRecord) {
            isRecording = false;

            audioRecord.stop();
            audioRecord.release();

            audioRecord = null;
            recordingThread = null;
        }

        copyWaveFile(getTempFilename(), getFilename());
        deleteTempFile();
    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());

        file.delete();
    }

    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = AudioSamplingRate;
        int channels = 2;
        long byteRate = RECORDER_BPP * frequency * channels / 8;

        byte[] data = new byte[recBufSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            //AppLog.logString("File size: " + totalDataLen);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while (in.read(data) != -1) {
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (1 * 16 / 8);  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }


    public void createAudioRecord() {
        recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, EncodingBitRate);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                channelConfiguration, EncodingBitRate, recBufSize);

    }

    //endregion



}
