/**
 * ClientImpl.java
 */

package client.implementations;

import serializable.Friend;
import client.GUI.MainWindow;
import client.interfaces.ClientConstants;
import client.interfaces.ClientInterface;
import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 *
 * @author Matteo Bogo
 */
public class ClientImpl extends UnicastRemoteObject implements ClientInterface, ClientConstants {
    
    private MainWindow mainWindow;
    
    public ClientImpl(MainWindow mainWindow) throws RemoteException {
        
        super();

        this.mainWindow = mainWindow;
    }
    
    @Override
    public synchronized String getUsername() throws RemoteException {
        
        return this.mainWindow.getData().getUsername();
    }
    
    @Override
    public synchronized int getUserPort() throws RemoteException {
        
        return this.mainWindow.getData().getPort();
    }
    
    @Override
    public synchronized String getUserAddress() throws RemoteException {
        
        return this.mainWindow.getData().getAddress();
    }

    /* Aggiorna le liste grafiche dei contatti di uscita offline e online dell'utente */
    private void updateOutputContactsJList(ArrayList<Friend> new_friendList) {
        
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                mainWindow.getOutputOnlineListModel().clear();
                mainWindow.getOutputOfflineListModel().clear();
                
                for(Friend f : new_friendList) {
                    
                    if(f.isOnline()) {
                        
                        mainWindow.getOutputOnlineListModel().addElement(f.getUsername());
                    }
                    else {
                        
                        mainWindow.getOutputOfflineListModel().addElement(f.getUsername());
                    }
                }
            }
        });
    }
    
    /* Aggiorna la lista grafica dei contatti di ingresso dell'utente */
    private void updateInputContactsJList(ArrayList<String> new_friendList) {
                
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                mainWindow.getInputListModel().clear();
                            
                for(String f : new_friendList) {
                    
                    mainWindow.getInputListModel().addElement(f);
                }
            }   
        });
    }
    
    /* Aggiorna la lista dei contatti di uscita */
    private void updateFriendList(ArrayList<Friend> new_friendList) {
            
        for(int i = 0; i < new_friendList.size(); i++) { 
            
            Friend friend = new_friendList.get(i);
            
            FriendHandler friendHandler = new FriendHandler(
                friend.getUsername(),
                friend.isOnline(),
                friend.getAddress(),
                friend.getPort(),
                mainWindow.getData());
            
            mainWindow.getData().getFriendList().put(friend.getUsername(), friendHandler);
        }
    }
    
    @Override
    public void notifyUpdateInputFriendList(ArrayList<String> new_inputFriendList) throws RemoteException {
        
        System.out.println("Loading pending friends requestes");
        
        /* Aggiorno la JList */
        updateInputContactsJList(new_inputFriendList);
    }
    
    @Override
    public void notifyUpdateOutputFriendList(ArrayList<Friend> outputFriendList) throws RemoteException {
                
        System.out.println("Loading friends list");
        
        /* Aggiorno la lista amici */
        updateFriendList(outputFriendList);
                        
        /* Aggiorno le JList */
        updateOutputContactsJList(outputFriendList);        
    }
    
    @Override
    public void notifyFriendAdd(Friend friend) throws RemoteException {
        
        System.out.println(friend.getUsername()+" has added you");
        
        /* Aggiungo il contatto nella lista amici */
        FriendHandler friendHandler = new FriendHandler(
            friend.getUsername(),
            friend.isOnline(),
            friend.getAddress(),
            friend.getPort(),
            mainWindow.getData());
            
        mainWindow.getData().getFriendList().put(friend.getUsername(),friendHandler);
        
        /* Aggiorno le JList */
        SwingUtilities.invokeLater(new Runnable() {
                
            @Override
            public void run() {
                    
                if(friend.isOnline()) {
                      
                    /* Aggiungo il contatto nella lista grafica dei contatti di uscita online */
                    mainWindow.getOutputOnlineListModel().addElement(friend.getUsername());
                }
                else {
                    
                    /* Aggiungo il contatto nella lista grafica dei contatti di uscita offline */
                    mainWindow.getOutputOfflineListModel().addElement(friend.getUsername());
                }
                
                /* Notifico */
                mainWindow.getServerStatus().setForeground(Color.green);
                mainWindow.getServerStatus().setText(friend.getUsername()+" has added you");
            }
        });
    }
            
    @Override
    public void notifyFriendRemove(String friend_username) throws RemoteException {
                
        /* Recupero l'istanza del contatto che mi ha rimosso */
        FriendHandler friendHandler = mainWindow.getData().getFriendList().get(friend_username);
        
        /* Verifico se ho una chat aperta con il contatto */
        if(friendHandler.getChatIsOpen()) {
            
            /* Verifico se la chat con il contatto e' visibile */
            if(friendHandler.getChatIsVisible()) {
                
                /* Disabilito la chat e notifico la rimozione */
                friendHandler.getChatWindow().getSendButton().setEnabled(false);
                friendHandler.getChatWindow().appendTextChatBox(friend_username+" has removed you");
            }
        }
                
        /* Aggiorno le JList */
        SwingUtilities.invokeLater(new Runnable() {
                
            @Override
            public void run() {
                    
                if(friendHandler.getStatus()) {
                      
                    /* Rimuovo il contatto dalla lista grafica dei contatti di uscita online */
                    mainWindow.getOutputOnlineListModel().removeElement(friendHandler.getFriendUsername());
                }
                else {
                    
                    /* Rimuovo il contatto dalla lista grafica dei contatti di uscita offline */
                    mainWindow.getOutputOfflineListModel().removeElement(friendHandler.getFriendUsername());
                }
                
                /* Notifico */
                mainWindow.getServerStatus().setForeground(Color.red);
                mainWindow.getServerStatus().setText(friendHandler.getFriendUsername()+" has removed you");
            }
        });                
        
        /* Rimuovo il contatto dalla lista dei contatti di uscita */
        mainWindow.getData().getFriendList().remove(friend_username);
    }
    
    /* Aggiorno il cambiamento di stato di un contatto nelle liste grafiche */
    private void manageFriendOnJList(Friend friend) {
        
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                if(friend.isOnline()) {
                    
                    mainWindow.getOutputOfflineListModel().removeElement(friend.getUsername());
                    mainWindow.getOutputOnlineListModel().addElement(friend.getUsername());
                }
                else {
                    
                    mainWindow.getOutputOnlineListModel().removeElement(friend.getUsername());
                    mainWindow.getOutputOfflineListModel().addElement(friend.getUsername());
                }
            }
        });
    }
    
    @Override
    public void notifyFriendStatusChanged(Friend friend) throws RemoteException {
        
        /* Aggiorno le JList */
        manageFriendOnJList(friend);
                
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                /* Recupero l'istanza dell'amico */
                FriendHandler friendHandler = mainWindow.getData().getFriendList().get(friend.getUsername());
                
                /* Aggiorno lo stato del contatto*/
                friendHandler.setFriendAddress(friend.getAddress());
                friendHandler.setFriendPort(friend.getPort());
                friendHandler.setStatus(friend.isOnline());
                
                /* Avviso nella barra delle notifiche */
                if(friend.isOnline()) {
            
                    /* Amico e' arrivato online */
                    System.out.println(friend.getUsername()+FRIEND_IS_ONLINE);
                    
                    /* Notifico nel Server Status */
                    mainWindow.getServerStatus().setForeground(Color.green);
                    mainWindow.getServerStatus().setText(friend.getUsername()+FRIEND_IS_ONLINE);
                    
                    if(friendHandler.getChatIsOpen()) {
                                                
                        /* Notifico nella chat */
                        friendHandler.getChatWindow().appendTextChatBox(friend.getUsername()+FRIEND_IS_ONLINE+"\n");
                    }
                }
                else {
            
                    /*Amico e' andato offline */
                    
                    System.out.println(friend.getUsername()+FRIEND_IS_OFFLINE);
                    
                    mainWindow.getServerStatus().setForeground(Color.red);
                    mainWindow.getServerStatus().setText(friend.getUsername()+FRIEND_IS_OFFLINE);
                    
                    if(friendHandler.getChatIsOpen()) {
                                                    
                        /* Notifico nella chat */
                        friendHandler.getChatWindow().appendTextChatBox(friend.getUsername()+FRIEND_IS_OFFLINE+"\n");
                        friendHandler.getChatWindow().appendTextChatBox(MSG_TO_PROXY+"\n");
                    }
                        
                    System.out.println(MSG_TO_PROXY);
                }                
            }
        });             
    }
    
    @Override
    public void isAlive() throws RemoteException {
        
        /* Testa se un client e' ancora attivo */
    }  
}