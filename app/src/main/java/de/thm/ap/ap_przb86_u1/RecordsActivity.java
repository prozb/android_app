package de.thm.ap.ap_przb86_u1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class RecordsActivity extends AppCompatActivity {
    private ListView recordListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_records);

        recordListView = findViewById(R.id.records_list);
        recordListView.setEmptyView(findViewById(R.id.records_list_empty));
    }
}
