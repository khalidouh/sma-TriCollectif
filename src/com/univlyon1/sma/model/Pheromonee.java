package com.univlyon1.sma.model;
public class Pheromonee {
    public double intensity;

    public Pheromonee(double intensity){
        this.intensity = intensity;
    }

    public double getIntensity(){
        return intensity;
    }

    public void decreaseIntensity(){
        intensity --;
    }

    public void decreaseIntensity(double wind){
        intensity -= intensity*wind;
    }
}
