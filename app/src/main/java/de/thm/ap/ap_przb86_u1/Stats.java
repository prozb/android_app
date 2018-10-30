package de.thm.ap.ap_przb86_u1;

import android.util.Log;

import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

public class Stats {
    private static final int MAX_CP = 180;
    private int sumCrp;
    private int sumHalfWeighted;
    private int averageMark;
    private int sumModules;

    public Stats(List<Record> records) {
        calculateSumCrp(records);
        calculateAverageMark(records);

        this.sumModules = records.size();
    }

    private void calculateAverageMark(List<Record> records) {
        float marksSum   = 0f;
        float percentage = 0f;
        for(Record record : records){
            float between;
            if(!record.getMark().equals("null")) {
                int mark = 0;
                try {
                    mark = Integer.parseInt(record.getMark());
                }catch (NumberFormatException e){
                    Log.e("ERROR", "mark conversion error");
                }

                if (record.isHalfWeighted()) {
                    percentage += 0.5;
                    between = (float) 0.5 * mark;
                } else {
                    percentage += 1;
                    between = mark;
                }
                marksSum += between;
            }
        }

        if(percentage == 0){
            averageMark = 0;
        }else {
            averageMark = (int) (marksSum / percentage);
        }
    }

    private void calculateSumCrp(List<Record> records){
        int sum     = 0;
        int sumHalf = 0;
        for(Record r : records){
            int mark = 0;
            try {
                mark = Integer.parseInt(r.getMark());
            }catch (NumberFormatException e){
                Log.e("ERROR", "mark conversion error");
            }

            if(r.getMark().equals("null") || mark >= 50 && mark <= 100) {
                sum += r.getCrp();

                if(r.isHalfWeighted())
                    sumHalf++;
            }
        }

        sumHalfWeighted = sumHalf;
        sumCrp = sum;
    }

    public int getSumCrp() {
        return this.sumCrp;
    }

    public int getCrpToEnd() {
        return MAX_CP - sumCrp;
    }

    public int getSumHalfWeighted() {
        return sumHalfWeighted;
    }

    public int getAverageMark() {
        return averageMark;
    }

    public int getSumModules(){
        return this.sumModules;
    }
}
