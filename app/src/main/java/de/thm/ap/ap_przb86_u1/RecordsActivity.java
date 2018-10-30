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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordsActivity extends AppCompatActivity {
    private static boolean DEBUGGING = true;
    private boolean INITIALIZED = false;
    private ListView recordListView;
    private ArrayAdapter<Record> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RecordDAO(this).close();

        setContentView(R.layout.activity_records);

        recordListView = findViewById(R.id.records_list);
        recordListView.setEmptyView(findViewById(R.id.records_list_empty));
        recordListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(RecordsActivity.this, RecordFormActivity.class);
            i.putExtra("position", position);

            startActivity(i);
        });
        recordListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        recordListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Toast.makeText(RecordsActivity.this, "chosed", Toast.LENGTH_SHORT).show();
            }

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
                        mode.finish();
                        return true;
                    case R.id.action_mail:
                        Log.d("PRESSED", "mail action in choose menu performed");
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "someone@protonmail.ch"});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Meine Leistungen");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hallo someone \n\nhier sind meine Leistungen:\n\n" + getStatistics());

                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
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
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
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
                Log.d("PRESSED", "add module action in menu performed");
                Intent i = new Intent(this, RecordFormActivity.class);
                startActivity(i);
                return true;

            case R.id.action_stats:
                Log.d("PRESSED", "statistics action in menu performed");
                showStatistic();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedItems(){
//        int selected = recordListView.getCheckedItemCount();
//        Toast.makeText(this, "removing " + selected + " items", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(this).
                setTitle(R.string.confirmation).
                setMessage(R.string.confirmation_message).
                setIcon(R.drawable.id_dialog_alert_24dp).
                setPositiveButton(R.string.yes, (dialog, which) -> {
                    ArrayList<Integer> selected = getSelectedItemsArray();

                    for(Integer s : selected){
                        new RecordDAO(this).remove(s);
                        Log.d("REMOVING", "pos " + s + "removed");
                    }
                    Log.d("PRESSED", "confirmed removing modules");
                    RecordsActivity.this.adapter.clear();
                    RecordsActivity.this.adapter.addAll(new RecordDAO(RecordsActivity.this).findAll());
//                    RecordsActivity.this.adapter.notifyDataSetChanged();
//                    mode.finish();
                }).
                setNegativeButton(R.string.no, (dialog, which) -> Log.d("PRESSED", "don't remove items")).show();
    }
    private void showStatistic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.stats);
        builder.setMessage(getStatistics());
        builder.setNeutralButton(R.string.close, null);
        builder.show();
    }

    private ArrayList<Integer> getSelectedItemsArray(){
        List<Record> records        = new RecordDAO(this).findAll();
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

    private String getStatistics(){
        Stats stats = new Stats(new RecordDAO(this).findAll());

        return  "Leistungen: " + stats.getSumModules()
                + "\n50% Leistungen: " + stats.getSumHalfWeighted()
                + "\nSumme Crp: " + stats.getSumCrp()
                + "\nDurchschnitt: " + stats.getAverageMark()
                + "%\nCrp bis Ziel: " + stats.getCrpToEnd();
    }
    @Override
    protected void onStart() {
        super.onStart();

        if(DEBUGGING && !INITIALIZED){
            RecordDAO recordDAO = new RecordDAO(this);
            recordDAO.persist(new Record("CS1013", "Objektorientierte Programmierung", 2016, true, true, 6, 73));
            recordDAO.persist(new Record("MN1007", "Diskrete Mathematik", 2016, false, true, 6, 81));
            recordDAO.persist(new Record("CS1019", "Compilerbau", 2017, false, false, 6, 81));
            recordDAO.persist(new Record("CS1020", "Datenbanksysteme", 2017, false, false, 6, 92));

            INITIALIZED = true;
        }

        List<Record> records = new RecordDAO(this).findAll();

        adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_activated_1, records);
        recordListView.setAdapter(adapter);
     }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        new RecordDAO(this).close();
    }
}
