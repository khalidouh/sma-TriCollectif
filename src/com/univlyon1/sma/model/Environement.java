package com.univlyon1.sma.model;

import java.util.ArrayList;

/**
 *
 *  @author khalid Ouhmaid
 */
public class Environement {

    private Creneau myGrid[][];
    private int sizeN;
    private int sizeM;

    private int numberOfAgents;
    private int numberOfObjects;
    private int AGENT_MOVE_INDEX;

    private View view;
    private ArrayList<Agent> agentsList;
    private int x;
    private int y;

    public Environement(int sizeM, int sizeN, int numberOfAgents, int numberOfObject, int agentMoveIndex) {
        this.sizeM = sizeM;
        this.sizeN = sizeN;
        this.numberOfAgents = numberOfAgents;
        this.numberOfObjects = numberOfObject;
        this.AGENT_MOVE_INDEX =agentMoveIndex;
        this.agentsList = new ArrayList<>();
        myGrid = new Creneau[sizeM][sizeN];
        for (int i = 0; i < sizeM; i++) {
            for (int j = 0; j < sizeN; j++) {
                myGrid[i][j] = new Creneau(i, j);
            }
        }
        generateGrid();
    }

    public void generateGrid() {
        ArrayList<Creneau> freeCreneaus = new ArrayList();

        for (int i = 0; i < sizeM; i++) {
            for (int j = 0; j < sizeN; j++) {
                if (myGrid[i][j].getValue() == 0) {
                    freeCreneaus.add(myGrid[i][j]);
                }
            }
        }


        for(int i = 1; i<= numberOfAgents ; i++){
            int alea = (int) (Math.random() * freeCreneaus.size());
            agentsList.add(new Agent(this.AGENT_MOVE_INDEX,0.1,0.3, this, 10,0 ));
            myGrid[freeCreneaus.get(alea).getX()][freeCreneaus.get(alea).getY()].setAgent(agentsList.get(agentsList.size()-1));
            freeCreneaus.remove(alea);
        }


        //Generate all objects
        for(int i = 1; i<= numberOfObjects ; i++){
            int alea = (int) (Math.random() * freeCreneaus.size());
            myGrid[freeCreneaus.get(alea).getX()][freeCreneaus.get(alea).getY()].setElements(new Type("A"));
            freeCreneaus.remove(alea);
            alea = (int) (Math.random() * freeCreneaus.size());
            myGrid[freeCreneaus.get(alea).getX()][freeCreneaus.get(alea).getY()].setElements(new Type("B"));
            freeCreneaus.remove(alea);
            alea = (int) (Math.random() * freeCreneaus.size());
            myGrid[freeCreneaus.get(alea).getX()][freeCreneaus.get(alea).getY()].setElements(new Type("C"));
            freeCreneaus.remove(alea);
        }
    }


    public void setView(View view) {
        this.view = view;
    }


    public int getSizeN() {
        return sizeN;
    }


