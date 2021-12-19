package com.FrederikDaniel_jmartMH.model;

import java.util.Date;

/**
 * Class pada package model yang akan membuat object Invoice
 */
public   class Invoice extends Serializable{
    public enum Status
    {
        WAITING_CONFIRMATION,
        CANCELLED,
        DELIVERED,
        ON_PROGRESS,
        ON_DELIVERY,
        COMPLAINT,
        FINISHED,
        FAILED
    }



    public Date date;
    public int buyerId;
    public int productId;
    public int complaintId;

}
