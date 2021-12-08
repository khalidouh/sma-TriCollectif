package com.univlyon1.sma.model;

import java.util.ArrayList;
import java.util.Random;

public class MegaRobot extends Robot {

    private Robot r1;
    private Robot r2;
    private boolean hasStarted = false;
    public boolean isActive = true;
    public MegaRobot(int nbCaseParPas, double kplus, double kminus, int memorySize, Robot r1, Robot r2){
        super(nbCaseParPas, kplus, kminus, memorySize);
        this.r1 = r1;
        this.r2 = r2;
    }
    
    private void deFusion(){
     /*   ArrayList<Case> voisins = grid.getVoisins(caseOn);
        Case r2Destination = null; 
        for (Case c : voisins){
            if(!c.hasRobot()){
                r2Destination = c;
            }*/
        }


        
        /*
        print(r1.caseOn);
        print(r2.caseOn);
        print("fin defusion");
        */


    public void start(){
        hasStarted = true;
        take(); // normalement le MegaRobot est créé sur une case avec un objet C
    }

    @Override
    protected void take() {

    }

    @Override  
    void perception(){ 
        perceptionHandFull();
        return; 
    }

    @Override
    void action(){
        Random random = new Random();
        float nb;
       
        float fC =  cInMemory  / memorySize;

        if(inHand != null){ // Devrait toujours être le cas, étant donné que dès que l'on lache l'objet le megarobot defusionne            
            nb = random.nextFloat();
            if( nb < Math.pow((double)fC/(kminus+(double)fC), 2) ){
                drop();
            }            
        }

        if(inHand == null){
            deFusion();
            return; 
        }
           // move();
    }

    @Override
    public void boucled() {
        if(!hasStarted){
            start();
        }
        perception();
        action();
        //print("fin boucle");
    }


    @Override
    protected void drop() {
        inHand = null;
        //deFusion();
    }

    public String toString(){
        return "MegaRobot";
    }
}
