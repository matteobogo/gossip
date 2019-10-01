/**
 * TimeoutSeeker.java
 */

package server.implementations;

import serializable.Friend;
import java.rmi.RemoteException;
import java.util.ArrayList;
import server.database.DatabaseInteraction;
import client.interfaces.ClientInterface;
import java.util.Map;
import server.interfaces.ServerConstants;

/**
 *
 * @author Matteo Bogo
 */
public class TimeoutSeeker extends Thread implements ServerConstants {
    
    private Map<String,ClientInterface> onlineUsers;
    private DatabaseInteraction db;
    private ProxyManager proxyManager;
    
    public TimeoutSeeker(Map<String,ClientInterface> onlineUsers, DatabaseInteraction db,
            ProxyManager proxyManager) {
        
        this.onlineUsers = onlineUsers;
        this.db = db;
        this.proxyManager = proxyManager;
        this.setName("TimeoutSeeker-Thread");
    }
    
    @Override
    public void run() {
        
        while(!Thread.currentThread().isInterrupted()) {
                                    
            ClientInterface cb;
            String username;
               
            /* Itero la lista degli utenti online */
            for(Map.Entry<String,ClientInterface> entry : onlineUsers.entrySet()) {
            
                cb = entry.getValue();
                username = entry.getKey();
                
                try {
                    
                    /* Controllo se l'utente e' online */
                    cb.isAlive();
                
                }catch(RemoteException e) {
                    
                    /* Client Timeout */
                    if(cb != null) {
                        
                        System.out.println("TimeoutSeeker: "+username+" TIMEOUT");
                        
                        /* Recupero le informazioni sul Proxy assegnato all'utente */
                        String proxy_info = proxyManager.getProxyAssigned(username);
                        String[] parts = proxy_info.split(":");
        
                        String proxy_address = parts[1];        
                        int proxy_port = Integer.parseInt(parts[2]);
                        
                        /* Setto lo stato dell'utente ad offline */
                        Friend new_userStatus = new Friend(username,false,proxy_address,proxy_port);

                        /* Rimuovo l'utente dalla lista degli utenti online */
                        onlineUsers.remove(username);
                        
                        /* Recupero la lista dei contatti online dell'utente */
                        ArrayList<String> outputFriendList = db.getFriendList(username);
                        
                        /* Notifico a tutti i contatti online il cambiamento di stato dell'utente */
                        for(int i = 0; i < outputFriendList.size(); i++) {
                 
                            String friend_username = outputFriendList.get(i);
                            
                            ClientInterface friend_cb = onlineUsers.get(friend_username);
                            
                            if(friend_cb != null) {
                                
                                /* Contatto Online */
                                
                                try {
                                
                                    /* Notifico al contatto il cambiamento di stato dell'utente */
                                    friend_cb.notifyFriendStatusChanged(new_userStatus);
                                    
                                }catch(RemoteException ex) {
                                    
                                    System.out.println("TimeoutSeeker: failed to notify updates to "+
                                                friend_username);
                                }
                            }  
                        }
                    }
                }
            }
            
            try {
                
                Thread.sleep(CHECKING_INTERVAL);
            
            }catch(InterruptedException e) {
                
                System.out.println(Thread.currentThread().getName()+" interrupted");
                Thread.currentThread().interrupt();
            }
        }
    }
}