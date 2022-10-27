package com.tutego.date4u.core.formData;

public class FilterSearchFormData {
    private int gender;
    private int attractedToGender;
    private int maxHornlength;
    private int minHornlength;
    private int minAge;
    private int maxAge;


    public FilterSearchFormData() {
        minAge=18;
        maxAge=99;
        minHornlength=0;
        maxHornlength=99;
    }

    public FilterSearchFormData(int gender, int attractedToGender, int maxHornlength, int minHornlength, int minAge, int maxAge) {
        this.gender = gender;
        this.attractedToGender = attractedToGender;
        this.maxHornlength = maxHornlength;
        this.minHornlength = minHornlength;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAttractedToGender() {
        return attractedToGender;
    }

    public void setAttractedToGender(int attractedToGender) {
        this.attractedToGender = attractedToGender;
    }

    public int getMaxHornlength() {
        return maxHornlength;
    }

    public void setMaxHornlength(int maxHornlength) {
        this.maxHornlength = maxHornlength;
    }

    public int getMinHornlength() {
        return minHornlength;
    }

    public void setMinHornlength(int minHornlength) {
        this.minHornlength = minHornlength;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public String toString() {
        return "FilterSearchFormData{" +
                "gender=" + gender +
                ", attractedToGender=" + attractedToGender +
                ", maxHornlength=" + maxHornlength +
                ", minHornlength=" + minHornlength +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                '}';
    }
}
