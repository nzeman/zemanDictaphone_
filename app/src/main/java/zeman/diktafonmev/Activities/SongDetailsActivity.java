package zeman.diktafonmev.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import zeman.diktafonmev.Helpers.FileSize;
import zeman.diktafonmev.R;

/**
 * Created by praktikant on 4.3.2016..
 */
public class SongDetailsActivity extends AppCompatActivity {


    // test
    private static final int REQUEST_DIRECTORY = 0;
    private static final String TAG = "DirChooserSample";
    public String fileName = "";
    public String path = "";
    private TextView nazivTextView;
    private TextView pathTextView;
    private TextView veličinaTextView;
    private TextView vrijemePosljednjeIzmejeneTextView;
    private TextView trajanjeTextView;
    private Button izbrisi;
    private Button rename;
    private Button test;
    private Toolbar toolbar;
    private TextView mDirectoryTextView;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        // or call onBackPressed()
        return true;
    }

    /*
        @Override
        protected void onDestroy() {
            super.onDestroy();
             Intent i = new Intent(getApplicationContext(), Popis.class);
                                                          startActivity(i);

        }
    */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song_details);

        //  za toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");


        File f1 = new File(path);

        //path = Environment.getExternalStorageDirectory() + "/ZemanRecording/" + fileName;

        //System.out.println("Test = " + path);


        nazivTextView = (TextView) findViewById(R.id.textView10);
        pathTextView = (TextView) findViewById(R.id.textView11);
        veličinaTextView = (TextView) findViewById(R.id.textView15);
        vrijemePosljednjeIzmejeneTextView = (TextView) findViewById(R.id.textView12);
        trajanjeTextView = (TextView) findViewById(R.id.textView14);


        izbrisi = (Button) findViewById(R.id.button2);
        rename = (Button) findViewById(R.id.button3);
        //test = (Button) findViewById(R.id.button5); // za testiranje


        final File file = new File(path);


        String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
        //Date newDate = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
        String DateToStr = format.format(file.lastModified());


        // load data file
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);

        String out = "";
        // get mp3 info



        // convert duration to minute:seconds
        String duration =
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Log.v("time", duration);
        long dur = Long.parseLong(duration);
        String seconds = String.valueOf((dur % 60000) / 1000);

        Log.v("seconds", seconds);
        String minutes = String.valueOf(dur / 60000);
        out = minutes + ":" + seconds;
        if (seconds.length() == 1) {
            trajanjeTextView.setText("0" + minutes + ":0" + seconds);
        } else {
            trajanjeTextView.setText("0" + minutes + ":" + seconds);
        }
        Log.v("minutes", minutes);
        // close object
        metaRetriever.release();

        nazivTextView.setText(file.getName());
        pathTextView.setText(path);
        veličinaTextView.setText(FileSize.readableFileSize(file.length()));
        vrijemePosljednjeIzmejeneTextView.setText(DateToStr.toString());
        trajanjeTextView.setText(out);


        izbrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getApplicationContext();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SongDetailsActivity.this);
                alertDialogBuilder.setTitle("Obrisati?");
                alertDialogBuilder.setMessage("Da li ste sigurni da želite obrisati \"" + path + "\"?");

                // setup a dialog window
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (file.delete()) {
                                    Toast.makeText(context, "Uspješno obrisano", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(context, "Greška kod brisanja", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Odustani",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog alert = alertDialogBuilder.create();

                alert.show();
            }
        });

/*
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent chooserIntent = new Intent(
                        SongDetailsActivity.this,
                        PlaySongActivity.class);

                startActivity(chooserIntent);

                /*
                final Intent chooserIntent = new Intent(
                        SongDetailsActivity.this,
                        DirectoryChooserActivity.class);

                final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                        .newDirectoryName("SongDetailsActivity")
                        .allowReadOnlyDirectory(true)
                        .allowNewDirectoryNameModification(true)
                        .build();

                chooserIntent.putExtra(
                        DirectoryChooserActivity.EXTRA_CONFIG,
                        config);

                startActivityForResult(chooserIntent, REQUEST_DIRECTORY);


            }
        });
*/



        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                          LayoutInflater layoutInflater = LayoutInflater.from(SongDetailsActivity.this);
                                          View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
                                          AlertDialog.Builder builder = new AlertDialog.Builder(SongDetailsActivity.this);
                                          builder.setView(promptView);
                                          builder.setTitle("Preimenovati?");
                                          builder.setMessage("Da li ste sigurni da želite preimenovati datoteku?");
                                          final EditText input = new EditText(SongDetailsActivity.this);
                                          File f = new File(path);
                                          final int[] ext = {0};

                                          if (f.getName().endsWith(".awb")) {
                                              input.setText(f.getName().substring
                                                      (0, f.getName().length() - 4));
                                              ext[0] = Integer.parseInt("0");
                                          }
                                          if (f.getName().endsWith(".3gp")) {
                                              input.setText(f.getName().substring
                                                      (0, f.getName().length() - 4));
                                              ext[0] = Integer.parseInt("1");
                                          }
                                          if (f.getName().endsWith(".wav")) {
                                              input.setText(f.getName().substring
                                                      (0, f.getName().length() - 4));
                                              ext[0] = Integer.parseInt("2");
                                          }

                                          if (f.getName().endsWith(".aac")) {
                                              input.setText(f.getName().substring
                                                      (0, f.getName().length() - 4));
                                              ext[0] = Integer.parseInt("3");
                                          }
                                          input.setInputType(InputType.TYPE_CLASS_TEXT |
                                                  InputType.TYPE_TEXT_VARIATION_NORMAL);
                                          builder.setView(input);
                                          builder.setPositiveButton("Preimenuj", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {
                                                  File f1 = new File(path);
                                                  f1 = new File(f1.getAbsolutePath());
                                                  String dir = f1.getParent();

                                                  File renamedFile = new File(path);
                                                  if (ext[0] == 0) {
                                                      renamedFile = new File(dir + "//" +
                                                              input.getText().toString() + ".awb");
                                                  }
                                                  if (ext[0] == 1) {
                                                      renamedFile = new File(dir + "//"
                                                              + input.getText().toString() + ".3gp");
                                                  }
                                                  if (ext[0] == 2) {
                                                      renamedFile = new File(dir + "//"
                                                              + input.getText().toString() + ".wav");
                                                  }
                                                  if (ext[0] == 3) {
                                                      renamedFile = new File(dir + "//"
                                                              + input.getText().toString() + ".aac");
                                                  }
                                                  try {
                                                      if (f1.renameTo(renamedFile)) {
                                                          Toast.makeText(SongDetailsActivity.this,
                                                                  "Uspješno preimenovano!", Toast.LENGTH_SHORT).show();
                                                          onBackPressed();

                                                      } else {
                                                          Toast.makeText(SongDetailsActivity.this,
                                                                  "Greška kod preimenovanja!", Toast.LENGTH_SHORT).show();
                                                      }

                                                  } catch (Exception e) {
                                                      Toast.makeText(getApplicationContext(),
                                                              "Greška kod preimenovanja!", Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          });
                                          builder.setNegativeButton("Odustani",
                                                  new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {
                                                  dialog.dismiss();
                                              }
                                          });
                                          builder.show();
                                      }

                                  }

        );


    }
}







