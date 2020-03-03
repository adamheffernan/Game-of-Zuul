
/**
 * Write a description of class Beamer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Beamer
{
    // instance variables - replace the example below with your own
    private Room room;

    /**
     * Constructor for objects of class Beamer
     */
    public Beamer()
    {
        // initialise instance variables
        room = null; 
    }
    public void charge(Room room)
    {
    this.room = room;
    }
    public Room fire(){
    Room newLocation = room; 
    room = null; //discharge the beamer
    return newLocation; 
    }
    
}
