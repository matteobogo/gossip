/*
 * ServerImpl.java
 */

package server.implementations;

import serializable.Friend;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import server.database.DatabaseInteraction;
import server.database.DatabaseInteractionImpl;
import client.interfaces.ClientInterface;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import server.interfaces.ServerConstants;
import server.interfaces.ServerInterface;

/**
 *
 * @author Matteo Bogo
 */
public class ServerImpl extends UnicastRemoteObject implements ServerInterface, ServerConstants {
    
    /* Database */
    private DatabaseInteraction db;
    
    /* Utenti online */
    private Map<String,ClientInterface> onlineUsers;
    
    /* Thread Timeout Seeker */
    private TimeoutSeeker timeoutSeeker;
    
    /* Proxy Manager */
    private ProxyManager proxyManager;
        
    public ServerImpl() throws RemoteException {
        
        /* Inizializzazione Database */
        db = new DatabaseInteractionImpl();
        db.initDatabase();
        
        /* Inizializzazione Proxy Manager */
        proxyManager = new ProxyManagerImpl(db);
        proxyManager.init();
        
        /* Inizializzazione Lista Utenti Online */
        onlineUsers = new ConcurrentHashMap<String,ClientInterface>();
    }
    
    /* Esegue il Thread Timeout Seeker */
    public void startTimeoutSeeker() {
        
        /* Avvio il Thread per il controllo degli utenti in timeout */
        timeoutSeeker = new TimeoutSeeker(onlineUsers,db,proxyManager);
        timeoutSeeker.start();
    }
    
    @Override
    public synchronized void isAlive() throws RemoteException {
        
        /* Test Server online */
    }
    
    @Override
    public synchronized boolean registerUser(String username, String password) throws RemoteException {
        
        /* Controlli */
        if(username.isEmpty() || password.isEmpty()) {
            
            return false;
        }
        
        if(!db.registerUser(username, password)) {
            
            System.out.println("GOSSIP System: "+username+" registration failed");
            return false;
        }
        else {
            
            System.out.println("GOSSIP System: "+username+" registration is successful");
            
            /* Assegno l'utente ad un Proxy */
            proxyManager.insertUser(username);
                        
            return true;
        }
    }
    
    @Override
    public synchronized boolean checkUsernameAvailable(String username) throws RemoteException {
        
        /* Controllo username */
        if(username.isEmpty()) {
            
            return false;
        }
        
        boolean checkUserStatus = db.checkUser(username);
        
        if(checkUserStatus) {
            
            System.out.println("GOSSIP System: "+username+" already exist");
            return false;
        }
        else {
            
            System.out.println("GOSSIP System: "+username+" available");
            return true;
        }
    }
    
    @Override
    public synchronized boolean login(String username, String password, ClientInterface cb) throws RemoteException {
        
        /* Controlli */
        if(username.isEmpty() || password.isEmpty() || cb == null) {
            
            return false;
        }
        
        /* Autenticazione */
        boolean userMatched = db.userAuthentication(username, password);
         
        if(userMatched) {
            
            /* Utente Autenticato */
            
            /* Verifico se una callback dell'utente e' gia' presente */
            ClientInterface cb_exist = onlineUsers.get(username);
            
            /* Utente Online */
            if(cb_exist != null) {
                
                return false;
            }
                             
            /* Utente Offline: registro la callback */            
            /* Aggiungo l'utente connesso nella lista degli utenti online */
            onlineUsers.put(username, cb);
                                                                                    
            /* Recupero una lista con le informazioni riguardanti i contatti dell'utente */
            ArrayList<String> outputList = db.getFriendList(username);
            ArrayList<Friend> friendList = getFriendListInformations(outputList);
            
            /* Notifico all'utente le sue liste dei contatti */
            cb.notifyUpdateInputFriendList(db.getInputFriendList(username));
            cb.notifyUpdateOutputFriendList(friendList);
            
            /* Notifico ai contatti online dell'utente il cambiamento di stato
             * (utente online) 
             */
            notifyStatusChanged(new Friend(username,true,cb.getUserAddress(),cb.getUserPort()), outputList);  
            
            System.out.println("GOSSIP System: "+username+" is connected");
        }
           
        return userMatched;  
    }
    
    @Override
    public synchronized void logout(String username) throws RemoteException {
        
        /* Controlli */
        if(username.isEmpty()) {
            
            return;
        }
        
        /* Elimina l'utente disconnesso dalla lista degli utenti online */        
        ClientInterface cb = onlineUsers.get(username);
        
        if(cb == null) {
            
            /* Utente non esiste */
            return;
        }
            
        /* Utente esiste */
                          
        /* Recupero la lista dei contatti dell'utente */
        ArrayList<String> outputList = db.getFriendList(username);
        
        /* Recupero le informazioni del Proxy assegnato all'utente */
        String proxy_info = proxyManager.getProxyAssigned(username);
        String[] parts = proxy_info.split(":");
        
        String proxy_address = parts[1];        
        int proxy_port = Integer.parseInt(parts[2]);
                
        /* Notifico ai contatti online dell'utente disconnesso il cambiamento di stato
         * (utente offline).
         */
        notifyStatusChanged(new Friend(username,false,proxy_address,proxy_port),outputList);
                
        /* Rimuovo l'utente */
        onlineUsers.remove(username);
        
        System.out.println("GOSSIP System: "+username+" disconnected");
    }
    
