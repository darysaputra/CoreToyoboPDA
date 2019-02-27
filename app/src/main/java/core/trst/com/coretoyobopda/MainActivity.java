package core.trst.com.coretoyobopda;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import core.trst.com.coretoyobopda.util.CustomAdapter;
import core.trst.com.coretoyobopda.util.DbHelper;
import core.trst.com.coretoyobopda.util.DeviceHelper;
import core.trst.com.coretoyobopda.util.JsonHelper;
import core.trst.com.coretoyobopda.util.NFCInterface;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements NFCInterface {
    //Generate Helper
    DbHelper SQLite = new DbHelper(this);
    JsonHelper jsonHelp=new JsonHelper();
    DeviceHelper deviceHelp=new DeviceHelper();
    //Generate Helper

    private ListView lv;
    TreeMap<Integer, String> dataList = null;
    private Subscription nfcSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare Get Komponen
        lv=(ListView) findViewById(R.id.lvScan);
        //Declare Get Komponen

        try {
            loadData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Event Ditekan Lama Di ListView Untuk Kebutuhan Hapus Data
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                deviceHelp.vibrateDevice(500,MainActivity.this );
                                SQLite.delete(position);
                                try {
                                    loadData();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this );
                builder.setMessage("Apakah Anda Akan Menghapus Core [ "+ dataList.get(position) +" ] ?").setPositiveButton("Ya", dialogClickListener).setNegativeButton("Tidak", dialogClickListener).show();
                return false;
                }
            }
        );
        //Event Ditekan Lama Di ListView Untuk Kebutuhan Hapus Data

    }

    //--LOAD DATA DITAMPILKAN DI LISTVIEW
    private void loadData() throws JSONException {
        dataList = new TreeMap<Integer, String>();
        //dataList = new TreeMap<Integer, String>();
        int x=1;
        for(int i=0;i<SQLite.getAllData().size();i++){
            System.out.println("->"+SQLite.getAllData().get(i));
            JSONObject jObj= jsonHelp.stringToObject(SQLite.getAllData().get(i).toString());
            dataList.put((SQLite.getAllData().size()-x),jObj.getString("ID_CORE"));
            x++;
        }
        CustomAdapter listAdapter = new CustomAdapter(this, dataList);
        lv.setAdapter(listAdapter);
    }
    //--LOAD DATA DITAMPILKAN DI LISTVIEW

    //--INSERT DATA
    public void insertData(String param){
        //Insert Data
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        SQLite = new DbHelper(getApplicationContext());
        SQLite.insert("1", param,date);
        //Insert Data
    }
    //INSERT DATA

    //Event Dari NFC Reader
    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {

            String type = intent.getType();
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcReader(tag);
        }
    }

    @Override
    public String nfcRead(Tag t) {
        try {
            Tag tag = t;
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                return null;
            }
            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records)
            {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT))
                {
                    try {return readText(ndefRecord);} catch (UnsupportedEncodingException e) {}
                }
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }

    @Override
    public String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    public void nfcReader(Tag tag) {

        nfcSubscription= Observable.just(nfcRead(tag))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (s != null) {
                            System.out.println("--->"+s);
                            try {
                                insertData(s);
                                loadData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unsubscribe(nfcSubscription);

    }


    private static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
        }
    }
    //Event Dari NFC Reader
}