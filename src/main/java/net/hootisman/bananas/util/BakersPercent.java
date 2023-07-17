package net.hootisman.bananas.util;

public class BakersPercent {
    private short amount;
    private short flourAmount;
    private float percent;
    public BakersPercent(short amount, short flourAmount){
        this.amount = amount;
        this.flourAmount = flourAmount;
        this.percent = (float) amount /flourAmount;
    }
    public float get(){
        return percent;
    }
}
