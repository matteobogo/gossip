/**
 * MainServer.java
 */

package server.implementations;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import server.interfaces.ServerConstants;
import server.interfaces.ServerInterface;

/**
 *
 * @author Matteo Bogo
 */
public class MainServer implements ServerConstants {
        
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("GOSSIP Chat System");
        
        System.out.println("-------------------");
        System.out.println("1. Start Server    ");
        System.out.println("2. Stop Server     ");
        System.out.println("-------------------");
        
        ServerImpl server = null;
        boolean exit = false;
        
        while(!exit) {
            
            int userInput = in.nextInt();
            
            switch(userInput) {
                
                case 1:
                    
                    /* Start RMI Server */
                    
                    if(server != null) {
                        
                        System.out.println("GOSSIP System: Server already running");
                        break;
                    }
                    
                    try {
                        
                        //System.setSecurityManager(new RMISecurityManager());
            
                        /* Server RMI */
                        server = new ServerImpl();
                        server.startTimeoutSeeker();
            
                        /* Costruzione RMI Skeleton */
                        UnicastRemoteObject.unexportObject(server, true);
                        ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);
            
                        LocateRegistry.createRegistry(RMI_PORT);
            
                        Registry registry = LocateRegistry.getRegistry(RMI_PORT);
                        registry.rebind(RMI_SKELETON_NAME, stub);
                                    
                        System.out.println("GOSSIP System: Chat Server started");
            
                    }catch(RemoteException e) {
            
                        System.out.println("GOSSIP System: Communication Error");
                    }
                    
                break;
                    
                case 2:
                    
                    /* Shutdown RMI Server */
                    
                    if(server == null) {
                        
                        System.out.println("GOSSIP System: Server not started");
                        break;
                    }
                    
                    System.out.println("GOSSIP System: Server Shutdown in progress");
                    
                    server.shutdown();
                    
                    exit = true;
                    break;
            }
        }
        
        /* Chiudo risorse */
        in.close();
        
        System.out.println("GOSSIP System: Shutdown complete");
        System.out.println("Goodbye!");
        
        System.exit(0);
    }
}