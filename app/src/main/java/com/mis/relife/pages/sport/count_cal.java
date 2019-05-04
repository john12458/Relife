package com.mis.relife.pages.sport;

public class count_cal {

    public count_cal(){

    }
    public int if_else_sport(String sport,int sport_min,int kg){
        int cal = 0;
        if(sport.equals("慢走")){
            cal = sport_count_cal_all(sport_min,kg,3.5);
        }
        else if(sport.equals("健走")){
            cal = sport_count_cal_all(sport_min,kg,5.5);
        }
        else if(sport.equals("拖地")){
            cal = sport_count_cal_all(sport_min,kg,3.7);
        }
        else if(sport.equals("慢跑")){
            cal = sport_count_cal_all(sport_min,kg,8.2);
        }
        else if(sport.equals("快跑")){
            cal = sport_count_cal_all(sport_min,kg,12.7);
        }
        else if(sport.equals("桌球")){
            cal = sport_count_cal_all(sport_min,kg,12.7);
        }
        else if(sport.equals("羽毛球")){
            cal = sport_count_cal_all(sport_min,kg,5.1);
        }
        else if(sport.equals("網球")){
            cal = sport_count_cal_all(sport_min,kg,6.6);
        }
        else if(sport.equals("棒壘球")){
            cal = sport_count_cal_all(sport_min,kg,4.7);
        }
        else if(sport.equals("高爾夫球")){
            cal = sport_count_cal_all(sport_min,kg,5);
        }
        else if(sport.equals("排球")){
            cal = sport_count_cal_all(sport_min,kg,3.6);
        }
        else if(sport.equals("保齡球")){
            cal = sport_count_cal_all(sport_min,kg,3.6);
        }
        else if(sport.equals("籃球(半場)  ")){
            cal = sport_count_cal_all(sport_min,kg,6.3);
        }
        else if(sport.equals("籃球(全場)  ")){
            cal = sport_count_cal_all(sport_min,kg,8.3);
        }
        else if(sport.equals("足球")){
            cal = sport_count_cal_all(sport_min,kg,7.7);
        }
        else if(sport.equals("騎馬")){
            cal = sport_count_cal_all(sport_min,kg,5.1);
        }
        else if(sport.equals("太極拳")){
            cal = sport_count_cal_all(sport_min,kg,4.2);
        }
        else if(sport.equals("游泳(較快)")){
            cal = sport_count_cal_all(sport_min,kg,10);
        }
        else if(sport.equals("游泳(較慢)")){
            cal = sport_count_cal_all(sport_min,kg,6.3);
        }
        else if(sport.equals("划獨木舟")){
            cal = sport_count_cal_all(sport_min,kg,3.4);
        }
        else if(sport.equals("划船比賽")){
            cal = sport_count_cal_all(sport_min,kg,12.4);
        }
        else if(sport.equals("瑜珈")){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!瑜珈");
            cal = sport_count_cal_all(sport_min,kg,3);
        }
        else if(sport.equals("跳舞(慢)")){
            cal = sport_count_cal_all(sport_min,kg,3.1);
        }
        else if(sport.equals("跳舞(快)")){
            cal = sport_count_cal_all(sport_min,kg,5.3);
        }
        else if(sport.equals("國標舞")){
            cal = sport_count_cal_all(sport_min,kg,5.3);
        }
        else if(sport.equals("有氧舞蹈")){
            cal = sport_count_cal_all(sport_min,kg,6.8);
        }
        else if(sport.equals("健康操")){
            cal = sport_count_cal_all(sport_min,kg,4);
        }
        else if(sport.equals("拳擊")){
            cal = sport_count_cal_all(sport_min,kg,11.4);
        }
        else if(sport.equals("園藝")){
            cal = sport_count_cal_all(sport_min,kg,4.2);
        }
        else if(sport.equals("製造")){
            cal = sport_count_cal_all(sport_min,kg,5.2);
        }
        else if(sport.equals("農林漁牧")){
            cal = sport_count_cal_all(sport_min,kg,7.4);
        }
        else if(sport.equals("搬重物")){
            cal = sport_count_cal_all(sport_min,kg,8.4);
        }
        else if(sport.equals("腳踏車 10km/時  ")){
            cal = sport_count_cal_all(sport_min,kg,4);
        }
        else if(sport.equals("腳踏車 20km/時  ")){
            cal = sport_count_cal_all(sport_min,kg,8.4);
        }
        else if(sport.equals("腳踏車 30km/時")){
            cal = sport_count_cal_all(sport_min,kg,12.6);
        }
        else if(sport.equals("滑雪")){
            cal = sport_count_cal_all(sport_min,kg,7.2);
        }
        else if(sport.equals("攀岩")){
            cal = sport_count_cal_all(sport_min,kg,7);
        }
        else if(sport.equals("溜冰刀")){
            cal = sport_count_cal_all(sport_min,kg,5.9);
        }
        else if(sport.equals("飛盤")){
            cal = sport_count_cal_all(sport_min,kg,3.2);
        }
        else if(sport.equals("溜直排輪")){
            cal = sport_count_cal_all(sport_min,kg,5.1);
        }
        else if(sport.equals("跳繩")){
            cal = sport_count_cal_all(sport_min,kg,8.4);
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