    @Override
    public synchronized boolean addFriend(String username, String friend_username) throws RemoteException {
                
        /* Controlli */
        if(username.isEmpty() || friend_username.isEmpty() || username.equals(friend_username)) {
            
            return false;
        }
        
        /* Recupero la callback dell'utente */
        ClientInterface  cb = onlineUsers.get(username);
        
        /* Verifico che l'utente sia online */
        if(cb == null) {
            
            return false;
        }
        
        /* Verifico che il contatto non sia gia' stato aggiunto */
        int userOnFriendListStatus = db.checkUserOnFriendList(username, friend_username);
                        
        if(userOnFriendListStatus != USER_NOT_IN_LIST_ID) {
                            
            return false;
        }
        
        /* CASISTICA lista dove e' presente il contatto
         *
         * 0 contatto nella lista dei contatti di ingresso
         * 1 contatto nella lista dei contatti di uscita
         */
                
        /* Aggiungo il contatto */
        boolean friendAdded = db.addFriend(username, friend_username);
        
        if(!friendAdded) {
            
            return false;
        }
            
        System.out.println("GOSSIP System: "+username+"@"+cb.getUserAddress()+":"+
                cb.getUserPort()+" added "+friend_username);        
            
        /* Contatto aggiunto correttamente */
        
        /* CASISTICA delle notifiche
         *
         * 1. Contatto offline e amico: Notifico all'utente outputFriendListOffline aggiornata
         * 2. Contatto offline e non amico: Notifico all'utente inputFriendList aggiornata
         * 3. Contatto online e amico: Notifico al contatto inputFriendList e outFriendListOnline aggiornate
         *                             Notifico all'utente outputFriendListOnline aggiornata
         * 4. Contatto online e non amico: Notifico all'utente inputFriendList aggiornata
         */
        
        /* Verifico in quale lista dei contatti del contatto si trova l'utente */
        int userOnFriendListAfterAddFriend = db.checkUserOnFriendList(friend_username, username);
        
        if(userOnFriendListAfterAddFriend == USER_NOT_IN_LIST_ID) {
            
            /* Contatto non ha l'utente come amico 
             * CASO 2 + CASO 4
             */
            
            /* Lista dei contatti di ingresso dell'utente aggiornata */
            ArrayList<String> new_inputFriendList_user = db.getInputFriendList(username);
            
            /* Notifico all'utente la lista aggiornata */
            cb.notifyUpdateInputFriendList(new_inputFriendList_user);
            return true;
        }
        else {
            
            /* Il Contatto ha l'utente come amico
             * (Ovvero lo ha a sua volta aggiunto)
             * CASO 1 + CASO 3
             */
            
            /* Verifico lo stato del Contatto (Online/Offline) */
            ClientInterface friend_cb = onlineUsers.get(friend_username);
            
            if(friend_cb == null) {
                
                /* Contatto offline
                 * CASO 1
                 */
                
                /* Recupero il Proxy associato al contatto */
                String proxy_info = proxyManager.getProxyAssigned(friend_username);
                String[] parts = proxy_info.split(":");
        
                String proxy_address = parts[1];        
                int proxy_port = Integer.parseInt(parts[2]);
                
                /* Notifico all'utente il contatto aggiunto */
                cb.notifyFriendAdd(new Friend(friend_username,false,proxy_address,proxy_port));
                return true;
            }
            else {
                
                /* Contatto Online
                 * CASO 3
                 */
                
                /* Lista dei contatti di ingresso del contatto aggiornata */
                ArrayList<String> new_inputFriendList_friend = db.getInputFriendList(friend_username);
                                                                                
                /* Notifico al contatto che l'utente lo ha aggiunto */
                friend_cb.notifyFriendAdd(new Friend(username,true,cb.getUserAddress(),cb.getUserPort()));
                
                /* Notifico al contatto la sua lista dei contatti di ingresso aggiornata */
                friend_cb.notifyUpdateInputFriendList(new_inputFriendList_friend);
                
                /* Notifico all'utente il contatto che lo ha aggiunto */     
                cb.notifyFriendAdd(new Friend(friend_username,true,friend_cb.getUserAddress(),friend_cb.getUserPort()));
                
                return true;
            }
        }
    }
    
