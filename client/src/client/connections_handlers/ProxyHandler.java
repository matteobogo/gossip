/**
 * ProxyHandler.java
 */

package client.connections_handlers;

import client.implementations.Data;
import client.implementations.FriendHandler;
import client.interfaces.ClientConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import serializable.Message;

/**
 *
 * @author Matteo Bogo
 */
public class ProxyHandler implements Runnable, ClientConstants {
    
    private Data data;
        
    public ProxyHandler(Data data) {
      
        this.data = data;
    }
    
    @Override
    public void run() {
        
        try { 
            
            Thread.sleep(PROXY_HANDLER_DELAY); 
        
        }catch(InterruptedException e) {
            
            System.out.println(Thread.currentThread().getName()+" interrupted");
            Thread.currentThread().interrupt();
        }
        
        System.out.println(Thread.currentThread().getName()+" started");
        
        ExecutorService exec = Executors.newCachedThreadPool();
        
        while(!Thread.currentThread().isInterrupted()) {
            
            try {
                
                /* Recupero il messaggio */
                Message message = data.getProxyQueue().take();
                                
                /* Verifico l'header del messaggio */
                String header = message.getHeader();
                
                /* Scarico i messaggi dal Proxy dell'utente */
                if(header.equals(PROXY_DOWNLOAD)) {
                    
                    ProxyDownloadWorker proxyDownload = new ProxyDownloadWorker(
                            data,message);
                    exec.submit(proxyDownload);
                    
                }
                /* Invio i messaggi sul Proxy di un contatto (con connessione) */
                else if(header.equals(MESSAGE_HEADER)) {
                    
                    ProxyUploadWorker proxyUpload = new ProxyUploadWorker(
                        data,message);
                    exec.submit(proxyUpload);
                }

            }catch(InterruptedException e) {
                
                System.out.println(Thread.currentThread().getName()+" interrupted");
                exec.shutdownNow();
                Thread.currentThread().interrupt();
            }
        } 
    }
}
      
/* Worker per il download dei messaggi di un utente dal suo Proxy */
class ProxyDownloadWorker implements Runnable, ClientConstants {
    
    private Data data;
    private Message message;
    
    public ProxyDownloadWorker(Data data, Message message) {
        
        this.data = data;
        this.message = message;
    }
    
