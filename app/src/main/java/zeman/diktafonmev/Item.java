package zeman.diktafonmev;

import android.os.Environment;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class Item {
    private String name;
    private String lastTimeModified;
    private String status;
    private Integer icon;
    private String filePath;


    public Item(String name, String lastTimeModified, Integer icon, String status, String filePath) {
        this.name = name;
        this.status = status;
        this.lastTimeModified = lastTimeModified;
        this.icon = icon;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastTimeModified() {
        return lastTimeModified;
    }

    public void setLastTimeModified(String lastTimeModified) {
        this.lastTimeModified = lastTimeModified;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }



    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String name) {
        this.filePath = filePath;
    }


    // ispis svih zapisa
    public static List<Item> createItemList(int numItems) {
        List<Item> items = new ArrayList<Item>();

        String endwav = ".wav";
        String end3gp = ".3gp";
        String endaac = ".aac";
        String endawb = ".awb";


        File myMp3Dir = new File(Environment.getExternalStorageDirectory() + File.separator + "ZemanRecording" + File.separator);

        listaSvih.clear();

        listFilesAndFilesSubDirectories(myMp3Dir.toString(), false);

        for (String s : listaSvih) {
            File f = new File(s);
            if (!f.isDirectory()) //if file is browsing not directory
            {
                if (f.getName().toLowerCase().contains(endwav)) { //if file with end is .mp3 is add to arraylist

                    String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
                    //Date newDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                    String DateToStr = format.format(f.lastModified());
                    //java.util.Date myDate = new java.util.Date(f.lastModified());
                    items.add(new Item(f.getName(), DateToStr, R.drawable.wav, "Play", f.getAbsolutePath()));

                }
                if (f.getName().toLowerCase().contains(end3gp)) {
                    String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
                    //Date newDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                    String DateToStr = format.format(f.lastModified());
                    //java.util.Date myDate = new java.util.Date(DateToStr);
                    items.add(new Item(f.getName(), DateToStr, R.drawable.trigp_2, "Play", f.getAbsolutePath()));
                }

                if (f.getName().toLowerCase().contains(endaac)) {
                    String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
                    //Date newDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                    String DateToStr = format.format(f.lastModified());
                    //java.util.Date myDate = new java.util.Date(DateToStr);
                    items.add(new Item(f.getName(), DateToStr, R.drawable.aac, "Play", f.getAbsolutePath()));
                }


                if (f.getName().toLowerCase().contains(endawb)) {
                    String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
                    //Date newDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                    String DateToStr = format.format(f.lastModified());
                    //java.util.Date myDate = new java.util.Date(DateToStr);
                    items.add(new Item(f.getName(), DateToStr, R.drawable.amr, "Play", f.getAbsolutePath()));
                }


            }


        }
        return items;


    }



    public static ArrayList<String> listaSvih = new ArrayList<>();
    public static ArrayList<String> listaFiltered = new ArrayList<>();

    public static void listFilesAndFilesSubDirectories(String directoryName, boolean filtered) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();


        if (filtered == false) {
            for (File file : fList) {
                if (file.isFile()) {
                    System.out.println(file.getAbsolutePath());
                    listaSvih.add(file.getAbsolutePath());

                }
                if (file.isDirectory()) {
                    listFilesAndFilesSubDirectories(file.getAbsolutePath(), false);

                }
            }
        }
        if (filtered == true) {
            for (File file : fList) {
                if (file.isFile()) {
                    System.out.println(file.getAbsolutePath());
                    listaFiltered.add(file.getAbsolutePath());

                }
                if (file.isDirectory()) {
                    listFilesAndFilesSubDirectories(file.getAbsolutePath(), true);

                }
            }

        }

    }





    // ispis svih mp3 i 3gp koji odgovaraju pretragi
    public static List<Item> createItemListSearch(int numItems, String search) {
        List<Item> items = new ArrayList<Item>();

        String endwav = ".wav";
        String end3gp = ".3gp";
        String endaac = ".aac";
        String endawb = ".awb";

        String pojam = search;

        File myMp3Dir = new File(Environment.getExternalStorageDirectory() + File.separator + "ZemanRecording" + File.separator);

        File[] fileList = null;

        listaFiltered.clear();
        listFilesAndFilesSubDirectories(myMp3Dir.toString(), true);

        for (String s : listaFiltered) {
            File f = new File(s);
            if (!f.isDirectory()) {

                if (f.getName().toLowerCase().contains(endwav)) {
                    String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
                    //Date newDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                    String DateToStr = format.format(f.lastModified());


                    String a = f.getName().toLowerCase().substring(0, f.getName().length() - 4);
                    String[] words = a.split("\\s+");
                    for (int i = 0; i < words.length; i++) {
                        words[i] = words[i].replaceAll("[^\\w]", "");
                        if (words[i].contains(pojam.toLowerCase()))
                            items.add(new Item(f.getName(), DateToStr, R.drawable.wav, "Play", f.getAbsolutePath()));
                    }


                }
                if (f.getName().toLowerCase().contains(end3gp)) {


                    String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
                    //Date newDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                    String DateToStr = format.format(f.lastModified());

                    String a = f.getName().toLowerCase().substring(0, f.getName().length() - 4);
                    String[] words = a.split("\\s+");
                    for (int i = 0; i < words.length; i++) {
                        words[i] = words[i].replaceAll("[^\\w]", "");
                        if (words[i].contains(pojam.toLowerCase()))
                            items.add(new Item(f.getName(), DateToStr, R.drawable.trigp_2, "Play", f.getAbsolutePath()));
                    }

                }

                if (f.getName().toLowerCase().contains(endaac)) {


                    String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
                    //Date newDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                    String DateToStr = format.format(f.lastModified());

                    String a = f.getName().toLowerCase().substring(0, f.getName().length() - 4);
                    String[] words = a.split("\\s+");
                    for (int i = 0; i < words.length; i++) {
                        words[i] = words[i].replaceAll("[^\\w]", "");
                        if (words[i].contains(pojam.toLowerCase()))
                            items.add(new Item(f.getName(), DateToStr, R.drawable.aac, "Play", f.getAbsolutePath()));
                    }

                }

                if (f.getName().toLowerCase().contains(endawb)) {


                    String simpleDateFormat = "dd.MM.yyyy. HH:mm:ss";
                    //Date newDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat); //hh-mm-ss__dd-MM-yyyy
                    String DateToStr = format.format(f.lastModified());

                    String a = f.getName().toLowerCase().substring(0, f.getName().length() - 4);
                    String[] words = a.split("\\s+");
                    for (int i = 0; i < words.length; i++) {
                        words[i] = words[i].replaceAll("[^\\w]", "");
                        if (words[i].contains(pojam.toLowerCase()))
                            items.add(new Item(f.getName(), DateToStr, R.drawable.amr, "Play", f.getAbsolutePath()));
                    }

                }


            }

        }
        return items;


    }


}
