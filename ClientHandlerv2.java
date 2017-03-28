
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import blackjack.game.Card;
import blackjack.message.CardMessage;
import blackjack.message.ChatMessage;
import blackjack.message.GameStateMessage;
import blackjack.message.GameStateMessage.GameAction;
import blackjack.message.Message;
import blackjack.message.MessageFactory;

public class ClientHandlerv2 implements Runnable{
	
	String studentName;
	InetAddress address;
    static ObjectOutputStream oos = null;
    static ObjectInputStream ois = null;
	static String fromUser;
	protected static HashMap<String, Card> playersToCards;

	
	
	public ClientHandlerv2(String name, InetAddress ip)
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
			 Message fromServer;
				try {
					myClient = new Socket("52.35.72.251", 8989);
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
			    	while (true)
					//while ((fromServer = (Message) ois.readObject()) != null) 
					{
			    		fromServer = (Message) ois.readObject();
						switch(fromServer.getType())
						{
						case ACK: 
							System.out.println("Ack");
							
							break;
						
						case DENY:
							System.out.println("Deny");
							break;
							
						case CHAT:
							String chat = ((ChatMessage) fromServer).getText();
							String username = fromServer.getUsername();
							GUIv2.textArea.append(username + " : " + chat + "\n");
							break;
							
						case CARD:
							String Username = fromServer.getUsername();
							Card card = ((CardMessage) fromServer).getCard();
							playersToCards.put(Username, card);
							//GUIv2.playersCards.setText(text);
							GUIv2.playersCards.append(Username + " : " + card.toString()); 
							break;
						case GAME_STATE:
							GameAction gameState = ((GameStateMessage) fromServer).getRequestedState();
							System.out.println(gameState.toString());
							break;
						default:
							break;
							

						}

					}
				} 
			    catch (EOFException e1) 
			    {
					
				}
				
				
		 	}
		 
		 		//Could not connect to the server
				catch(Exception e) 
		 	{
					System.out.println("Couldn't connect to the server");
				
		}
		 

}
}
