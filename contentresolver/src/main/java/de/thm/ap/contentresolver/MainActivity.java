package de.thm.ap.contentresolver;

import android.content.ContentResolver;
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
import java.io.PrintWriter;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Uri uri = Uri.parse("content://de.thm.ap.ap_przb86_u1.cp/records-db/4");
//                ContentResolver cr = getContentResolver();
//
//                String [] projection = {"id", "moduleName"};
//                Cursor c = cr.query(uri, projection, null, null, "moduleName");
//
//                if(c != null){
//                    Log.d("RESOLVER", "called content resolver");
//
//                    while (c.moveToNext()){
//                        Toast.makeText(MainActivity.this, "id: " + c.getLong(0) + " module name: " + c.getString(1),
//                                LENGTH_SHORT).show();
//                        Log.d(TAG, "id: " + c.getLong(0) + " module name: " + c.getString(1));
//                    }
//                }

//////////
                Uri uri = Uri.parse("content://de.thm.ap.ap_przb86_u1.cp/records-db");
                ContentResolver cr = getContentResolver();

                try {
                    FileDescriptor descriptor = Objects.requireNonNull(cr.openFileDescriptor(uri, "r")).getFileDescriptor();
                    FileInputStream reader = new FileInputStream(descriptor);

                    int i;
                    while((i = reader.read()) != -1){
                        Log.d(TAG, String.valueOf((char)i));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
