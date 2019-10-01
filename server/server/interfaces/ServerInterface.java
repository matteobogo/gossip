/**
 * ServerInterface.java
 */

package server.interfaces;

import client.interfaces.ClientInterface;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Matteo Bogo
 */
public interface ServerInterface extends Remote {
    
    /**
     * Verifica la connessione del Server
     * 
     * @throws RemoteException 
     */
    void isAlive() throws RemoteException;

    /**
     * Registra un utente
     * 
     * @param username
     * @param password
     * @return
     * @throws RemoteException 
     */
    boolean registerUser(String username, String password) throws RemoteException;
    
    /**
     * Verifica la disponibilita' di un username
     * 
     * @param username
     * @return
     * @throws RemoteException 
     */
    boolean checkUsernameAvailable(String username) throws RemoteException;
    
    /**
     * Autenticazione di un utente 
     * 
     * @param username
     * @param password
     * @param cb
     * @return
     * @throws RemoteException 
     */
    boolean login(String username, String password, ClientInterface cb) throws RemoteException;
    
    /**
     * Disconnessione di un utente
     * 
     * @param username
     * @throws RemoteException 
     */
    void logout(String username) throws RemoteException;
    
    /**
     * Aggiunge un contatto
     * 
     * @param username
     * @param friend_username
     * @return
     * @throws RemoteException 
     */
    boolean addFriend(String username, String friend_username) throws RemoteException;
    
    /**
     * Rimuove un contatto
     * 
     * @param username
     * @param friend_username
     * @return
     * @throws RemoteException 
     */
    boolean removeFriend(String username, String friend_username) throws RemoteException;
        
    /**
     * Restituisce le informazioni sul Proxy di un utente
     * 
     * @param username
     * @return
     * @throws RemoteException 
     */
    String getProxyInformation(String username) throws RemoteException;
}