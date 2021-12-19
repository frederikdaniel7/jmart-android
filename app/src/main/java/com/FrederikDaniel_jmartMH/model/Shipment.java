package com.FrederikDaniel_jmartMH.model;

import java.text.SimpleDateFormat;

/**
 * Class pada package model yang akan membuat object Shipment
 */
public class Shipment {
    public static final Plan INSTANT = new Plan((byte)(1 << 0));
    public static final Plan SAME_DAY = new Plan((byte)(1 << 1));
    public static final Plan NEXT_DAY = new Plan((byte)(1 << 2));
    public static final Plan REGULER = new Plan((byte)(1 << 3));
    public static final Plan KARGO = new Plan((byte)(1 << 4));
    public static final SimpleDateFormat ESTIMATION_FORMAT = new SimpleDateFormat("EEE MMMM dd yyyy");
    public String address;
    public int cost;
    public byte plan;
    public String receipt;

    static class Plan{
        public final byte bit;
        private Plan(byte bit)
        {
            this.bit = bit;
        }
    }
}
