package de.thm.ap.ap_przb86_u1;

import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

public class Stats {
    private static final int MAX_CP = 180;
    private int sumCrp;
    private int sumHalfWeighted;
    private int averageMark;

    public Stats(List<Record> records) {
        calculateSumCrp(records);
        calculateAverageMark(records);
    }

    private void calculateAverageMark(List<Record> records) {
        float sumCrpHalf = 0f;
        int sumMark = 0;

        for(Record record : records){
            if(record.isHalfWeighted()){
                sumCrpHalf += 0.5 * record.getCrp();
            }else{
                sumCrpHalf += record.getCrp();
            }
        }

//        averageMark = (int)()
    }

    private void calculateSumCrp(List<Record> records){
        int sum     = 0;
        int sumHalf = 0;
        for(Record r : records){
            if(r.getMark() == -1 || (r.getMark() >= 50 && r.getMark() <= 100)) {
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
}
