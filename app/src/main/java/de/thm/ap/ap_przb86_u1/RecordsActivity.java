package de.thm.ap.ap_przb86_u1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordsActivity extends AppCompatActivity {
    private static boolean DEBUGGING = false;

    private boolean INITIALIZED = false;
    private ListView recordListView;
    private ProgressBar progressBar;
    private StringBuilder builder;
    private RecordDAO recordDAO;
    private List<Record> records;
    private ArrayAdapter<Record> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        builder        = new StringBuilder();
        recordListView = findViewById(R.id.records_list);
        recordDAO      = AppDatabase.getDb(this).recordDAO();
        records        = new ArrayList<>();
        adapter        = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, records);
        recordListView.setEmptyView(findViewById(R.id.records_list_empty));
        recordDAO.findAll().observe(this,
                records1 -> {
                    records = records1;
                    adapter.clear();
                    adapter.addAll(records);
                    recordListView.setAdapter(adapter);
                });

        recordListView.setOnItemClickListener((parent, view, position, id) -> {
            if(getIntent().getAction().equals(Intent.ACTION_PICK)){
                Log.d("ACTION", "action pick performed");
                int recordId = 0;
                try {
                    recordId = records.get(position).getId();
                }catch (Exception e){
                    setResult(RESULT_CANCELED);
                }
                Uri uriSelected = Uri.parse("content//content://de.thm.ap.ap_przb86_u1.cp/records_menu-db/" + recordId);
                setResult(RESULT_OK, new Intent().setData(uriSelected));
                finish();
            }else {
                Intent i = new Intent(RecordsActivity.this, RecordFormActivity.class);
                i.putExtra("position", records.get(position).getId());

                startActivity(i);
            }
        });
        recordListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                .Builder(UpdateModulesWorker.class, 30, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance()
                   .enqueueUniquePeriodicWork("updating modules", ExistingPeriodicWorkPolicy.KEEP, workRequest);

        if(DEBUGGING && !INITIALIZED){
                Executors.newSingleThreadExecutor()
                        .submit(() -> recordDAO.persist(new Record(
                                "CS1013", "Objektorientierte Programmierung",
                                2016, true, true, 6, 73)));
                Executors.newSingleThreadExecutor()
                        .submit(() -> recordDAO.persist(new Record(
                                "MN1007", "Diskrete Mathematik",
                                2016, false, true, 6, 81)));
                Executors.newSingleThreadExecutor()
                        .submit(() -> recordDAO.persist(new Record(
                                "LEL", "LUL",
                                2016, false, true, 6, 81)));
            INITIALIZED = true;
        }

        recordListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) { }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) { // Inflate the menu for the CAB
                getMenuInflater().inflate(R.menu.choose_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_delete:
                        Log.d("PRESSED", "delete action in choose menu performed");
                        Toast.makeText(RecordsActivity.this, "records_menu: " + records.size(), Toast.LENGTH_SHORT).show();
                        deleteSelectedItems();
                        Toast.makeText(RecordsActivity.this, "lol records_menu: " + records.size(), Toast.LENGTH_SHORT).show();
                        mode.finish();
                        return true;
                    case R.id.action_mail:
                        Log.d("PRESSED", "mail action in choose menu performed");
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "someone@protonmail.ch"});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Meine Leistungen");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hallo someone \n\nhier sind meine" +
                                        " Leistungen:\n\n" + getStatisticsForMail());

                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                                                    emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                        boolean isIntentSafe = activities.size() > 0;
                        if(isIntentSafe) {
                            startActivity(emailIntent);
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.records_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                Log.d("PRESSED", "add module action in menu performed");
                Intent i = new Intent(this, RecordFormActivity.class);
                startActivity(i);
                return true;

            case R.id.action_stats:
                Log.d("PRESSED", "statistics action in menu performed");
                progressBar = findViewById(R.id.indeterminate_bar);
                progressBar.setVisibility(View.VISIBLE);

                new StatsTask(this).execute(records);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
     }

    private void deleteSelectedItems(){
        new AlertDialog.Builder(this).
                setTitle(R.string.confirmation).
                setMessage(R.string.confirmation_message).
                setPositiveButton(R.string.yes, (dialog, which) -> {
                        ArrayList<Integer> selected = getSelectedItemsArray();
                        for(int i = selected.size() - 1; i >= 0; i--){
                            Toast.makeText(this, "records_menu: " + records.size(), Toast.LENGTH_SHORT).show();
                            removeById(selected.get(i));
                            Toast.makeText(this, "records_menu: " + records.size(), Toast.LENGTH_SHORT).show();
                            Log.d("REMOVING", "removed " + selected.get(i));
                        }
                        recordDAO.findAll();
                })
                .setIcon(R.drawable.id_dialog_alert_24dp)
                .setNegativeButton(R.string.no, (dialog, which) -> Log.d("PRESSED", "don't remove items")).show();
    }

    private String getStatisticsForMail() {
        ArrayList<Integer> selected = getSelectedItemsArray();
        builder.setLength(0);

        Record record;

        for(int i = 0; i < selected.size(); i++){
            int pos = selected.get(i);
            record = records.get(pos);

            if(record != null){
                builder.append(record.getModuleName());
                builder.append(" ");
                builder.append(record.getModuleNum());
                builder.append(" (");
                builder.append(record.getMark());
                builder.append("% ");
                builder.append(record.getCredits());
                builder.append(" crp");
                builder.append(")");
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public void showStatisticAlert(String stats) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.stats);
        builder.setMessage(stats);
        builder.setNeutralButton(R.string.close, null);
        builder.show();
    }

    public void setStatistics(Stats stats){
//        Stats stats = new Stats(records_menu);

        builder.setLength(0);
        builder.append("Leistungen: ");
        builder.append(stats.getSumModules());
        builder.append("\n50% Leistungen: ");
        builder.append(stats.getSumHalfWeighted());
        builder.append("\nSumme Crp: ");
        builder.append(stats.getSumCrp());
        builder.append("\nDurchschnitt: ");
        builder.append(stats.getAverageMark());
        builder.append("%\nCrp bis Ziel: ");
        builder.append(stats.getCrpToEnd());

        showStatisticAlert(builder.toString());
    }

    // getting selected items from listview
    private ArrayList<Integer> getSelectedItemsArray(){
        ArrayList<Integer> selected = new ArrayList<>();

        SparseBooleanArray checkedPositions = recordListView.getCheckedItemPositions();
        boolean run = true;

        for(int i = 0; i < this.records.size() && run; i++){
            try {
                if (checkedPositions.valueAt(i)) {
                    int selectedElem = checkedPositions.keyAt(i);

                    selected.add(selectedElem);
                    Log.d("CHECKED", "selected element is: " + selectedElem);
                }
            }catch (ArrayIndexOutOfBoundsException e){

        run = false;
    }
}
        return selected;
    }

    private void removeById(int id){
        Executors.newSingleThreadExecutor().submit(() ->
                recordDAO.remove(records.get(id).getId()));
    }

    public void stopProgressBar(){
        progressBar.setVisibility(View.GONE);
    }
}
