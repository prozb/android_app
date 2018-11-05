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
    private List<Record> records;

    public Stats(List<Record> records) {
        calculateSumCrp(records);
        calculateAverageMark(records);
        calculateSumOfHalfWeighted(records);
        this.records = records;
        this.sumModules = records.size();
    }

    private void calculateAverageMark(List<Record> records) {
        float marksSum   = 0f;
        float percentage = 0f;
        for(Record record : records){
            float between;
            if(record.getMark() != 0) {
                int mark = record.getMark();

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
            int mark = r.getMark();

            if(r.getMark() == 0 || mark >= 50 && mark <= 100) {
                sum += r.getCredits();

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

    private void calculateSumOfHalfWeighted(List<Record> records){
        int sum = 0;
        for(Record record : records){
            if(record.isHalfWeighted()){
                sum++;
            }
        }
        this.sumHalfWeighted = sum;
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
