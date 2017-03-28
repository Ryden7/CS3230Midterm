

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import blackjack.message.MessageFactory;




public class GUIv2 extends JFrame {


	private static final long serialVersionUID = 1L;
	JTextArea messageArea; 
	JTextField field;
	static JTextArea textArea;
	String name;
	static JTextArea playersCards;

	
	public static void main(String[] args) throws IOException 
	{
		new GUIv2();
	}
	
	public GUIv2() throws IOException
	{
		//52.35.72.251
		
		  InetAddress address;

		  JTextField nameField = new JTextField(5);
	      JTextField ipField = new JTextField(5);
	      
			textArea = new JTextArea(15, 30);
			textArea.setEditable(false);
			
			playersCards = new JTextArea(15, 30);
			playersCards.setEditable(false);
			
	      JPanel myPanel = new JPanel();
	      myPanel.add(new JLabel("Name"));
	      myPanel.add(nameField);
	      myPanel.add(new JLabel("IP address"));
	      myPanel.add(ipField);

	      int result = JOptionPane.showConfirmDialog(null, myPanel, 
	               "Please Enter Name and IP", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.CANCEL_OPTION) 
	      {
	    	  return;
	      }
	      
	      
			try {
				address = InetAddress.getByName(ipField.getText());
				ClientHandlerv2 ch = new ClientHandlerv2(nameField.getText(), address);
				name = nameField.getText();
				new Thread(ch).start();
				
			}
			 catch (Exception e1)
			{

			}
				
	    		

		
		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout(BoxLayout.Y_AXIS));	

		
		JButton button = new JButton("Send");


		add(panel);
		
		messageArea = new JTextArea(1, 1);
		field = new JTextField();
		
		messageArea.setLineWrap(true);
		messageArea.setSize(500, 500);
		messageArea.addKeyListener(new ClassListener());

		
		JScrollPane scrollPane = 
			new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(100, 100);
		
		//ClientHandlerv2.playersToCards.keySet().toString();
		
		panel.add(scrollPane);
		panel.add(messageArea);
		panel.add(button);
		panel.add(playersCards);
		
		
		
		


		
		setSize(600, 600);
		setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		button.addActionListener((e) -> sendMessage()); 

	}
	
	private void sendMessage()
	{
		{
			String text;
			text = messageArea.getText();
		

		messageArea.setText("");
		
		
		 	ClientHandlerv2.fromUser = text;
		 	
		 	String[] cmdCheck = text.split(" ");
		 	
		 	try{
			 	if (cmdCheck[0].equals("START"))
			 		ClientHandlerv2.oos.writeObject(MessageFactory.getStartMessage());
			 	else if (cmdCheck[0].equals("HIT"))
			 		ClientHandlerv2.oos.writeObject(MessageFactory.getHitMessage());
			 	else if (cmdCheck[0].equals("STAY"))
			 		ClientHandlerv2.oos.writeObject(MessageFactory.getStayMessage());
			 	else if (cmdCheck[0].equals("WIN"))
			 		ClientHandlerv2.oos.writeObject(MessageFactory.getWinMessage(name));
			 	else if (cmdCheck[0].equals("BUST"))
			 		ClientHandlerv2.oos.writeObject(MessageFactory.getBustMessage());
			 	else if (cmdCheck[0].equals("JOIN"))
			 		ClientHandlerv2.oos.writeObject(MessageFactory.getJoinMessage());
			 	else
					ClientHandlerv2.oos.writeObject(MessageFactory.getChatMessage(text));
			 	
			 	ClientHandlerv2.oos.flush();


		 	}
		 	catch(Exception e)
		 	{
		 		e.printStackTrace();
		 	}

		 	/*
		    if (ClientHandlerv2.fromUser != null) 
		    {
		        try
		        {
				} catch (IOException e) 
		        {
					e.printStackTrace();
				}
		    }
		    */
		 }

	}
	
	
	private class ClassListener implements KeyListener {
		
	    private final HashSet<Character> pressed = new HashSet<Character>();

		@Override
		public void keyPressed(KeyEvent arg0) 
		{
	        if (arg0.getKeyCode() == KeyEvent.VK_CONTROL)
	            pressed.add(arg0.getKeyChar());
	        
	        if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
	            pressed.add(arg0.getKeyChar());

			
			if (pressed.size() > 1)
			{
				if (!(messageArea.getText().equals(null)))
				{
					sendMessage();
			
				}
		
				else {
				}		
			}

			
			
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
	        pressed.remove(e.getKeyChar());
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	
	};
	

}
