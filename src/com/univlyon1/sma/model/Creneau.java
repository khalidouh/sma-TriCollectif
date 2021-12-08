package com.univlyon1.sma.model;

/**
 *
 * @author khalid Ouhmaid
 */
public class Creneau {
    private int value;
    private int x,y;
    private Agent agent;
    private Type type;

    private double limitPheromoneSensibility = 1.0;
    public boolean pheromone;
    public Pheromonee pheromonee;

    public int getValue() {
        if(agent != null){
            return 99;
        }
        if(type != null){
            if(type.getLetter().equals("A")){
                return 1;
            }else if(type.getLetter().equals("B")){
                return 2;
            } else{
                return 3;
            }
        }
        return 0;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        if(agent == null){
            this.value = 0;
            this.agent = null;
            return;
        }
        this.value = 64;
        this.agent = agent;
    }

    public Type getElement() {
        return type;
    }

    public void setPheromone(Pheromonee p){
        this.pheromone = true;
    }


    public boolean hasPheromone(){
        if (pheromone != false){
            return true;
        }
        return false;
    }




    public  boolean isFree(){
        return (this.agent == null && this.type ==null);
    }
    public void setElements(Type type) {

        if(type == null){
            this.value = 0;
            this.type = null;
            return;
        }

        if(type.getLetter().equals("A")){
            this.value= 2;
        }else if (type.getLetter().equals("B")) {
            this.value = 8;
        }else {
            this.value = 10;
        }
        this.type = type;
    }

    public Creneau(int x, int y) {
        this.value = 0;
        this.x = x;
        this.y = y;
    }
}