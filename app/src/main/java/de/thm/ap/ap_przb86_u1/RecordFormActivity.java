package de.thm.ap.ap_przb86_u1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordFormActivity extends AppCompatActivity {
    private static final int PICK_MODULE_NUM = 1;

    private EditText moduleNum;
    private EditText creditPoints;
    private EditText mark;
    private AutoCompleteTextView moduleName;
    private Spinner year;
    private CheckBox soseCheckbox;
    private CheckBox halfWeightedCheckbox;

    private ArrayList<Integer> years;
    private int positionInList;
    private boolean updateFlag;
    private ModuleDAO moduleDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_records_form);
        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> actionBar.setDisplayHomeAsUpEnabled(true));

        moduleDAO    = AppDatabase.getDb(this).moduleDAO();
        moduleNum    = findViewById(R.id.module_num);
        moduleName   = findViewById(R.id.module_name);
        creditPoints = findViewById(R.id.credits_number);
        mark         = findViewById(R.id.mark_percent_number);
        year         = findViewById(R.id.semester_spinner);
        soseCheckbox = findViewById(R.id.sose_checkbox);

        halfWeightedCheckbox = findViewById(R.id.half_weighted_checkbox);

        String [] names = getResources().getStringArray(R.array.module_names);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        moduleName.setAdapter(adapter);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getYears());
        year.setAdapter(adapter2);

        if(getIntent().getExtras() != null && getIntent().hasExtra("position")){
            int position = getIntent().getExtras().getInt("position");
            Log.d("EDIT", "parsed position in list: " + position);
            showRecordValues(position);
        }
    }

    public void showRecordValues(int position){
        Record record = getRecordByPos(position);

        this.positionInList = position;
        this.updateFlag = true;

        if(record != null) {
            moduleNum.setText(record.getModuleNum());
            moduleName.setText(record.getModuleName());
            mark.setText(String.valueOf(record.getMark()));
            soseCheckbox.setChecked(record.isSummerTerm());
            halfWeightedCheckbox.setChecked(record.isHalfWeighted());
            creditPoints.setText(String.valueOf(record.getCredits()));
            year.setSelection(years.indexOf(record.getYear()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_start_search_module:
                Intent i = new Intent(this, ModuleSelectActivity.class);
                startActivityForResult(i, PICK_MODULE_NUM);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_MODULE_NUM){
            if(resultCode == Activity.RESULT_OK){
                String moduleNr  = data.getStringExtra("id");

                ExecutorService getByIdExecutor = Executors.newSingleThreadExecutor();
                Future<Module> moduleFuture = getByIdExecutor.submit(() ->
                        moduleDAO.findByNr(moduleNr));
                Module module = null;
                try {
                    module = moduleFuture.get();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e("ERROR", e.toString());
                }
                setModuleFields(module);
            }
        }
    }

    private void setModuleFields(Module module){
        moduleNum.setText(module.getNr());
        moduleName.setText(module.getName());
        creditPoints.setText(String.valueOf(module.getCrp()));
    }

    public void onSave(View view){
        Record record = new Record();

        boolean isValid = true;
        record.setModuleName(moduleName.getText().toString().trim());
        if("".equals(record.getModuleName())){
            moduleName.setError(getString(R.string.module_name_not_empty));
            isValid = false;
        }

        try{
            int credits = Integer.parseInt(creditPoints.getText().toString().trim());

            record.setCredits(Integer.parseInt(creditPoints.getText().toString().trim()));
            if(credits < 3 || credits > 15){
                creditPoints.setError(getString(R.string.illegal_cpr_number));
                isValid = false;
            }

            int note = 0;
            try {
                note = Integer.parseInt(mark.getText().toString().trim());
                if (!(note == 0 || note > 49 && note < 101)) {
                    throw new NumberFormatException();
                }
            }catch (NumberFormatException e){
                mark.setError(getString(R.string.illegal_mark));
                isValid = false;
            }

            record.setMark(note);
        }catch (NumberFormatException e){
            isValid = false;
        }

        Toast.makeText(this, year.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        int yearValue = Integer.parseInt(String.valueOf(year.getSelectedItem().toString()));


        if(isValid){
            record.setModuleName(moduleName.getText().toString().trim());
            record.setModuleNum(moduleNum.getText().toString().trim());
            record.setCredits(Integer.parseInt(creditPoints.getText().toString().trim()));
            record.setYear(yearValue);
            record.setIsHalfWeighted(halfWeightedCheckbox.isChecked());
            record.setSummerTerm(soseCheckbox.isChecked());


            Record recordInDAO = getRecordByPos(positionInList);
            if(updateFlag && recordInDAO != null){
                this.updateFlag = false;

                record.setId(positionInList);
                Executors.newSingleThreadExecutor().submit(() -> AppDatabase.getDb(this)
                        .recordDAO()
                        .update(record));

            }else {
                Executors.newSingleThreadExecutor().submit(() -> AppDatabase.getDb(this)
                        .recordDAO()
                        .persist(record));
            }
            finish();
        }
    }

    private Record getRecordByPos(int pos){
        ExecutorService getByIdExecutor = Executors.newSingleThreadExecutor();
        Future<Record> recordFuture = getByIdExecutor.submit(() -> AppDatabase.getDb(this)
                .recordDAO()
                .findById(pos));

        Record record = null;
        try {
            record = recordFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("ERROR", e.toString());
        }
        return record;
    }

    private ArrayList<Integer> getYears(){
        years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int yearsCount  = 5;

        for(int i = 0; i < yearsCount; i++){
            years.add(currentYear--);
        }
        return years;
    }
}
