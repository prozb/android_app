package de.thm.ap.ap_przb86_u1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordsActivity extends AppCompatActivity {
    private ListView recordListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    protected void onStart() {
        super.onStart();

//        List<Record> records = new RecordDAO(this).findAll();
//
//        ArrayAdapter<Record> adapter = new ArrayAdapter<>(this,
//                    android.R.layout.simple_list_item_1, records);
//
//        recordListView.setAdapter(adapter);
     }
}
