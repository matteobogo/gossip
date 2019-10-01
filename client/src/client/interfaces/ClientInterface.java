/**
 * ClientInterface.java
 */

package client.interfaces;

import serializable.Friend;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Matteo Bogo
 */
public interface ClientInterface extends Remote {
    
    /**
     * Restituisce l'username dell'utente
     * 
     * @return
     * @throws RemoteException 
     */
    String getUsername() throws RemoteException;
    
    /**
     * Restituisce la porta UDP dell'utente
     * 
     * @return
     * @throws RemoteException 
     */
    int getUserPort() throws RemoteException;
    
    /**
     * Restituisce l'indirizzo IP dell'utente
     * 
     * @return
     * @throws RemoteException 
     */
    String getUserAddress() throws RemoteException;
    
    /**
     * Notifica all'utente la lista dei contatti di ingresso
     * 
     * @param new_inputFriendList
     * @throws RemoteException 
     */
    void notifyUpdateInputFriendList(ArrayList<String> new_inputFriendList) throws RemoteException;
        
    /**
     * Notifica all'utente la lista dei contatti di uscita
     * 
     * @param outputFriendList
     * @throws RemoteException 
     */
    void notifyUpdateOutputFriendList(ArrayList<Friend> outputFriendList) throws RemoteException;
    
    /**
     * Notifica all'utente che un contatto lo ha aggiunto
     * 
     * @param friend
     * @throws RemoteException 
     */
    void notifyFriendAdd(Friend friend) throws RemoteException;
        
    /**
     * Notifica all'utente che un contatto lo ha rimosso
     * 
     * @param friend_username
     * @throws RemoteException 
     */
    void notifyFriendRemove(String friend_username) throws RemoteException;
    
    /**
     * Notifica all'utente il cambiamento di stato di un contatto
     * 
     * @param friend
     * @throws RemoteException 
     */
    void notifyFriendStatusChanged(Friend friend) throws RemoteException;
        
    /**
     * Verifica la connessione del Client
     * 
     * @throws RemoteException 
     */
    void isAlive() throws RemoteException;
}