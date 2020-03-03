import java.util.Random;
/**
 * Write a description of class TransporterRoom here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
/**
 * TransporterRoom - Room subclass
 * A transporter room behaves a little bit differently from a normal room. Instead of
 * having specified neighbor rooms it puts the player in a random room when exited.
 */

public class TransporterRoom extends Room{
    /**
     * Creates a transporter room with the specified description.
     * @param roomDescription A description of the room.
     */
    public TransporterRoom(String roomDescription){
        super(roomDescription);
    }
    /**
     * Return a random room.
     * @direction The exit's direction.
     * @return A random room, if exit exists.
     *
     *
     */private Room findRandomRoom(){
        Random rand = new Random();
        Room rm;
        Room[] rmArr = new Room[4];
        rmArr[0] = super.getExit("east");
        rmArr[1] = super.getExit("west");
        rmArr[2] = super.getExit("south");
        rmArr[3] = super.getExit("north");
        rm = rmArr[rand.nextInt(rmArr.length)];
        return rm; 
        }
        
        
    public Room getExit(String direction){
        
         return findRandomRoom();
        } 
        
        
        
    }
 

