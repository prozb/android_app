package de.thm.ap.ap_przb86_u1;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
    private List<Record> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Toast.makeText(this, "file deleted", Toast.LENGTH_SHORT).show();
        new RecordDAO(this).close();
        setContentView(R.layout.activity_records);

        recordListView = findViewById(R.id.records_list);
        recordListView.setEmptyView(findViewById(R.id.records_list_empty));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.records, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("ADD", "onOptionsItemSelected");

        switch(item.getItemId()) {
            case R.id.action_add:
                Log.e("ADD", "add pressed");
                Intent i = new Intent(this, RecordFormActivity.class);
                startActivity(i);
                return true;

            case R.id.action_stats:
                showStatistik();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStatistik() {
        Stats stats = new Stats(new RecordDAO(this).findAll());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.stats);
        builder.setMessage("Leistungen: " + stats.getSumModules()
                + "\n50% Leistungen: " + stats.getSumHalfWeighted()
                + "\nSumme Crp: " + stats.getSumCrp()
                + "\nDurchschnitt: " + stats.getAverageMark()
                + "%\nCrp bis Ziel: " + stats.getCrpToEnd());
        builder.setNeutralButton(R.string.close, null);
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        records = new RecordDAO(this).findAll();

        ArrayAdapter<Record> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, records);

        recordListView.setAdapter(adapter);
     }
}
