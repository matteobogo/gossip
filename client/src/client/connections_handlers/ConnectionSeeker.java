/**
 * ConnectionSeeker.java
 */

package client.connections_handlers;

import client.GUI.MainWindow;
import client.implementations.Data;
import client.implementations.FriendHandler;
import client.interfaces.ClientConstants;
import server.interfaces.ServerInterface;
import java.awt.Color;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import javax.swing.SwingUtilities;

/**
 *
 * @author Matteo Bogo
 */
public class ConnectionSeeker extends Thread implements ClientConstants {
        
    /* Interfaccia Grafica */
    private MainWindow mainWindow;
    
    /* Tentativi di Connessione effettuati */
    private static int connections_attempts;
    
    public ConnectionSeeker(RMIConnection connection, MainWindow mainWindow) {
        
        this.mainWindow = mainWindow;
        
        connections_attempts = 0;
        
        this.setName("ConnectionSeeker-Thread");
    }
    
    @Override
    public void run() {
        
        System.out.println("Starting "+Thread.currentThread().getName());
        
        /* Prima Connessione a GOSSIP */
        firstConnection();
                
        /* Controllo periodicamente la disponibilita' di GOSSIP */
        while (!Thread.currentThread().isInterrupted()) {
            
            try {
                
                Thread.sleep(CONNECTION_SEEKER_INTERVAL);
                
                if(!mainWindow.getRMIConnection().getConnectionStatus()) {
                    
                    /* Connessione interrotta */
                    System.out.println("Connection to GOSSIP timeout");
                    
                    Data data = mainWindow.getData();
                    
                    /* Chiudo tutte le Chat Aperte */
                    System.out.println("Closing all open chats");
        
                    for(Map.Entry<String,FriendHandler> entry : data.getFriendList().entrySet()) {
            
                        FriendHandler friendHandler = entry.getValue();
            
                        /* Controllo se la chat e' aperta */
                        if(friendHandler.getChatIsVisible()) {
                
                            System.out.println("Closing Chat for ["+friendHandler.getFriendUsername()+"]");
                
                            friendHandler.shutdownChatWindow();
                        }
                    }
                    
                    while(connections_attempts < CONNECTION_ATTEMPTS) {
                        
                        /* Tentativo di connessione a GOSSIP */
                        for(int i = 0; i < SECONDS_BEFORE_CONNECTION_ATTEMPT; i++) {
                            
                            updateServerStatus("Connection Attempt #"+(connections_attempts+1)+
                                    " in "+(SECONDS_BEFORE_CONNECTION_ATTEMPT - i),0);
                            
                            Thread.sleep(CONNECTION_SEEKER_SECOND);
                        }
                        
                        boolean connectionAttemptStatus = connectToRMIServer();
                        
                        if(connectionAttemptStatus) {
                            
                            /* Connessione ristabilita */
                            connections_attempts = 0;
                            mainWindow.getRMIConnection().setConnectionStatus(true);
                            updateServerStatus("GOSSIP Online",1);
                            break;
                        }
                        else {
                            
                            connections_attempts++;
                        }
                    }
                    
                    /* Ho raggiunto il limite di tentativi di riconnessione */
                    if(connections_attempts == CONNECTION_ATTEMPTS) {

                        updateServerStatus("GOSSIP Offline, try again later",0);
                        break;                       
                    }
                }
                                
                try {
                    
                    /* Controllo se GOSSIP e' raggiungibile */
                    mainWindow.getRMIConnection().getServerObject().isAlive();
                    
                }catch(RemoteException e) {
                    
                    /* GOSSIP non e' piu' raggiungibile */
                    mainWindow.getRMIConnection().setConnectionStatus(false);
                }
                                
            }catch(InterruptedException e) {
                
                System.out.println(Thread.currentThread().getName()+" interrupted");
                Thread.currentThread().interrupt();
            }  
        }
    }
    
    /* Prima connessione a GOSSIP */
    private void firstConnection() {
        
        boolean firstConnectionStatus = connectToRMIServer();
        
        if(firstConnectionStatus) {
            
            mainWindow.getRMIConnection().setConnectionStatus(true);
            updateServerStatus("GOSSIP Online",1);
        }
        else {
            
            updateServerStatus("GOSSIP Offline - Attempts to Reconnect",0);
        }        
    }
    
    /* Notifica il cambio di stato del Server all'utente */
    private void updateServerStatus(String message, int operation) {
        
        SwingUtilities.invokeLater(new Runnable() { 
            
            @Override
            public void run() {
                
                mainWindow.getServerStatus().setText(message);
                
                if(operation == 0) {
                    
                    /* Server Offline */
                    mainWindow.getServerStatus().setForeground(Color.red);
                    mainWindow.getCardLayout().show(mainWindow.getMainPanel(),"loginPanel");
                }
                else {
                    
                    /* Server Online */
                    mainWindow.getServerStatus().setForeground(Color.green);
                }
            }
        });
    }
    
    /* Connessione a GOSSIP */
    public boolean connectToRMIServer() {
        
        /* Sovrascrivo l'indirizzo del Server GOSSIP (Default: localhost) */
        System.setProperty("java.rmi.server.hostname", DEFAULT_RMI_GOSSIP_HOST);
        
        System.out.println("Attempting to connect to GOSSIP RMI Server");
        System.out.println("Recovery GOSSIP Skeleton from RMI Registry @"+
                DEFAULT_RMI_GOSSIP_HOST+":"+DEFAULT_RMI_GOSSIP_PORT);
        
        Remote remoteObject;
        Registry registry;
        ServerInterface serverObject;
        
        try {
        
            /* Recupero RMI Registry */
            registry = LocateRegistry.getRegistry(DEFAULT_RMI_GOSSIP_PORT);
            
            /* Recupero Server Object */
            remoteObject = registry.lookup(DEFAULT_RMI_GOSSIP_STUB);
            serverObject = (ServerInterface) remoteObject;
            
            mainWindow.getRMIConnection().setServerObject(serverObject);
            
            System.out.println("RMI connection to GOSSIP established");
            
            return true;
                        
        }catch(RemoteException e) {
            
            System.out.println("Connection to GOSSIP failed");
            return false;
                        
        }catch(NotBoundException e) {
            
            System.out.println("GOSSIP Skeleton not found");
            return false;
        }
    }    
}