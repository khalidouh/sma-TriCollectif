package com.univlyon1.sma.model;

import java.util.ArrayList;
import java.util.Objects;

public class Agent{

    private int moveIndex;
    private ArrayList<String> memory;
    private double kPos;
    private double kNeg;
    private double error;

    public Pheromonee pheromonee;

    private Environement environement;

    private Type takenType;
    private ArrayList<ArrayList<Creneau>> nearByCreneau;

    private ArrayList<Movement> possibleMovement;


    public Agent(int moveIndex, double kPos, double kNeg, Environement environement, int memorySize, double error) {
        this.moveIndex = moveIndex;
        this.kPos = kPos;
        this.kNeg = kNeg;
        this.error = error;

        this.environement = environement;
        this.memory= new ArrayList<>();
        for(int i =0 ; i<memorySize; i++){
            this.memory.add("0");
        }
        this.nearByCreneau = new ArrayList<>();
        this.possibleMovement = new ArrayList<>();
        this.takenType = null;
    }

    public void setTakenElement(Type takenType) {
        this.takenType = takenType;
    }

    public void action(){

        if(this.takenType != null){
            double dropProba = calculateProbaDrop(nearbySlotPercentage());
            if(Math.random()<dropProba){
                //drop algo
                ArrayList<Creneau> freeCreneaus = getFreeSlots();
                if(freeCreneaus.size()>0){
                    Creneau creneauToDrop = freeCreneaus.get((int) (Math.random() * freeCreneaus.size()));
                    environement.dropElementFromAgent(this, creneauToDrop);
                    return;
                }
            }
        }else{
            double pickUpAProba = getObjectFrequencyInMemory("A");
            double pickUpBProba = getObjectFrequencyInMemory("B");
            double pickUpProba;
            String letter;
            if(pickUpAProba > pickUpBProba){
                letter = "A";
                pickUpProba = calculateProbaPick(pickUpAProba);
            }else{
                letter = "B";
                pickUpProba = calculateProbaPick(pickUpBProba);
            }
            if(pickUpAProba == pickUpBProba){
                letter = null;
                pickUpProba = calculateProbaPick(pickUpAProba);
            }
            if(Math.random()<pickUpProba){
                //drop algo
                ArrayList<Creneau> occupiedSlotsByElements = getElementsCoordinates(letter);
                if(occupiedSlotsByElements.size()>0){
                    Creneau creneauWithElement = occupiedSlotsByElements.get((int) (Math.random() * occupiedSlotsByElements.size()));
                    this.addElmentmentMemory(creneauWithElement.getElement().getLetter());
                    environement.agentPickUpElement(this, creneauWithElement);
                    return;
                }

            }
        }

        if(possibleMovement.size()==0){
            return;
        }

        int alea = (int) (Math.random() * possibleMovement.size());
        Movement selectedMove = possibleMovement.get(alea);
        environement.moveAgents(this, selectedMove);
        this.addElmentmentMemory("0");
        return;
    }

    public void perception(){
        environement.agentPerception(this);
    }

    public int getMoveIndex() {
        return moveIndex;
    }

    public ArrayList<String> getMemory() {
        return memory;
    }


    public double calculateProbaPick(double f) {
        return Math.pow((this.kPos / (this.kPos + f)), 2);
    }

    public double calculateProbaDrop(double f) {
        return Math.pow((f / (this.kNeg + f)), 2);
    }

    public double getObjectFrequencyInMemory(String letter){
        int occurences = 0;
        for(int i = 0; i < this.memory.size(); i++){
                if(memory.get(i).compareTo(letter) == 0){
                    occurences++;
                }
        }
        return  occurences/this.memory.size();
    }

    public double nearbySlotPercentage(){
        int numberOfSlots = 0;
        int numberOfSameElements = 0;
        for(int i = 0; i<this.nearByCreneau.size(); i++){
            for(int j = 0; j<this.nearByCreneau.get(i).size(); j++){
                if(this.nearByCreneau.get(i).get(j).getElement()!=null && this.nearByCreneau.get(i).get(j).getElement().getLetter().compareTo(this.takenType.getLetter())==0){
                    numberOfSameElements++;
                }
                numberOfSlots++;
            }
        }
        return (double) numberOfSameElements/numberOfSlots;
    }

    private ArrayList<Creneau> getFreeSlots(){
        ArrayList<Creneau> freeCreneaus = new ArrayList<>();
        for(int i = 0; i<this.nearByCreneau.size(); i++){
            for(int j = 0; j<this.nearByCreneau.get(i).size(); j++){
                if(this.nearByCreneau.get(i).get(j).isFree()){
                    freeCreneaus.add(this.nearByCreneau.get(i).get(j));
                }
            }
        }
        return freeCreneaus;
    }

    private ArrayList<Creneau> getElementsCoordinates(String letter){
        ArrayList<Creneau> elementsCordinates = new ArrayList<>();
        for(int i = 0; i<this.nearByCreneau.size(); i++){
            for(int j = 0; j<this.nearByCreneau.get(i).size(); j++){
                if(this.nearByCreneau.get(i).get(j).getElement()!=null){
                    if(letter == null){
                        elementsCordinates.add(this.nearByCreneau.get(i).get(j));
                    }else{
                        if(this.nearByCreneau.get(i).get(j).getElement().getLetter().compareTo(letter)==0){
                            elementsCordinates.add(this.nearByCreneau.get(i).get(j));
                        }
                    }
                }
            }
        }
        return elementsCordinates;
    }

    public void addElmentmentMemory(String visualized){
        if(memory.size() == 10){
            memory.remove(0);
        }
        memory.add(visualized);
    };

    public double getkPos() {
        return kPos;
    }

    public double getkNeg() {
        return kNeg;
    }

    public Type getTakenElement() {
        return takenType;
    }


    public ArrayList<Movement> getPossibleMovement() {
        return possibleMovement;
    }

    public ArrayList<ArrayList<Creneau>> getNearByCreneau() {
        return nearByCreneau;
    }

    public void setNearByCreneau(ArrayList<ArrayList<Creneau>> nearByCreneau) {
        this.nearByCreneau = nearByCreneau;
    }

    public void setPossibleMovement(ArrayList<Movement> possibleMovement) {
        this.possibleMovement = possibleMovement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;
        Agent agent = (Agent) o;
        return getMoveIndex() == agent.getMoveIndex() &&
                Double.compare(agent.getkPos(), getkPos()) == 0 &&
                Double.compare(agent.getkNeg(), getkNeg()) == 0 &&
                Objects.equals(getMemory(), agent.getMemory()) &&
                Objects.equals(environement, agent.environement) &&
                Objects.equals(getTakenElement(), agent.getTakenElement()) &&
                Objects.equals(getNearByCreneau(), agent.getNearByCreneau()) &&
                Objects.equals(getPossibleMovement(), agent.getPossibleMovement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMoveIndex(), getMemory(), getkPos(), getkNeg(), environement, getTakenElement(), getNearByCreneau(), getPossibleMovement());
    }

}
