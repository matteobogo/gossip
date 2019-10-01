/**
 * ProxyManager.java
 */

package server.implementations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import server.database.DatabaseInteraction;

/**
 *
 * @author Matteo Bogo
 */
public class ProxyManagerImpl implements ProxyManager {
    
    private DatabaseInteraction db;
    
    /* Lista Proxy attivi */
    private static ArrayList<Proxy> proxyList;
    
    /* Utenti assegnati ai Proxy */
    private static Map<String,String> usersAssignedToProxy;
    
    public ProxyManagerImpl(DatabaseInteraction db) {

        /* Database */
        this.db = db;

        /* Lista Proxy */
        proxyList = new ArrayList<Proxy>();

        /* Utenti assegnati ai Proxy */
        usersAssignedToProxy = new ConcurrentHashMap<String,String>();
    }
    
    /* Restituisce il Proxy con capacita' disponibile maggiore (bilanciamento) */
    private Proxy getProxyAvailable() {
        
        Proxy available_proxy = null;
        int capacity = 0;
        
        for(int i = 0; i < proxyList.size(); i++) {
            
            Proxy proxy = proxyList.get(i);
            int proxy_capacity = proxy.getAvailableCapacity();
                        
            if(proxy_capacity > capacity) {
                
                capacity = proxy_capacity;
                available_proxy = proxy;
            }
        }
        
        return available_proxy;
    }
    
    /* Inizializzazione Proxy */
    @Override
    public boolean init() {
        
        System.out.println("GOSSIP System: Loading Proxy Informations from "+
                PROXY_CONFIG_PATH);
        
        boolean proxyInfoLoaded = true;
        BufferedReader br = null;
        
        try {
        
            /* Recupero le informazioni sui Proxy: ID#IP#PORTA#CAPACITA */
            br = new BufferedReader(new FileReader(PROXY_CONFIG_PATH));
            String line;
            
            while ((line = br.readLine()) != null) {
                
                String[] proxy_data = line.split("#");
                
                if(proxy_data.length != 4) {
                    
                    System.out.println("GOSSIP System: Proxy config file invalid");
                    proxyInfoLoaded = false;
                    break;
                }
                
                String proxy_id = proxy_data[0];
                String proxy_address = proxy_data[1];
                String proxy_port = proxy_data[2];
                int proxy_capacity = Integer.parseInt(proxy_data[3]);
                
                System.out.println("GOSSIP System: Proxy "+
                        proxy_id+"@"+
                        proxy_address+":"+
                        proxy_port+" [Capacity: "+
                        proxy_capacity+"] loaded");
                
                /* Proxy */
                Proxy proxy = new Proxy(proxy_id, proxy_address, proxy_port,
                        proxy_capacity);
                
                Thread t = new Thread(proxy,"Proxy "+
                        proxy_id+"@"+
                        proxy_address+":"+
                        proxy_port+"-thread");
                
                t.start();
                proxyList.add(proxy);
            }
            
            /* Associo gli utenti registrati ai Proxy */
            
            /* Recupero lista utenti registrati a GOSSIP */
            ArrayList<String> registeredUsersList = db.getRegisteredUsers();
            
            for(int i = 0; i < registeredUsersList.size(); i++) {
                
                Proxy proxy = getProxyAvailable();
                
                if(proxy != null) {
                    
                    String username = registeredUsersList.get(i);
                    
                    proxy.insertUser(username);
                    usersAssignedToProxy.put(username,
                        proxy.getProxyID()+":"+
                        proxy.getProxyAddress()+":"+
                        proxy.getProxyPort());
                }
                else {
                    
                    System.out.println("GOSSIP System: No Proxy available for +"
                            + "storing users");
                    break;
                }
            }

        }catch(FileNotFoundException e) {
            
            System.out.println("GOSSIP System: failed to load Proxy config file");
            proxyInfoLoaded = false;
            
        }catch(IOException e) {
            
            System.out.println("GOSSIP System: failed to load Proxy informations");
            proxyInfoLoaded = false;
            
        }finally {
            
            try { br.close(); }catch(IOException e) { 
                System.out.println("GOSSIP System: loading proxy informations error"); }
        }
        
        return proxyInfoLoaded;
    }
    
    /* Associa un utente ad un Proxy */
    @Override
    public boolean insertUser(String username) {
        
        /* Recupero un Proxy disponibile */
        Proxy proxy = getProxyAvailable();
        
        if(proxy == null) {
            
            return false;
        }
        
        proxy.insertUser(username);
        usersAssignedToProxy.put(username,
                proxy.getProxyID()+":"+
                proxy.getProxyAddress()+":"+
                proxy.getProxyPort());
        
        return true;
    }
    
    /* Restituisce le informazioni sul Proxy associato ad un utente */
    @Override
    public synchronized String getProxyAssigned(String username) {
        
        return usersAssignedToProxy.get(username);
    }
    
    /* Shutdown dei Proxy */
    @Override
    public void shutdown() {
        
        /* Itero la lista dei Proxy */
        System.out.println("GOSSIP System: Starting shutdown all Proxy");
        
        for(int i = 0; i < proxyList.size(); i++) {
            
            proxyList.get(i).stopProxy();
        }
    }
}