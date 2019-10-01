/**
 * Proxy.java
 */

package server.implementations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import serializable.Message;
import server.interfaces.ServerConstants;

/**
 *
 * @author Matteo Bogo
 */
public class Proxy implements Runnable, ServerConstants {
    
    private final String id;
    private final String address;
    private final String port;
    private int capacity;
    private int max_capacity;
    
    private ServerSocket ss;
    
    /* Lista Utenti assegnati al Proxy e code dei messaggi salvati */
    private final Map<String,BlockingQueue<Message>> usersList;

    private boolean isStopped;
    
    public Proxy(String id, String address, String port, int maxcapacity) {
        
        this.id = id;
        this.address = address;
        this.port = port;
        this.max_capacity = maxcapacity;
        
        this.capacity = 0;
        this.ss = null;
        this.usersList = new ConcurrentHashMap<String,BlockingQueue<Message>>();
    }
    
    @Override
    public void run() {
        
        try { Thread.sleep(PROXY_DELAY); }catch(InterruptedException e) {
            
            Thread.currentThread().interrupt();
            return;
        }
        
        System.out.println("GOSSIP System: Proxy "+id+"@"+
            address+":"+port+" [Capacity: "+max_capacity+"][Available: "+
            (max_capacity-capacity)+"] starting");
        
        int proxy_port = Integer.parseInt(port);
               
        try {
           
            ss = new ServerSocket(proxy_port);
           
        }catch(IOException e) {
           
            System.out.println("GOSSIP System: Proxy "+id+"@"+
                address+":"+port+" [Capacity: "+max_capacity+"][Available: "+
                (max_capacity-capacity)+"] listening failed");
        }
       
        if(ss != null) {
           
            Socket soc;
            
            ExecutorService exec = Executors.newCachedThreadPool();            
           
            while(!isStopped) {
               
                System.out.println("GOSSIP System: Proxy "+id+"@"+
                    address+":"+port+" [Capacity: "+max_capacity+"][Available: "+
                    (max_capacity-capacity)+"] waiting connections");
               
                try {
                   
                    soc = ss.accept();
                   
                    /* Delego al worker */
                    exec.submit(new ProxyWorker(usersList,this.id,this.address,
                            this.port,soc));    
                   
                }catch(IOException e) {
                   
                    System.out.println("GOSSIP System: Proxy "+id+"@"+
                        address+":"+port+" [Capacity: "+max_capacity+"][Available: "+
                        (max_capacity-capacity)+"] interrupted");
                }
            }
        }
    }
    
    /* Esegue lo shutdown del Proxy */
    public synchronized void stopProxy() {
        
        isStopped = true;
        
        try {
            
            this.ss.close();
            
        }catch(IOException e) { 
            
            throw new RuntimeException("Error closing "+Thread.currentThread().getName(), e); 
        }
    }
    
    /* Restituisce l'ID del Proxy */
    public synchronized String getProxyID() {
        
        return this.id;
    }
    
    /* Restituisce l'indirizzo IP del Proxy */
    public synchronized String getProxyAddress() {
        
        return this.address;
    }
    
    /* Restituisce la porta TCP del Proxy */
    public synchronized String getProxyPort() {
        
        return this.port;
    }
    
    /* Restituisce la capacita' disponibile del Proxy */
    public synchronized int getAvailableCapacity() {
        
        return (max_capacity-this.capacity);
    }
    
    /* Associa un utente al Proxy */
    public synchronized void insertUser(String username) {
        
        if(this.capacity < max_capacity) {
        
            /* Genero la coda dei messaggi offline */
            BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();
        
            /* Inserisco utente e la sua coda nella HashMap */
            usersList.put(username, messageQueue);
        
            /* Nuovo utente servito */
            capacity++;
            
            System.out.println("GOSSIP System: "+username+" assigned to Proxy "+
                    id+"@"+address+":"+port+" [Capacity: "+max_capacity+
                    "][Available: "+(max_capacity-capacity)+"]");
        }
    }
    
    /* Rimuove un utente dal Proxy */
    public synchronized void removeUser(String username) {
        
        if(this.capacity > 0) {
            
            /* Rimuovo utente dalla HashMap */
            usersList.remove(username);
            
            /* Utente rimosso */
            capacity--;
            
            System.out.println("GOSSIP System: "+username+" removed from Proxy "+
                    id+"@"+address+":"+port+" [Capacity: "+max_capacity+
                    "][Available: "+(max_capacity-capacity)+"]");
        }
    }
}

/* Worker per la gestione delle connessioni in arrivo sul Proxy */
class ProxyWorker implements Runnable, ServerConstants {
    
    private String proxy_id;
    private String proxy_address;
    private String proxy_port;
    private Map<String,BlockingQueue<Message>> usersList;
    private Socket soc;
    
