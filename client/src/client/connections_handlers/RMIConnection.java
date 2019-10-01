/**
 * RMIConnection.java
 */

package client.connections_handlers;

import server.interfaces.ServerInterface;

/**
 *
 * @author Matteo Bogo
 */
public class RMIConnection {
    
    private boolean isConnected;
    private ServerInterface serverObject;
    
    public RMIConnection() {
        
        isConnected = false;
    }
    
    /* Setta lo stato della connessione a GOSSIP */
    public synchronized void setConnectionStatus(boolean status) {
        
        this.isConnected = status;
    }
    
    /* Restituisce lo stato della connessione a GOSSIP */
    public synchronized boolean getConnectionStatus() {
        
        return this.isConnected;
    }
    
    /* Setta il riferimento allo Skeleton di GOSSIP */
    public synchronized void setServerObject(ServerInterface serverObject) {
        
        this.serverObject = serverObject;
    }
    
    /* Restituisce il riferimento allo Skeleton di GOSSIP */
    public synchronized ServerInterface getServerObject() {
        
        return this.serverObject;
    }
}