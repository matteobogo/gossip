/**
 * ChatWindow.java
 */

package client.GUI;

import client.implementations.FriendHandler;
import client.interfaces.ClientConstants;
import javax.swing.JButton;
import javax.swing.JFrame;
import serializable.Message;

/**
 *
 * @author Matteo Bogo
 */
public class ChatWindow extends JFrame implements ClientConstants {

    private FriendHandler friendHandler;
    
    public ChatWindow(FriendHandler friendHandler) {
        
        this.friendHandler = friendHandler;
        this.setTitle("Chat with "+friendHandler.getFriendUsername());
        
        initComponents();        
        joinChat();
    }
    
    /* Visualizza nella chat lo stato del contatto */
    private void joinChat() {
        
        appendTextChatBox("Entered Chat with "+friendHandler.getFriendUsername()+"\n");
        
        if(friendHandler.getStatus()) {
            
            appendTextChatBox(friendHandler.getFriendUsername()+" is Online\n");
        }
        else {
            
            appendTextChatBox(friendHandler.getFriendUsername()+" is Offline\n");
            appendTextChatBox(MSG_TO_PROXY+"\n");
        }        
    }
    
    /* Inserisce un testo nella chat */
    public void appendTextChatBox(String text) {
        
        chatBoxTextArea.append(text);
    }
    
    /* Restituisce il pulsante Send */
    public JButton getSendButton() {
        
        return this.sendMessageButton;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainContainer = new javax.swing.JPanel();
        sendMessageScrollPane = new javax.swing.JScrollPane();
        sendMessageTextArea = new javax.swing.JTextArea();
        sendMessageButton = new javax.swing.JButton();
        chatBoxTextAreaScrollPane = new javax.swing.JScrollPane();
        chatBoxTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                windowClosingAction(evt);
            }
        });

        sendMessageScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        sendMessageTextArea.setColumns(20);
        sendMessageTextArea.setRows(3);
        sendMessageScrollPane.setViewportView(sendMessageTextArea);

        sendMessageButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        sendMessageButton.setText("Send");
        sendMessageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessageButtonActionPerformed(evt);
            }
        });

        chatBoxTextArea.setEditable(false);
        chatBoxTextArea.setColumns(20);
        chatBoxTextArea.setRows(5);
        chatBoxTextAreaScrollPane.setViewportView(chatBoxTextArea);

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chatBoxTextAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addComponent(sendMessageScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendMessageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainContainerLayout.setVerticalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chatBoxTextAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sendMessageScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendMessageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(400, 300));
    }// </editor-fold>//GEN-END:initComponents

    /* Invia un messaggio al contatto */
    private void sendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMessageButtonActionPerformed
        
        /* Recupero il testo del messaggio */
        String text = sendMessageTextArea.getText();
        
        if(text.isEmpty()) {
            
            return;
        }
        
        /* Genero il messaggio */
        Message message = new Message(
                text,
                MESSAGE_HEADER,
                friendHandler.getData().getUsername(),
                friendHandler.getFriendUsername(),
                friendHandler.getFriendAddress(),
                friendHandler.getFriendPort());
        
        try {
        
            /* Verifico lo stato del contatto */
            if(friendHandler.getStatus()) {
            
                /* Contatto online */
                if(friendHandler.getChatIsOpen() && friendHandler.getChatIsVisible()) {
                
                    /* Invio il messaggio al contatto */
                    friendHandler.getData().getMessageQueue().put(message);
                }
            }
            else {
            
                /* Contatto offline */
                if(friendHandler.getChatIsOpen() && friendHandler.getChatIsVisible()) {
                                        
                    /* Invio il messaggio al Proxy */
                    friendHandler.getData().getProxyQueue().put(message);
                }
            }
            
            /* Scrivo il messaggio inviato nella chat */
            this.appendTextChatBox("Me: "+text+"\n");
        
        }catch(InterruptedException e) {
            
            System.out.println("Invio messaggio a "+friendHandler.getFriendUsername()+" fallito");
            this.appendTextChatBox("<failed to send message>");
        }
        
        sendMessageTextArea.setText(null);
    }//GEN-LAST:event_sendMessageButtonActionPerformed

    /* Chiude la chat */
    private void windowClosingAction(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosingAction
        
        /* Il contatto esiste sempre */
        if(friendHandler != null) {
            
            /* Verifico lo stato del contatto */
            if(friendHandler.getStatus()) {
                
                /* Utente online */
                friendHandler.setChatIsVisible(false);
                this.setVisible(false);
            }
            else {
                
                /* Utente offline */
                friendHandler.setChatIsVisible(false);
                friendHandler.setChatIsOpen(false);
                                
                this.dispose();
            }
        }
        else {
        
            /* Il Contatto mi ha rimosso */
            this.dispose();
        }
    }//GEN-LAST:event_windowClosingAction

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatBoxTextArea;
    private javax.swing.JScrollPane chatBoxTextAreaScrollPane;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JButton sendMessageButton;
    private javax.swing.JScrollPane sendMessageScrollPane;
    private javax.swing.JTextArea sendMessageTextArea;
    // End of variables declaration//GEN-END:variables
}