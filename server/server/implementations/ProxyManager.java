/**
 * ProxyManager.java
 */

package server.implementations;

/**
 *
 * @author Matteo Bogo
 */
public interface ProxyManager {
    
    /* Proxy Config File */
    final static String PROXY_CONFIG_PATH = "src/resources/proxy.txt";
    
    /**
     * Inizializzazione Proxy
     * 
     * @return 
     */
    boolean init();
    
    /**
     * Associa un utente ad un Proxy disponibile
     * 
     * @param username 
     * @return  
     */
    boolean insertUser(String username);
    
    /**
     * Restituisce le informazioni su un Proxy associato ad un utente
     * 
     * @param username
     * @return 
     */
    String getProxyAssigned(String username);
    
    /**
     * Termina i Proxy attivi
     */
    void shutdown();
}