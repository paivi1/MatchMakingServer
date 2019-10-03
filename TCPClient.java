import java.io.*; 
import java.io.DataInputStream; 
import java.net.*;
import java.util.*;
 
class TCPClient { 
    public static void main(String arg[])throws Exception { 
        try {
            Scanner scn = new Scanner(System.in);
            boolean matching = false;

            //InetAddress ip = InetAddress.getByName("localhost");
            Socket server=new Socket("127.0.0.1", 7896); 
            DataInputStream dis=new DataInputStream(server.getInputStream());         
            DataOutputStream dos=new DataOutputStream(server.getOutputStream());

            PrintLine("Welcome to our server! Input <quit> to exit!"); // Messages to client on starts
            System.out.println("Login---------------------- ");
            Print("Please input your username:"); // Receive and send username. 
            String toSend = scn.nextLine();
            System.out.println("Your username: " + toSend);
            dos.writeUTF(toSend);
            if(toSend.equals("quit")){
                System.out.println("Closing this connection : " + server); 
                server.close(); 
                System.out.println("Connection closed");
            }

            boolean registered = dis.readBoolean(); // Can't use same username as someone else.
            if(!registered){
                System.out.println("Could not register that Username. Closing this connection : " + server); 
                server.close(); 
                System.out.println("Connection closed. Please login again.");
            }else{
                matching=true; // Set matching to true to show that we are now looking for a second player to pair the first with
            }

            Timer timer = new Timer();
            timer.schedule(new RunnableClass(), 0, 2000); // Output messages to client while waiting

            while(matching == true){
                int matchingCode = dis.readInt(); // Wait for a match code that signals the found match and start of game
                if(matchingCode == 118){
                    timer.cancel();
                    clearScreen();
                    PrintLine("Found Match!\n");
                    Match(dis, dos, scn); // Run the client-size match stub
                }
                
            }
            scn.close(); 
            dis.close(); 
            dos.close(); 
		}catch (UnknownHostException e){
			System.out.println("Sock:"+e.getMessage()); 
		}catch (EOFException e){
			System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){
			System.out.println("IO:"+e.getMessage());
		}
    }
    public static void Match(DataInputStream dis, DataOutputStream dos, Scanner scn){
        while(true){
            try{
                switch(dis.readInt()){
                    case 117:
                        System.in.read(new byte[System.in.available()]);
                        PrintLine("You're first! You're 1's!\n");
                        PrintLine(dis.readUTF()); //Board Print
                        PrintLine(dis.readUTF()); //Message
                        PrintLine("-------------------------");
                        dos.writeInt(scn.nextInt()); //Write input
                        clearScreen();
                        PrintLine(dis.readUTF()); //Print updated board
                        PrintLine("Opponents Turn!\n");
                        break;
                    case 116:
                        System.in.read(new byte[System.in.available()]);
                        clearScreen();
                        PrintLine(dis.readUTF()); //Board Print
                        PrintLine(dis.readUTF()); //Message
                        PrintLine("-------------------------");
                        dos.writeInt(scn.nextInt()); //Write input
                        clearScreen();
                        PrintLine(dis.readUTF()); //Print updated board
                        PrintLine("Opponents Turn!\n");
                        break;
                    case 115:
                    try{
                        clearScreen();
                        PrintLine(dis.readUTF()); //Board Print
                        System.out.println("You've won!");
                        dis.close();
                        dos.close();
                        System.exit(0);
                    }catch (IOException e){
                        System.out.println("IO:"+e.getMessage());
                    }
                        
                    case 114:
                        try{
                            clearScreen();
                            PrintLine(dis.readUTF()); //Board Print
                            System.out.println("You've lost!");
                            dis.close();
                            dos.close();
                            System.exit(0);
                        }catch (IOException e){
                            System.out.println("IO:"+e.getMessage());
                        }
                        
                    case 113:
                    try{
                        clearScreen();
                        PrintLine(dis.readUTF()); //Board Print
                        System.out.println("You've Tied!");
                        dis.close();
                        dos.close();
                        System.exit(0);
                    }catch (IOException e){
                        System.out.println("IO:"+e.getMessage());
                    }
                        
                    default:
                        PrintLine("You shouldnt even see the default case.");
                }
            }catch(IOException e){
                PrintLine("IO:"+e.getMessage());
                System.exit(0);
            }catch(InterruptedException e){
                PrintLine("Interrupt:"+e.getMessage());
                System.exit(0);
            }
        }
    }
    public static void clearScreen() throws IOException, InterruptedException{  
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
    }
    public static void Print(String str){ //Implemented quicker print methods to save time
      System.out.print(str);
    } 
    public static void PrintLine(String str){
      System.out.println(str);
    } 
}