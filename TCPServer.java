import java.io.*; 
import java.io.DataInputStream; 
import java.net.*; 
import java.util.*;

//Doesn't implement a client request volume cap as specified in the lab
class TCPServer extends Thread{ 
    public static void main(String[] arg)throws Exception {

		
        ServerSocket server;
		DataOutputStream dos;
		DataInputStream dis;
		ArrayList<String> namesDB = new ArrayList<String>();
		boolean p1Here = false;
		boolean p2Here = false;
		Player p1 = null, p2 = null;
        
		server = new ServerSocket(7896);
		System.out.println("Server Started!");
		while (true)  
        {
			Socket client = null;
            try {
				
			client=server.accept(); 

			System.out.println("A new client has connected : " + client);
			dis=new DataInputStream(client.getInputStream()); 
			dos=new DataOutputStream(client.getOutputStream());

			String uName;
            uName = dis.readUTF();

			if(Register(uName,namesDB)){
				if (!p1Here){
					p1 = new Player(uName, client, dis, dos);
					dos.writeBoolean(true);
					p1Here = true;
					System.out.println("Matching!");
					
				}
				else if(p1Here && !p2Here){
					p2 = new Player(uName, client, dis, dos);
					dos.writeBoolean(true);
					p2Here = true;
					System.out.println("Found Match!");
					p1.GetDOS().writeInt(118);
					p2.GetDOS().writeInt(118);

					Thread match = new ClientHandler(p1, p1.GetDIS(),p1.GetDOS(), p2, p2.GetDIS(),p2.GetDOS());
					match.start();
					System.out.println("Assigned match.");
					p1 = null;
					p2 = null;
					p1Here = false;
					p2Here = false;
					
				}
			}
			else{
				dos.writeBoolean(false);
			}

		} catch(IOException e)  {
			client.close();
			System.out.println("Connection:"+e.getMessage());
		}
        }
    }
	public static boolean Register(String uName, ArrayList<String> namesDB){
      try {
         //System.out.println("Registering "+ getClientHost() + " as " + uName + "...");
         for(int i = 0; i < namesDB.size(); i++){
            if (uName.equals(namesDB.get(i))){
               System.out.println("Name in use. Could Not Register!");
               return false;
            }
         }
         namesDB.add(uName);
         PrintRegistered(namesDB);
         return(true);
      } catch(Exception e){
         System.out.println("FileImpl: "+e.getMessage());
         e.printStackTrace();
         return(false);
      }
   }
   public static void PrintRegistered(ArrayList<String> namesDB){
      System.out.println("\n\nCurrent User List:");
         Print("--------------------------------");
         Print("|                              |");
         for(int i = 0; i < namesDB.size(); i++){
         System.out.println("| " + namesDB.get(i));
         }
         Print("|                              |");
         Print("--------------------------------");
   }
   public static void Print(String str){ // Implemented quicker print method to save time
      System.out.println(str);
   }
}