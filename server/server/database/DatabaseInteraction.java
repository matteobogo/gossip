/**
 * DatabaseInteraction.java
 */

package server.database;

import java.util.ArrayList;

/**
 *
 * @author Matteo Bogo
 */
public interface DatabaseInteraction {

    /**
     * Inizializzazione Database
     */
    void initDatabase();

    /**
     * Registra un utente sulla base di dati
     * 
     * @param username
     * @param password
     * @return 
     */
    boolean registerUser(String username, String password);
    
    /**
     * Restituisce la lista degli utenti registrati
     * 
     * @return 
     */
    ArrayList<String> getRegisteredUsers();

    /**
     * Verifica l'esistenza di un utente sulla base di dati
     * 
     * @param username
     * @return 
     */
    boolean checkUser(String username);

    /**
     * Verifica le credenziali di un utente sulla base di dati
     * 
     * @param username
     * @param password
     * @return 
     */
    boolean userAuthentication(String username, String password);

    /**
     * Verifica in quale lista dei contatti dell'utente e' presente il contatto
     * 
     * @param username
     * @param friend_username
     * @return 
     */
    int checkUserOnFriendList(String username, String friend_username);

    /**
     * Restituisce la lista dei contatti di ingresso dell'utente
     * 
     * @param username
     * @return 
     */
    ArrayList<String> getInputFriendList(String username);
    
    /**
     * Restituisce la lista dei contatti di uscita dell'utente
     * 
     * @param username
     * @return 
     */
    ArrayList<String> getFriendList(String username);

    /**
     * Aggiunge un contatto nelle liste dei contatti dell'utente
     * 
     * @param username
     * @param friend_username
     * @return 
     */
    boolean addFriend(String username, String friend_username);
    
    /**
     * Rimuove un contatto dalle liste dei contatti dell'utente
     * 
     * @param username
     * @param friend_username
     * @return 
     */
    int removeFriend(String username, String friend_username);
}