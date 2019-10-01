/**
 * Message.java
 */

package serializable;

import java.io.Serializable;

/**
 *
 * @author Matteo Bogo
 */
public class Message implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /* Contenuto del messaggio */
    private final String text;
    
    /* Header del messaggio */
    private final String header;
    
    /* Mittente del messaggio */
    private final String sender;
    
    /* Destinatario del messaggio */
    private final String recipient;
    
    /* Indirizzo IP destinazione */
    private final String address;
    
    /* Porta UDP/TCP destinazione */
    private final int port;
    
    public Message(String text, String header, String sender, String recipient,
            String address, int port) {
        
        this.text = text;
        this.header = header;
        this.sender = sender;
        this.recipient = recipient;
        this.address = address;
        this.port = port;
    }
    
    /* Restituisce il contenuto del messaggio */
    public String getText() {
        
        return this.text;
    }
    
    /* Restituisce l'header del messaggio */
    public String getHeader() {
        
        return this.header;
    }
    
    /* Restituisce il mittente del messaggio */
    public String getSender() {
        
        return this.sender;
    }
    
    /* Restituisce il destinatario del messaggio */
    public String getRecipient() {
        
        return this.recipient;
    }
    
    /* Restituisce l'indirizzo IP della destinazione */
    public String getAddress() {
        
        return this.address;
    }
    
    /* Restituisce la porta UDP/TCP della destinazione */
    public int getPort() {
        
        return this.port;
    }
    
    @Override
    public String toString() {
        
        return "["+this.header+"]["+this.text+"][From: "+this.sender+"][To: "+this.recipient+
                "@"+this.address+":"+this.port+"]";
    }
    
}