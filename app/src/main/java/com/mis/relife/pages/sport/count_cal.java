package com.mis.relife.pages.sport;

import com.mis.relife.data.model.Sport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class count_cal {

    public List<String> sportType = new ArrayList<>(Arrays.asList("慢走","健走","拖地","慢跑","快跑","桌球","羽毛球","網球","棒壘球","高爾夫球",
            "排球","保齡球","籃球(半場)  ","籃球(全場)  ","足球","騎馬","太極拳","游泳(較快)","游泳(較慢)","划獨木舟",
            "划船比賽","瑜珈","跳舞(慢)","跳舞(快)","國標舞","有氧舞蹈","健康操","拳擊","園藝","製造","農林漁牧",
            "搬重物","腳踏車 10km/時  ","腳踏車 20km/時  ","腳踏車 30km/時","滑雪","攀岩","溜冰刀","飛盤","溜直排輪","跳繩"));
    public List<Double> sportLoss = new ArrayList<>(Arrays.asList(3.5,5.5,3.7,8.2,12.7,12.7,5.1,6.6,4.7,5.0,3.6,3.6,6.3,8.3,7.7,5.1,4.2,10.0,6.3,3.4,12.4,
            3.0,3.1,5.3,5.3,6.8,4.0,11.4,4.2,5.2,7.4,8.4,4.0,8.4,12.6,7.2,7.0,5.9,3.2,5.1,8.4));
    public count_cal(){

    }
    public int if_else_sport(String sport,int sport_min,int kg){
        int cal = 0;
        for (int i = 0;i < sportType.size();i++){
            if(sport.equals(sportType.get(i))){
                cal = sport_count_cal_all(sport_min,kg,sportLoss.get(i));
            }
        }
        return cal;
    }

    private int sport_count_cal_all(int sport_min,int kg,double Consumption) {
        int count_cal = 0;
        double min = (double) sport_min / 60.0;
        count_cal = (int) (Consumption * kg * min);
        System.out.println(Consumption + "!!!!!!!!!!!!!!!" +  min + "!!!!!!!!!!!!!!!" + count_cal + "!!!!!!!!!!!!!!!" + kg);
        return count_cal;
    }

}
