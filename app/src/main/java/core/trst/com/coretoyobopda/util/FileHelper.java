package core.trst.com.coretoyobopda.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {

    public String readFile(String paramParent,String paramChild,String fileName){
        //Get Value From Config TXT File
        File sdcard = new File(paramParent,paramChild);
        File file = new File(sdcard,fileName);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.d(e.getMessage(), "onClick: ");
        }
        //Get Value From Config TXT File

        return text.toString();
    }

    public void writeFile(String paramValue,String paramParent,String paramChild,String fileName,Context mcoContext) throws JSONException {
        File file = new File(paramParent,paramChild);
        if(!file.exists()){
            file.mkdir();
        }
        try{
            File gpxfile = new File(file, fileName);
            if(!gpxfile.exists()){
                FileWriter writer = new FileWriter(gpxfile, true);
                writer.append(paramValue);
                writer.flush();
                writer.close();

                Toast.makeText(mcoContext, "Configuration Created",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(gpxfile));
                mcoContext.sendBroadcast(intent);
            }
        }catch (Exception e){
            Log.d(e.getMessage(), "writeFileOnInternalStorage: ");
        }
    }
}