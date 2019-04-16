package com.mis.relife.data.model;

public class Food {
    public String food;
    public int number;
    public int cal;

    public Food(String food, int number, int cal) {
        this.food = food;
        this.number = number;
        this.cal = cal;
    }

    public Food(){}

    @Override
    public String toString() {
        String txt="\n\t--- food ---\n";
        txt+="\tfood:\t"+food+"\n";
        txt+="\tnumber:\t"+String.valueOf(number)+"\n";
        txt+="\tcal:\t"+String.valueOf(cal)+"\n";
        txt+="\t------------\n";
        return txt;
    }
}
