package com.paris.IOU;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/8/12
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Owe implements Serializable {

    private long id;
    private String name;
    private double oweAmount;
    private String description;
    private String dateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Owe owe = (Owe) o;

        if (name != null ? !name.equals(owe.name) : owe.name != null) return false;

        return true;
    }

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
