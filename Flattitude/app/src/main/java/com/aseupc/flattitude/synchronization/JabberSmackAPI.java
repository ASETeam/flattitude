package com.aseupc.flattitude.synchronization;

import com.aseupc.flattitude.ChatArrayAdapter;
import com.aseupc.flattitude.Models.ChatMessage;

import java.util.*;
import java.io.*;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration.Builder;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;

public class JabberSmackAPI {

	private XMPPTCPConnection connection;
	private final String mHost = "54.218.39.214"; // server IP address or the host
	private MultiUserChat currentMUC;
	
	public void login(String userName, String password) throws Exception {
	    Builder config = XMPPTCPConnectionConfiguration.builder();
	    
	    config.setHost(mHost);
	    config.setPort(5222);
	    config.setServiceName("ip-172-31-40-57");
	    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
	    
	    config.setSendPresence(true);
	    config.setDebuggerEnabled(true);
	    
	    connection = new XMPPTCPConnection(config.build());
	    connection.connect();
	    connection.login(userName, password);
	    connection.setPacketReplyTimeout(20000);
		Log.e("HELLO", "It connects");
	}
	
	public void sendMessage(String message, String to) throws Exception {
	    ChatManager cm = ChatManager.getInstanceFor(connection);
	    cm.addChatListener(new ChatManagerListenerImpl());
	    
	    Chat chat = cm.createChat(to);    
	    chat.sendMessage(message);
	}
	
	public void displayBuddyList() {
		Roster roster = Roster.getInstanceFor(connection);
		
	    Collection<RosterEntry> entries = roster.getEntries();
	
	    System.out.println("\n\n" + entries.size() + " buddy(ies):");
	    for (RosterEntry r : entries) {
	        System.out.println(r.getUser());
	    }
	}
	
	public void disconnect() {
	    connection.disconnect();
	}
	
	public void processMessage(Chat chat, Message message) {
	    System.out.println("Received something: " + message.getBody());
	    if (message.getType() == Message.Type.chat)
	        System.out.println(chat.getParticipant() + " says: "
	                + message.getBody());
	}
	
	public void joinMUC (String roomName, String nickname) {
		try {
		    MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);

			String roomNameCorrected = roomName.replaceAll("\\s+","");

		    this.currentMUC = manager.getMultiUserChat(roomNameCorrected + "@conference.ip-172-31-40-57");
			this.currentMUC.join(nickname);
		} catch (Exception ex) {ex.printStackTrace();}
	}

	public void setReceiveListener(ChatArrayAdapter adapter) {
		this.currentMUC.addMessageListener(new MUCManagerListenerImpl(adapter));

	}

	public void sendGroupMessage(String message) {
		try {
			this.currentMUC.sendMessage(message);
		} catch (Exception ex) {
			ex.printStackTrace();;
		}
	}



	private class ChatManagerListenerImpl implements ChatManagerListener {

	    /** {@inheritDoc} */
	    @Override
	    public void chatCreated(final Chat chat, final boolean createdLocally) {
	        chat.addMessageListener(new ChatMessageListener()
	        {
	            public void processMessage(Chat chat, Message message)
	            {
	              System.out.println("Received message: " 
	                + (message != null ? message.getBody() : "NULL"));
	            }
	        });
	    }

	}
	
	private class MUCManagerListenerImpl implements MessageListener {

		private ChatArrayAdapter adapter;

		public MUCManagerListenerImpl(ChatArrayAdapter adapter) {
			this.adapter = adapter;
		}


	    /** {@inheritDoc} */
		@Override
		public void processMessage(Message message) {

            adapter.add(new ChatMessage(message.getBody(), true));
			/*System.out.println("Received message: "
	                + (message != null ? message.getBody() : "NULL"));*/
		}

	}
}