    @Override
    public void run() {
        
        Socket soc = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        
        boolean workDone = false;
        
        try {
            
            /* IP/Porta Proxy */
            InetAddress ia = InetAddress.getByName(message.getAddress());
            soc = new Socket(ia,message.getPort());
            
            ois = new ObjectInputStream(soc.getInputStream());
            oos = new ObjectOutputStream(soc.getOutputStream());
            
        }catch(IOException e) {
            
            System.out.println("ProxyDownloadWorker@"+message.getAddress()+":"+
                    message.getPort()+" failed to establish connection");
        }
        
        if(soc != null) {
            
            /* Connessione stabilita */
            System.out.println("ProxyDownloadWorker@"+message.getAddress()+":"+
                    message.getPort()+" establish connection with Proxy");
            
            try {
                
                System.out.println("ProxyDownloadWorker@"+message.getAddress()+":"+
                    message.getPort()+" sending "+message.toString());
            
                /* Invio il messaggio di richiesta */
                oos.writeObject(message);
                oos.flush();
                                
            }catch(IOException e) {
                
                System.out.println("ProxyDownloadWorker@"+message.getAddress()+":"+
                    message.getPort()+" failed to send request to Proxy");
                
                workDone = true;
            }
                        
            /* Recupero i messaggi offline */
            while(!workDone) {
                
                try {
                                                        
                    /* Timeout */
                    soc.setSoTimeout(PROXY_HANDLER_TIMEOUT);
                                        
                    /* Ricevo il messaggio */
                    Message incMsg = (Message) ois.readObject();
                    
                    System.out.println("ProxyDownloadWorker@"+message.getAddress()
                        +" received "+incMsg.toString());
                    
                    String header = incMsg.getHeader();
                    String sender = incMsg.getSender();
                    String recipient = incMsg.getRecipient();
                    
                    if(header.equals(PROXY_END)) {
                        
                        /* Proxy ha terminato di inviarmi i messaggi salvati */
                        workDone = true;
                    }
                    else if(header.equals(MESSAGE_HEADER) &&
                            recipient.equals(data.getUsername())) {
                        
                        /* Recupero i messaggi offline dal Proxy */
                        
                        /* Recupero l'istanza del mittente */
                        FriendHandler friendHandler = data.getFriendList().get(
                            sender);
                            
                        if(friendHandler != null) {
                            
                            if(!friendHandler.getChatIsOpen()
                                && !friendHandler.getChatIsVisible()) {
                                
                                /* Starto la chat */
                                friendHandler.startChatWindow();
                            }
                            
                            /* Invio il messaggio nella chatbox */
                            SwingUtilities.invokeLater(new Runnable() {
                                
                                @Override
                                public void run() {
                                    
                                    friendHandler.getChatWindow().appendTextChatBox(
                                        "["+incMsg.getSender()+"]"+incMsg.getText()+"\n");
                                }
                            });
                        }
                        
                        /* Invio la risposta di avvenuta ricezione */
                        Message resMsg = new Message("",PROXY_DOWNLOAD_OK,"","",
                            soc.getRemoteSocketAddress().toString(),soc.getPort());
                        
                        oos.writeObject(resMsg);
                        oos.flush();
                    }
                
                }catch(SocketException e) {
                    
                    System.out.println("ProxyDownloadWorker@"+message.getAddress()
                        +" socket timeout");
                    
                }catch(IOException e) {
                    
                    System.out.println("ProxyDownloadWorker@"+message.getAddress()
                        +" failed to read messages");
                
                }catch(ClassNotFoundException e) {
                    
                    System.out.println("ProxyDownloadWorker@"+message.getAddress()
                        +" failed to read messages");
                }
            }     
        }
    }
}

/* Worker per l'invio di messaggi offline sul Proxy di un contatto */
class ProxyUploadWorker implements Runnable, ClientConstants {
    
    private Data data;
    private Message message;
    
    public ProxyUploadWorker(Data data, Message message) {
        
        this.data = data;
        this.message = message;
    }
    
    @Override
    public void run() {
        
        Socket soc = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        
        try {
            
            InetAddress ia = InetAddress.getByName(message.getAddress());
            soc = new Socket(ia,message.getPort());
            
            ois = new ObjectInputStream(soc.getInputStream());
            oos = new ObjectOutputStream(soc.getOutputStream());
            
        }catch(IOException e) {
            
            System.out.println("ProxyUploadWorker@"+message.getAddress()+":"+
                    message.getPort()+" failed to establish connection");
        }
        
        if(soc != null) {
            
            /* Connessione stabilita */
            System.out.println("ProxyUploadWorker@"+message.getAddress()+":"+
                message.getPort()+" establish connection with Proxy");
            
            /* Invio messaggio offline al contatto */
            try {

                System.out.println("ProxyUploadWorker@"+message.getAddress()+":"+
                    message.getPort()+" sending "+message.toString());

                /* Invio il messaggio */
                oos.writeObject(message);
                oos.flush();

                Thread.sleep(1000);

                soc.close();

            }catch(SocketException e) {

                System.out.println("ProxyUploadWorker@"+message.getAddress()+":"+
                    message.getPort()+" socket timeout");

            }catch(IOException e) {

                System.out.println("ProxyUploadWorker@"+message.getAddress()+":"+
                    message.getPort()+" failed to read messages");

            }catch(InterruptedException e) {

                System.out.println("ProxyUploadWorker@"+message.getAddress()+":"+
                    message.getPort()+" interrupted");

            }finally {

                try { soc.close(); }catch(IOException e) {

                    throw new RuntimeException("ProxyUploadWorker@"+message.getAddress()+":"+
                    message.getPort()+" error retrieving messages", e);
                }
            }    
        }
    }
}