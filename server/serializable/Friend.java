/**
 * Friend.java
 */

package serializable;

import java.io.Serializable;

/**
 *
 * @author Matteo Bogo
 */
public class Friend implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /* Username */
    private final String username;
    
    /* Stato */
    private final boolean online;
    
    /* Indirizzo IP contatto / Proxy */
    private final String address;
    
    /* Porta UDP contatto / TCP Proxy */
    private final int port;
    
    public Friend(String username, boolean online, String address, int port) {
        
        this.username = username;
        this.online = online;
        this.address = address;
        this.port = port;
    }
    
    /* Restituisce l'username */
    public String getUsername() {
        
        return this.username;
    }
    
    /* Restituisce lo stato */
    public boolean isOnline() {
        
        return this.online;
    }
    
    /* Restituisce l'indirizzo IP */
    public String getAddress() {
        
        return this.address;
    }
    
    /* Restituisce la porta UDP/TCP */
    public int getPort() {
        
        return this.port;
    }
}