/**
 * DataOutputHandler.java
 */

package client.connections_handlers;

import client.implementations.Data;
import client.interfaces.ClientConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import serializable.Message;

/**
 *
 * @author Matteo Bogo
 */
public class DataOutputHandler implements Runnable, ClientConstants {
    
    private Data data;
    
    public DataOutputHandler(Data data) {
        
        this.data = data;
    }
    
    @Override
    public void run() {
        
        try { 
            
            Thread.sleep(DATA_OUTPUT_HANDLER_DELAY); 
        
        }catch(InterruptedException e) {
            
            System.out.println(Thread.currentThread().getName()+" interrupted");
            Thread.currentThread().interrupt();
        }
        
        System.out.println(Thread.currentThread().getName()+": Starting UDP packets sender");
        
        ExecutorService exec = Executors.newCachedThreadPool();

        while(!Thread.currentThread().isInterrupted()) {
            
            try {
                   
                /* Prelevo il messaggio da inviare */
                Message msgToSend = data.getMessageQueue().take();
                                
                exec.submit(new DataOutputWorker(msgToSend));
                
            }catch(InterruptedException e) {
                
                System.out.println(Thread.currentThread().getName()+" interrupted");
                exec.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}

/* Worker per la gestione dei messaggi in uscita */
class DataOutputWorker implements Runnable {
    
    private Message message;
    
    public DataOutputWorker(Message message) {
        
        this.message = message;
    }
    
    @Override
    public void run() {
                
        DatagramSocket ds = null;
        ObjectOutputStream os = null;
        
        try {
        
            ds = new DatagramSocket();
        
        /* Indirizzo IP destinatario */
        InetAddress ia = InetAddress.getByName(this.message.getAddress());
        
        /* Porta UDP destinatario */
        int port = message.getPort();
        
        /* Trasformo il Message in un Array di Bytes */
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        os = new ObjectOutputStream(outputStream);
        os.writeObject(this.message);
        
        byte[] data = outputStream.toByteArray();
        
        /* Genero il pacchetto */
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, ia, port);
        
        System.out.println("DataOutputWorker: Sending "+message.toString());
        
        /* Invio il pacchetto */
        ds.send(sendPacket);
        
        Thread.sleep(500);
        
        }catch(SocketException e) {
            
            System.out.println(Thread.currentThread().getName()+
                    " failed to create socket");
            
        }catch(UnknownHostException e) {
            
            System.out.println(Thread.currentThread().getName()+
                    " failed to resolve host");
            
        }catch(IOException e) {
            
            System.out.println(Thread.currentThread().getName()+
                    " failed to transform data in byte array");
            
        }catch(InterruptedException e) {
            
            System.out.println(Thread.currentThread().getName()+" interrupted");
            
        }finally {
            
            ds.close();
            
            try { 
                
                os.close(); 
            
            }catch(IOException e) { 
                
                throw new RuntimeException("Error closing "+Thread.currentThread().getName(), e); 
            }
        }
    }
}