    public int getSizeM() {
        return sizeM;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public ArrayList<Agent> getAgentsList() {
        return agentsList;
    }


    public int getSlot(int i, int j) {
        return myGrid[i][j].getValue();
    }

    public int[] agentCoordinates(Agent agent){
        int[] coordinates = new int[2];
        for(int i = 0 ; i<this.myGrid.length; i++){
            for(int j = 0 ; j<this.myGrid[i].length; j++){
                if(myGrid[i][j].getAgent() == agent ){
                    coordinates[0]=i;
                    coordinates[1]=j;
                    break;
                }
            }
        }
        return coordinates;
    }

    public void moveAgents(Agent agent, Movement movement){
        int[] coordinates = agentCoordinates(agent);

        switch (movement){
            case UP:
               this.myGrid[coordinates[0]-agent.getMoveIndex()][coordinates[1]].setAgent(agent);
                break;
            case DOWN:
                this.myGrid[coordinates[0]+agent.getMoveIndex()][coordinates[1]].setAgent(agent);
                break;
            case RIGHT:
                this.myGrid[coordinates[0]][coordinates[1]+agent.getMoveIndex()].setAgent(agent);
                break;
            case LEFT:
                this.myGrid[coordinates[0]][coordinates[1]-agent.getMoveIndex()].setAgent(agent);
                break;
        }
        this.myGrid[coordinates[0]][coordinates[1]].setAgent(null);

    }

    public void agentPickUpElement(Agent agent, Creneau pickupCreneau){
        agent.setTakenElement(this.myGrid[pickupCreneau.getX()][pickupCreneau.getY()].getElement());
        this.myGrid[pickupCreneau.getX()][pickupCreneau.getY()].setElements(null);
    }

    public void dropElementFromAgent(Agent agent, Creneau dropCreneau){
        this.myGrid[dropCreneau.getX()][dropCreneau.getY()]
                .setElements(agent.getTakenElement());
        agent.setTakenElement(null);
    }

    public void notifyView(){
        this.view.repaint();
    }

    public void agentPerception(Agent agent){
        int[] coordinates = agentCoordinates(agent);

        //Possible mouvement
        int agentX = coordinates[0];
        int agentY = coordinates[1];

        ArrayList<Movement> autorizedMovements = new ArrayList<>();

        if((agentX-agent.getMoveIndex())>=0 && myGrid[agentX-agent.getMoveIndex()][agentY].isFree() ){
            autorizedMovements.add(Movement.UP);
        }
        if((agentY-agent.getMoveIndex())>=0 && myGrid[agentX][agentY-agent.getMoveIndex()].isFree()){
            autorizedMovements.add(Movement.LEFT);
        }
        if((agentX+agent.getMoveIndex())<this.sizeM && myGrid[agentX+agent.getMoveIndex()][agentY].isFree()){
            autorizedMovements.add(Movement.DOWN);
        }
        if((agentY+agent.getMoveIndex())<this.sizeN &&  myGrid[agentX][agentY+agent.getMoveIndex()].isFree()){
            autorizedMovements.add(Movement.RIGHT);
        }
        agent.setPossibleMovement(autorizedMovements);

        ArrayList<ArrayList<Creneau>> nearBySlots = new ArrayList<>();
        int x0 = agentX-agent.getMoveIndex();
        int xf = agentX+agent.getMoveIndex();
        int y0 = agentY-agent.getMoveIndex();
        int yf = agentY+agent.getMoveIndex();

        if(x0<0){
            x0=0;
        }
        if(xf> this.sizeM){
            xf=this.sizeM;
        }
        if(y0<0){
            y0=0;
        }
        if(yf> this.sizeN){
            yf=this.sizeN;
        }
        for(int i = x0; i<xf;i++){
            ArrayList<Creneau> creneaus = new ArrayList<>();

            for(int j = y0; j<yf;j++){
                creneaus.add(myGrid[i][j]);
            }
            nearBySlots.add(creneaus);
        }
        agent.setNearByCreneau(nearBySlots);
    }
    private boolean getCase(int i, int i1) {
        i= 0;
        i=0;
        return true;
    }

    @Override
    public String toString() {
        String s = new String();
        for (int i = 0; i < sizeM; i++) {
            for (int j = 0; j < sizeN; j++) {
                s += myGrid[i][j].getValue();
            }
            s += "\n";
        }
        return s;
    }
    // Partie 2 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private double wind = 0.1; // r dans le sujet, gère la vitesse d'évaporation des phéromones
    public void setWind(double wind){
        this.wind = wind;
    }
    /**
     * Demande aux cases de s'update puis
     * diffuse les phéromones
     *
     */
    public void updateGrid(){
        for(Creneau[] l : myGrid){
            for(Creneau c : l){
                c.pheromone=true;
            }
        }

        for(Creneau[] l : myGrid){
            for(Creneau c : l){
                if( c.hasPheromone() ){
                    difusePheromone(c);
                }
            }
        }
    }

    /**
     * diffuse le phéromone de voisin en voisin de façon récursive
     * @param c case de départ
     */
    public void difusePheromone(Creneau c){
        double intensity = c.pheromonee.getIntensity();
        int x = c.getX();
        int y = c.getY();
        if(intensity > 1){
            for(int i = -1; i<2; i++){ // on va aller chopper tous les voisins
                for(int j = -1; j<2; j++){
                    if( x+i >= 0 && x+i < this.x && y+j >= 0 && y+j < this.y){ // on verifie qu'on est dans le plateau
                        Creneau voisin =c ;
                        if( (!voisin.hasPheromone()) || voisin.pheromonee.intensity < intensity *0.75  ){   // on donne le pheromonee uniquement si le voisin n'en a pas ou en a un plus faible
                            voisin.setPheromone(new Pheromonee(intensity*0.75));
                            difusePheromone(voisin);
                        }
                        voisin.pheromonee.decreaseIntensity();
                    }
                }
            }
        }
    }






}
