package com.paris.IOU;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/16/12
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Owed implements Serializable {

    private long id;
    private String name;
    private double owedAmount;
    private String description;
    private String dateTime;

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
