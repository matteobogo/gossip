/**
 * Data.java
 */

package client.implementations;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import serializable.Message;

/**
 *
 * @author Matteo Bogo
 */
public class Data {
    
    /* Lista Contatti */
    private Map<String,FriendHandler> friendList;
    
    /* Coda dei messaggi da inviare ai contatti online */
    private BlockingQueue<Message> messageQueue;
    
    /* Coda dei messaggi da inviare ai Proxy */
    private BlockingQueue<Message> proxyQueue;
            
    /* Username */
    private String username;
    
    /* Indirizzo IP */
    private String address;
    
    /* Porta UDP */
    private int port;
    
    public Data() {
        
        this.friendList = new ConcurrentHashMap<String,FriendHandler>();
        this.messageQueue = new LinkedBlockingQueue<Message>();
        this.proxyQueue = new LinkedBlockingQueue<Message>();
    }
    
    /* Restituisce la lista dei contatti di uscita dell'utente */
    public synchronized Map<String,FriendHandler> getFriendList() {
        
        return this.friendList;
    }
    
    /* Restituisce la coda dei messaggi in uscita verso i contatti */
    public synchronized BlockingQueue<Message> getMessageQueue() {
        
        return this.messageQueue;
    }
    
    /* Restituisce la coda dei messaggi in uscita verso i Proxy */
    public synchronized BlockingQueue<Message> getProxyQueue() {
        
        return this.proxyQueue;
    }
    
    /* Setta l'username dell'utente */
    public synchronized void setUsername(String username) {
        
        this.username = username;
    }
    
    /* Restituisce l'username dell'utente */
    public synchronized String getUsername() {
        
        return this.username;
    }
    
    /* Setta l'indirizzo IP dell'utente */
    public synchronized void setAddress(String address) {
        
        this.address = address;
    }
    
    /* Restituisce l'indirizzo IP dell'utente */
    public synchronized String getAddress() {
        
        return this.address;
    }
    
    /* Setta la porta UDP dell'utente */
    public synchronized void setPort(int port) {
        
        this.port = port;
    }
    
    /* Restituisce la porta UDP dell'utente */
    public synchronized int getPort() {
        
        return this.port;
    }
}