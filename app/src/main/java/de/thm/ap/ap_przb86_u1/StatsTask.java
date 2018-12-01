package de.thm.ap.ap_przb86_u1;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

public class StatsTask extends AsyncTask<List<Record>, Void, Stats> {
    private RecordsActivity recordsActivity;

    public StatsTask(RecordsActivity recordsActivity) {
        this.recordsActivity = recordsActivity;
    }

    @Override
    protected Stats doInBackground(List<Record>... lists) {
        //calculate statistics
        Log.i(StatsTask.class.getName(), "Stats started");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(lists != null && lists.length > 0){
            Stats stats = new Stats(lists[0]);
            Log.i(StatsTask.class.getName(), "Stats calculated");
            return stats;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        // starting progress bar
    }

    @Override
    protected void onPostExecute(Stats stats) {
        // stop progress bar
        // show result dialog
        Log.i(StatsTask.class.getName(), "Showing statistics");
        recordsActivity.stopProgressBar();
        recordsActivity.setStatistics(stats);
    }

}
