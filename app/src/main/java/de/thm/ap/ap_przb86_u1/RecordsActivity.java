package de.thm.ap.ap_przb86_u1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordsActivity extends AppCompatActivity {
    private ListView recordListView;
    private TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_records);

        recordListView = findViewById(R.id.records_list);
        emptyView = findViewById(R.id.records_list_empty);
        recordListView.setEmptyView(emptyView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.records, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Toast.makeText(this, "smth selected", Toast.LENGTH_SHORT).show();
        Log.e("ADD", "onOptionsItemSelected");

        switch(item.getItemId()) {
            case R.id.action_add:
//                Toast.makeText(this, "add button pressed", Toast.LENGTH_SHORT).show();
                Log.e("ADD", "add pressed");
                Intent i = new Intent(this, RecordFormActivity.class);
                startActivity(i);
                return true;

            case R.id.action_stats:
                calculateStatistic();
//                Toast.makeText(this, "calc statistic button pressed", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void calculateStatistic() {
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<Record> records = new RecordDAO(this).findAll();

        ArrayAdapter<Record> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, records);

        recordListView.setAdapter(adapter);
     }
}
