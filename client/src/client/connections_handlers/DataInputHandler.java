/**
 * DataInputHandler.java
 */

package client.connections_handlers;

import client.implementations.Data;
import client.implementations.FriendHandler;
import client.interfaces.ClientConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import serializable.Message;

/**
 *
 * @author Matteo Bogo
 */
public class DataInputHandler implements Runnable, ClientConstants {
    
    private Data data;
    private DatagramSocket ds;
    private boolean isStopped;
    
    public DataInputHandler(Data data) {
        
        this.data = data;
        this.ds = null;
        this.isStopped = false;
    }
    
    @Override
    public void run() {
                
        try { 
            
            Thread.sleep(DATA_INPUT_HANDLER_DELAY); 
        
        }catch(InterruptedException e) { 
            
            isStopped = true; 
            System.out.println(Thread.currentThread().getName()+" interrupted");
        }
        
        /* Thread Pool */
        ExecutorService exec = null;
        
        /* UDP */
        DatagramPacket dp = null;
        byte[] buffer = new byte[DATA_INPUT_HANDLER_UDP_BUFFER];
        
        /* Porta UDP */
        int port = data.getPort();
        
        /* Inizializzazione */
        System.out.println("DataInputHandler: Starting UDP packets receiver");
        
        if(!isStopped) {

            System.out.println("DataInputHandler: Start listening on UDP port @"+port);
                
            try {
            
                ds = new DatagramSocket(port);
                dp = new DatagramPacket(buffer,buffer.length);
            
            }catch(SocketException e) {
            
                System.out.println(Thread.currentThread().getName()+
                        "failed to listening on UDP port @"+port);
                isStopped = true;
            } 
            
            exec = Executors.newCachedThreadPool();
        }
                        
        while(!isStopped) {
            
            try {
                
                ds.receive(dp);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
                
                /* Messaggio ricevuto */
                Message rcvMsg = (Message) ois.readObject();
                                
                exec.submit(new DataInputWorker(data,rcvMsg));
            
            }catch(IOException e) {
                
                 System.out.println(Thread.currentThread().getName()+" closing in progress");
                
            }catch(ClassNotFoundException e) {
                
                System.out.println(Thread.currentThread().getName()+" error reading message");
            }
        }
    }
    
    /* Chiude la socket e genera una IOException */
    public synchronized void stopHandler() {
                
        this.isStopped = true;
        this.ds.close();
    }
}

/* Worker per la gestione dei messaggi in ingresso */
class DataInputWorker implements Runnable {
    
    private Data data;
    private Message message;
    
    public DataInputWorker(Data data, Message message) {
        
        this.data = data;
        this.message = message;
    }
    
    @Override
    public void run() {
                
        System.out.println("DataInputWorker: Received "+message.toString());
        
        /* Verifico il destinatario */
        if(!data.getUsername().equals(message.getRecipient())) {
            
            System.out.println(Thread.currentThread().getName()+": recipient does not match");
            return;
        }
            
        /* Recupero l'istanza del contatto */
        FriendHandler friendHandler = data.getFriendList().get(message.getSender());
            
        if(friendHandler == null) {
         
            System.out.println(Thread.currentThread().getName()+": sender does not exist");
            return;
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                if(friendHandler.getChatIsOpen()) {
                    
                    if(!friendHandler.getChatIsVisible()) {
                        
                        /* Ripristino la chat */
                        friendHandler.getChatWindow().setVisible(true);
                        friendHandler.setChatIsVisible(true);
                    }
                }
                else {
                    
                    /* Avvio la chat */
                    friendHandler.startChatWindow();
                }
                
                /* Messaggio in chat */
                friendHandler.getChatWindow().appendTextChatBox("["+message.getSender()+"]: "+message.getText()+"\n");
            }
        });  
    }
}