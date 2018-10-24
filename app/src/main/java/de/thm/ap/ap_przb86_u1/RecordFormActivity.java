package de.thm.ap.ap_przb86_u1;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Optional;

import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordFormActivity extends AppCompatActivity {
    private EditText moduleNum;
    private EditText creditPoints;
    private EditText mark;
    private AutoCompleteTextView moduleName;
    private Spinner year;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "stasda sd", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_records_form);

        //TODO: how do i rewrite this without lambdas?
//        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> actionBar.setDisplayHomeAsUpEnabled(true));

        moduleNum  = findViewById(R.id.module_num);
        moduleName = findViewById(R.id.module_name);
        year       = findViewById(R.id.semester_spinner);
//        mark = findViewById(R.id.);

        String [] names = getResources().getStringArray(R.array.module_names);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        moduleName.setAdapter(adapter);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getYears());
        year.setAdapter(adapter2);
    }

    public void onSave(View view){
        Record record = new Record();

        boolean isValid = true;
        //TODO: implement check user input
        finish();
    }


    private Integer [] getYears(){
        Integer [] years = new Integer [6];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for(int i = years.length - 1; i >= 0; i--){
            years[i] = currentYear--;
        }

        return years;
    }
}
