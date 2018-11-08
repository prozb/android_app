package de.thm.ap.contentresolver;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab    = findViewById(R.id.fab);
        FloatingActionButton getFan = findViewById(R.id.get);
        getFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "sent pick request", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(Intent.ACTION_PICK);
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(
                        intent, PackageManager.MATCH_DEFAULT_ONLY);

                boolean isIntentSafe = activities.size() > 0;
                if(isIntentSafe) {
                    startActivity(intent);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sent request to database", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                testCSVRequest();
//                testRecordsRequest();
            }
        });
    }

    private void testRecordsRequest(){
        Uri uri = Uri.parse("content://de.thm.ap.ap_przb86_u1.cp/records-db/1000");
        ContentResolver cr = getContentResolver();

        String [] projection = {"id", "moduleName"};
        Cursor c = cr.query(uri, projection, null, null, "moduleName");

        if(c != null){
            Log.d("RESOLVER", "called content resolver");

            while (c.moveToNext()){
                Toast.makeText(MainActivity.this, "id: " + c.getLong(0) + " module name: " + c.getString(1),
                        LENGTH_SHORT).show();
                Log.d(TAG, "id: " + c.getLong(0) + " module name: " + c.getString(1));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void testCSVRequest(){
        Uri uri = Uri.parse("content://de.thm.ap.ap_przb86_u1.cp/records-db");
        ContentResolver cr = getContentResolver();

        try {
            FileDescriptor descriptor = Objects.requireNonNull(cr.openFileDescriptor(uri, "r")).getFileDescriptor();
            FileInputStream reader = new FileInputStream(descriptor);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(reader));
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                Log.d(TAG, line);
            }
        } catch (Exception e){
            Log.e("ERROR", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
