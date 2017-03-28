
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import blackjack.message.Message;
import blackjack.message.MessageFactory;

public class ClientHandler implements Runnable{
	
	String studentName;
	InetAddress address;
    static ObjectOutputStream oos = null;
    static ObjectInputStream ois = null;
	static String fromUser;
	//137.190.250.174:8989

	
	
	public ClientHandler(String name, InetAddress ip)
	{
		studentName = name;
		address = ip;
	}

	@Override
	public void run() 
	{
		Socket myClient = null;

	    
		try {
			 
			//Sets up the streams for the client
			 Object fromServer;
				try {
					myClient = new Socket("137.190.250.174", 8989);
					oos = new ObjectOutputStream(myClient.getOutputStream());
					ois = new ObjectInputStream(myClient.getInputStream());


				}
				catch (ConnectException e1) 
				{
					System.out.println("Couldn't find server.");
				}

			    			      
				oos.writeObject(MessageFactory.getLoginMessage(studentName));
				oos.flush();
				


				//Reads the object from the server 
			    try {
					while ((fromServer = ois.readObject()) != null) 
					{
						Message connectionMsg = MessageFactory.getAckMessage();
					    System.out.println("Server: " + fromServer);
					    System.out.println("Server: " + connectionMsg);
					    MidtermGUI.textArea.append(fromServer+ "\n");
					    fromServer = "";
					   
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
		 	}
		 
		 		//Could not connect to the server
				catch(Exception e) 
		 	{
					System.out.println("Couldn't connect to the server");
				
		}
		 

}
}