    public ProxyWorker(Map<String,BlockingQueue<Message>> usersList,
            String proxy_id, String proxy_address, String proxy_port, Socket soc) {
        
        this.proxy_id = proxy_id;
        this.proxy_address = proxy_address;
        this.proxy_port = proxy_port;
        this.usersList = usersList;
        this.soc = soc;
    }
    
    @Override
    public void run() {
        
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        
        boolean workDone = false;
        
        try {
        
            oos = new ObjectOutputStream(soc.getOutputStream());
            ois = new ObjectInputStream(soc.getInputStream());
            
        }catch(IOException e) {
            
            System.out.println("ProxyWorker@"+soc.getRemoteSocketAddress().toString()
                    +" failed to establish connection");
            workDone = true;
        }
        
        if(!workDone) {
            
            System.out.println("ProxyWorker@"+soc.getRemoteSocketAddress().toString()
                    +" connection established");
            
            Message incMsg = null;
            
            try {
            
                /* Timeout */
                soc.setSoTimeout(PROXY_WORKER_TIMEOUT);

                /* Ricevo il messaggio */
                incMsg = (Message) ois.readObject();
                
            }catch(SocketException e) {
                    
                System.out.println("ProxyWorker@"+soc.getRemoteSocketAddress().toString()
                    +" socket timeout");

            }catch(IOException e) {

                System.out.println("ProxyWorker@"+soc.getRemoteSocketAddress().toString()
                    +" failed to read message");

            }catch(ClassNotFoundException e) {

                System.out.println("ProxyWorker@"+soc.getRemoteSocketAddress().toString()
                    +" failed to read message");
            }
            
            if(incMsg != null) {
                
                System.out.println("ProxyWorker@"+soc.getRemoteSocketAddress().toString()
                    +" received "+incMsg.toString());
                
                /* Recupero l'header */
                String header = incMsg.getHeader();
                
                if(header.equals(PROXY_DOWNLOAD)) {
                    
                    /* Richiesta di download dei messaggi offline da parte di un utente */
                    String user = incMsg.getSender();
                    
                    /* Recupero la coda dei messaggi salvati dell'utente */
                    BlockingQueue<Message> msgQueue = usersList.get(user);
                                        
                    boolean closingMsg = false;
                                        
                    while(!workDone) {
                        
                        if(msgQueue.size() == 0) {
                                                        
                            closingMsg = true;
                            workDone = true;
                        }
                        else {
                                                    
                            try {
                                                                
                                /* Messaggio offline */
                                Message msgSend = msgQueue.take();
                                                            
                                /* Invio il messaggio */
                                oos.writeObject(msgSend);
                                oos.flush();
                                
                                Message resMsg = (Message) ois.readObject();
                                
                                System.out.println("ProxyWorker@"+
                                        soc.getRemoteSocketAddress().toString()
                                        +" received "+resMsg.toString());
                            
                            }catch(InterruptedException e) {
                            
                                System.out.println("ProxyWorker@"+
                                        soc.getRemoteSocketAddress().toString()
                                        +" interrupted");
                            
                            }catch(IOException e) {
                            
                                System.out.println("ProxyWorker@"+
                                        soc.getRemoteSocketAddress().toString()
                                        +" failed to send message");
                            
                            }catch(ClassNotFoundException e) {
                                
                                System.out.println("ProxyWorker@"+
                                        soc.getRemoteSocketAddress().toString()
                                        +" failed to send message");
                            }                            
                        }
                    }
                    
                    /* Invio messaggio di chiusura */
                    if(closingMsg) {
                        
                        /* Messaggio di chiusura */
                        Message endMsg = new Message("",PROXY_END,proxy_id+"@"+
                                proxy_address+":"+proxy_port,incMsg.getSender(),
                            soc.getInetAddress().getHostAddress(),soc.getPort());
                        
                        try {
                                                    
                            /* Invio il messaggio */
                            oos.writeObject(endMsg);
                            oos.flush();
                            
                        }catch(IOException e) {
                            
                            System.out.println("ProxyWorker@"+soc.getRemoteSocketAddress().toString()
                                    +" failed to send message");
                        }
                    }
                }
                else if(header.equals(MESSAGE_HEADER)) {
                    
                    /* Richiesta di salvataggio di messaggi offline da parte
                       di un contatto dell'utente
                    */
                    
                    /* Destinatario */
                    String recipient = incMsg.getRecipient();
                    
                    /* Inserisco il messaggio nella coda del destinatario */
                    BlockingQueue msgQueue = usersList.get(recipient);
                    
                    if(msgQueue != null) {
                    
                        try {

                            msgQueue.put(incMsg);

                        }catch(InterruptedException e) {

                            System.out.println("ProxyWorker@"+
                                    soc.getRemoteSocketAddress().toString()
                                    +" interrupted");

                        }finally {

                            try { soc.close(); }catch(IOException e) {

                                throw new RuntimeException("ProxyWorker@"+
                                    soc.getRemoteSocketAddress().toString()+
                                    " error sending messages", e);
                            }
                        }
                    }
                }
            }
        }
    }
}