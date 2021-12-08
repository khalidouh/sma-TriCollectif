package com.univlyon1.sma.model;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public abstract class Robot<Objet, Case, Grid> {
    public int nbCaseParPas; // Vitesse en cases parcourues/appel à move()
    public double kplus; // Facteur modifiant la chance de prise d'un objet
    public double kminus; // Facteur modifiant la chance de dépôt d'un objet
    public Queue<Character> memory; // Mémoire du robot
    public int memorySize; // Taille de la mémoire
    public Objet inHand; // Objet tenu par le robot
    public Case caseOn; // Case sur laquelle est le robot
    public Grid grid; // Grille sur laquelle est le robot
    public int x; // Position en abscisse du robot sur la grille
    public int y; // Position en ordonnée du robot sur la grille
    public int aInMemory = 0; // Nombre d'objets de type A en mémoire
    public int bInMemory = 0; // Nombre d'objets de type B en mémoire

    

    protected boolean aTakable = false;
    protected boolean bTakable = false;
    protected double error = 0; // Partie 1 : ajout d'une erreur dans la perception

    // PARTIE 2 //////////////////////////
    private double pheromoneIntensity = 2.5;
    protected boolean cTakable = false;
    public int cInMemory = 0;
    private int wait = 0;
    private boolean isSniffing = false; // true si on a sentit des pheromones à la derniere perception()
    private double chanceToSniff = 0.5; // Permet de moduler le % de chance que le robot s'intéresse aux phéromones
    private double stopWaitingChance = 0.02;
    public boolean isActive = true;
    private int coolDown = 0;  // Temps de repos actuel
    private boolean isCoolingDown = false; // Le robot est-il en repos
    private int coolDownTime = 1000; // Temps d'un repos
    //////////////////////////////////////
    

    public Robot(int nbCaseParPas, double kplus, double kminus, int memorySize) {
        this.nbCaseParPas = nbCaseParPas;
        this.kplus = kplus;
        this.kminus = kminus;
        this.memorySize = memorySize;
    }
    public Robot(int nbCaseParPas, double kplus, double kminus, int memorySize, double errorPercent) {
        this.nbCaseParPas = nbCaseParPas;
        this.kplus = kplus;
        this.kminus = kminus;
        this.memorySize = memorySize;
        this.error =  errorPercent;
    }

    public Robot(int x, int y, Grid g) {
        this.x = x;
        this.y = y;
        this.grid = g;
    }

    public void setRobot(int nbCaseParPas, double kplus, double kminus, int memorySize){
        this.nbCaseParPas = nbCaseParPas;
        this.kplus = kplus;
        this.kminus = kminus;
        this.memorySize = memorySize;
    }
    
    public void setError(double e){
        this.error = e;
    }
    
   /* public Move generateMove(){
        if(inHand != null || isSniffing == false || caseOn.pheromone == null){ // PARTIE 2 : Si on a un objet dans la main ou pas de pheromone sur la case on agit normalement
            return ( new Move( genererInt(-1,1) , genererInt(-1,1) , nbCaseParPas) ) ;
        }
        else{ // si ce n'est pas le cas on trouve un voisin où l'intensité du phéromone est plus fort et l'on se dirige par là
            double intensity = caseOn.pheromone.intensity;
            ArrayList<Case> voisins = grid.getVoisins(caseOn);
            int directionX = 0;
            int directionY = 0;
            for( Case c : voisins){
                if(c.hasPheromone() && c.pheromone.intensity > intensity){
                    directionX = c.x - caseOn.x;
                    directionY = c.y - caseOn.y;
                    return ( new Move( directionX , directionY , nbCaseParPas) ) ;
                }
            }
        }
        if (Main.DEBUG){
            System.out.println("Pheromone sur la case mais pas trouvé plus fort chez les voisins");
        }
        return ( new Move( genererInt(-1,1) , genererInt(-1,1) , nbCaseParPas) ) ;
    }*/

    public void setCaseOn(Case c){
        caseOn = c;
    }

    int genererInt(int borneInf, int borneSup){
        Random random = new Random();
        int nb;
        nb = borneInf+random.nextInt(1+borneSup-borneInf);
        return nb;
    }

    protected abstract void take();

    void perception(){
        if(inHand != null){
            perceptionHandFull();
            return;
        }
        //print("bip");

        Random rnd = new Random();
        double roll = rnd.nextDouble();
        if( roll > 1- error){ // On ne s'interesse pas a la partie error 
        // Pour les objets de type C car pas dans le sujet
            if(caseOn.equals('A')){
                memory.add('B');
            }
            else{
                memory.add('A');
            }
        }
        else{
           // memory.add(caseOn.toString());
        }
      /*  aInMemory = memory.getNumberOfPrimType('A');
        bInMemory = memory.getNumberOfPrimType('B');
        cInMemory = memory.getNumberOfPrimType('C');*/

        // PARTIE 2 : SNIFF DES PHEROMONES///////////////////////////////////////////////////////
        roll = rnd.nextDouble();
        if(roll < chanceToSniff){
            isSniffing = true;
        }
        else{
            isSniffing = false;
        }
        // PARTIE 2 : COOLDOWN///////////////////////////////////////////////////////////////////
        if(isCoolingDown){
            if(coolDown > coolDownTime){
                isCoolingDown = false;
                coolDown = 0;
            }
            else{
                coolDown++;
            }
        }
        
    }
    public void perceptionHandFull(){

    }

    void action(){
        Random random = new Random();
        float nb;
        // PARTIE 2 : ON EST EN ATTENTE D'AIDE///////////////////////////////////////////////////////
        if (isWaiting()){
            double roll = random.nextDouble();
            if(roll <  stopWaitingChance * wait ){ // On a une chance d'arreter d'attendre qui augmente plus on attend depuis longtemps
                wait = 0;
                startCooldown(); // On se met en cooldown pour pas boucler betement sur le meme objet
            }
            else{
                if(wait>5){ // Si on attend depuis plus de 5 tours on relache un phéromone et on reprend l'attente depuis le début
                    wait = 0;
                    dropPheromone();
                }
                wait++;
                return;
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////


        if(inHand != null){ // On ne s'interesse pas à C pour cette partie là car gérée par MegaRobot
            float f;
            if(inHand.equals('A') ){
                f = ( aInMemory + (float)(bInMemory * error) ) / memorySize;
            }
            else{
                f = ( bInMemory + (float)(aInMemory * error) ) / memorySize;
            }
            nb = random.nextFloat();
            if( nb < Math.pow((double)f/(kminus+(double)f), 2) ){
                drop();
            }
        }


        // PARTIE 2 ON CHANGE LE MOVE SELON QUE L'ON TIENNE QUELQUE CHOSE OU NON :
        // Si l'on tient quelque chose on agit normalement, sinon on prend en compte l'eventuelle présence de phéromones
        // Implémentation dans generateMove()
           // move();
    }

    protected void drop(){
        inHand = null;
        startCooldown(); /// TEST TO REMOVE PEUT ETRE -> Non en fait ça marche super donc je vais le garder
    }







    /**
     * Effectue la boucle perception - action
     */
    public void boucled(){
        perception();
        action();
       // memory.print();
    }


    public void setCooldownTime(int cooldownTime){
        this.coolDownTime = cooldownTime;
    }

    //////////////////////////////////////// PARTIE 2 ////////////////////////////////////////////////////////
    /**
     * Place un pheromone d'intensité prédéfinie sur la case courante
     */
    public void dropPheromone(){
        //this.caseOn.setPheromone(new Pheromone(pheromoneIntensity+1));
        // intensity +1 pour un soucis d'implémentation : lors de son
        // premier tour le phéromone est réduit avant d'être diffusé
    }

    /**
     * Lorsque le robot emet un phéromone pour demander de l'aide, il entre en 
     * attente.
     * @return true si le robot est en attente d'aide, false sinon.
     */
    public boolean isWaiting(){
        return wait!=0;
    }

    /**
     * Fusionne avec le robot qui demande de l'aide 
     * Pour créer un megarobot (de la mort qui tue)
     * @param
     */


    static void print(Object o){
        System.out.println(o);
    }

    public String toString(){
        return "Robot";
    }

    public void startCooldown(){
        isCoolingDown = true;
        coolDown = 0;
    }
}