    @Override
    public synchronized boolean removeFriend(String username, String friend_username) throws RemoteException {
                
        /* Controlli */
        if(username.isEmpty() || friend_username.isEmpty() || username.equals(friend_username)) {
            
            return false;
        }
        
        System.out.println("GOSSIP System: "+username+" trying to remove "+friend_username);
                
        /* Verifico utente sia online */
        ClientInterface cb = onlineUsers.get(username);
        
        if(cb == null) {
            
            return false;
        }
        
        /* Verifico contatto esista */
        if(!db.checkUser(friend_username)) {
            
            System.out.println("GOSSIP System: "+friend_username+" not exist");
            return false;
        }
        
        /* Rimuovo il contatto */
        int operation = db.removeFriend(username, friend_username);
        
        System.out.println("GOSSIP System: "+username+" has removed "+friend_username);
        
        /* 
        Contatto rimosso dalla lista dei contatti di ingresso dell'utente
        Notifico all'utente la lista dei contatti di ingresso aggiornata in tempo
        reale
        */
        if(operation == 0) {
            
            /* Recupero la lista dei contatti di ingresso aggiornata dell'utente */
            ArrayList<String> new_inputFriendList_user = db.getInputFriendList(username);
            
            /* Notifico all'utente la lista dei contatti di ingresso aggiornata */
            cb.notifyUpdateInputFriendList(new_inputFriendList_user);
            
            return true;
        }
        
        /* 
        Contatto rimosso dalla lista dei contatti di uscita dell'utente
        Notifico all'utente il contatto rimosso in tempo reale, se il contatto
        eliminato e' online notifico anche a lui la rimozione
        */
        if(operation == 1) {
            
            /* Notifico all'utente il contatto rimosso */
            cb.notifyFriendRemove(friend_username);
            
            /* Verifico che il contatto sia online */
            ClientInterface friend_cb = onlineUsers.get(friend_username);
            
            if(friend_cb != null) {
                
                /* Contatto online: notifica rimozione */
                friend_cb.notifyFriendRemove(username);
            }
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public synchronized String getProxyInformation(String username) throws RemoteException {
        
        if(username.isEmpty()) {
            
            return null;
        }
        
        /* Recupero il Proxy assegnato all'utente */
        String proxy_info = proxyManager.getProxyAssigned(username);
        String[] parts = proxy_info.split(":");
        
        String proxy_address = parts[1];        
        int proxy_port = Integer.parseInt(parts[2]);
        
        return proxy_address+":"+proxy_port;
    }
    
    /* Costruisce una lista dei contatti di un utente contenente informazioni 
       sui contatti (username, stato, indirizzo IP, porta)
    */
    private ArrayList<Friend> getFriendListInformations(ArrayList<String> outputList ) throws RemoteException {

        ArrayList<Friend> friendList = new ArrayList<Friend>();
            
        for(int i = 0; i < outputList.size(); i++) {
                
            Friend friendInfo;
                
            /* Recupero l'istanza di un contatto*/
            String friend_username = outputList.get(i);
            ClientInterface friend_cb = onlineUsers.get(friend_username);

            if(friend_cb == null) {
                    
                /* Il contatto e' offline */
                
                /* Recupero le informazioni sul Proxy */                
                String proxy_info = proxyManager.getProxyAssigned(friend_username);
                
                String[] parts = proxy_info.split(":");
        
                String proxy_address = parts[1];        
                int proxy_port = Integer.parseInt(parts[2]);               
                
                friendInfo = new Friend(friend_username,false,proxy_address,proxy_port);
            }
            else {
                    
                /* Il contatto e' online */                    
                friendInfo = new Friend(friend_username,
                    true,friend_cb.getUserAddress(),friend_cb.getUserPort());
            }
                
            friendList.add(friendInfo);
        }
        
        return friendList;
    }

    /* Notifica il cambiamento di stato di un utente ai suoi contatti online */
    private void notifyStatusChanged(Friend new_userStatus, ArrayList<String> outputList) {
        
        for(int i = 0; i < outputList.size(); i++) {
            
            String friend_username = outputList.get(i);
            
            /* Recupero la callback del contatto */
            ClientInterface friend_cb = onlineUsers.get(friend_username);
            
            if(friend_cb != null) {
                
                /* Contatto Online */
                                
                try {
                
                    /* Notifico il cambiamento di stato dell'utente al contatto online */
                    friend_cb.notifyFriendStatusChanged(new_userStatus);
                    
                }catch(RemoteException e) {
                    
                    System.out.println("GOSSIP System: failed to notify to "+
                            friend_username+" updated status of "+
                            new_userStatus.getUsername());
                }
            }
        }
    }
    
    /* Shutdown GOSSIP */
    protected void shutdown() {
        
        /* Shutdown dei Threads */
        
        /* Interruzione Timeout Seeker */
        timeoutSeeker.interrupt();
        
        /* Interruzione dei Proxy */
        proxyManager.shutdown();
    }
}