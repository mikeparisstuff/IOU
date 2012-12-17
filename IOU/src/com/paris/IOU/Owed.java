package com.paris.IOU;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/16/12
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Owed {

    private long id;
    private String name;
    private double owedAmount;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getOwedAmount() {
        return owedAmount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwedAmount(double oweAmount) {
        this.owedAmount = oweAmount;
    }

    //for printing name and amountf
    @Override
    public String toString() {
        return "You owe " + name + " $" + owedAmount;
    }
}
