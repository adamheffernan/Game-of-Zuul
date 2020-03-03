import java.util.Stack;
import java.util.ArrayList;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version A3 Solution
 */

public class Game 
{
    private Parser parser;
    private boolean hasFired = false; 
    private boolean isCharged = false; 
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    private Room beamerRoom;
    private Beamer beamer;
    private boolean hasEaten = false; 
    Room outside, theatre, pub, lab, office, hole;
    ArrayList<Item> inventory = new ArrayList<Item>();
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        beamer = new Beamer(); 
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
         
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        
        Item chair, bar, computer, computer2, tree, Computer,cookie;
        
        // create some items
        chair = new Item("a wooden chair",5,"wc50");
        bar = new Item("a long bar with stools",95.67, "bws95");
        computer = new Item("a PC",10, "pc10");
        computer2 = new Item("a Mac",5, "ma5");
        tree = new Item("a fir tree",500.5, "ft50");
        cookie = new Item("A cookie",0.5, "ck0.5");
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        beamerRoom = new Room("Beamer Room"); 
        // put items in the rooms
        outside.addItem(tree);
        outside.addItem(tree);
        theatre.addItem(chair);
        pub.addItem(bar);
        theatre.addItem(cookie);
        pub.addItem(cookie);
        lab.addItem(chair);
        lab.addItem(computer);
        lab.addItem(chair);
        lab.addItem(computer2);
        office.addItem(chair);
        office.addItem(computer);
        
        hole = new TransporterRoom("in A Transporter room");
        
        // initialise room exits
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);
        hole.setExit("east",pub);
        
        pub.setExit("east", outside);
        outside.setExit("north",hole);
        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        beamerRoom = outside;
        currentRoom = outside;  // start game outside
        Computer = new Item("A Computer",50, "co50");
        outside.addItem(Computer);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if(commandWord.equals("inventory")){
           printInventory();
        }
        else if(commandWord.equals("take")){
           getItem(command);
           printInventory();
        }
        else if(commandWord.equals("drop")){
           removeItem();
           
        }
        else if(commandWord.equals("charge")){
         charge(command);
        }
        else if(commandWord.equals("fire")){
        fire(command);
        }
        // else command not recognised.
        return wantToQuit;
    }
    
    private void printInventory(){
         String output = "";
         for (int i = 0; i < inventory.size(); i++){
            output += inventory.get(i).getDescription() + "\n" +
            "This items short Description is: "  
            + inventory.get(i).name();
            }
            if(output == null){
            System.out.println("You are carrying nothing " + output);
        }
        else{
        System.out.println("You are carrying " + output);
        }
        
        
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }
     
    private void charge(Command command){
    if(isCharged == false){
    if (!command.hasSecondWord()){
    System.out.println("Charge what?");
    return; 
    }
    beamer.charge(currentRoom);
    System.out.println("Beamer is Charged");
    isCharged = true; 
}
else{
System.out.println("Beamer is already charged");
}
}
    private void fire(Command command){
    if(!command.hasSecondWord()){
    System.out.println("Fire what?");
    return;
    }
    Room nextRoom = beamer.fire();
    if(nextRoom == null){
     System.out.println("You tried to fire beamer " + 
     "but it is not charged");
    }
    else{
    currentRoom = nextRoom;
    isCharged = false; 
    System.out.println(currentRoom.getLongDescription()); 
    }
    } 
    
    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else if(inventory.size() == 0) {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
        else {
        
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println("\nYou are carrying a: " 
            + inventory.get(0).getDescription() + 
            "Who's short description is: " 
            + inventory.get(0).name());
            System.out.println(currentRoom.getLongDescription());
        }
    }
private void getItem(Command command) 
    {   
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Get what?");
            return;
        }
        
        
        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = currentRoom.getItems(item);
        
        if (newItem == null) {
            System.out.println("\nThat item is not here\n");
            return;
        }
        else if(inventory.size() >= 1 ){
         System.out.println("\nYou already have an item in your hand\n");
         return;
        }
        
        else {
            inventory.add(newItem);// add new item to inventory.
            
            System.out.println(newItem.name());
        }
    
}
    private boolean isFull(){
    if(inventory.size() >= 1 ){
        return true; 
}
    else{
        return false;
    }
    }
    
    
    private void removeItem() 
    {
       
        
        if(inventory.size() > 0) {
        Item newItem = inventory.get(0);
       
         
        if (newItem == null) {
            System.out.println("\nThat item is not here\n");
            return;
        }
        else if(inventory.get(0).name() == null){
         System.out.println("\nYou have nothing to drop\n");
         return;
        }
        
        else if (inventory.get(0).name() == newItem.name()){
            // add new item to inventory.
            inventory.remove(newItem);
            System.out.println("\n"+newItem.name()+"\n");
        }

           printInventory();
           System.out.println("\n Nothing");
        }
        else {
        
         System.out.println("You have nothing to drop\n");
        }
    }
   
       
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        
        else {
            if(isFull()  == true){
            // output the long description of this room
            System.out.println("\nYou are carrying a: " 
            + inventory.get(0).getDescription()+ 
            "Who's short description is: " 
            + inventory.get(0).name());
            System.out.println(currentRoom.getLongDescription());
        }
        else {
            System.out.println("\nYou are not carrying an item");
          System.out.println(currentRoom.getLongDescription());
        }
        }
    }
    
    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Eat what?");
        }
         else if(inventory.size() == 0) {
            // output that we have eaten
            System.out.println("\nYou have no food and need a Cookie\n");
        }
        else if(inventory.get(0).name() == "ck0.5"){
        System.out.println("You have eaten the cookie you picked up");
        hasEaten = true; 
        removeItem();
         
        }
        else{
        System.out.println("You can't eat this item");
        }
       
    }
    

    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }
}
