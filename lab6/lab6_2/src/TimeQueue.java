import java.util.ArrayList;

public class TimeQueue {
    private ArrayList<Integer> timeList;
    private int pos;
    TimeQueue(){
        timeList = new ArrayList<Integer>();
        for (int i = 0; i < 3600; i++){
            timeList.add(2);
        }
        pos = 0;
    }

    public void add(int item){
        if(pos < 3599){
            timeList.set(pos, item);
            pos++;
        }
        else{
            timeList.remove(0);
            timeList.add(item);
        }
    }

    public int adTime(){
        int ad = 0;
        for(int i = 0; i < timeList.size(); i++){
            if(timeList.get(i) == 1){
                ad++;
            }
        }
        return ad;
    }
}
