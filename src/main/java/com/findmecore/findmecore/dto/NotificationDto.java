package com.findmecore.findmecore.dto;

/**
 * @author ShanilErosh
 */
public class NotificationDto {

    private int count;

    public NotificationDto(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void increment() {
        this.count++;
    }
}
