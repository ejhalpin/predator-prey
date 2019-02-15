/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life;

/**
 *
 * @author Eugio
 */
public class Animal {
    //TO-DO: CAP THE HEALTH OF PREDATORS AND PREY
    private int type;
    private boolean hadTurn = false;
    private boolean didEat = false;
    private boolean didBreed = false;
    private int age;
    private double health;
    
    public Animal(int type, int age, double health){
        this.type = type;
        this.age = age;
        this.health = health;
        
        
    }
    
    public int type(){
        return this.type;
    }
    public void type(int type){
        this.type = type;  
    }
    public boolean hadTurn(){
        return this.hadTurn;
    }
    public void hadTurn(boolean hadTurn){
        this.hadTurn = hadTurn;
    }
    public boolean didEat(){
        return this.didEat;
    }
    public void didEat(boolean didEat){
        this.didEat = didEat;
    }
    public int age(){
        return this.age;
    }
    public void age(int age){
        this.age = this.age + age;
    }
    public double health(){
        return this.health;
    }
    public void health(double health){
        this.health = this.health + health;
    }
    public void didBreed(boolean bred){
        this.didBreed = bred;
    }
    public boolean didBreed(){
        return this.didBreed;
    }
}
