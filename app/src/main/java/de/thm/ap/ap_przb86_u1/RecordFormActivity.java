package de.thm.ap.ap_przb86_u1;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Optional;

public class RecordFormActivity extends AppCompatActivity {
    private EditText moduleNum;
    private EditText creditPoints;
    private EditText mark;
    private AutoCompleteTextView moduleName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_records_form);

        //TODO: how do i rewrite this without lambdas?
        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> actionBar.setDisplayHomeAsUpEnabled(true));

        moduleNum = findViewById(R.id.module_num);
        moduleName = findViewById(R.id.module_name);
//        mark = findViewById(R.id.);

        String [] names = getResources().getStringArray(R.array.module_names);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        moduleName.setAdapter(adapter);
    }
}
