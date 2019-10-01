/**
 * FriendHandler.java
 */

package client.implementations;

import client.GUI.ChatWindow;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;

/**
 *
 * @author Matteo Bogo
 */
public class FriendHandler {
    
    /* Username Contatto */
    private String friend_username;
    
    /* Stato del Contatto */
    private boolean status;
    
    /* Indirizzo IP del Contatto */
    private String friend_address;
    
    /* Porta UDP del Contatto */
    private int friend_port;
    
    private ChatWindow chatWindow;
    private boolean isChatOpen;
    private boolean isChatVisible;
        
    private Data data;
            
    public FriendHandler(
            String friend_username,
            boolean status,
            String friend_address,
            int friend_port,
            Data data) {
        
        this.friend_username = friend_username;
        this.status = status;
        this.friend_address = friend_address;
        this.friend_port = friend_port;
        this.data = data;
        
        this.isChatOpen = false;
        this.isChatVisible = false;
    }
    
    /* Restituisce l'username del Contatto */
    public synchronized String getFriendUsername() {
        
        return this.friend_username;
    }
    
    /* Setta lo stato del Contatto */
    public synchronized void setStatus(boolean status) {
        
        this.status = status;
    }
    
    /* Restituisce lo stato del Contatto */
    public synchronized boolean getStatus() {
        
        return this.status;
    }
    
    /* Setta l'indirizzo IP del Contatto */
    public synchronized void setFriendAddress(String friend_address) {
        
        this.friend_address = friend_address;
    }
    
    /* Restituisce l'indirizzo IP del Contatto */
    public synchronized String getFriendAddress() {
        
        return this.friend_address;
    }
    
    /* Setta la porta UDP del Contatto */
    public synchronized void setFriendPort(int friend_port) {
        
        this.friend_port = friend_port;
    }
    
    /* Restituisce la porta UDP del Contatto */
    public synchronized int getFriendPort() {
        
        return this.friend_port;
    }
    
    /* Restituisce la finestra di chat del Contatto */
    public synchronized ChatWindow getChatWindow() {
        
        return this.chatWindow;
    }
    
    /* Setta lo stato della chat */
    public synchronized void setChatIsOpen(boolean status) {
        
        this.isChatOpen = status;
    }
    
    /* Restituisce lo stato della chat */
    public synchronized boolean getChatIsOpen() {
        
        return this.isChatOpen;
    }
    
    /* Setta la visibilita' della chat */
    public synchronized void setChatIsVisible(boolean status) {
        
        this.isChatVisible = status;
    }
    
    /* Restituisce la visibilita' della chat */
    public synchronized boolean getChatIsVisible() {
        
        return this.isChatVisible;
    }
    
    /* Restituisce le strutture dati dell'utente */
    public synchronized Data getData() {
        
        return this.data;
    }

    /* Esegue una finestra di chat */
    public void startChatWindow() {
                
        /* Chat */
        chatWindow = new ChatWindow(this);
        
        /* Autoposizionamento frame al centro dello schemo */
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        chatWindow.setLocation(dim.width/2-chatWindow.getSize().width/2, dim.height/2-chatWindow.getSize().height/2);
        chatWindow.setResizable(false);
        chatWindow.setVisible(true);

        this.isChatOpen = true;
        this.isChatVisible = true;
    }
    
    /* Shutdown chat */
    public synchronized void shutdownChatWindow() {
        
        SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {
                
                chatWindow.dispose();
            }
        });
        
        this.isChatOpen = false;
        this.isChatVisible = false;
    }    
}