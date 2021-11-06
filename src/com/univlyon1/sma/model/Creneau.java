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

    public int getValue() {
        if(agent != null){
            return 99;
        }
        if(type != null){
            if(type.getLetter().equals("A")){
                return 1;
            }else{
                return 2;
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
        }else{
            this.value = 8;
        }
        this.type = type;
    }

    public Creneau(int x, int y) {
        this.value = 0;
        this.x = x;
        this.y = y;
    }
}