package de.thm.ap.ap_przb86_u1;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordsActivity extends AppCompatActivity {
    private static boolean DEBUGGING = true;
    private boolean INITIALIZED = false;
    private ListView recordListView;
    private ArrayAdapter<Record> adapter;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        sb = new StringBuilder();
        recordListView = findViewById(R.id.records_list);
        recordListView.setEmptyView(findViewById(R.id.records_list_empty));
        AppDatabase.getDb(this).recordDAO().findAll().observe(this,
                records -> {
                    if(records != null) {
                        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, records);
                        recordListView.setAdapter(adapter);
                    }
                });
        recordListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(RecordsActivity.this, RecordFormActivity.class);
            i.putExtra("position", position);

            startActivity(i);
        });
        recordListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        if(DEBUGGING && !INITIALIZED){
            Executors.newSingleThreadExecutor()
                    .submit(() -> AppDatabase.getDb(this).recordDAO().persist(new Record(
                            "CS1013", "Objektorientierte Programmierung",
                            2016, true, true, 6, 73)));
            Executors.newSingleThreadExecutor()
                    .submit(() -> AppDatabase.getDb(this).recordDAO().persist(new Record(
                            "MN1007", "Diskrete Mathematik",
                            2016, false, true, 6, 81)));
            Executors.newSingleThreadExecutor()
                    .submit(() -> AppDatabase.getDb(this).recordDAO().persist(new Record(
                            "LEL", "LUL",
                            2016, false, true, 6, 81)));
//            recordDAO.persist(new Record("CS1019", "Compilerbau", 2017, false, false, 6, 81));
//            recordDAO.persist(new Record("CS1020", "Datenbanksysteme", 2017, false, false, 6, 92));

            INITIALIZED = true;
        }
        //updateAdapter();

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
                        deleteSelectedItems();
//                        updateAdapter();
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
        getMenuInflater().inflate(R.menu.records, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                Log.d("PRESSED", "add module action in menu performed");
                Intent i = new Intent(this, RecordFormActivity.class);
                startActivity(i);
//                updateAdapter();
                return true;

            case R.id.action_stats:
                Log.d("PRESSED", "statistics action in menu performed");
                showStatisticAlert();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if(DEBUGGING && !INITIALIZED){
//            RecordFileDAO recordFileDAO = new RecordFileDAO(this);
//            recordFileDAO.persist(new Record("CS1013", "Objektorientierte Programmierung", 2016, true, true, 6, 73));
//            recordFileDAO.persist(new Record("MN1007", "Diskrete Mathematik", 2016, false, true, 6, 81));
//            recordFileDAO.persist(new Record("CS1019", "Compilerbau", 2017, false, false, 6, 81));
//            recordFileDAO.persist(new Record("CS1020", "Datenbanksysteme", 2017, false, false, 6, 92));
//
//            INITIALIZED = true;
//        }
//
//        adapter.clear();
//        adapter.addAll(new RecordFileDAO(this).findAll());

//
//        if(DEBUGGING && !INITIALIZED){
//            RecordFileDAO recordFileDAO = new RecordFileDAO(this);
//            recordFileDAO.persist(new Record("CS1013", "Objektorientierte Programmierung", 2016, true, true, 6, 73));
//            recordFileDAO.persist(new Record("MN1007", "Diskrete Mathematik", 2016, false, true, 6, 81));
//            recordFileDAO.persist(new Record("CS1019", "Compilerbau", 2017, false, false, 6, 81));
//            recordFileDAO.persist(new Record("CS1020", "Datenbanksysteme", 2017, false, false, 6, 92));
//
//            INITIALIZED = true;
//        }
//
//        updateAdapter();
     }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        new RecordFileDAO(this).close();
//    }

    private void updateAdapter(){
        RecordsActivity.this.adapter.clear();
//        LiveData<List<Record>> recordsLive = AppDatabase.getDb(this).recordDAO().findAll();
//        RecordsActivity.this.adapter.addAll(recordsLive.getValue());
        Executors.newSingleThreadExecutor()
                .submit(() -> AppDatabase.getDb(this).recordDAO().persist(new Record(
                        "CS50", "Programmierung",
                        2016, true, true, 6, 73)));
    }

    private void deleteSelectedItems(){
        new AlertDialog.Builder(this).
                setTitle(R.string.confirmation).
                setMessage(R.string.confirmation_message).
                setIcon(R.drawable.id_dialog_alert_24dp).
                setPositiveButton(R.string.yes, (dialog, which) -> {
                    ArrayList<Integer> selected = getSelectedItemsArray();
                    for(int i = selected.size() - 1; i >= 0; i--){
                        AppDatabase.getDb(this).recordDAO().remove(selected.get(i));
                        Log.d("REMOVING", "removed " + selected.get(i));
                    }
                }).
                setNegativeButton(R.string.no, (dialog, which) -> Log.d("PRESSED", "don't remove items")).show();
    }

    private String getStatisticsForMail() {
        ArrayList<Integer> selected = getSelectedItemsArray();
        sb.setLength(0);

        Record record;

        for(int i = 0; i < selected.size(); i++){
            int pos = selected.get(i);
            record = new RecordFileDAO(this).getRecord(pos);

            if(record != null){
                sb.append(record.getModuleName());
                sb.append(" ");
                sb.append(record.getModuleNum());
                sb.append(" (");
                sb.append(record.getMark());
                sb.append("% ");
                sb.append(record.getCredits());
                sb.append(" crp");
                sb.append(")");
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private void showStatisticAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.stats);
        builder.setMessage(getStatistics());
        builder.setNeutralButton(R.string.close, null);
        builder.show();
    }

    private String getStatistics(){
        Stats stats = new Stats(new RecordFileDAO(this).findAll());

        sb.setLength(0);
        sb.append("Leistungen: ");
        sb.append(stats.getSumModules());
        sb.append("\n50% Leistungen: ");
        sb.append(stats.getSumHalfWeighted());
        sb.append("\nSumme Crp: ");
        sb.append(stats.getSumCrp());
        sb.append("\nDurchschnitt: ");
        sb.append(stats.getAverageMark());
        sb.append("%n\nCrp bis Ziel: ");
        sb.append(stats.getCrpToEnd());

        return sb.toString();
    }

    // getting selected items from listview
    private ArrayList<Integer> getSelectedItemsArray(){
        List<Record> records        = new RecordFileDAO(this).findAll();
        ArrayList<Integer> selected = new ArrayList<>();

        SparseBooleanArray checkedPositions = recordListView.getCheckedItemPositions();

        for(int i = 0; i < records.size(); i++){
            if(checkedPositions.valueAt(i)){
                int selectedElem = checkedPositions.keyAt(i);

                selected.add(selectedElem);
                Log.d("CHECKED", "selected element is: " + selectedElem);
            }
        }

        return selected;
    }
}
