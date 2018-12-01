package de.thm.ap.webtester;

import android.content.Intent;
import android.os.AsyncTask;

public class SumCalcTask extends AsyncTask<Integer, Integer, Integer> {
    @Override
    protected Integer doInBackground(Integer... integers) {
        Integer sum = 0;

        for (Integer integer : integers) {
            sum += integer;
        }

        return sum;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}
