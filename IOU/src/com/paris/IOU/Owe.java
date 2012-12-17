package com.paris.IOU;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/8/12
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Owe {

    private long id;
    private String name;
    private double oweAmount;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getOweAmount() {
        return oweAmount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOweAmount(double oweAmount) {
        this.oweAmount = oweAmount;
    }

    //Will be used by the array adapter in the ListView

    @Override
    public String toString() {
        return "You owe " + name + " $" + oweAmount;
    }

}
