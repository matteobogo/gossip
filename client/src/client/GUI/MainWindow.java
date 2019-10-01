/**
 * MainWindow.java
 */

package client.GUI;

import client.implementations.ClientImpl;
import client.connections_handlers.RMIConnection;
import client.connections_handlers.ConnectionSeeker;
import client.connections_handlers.DataInputHandler;
import client.connections_handlers.DataOutputHandler;
import client.connections_handlers.ProxyHandler;
import client.implementations.Data;
import client.implementations.FriendHandler;
import client.interfaces.ClientConstants;
import client.interfaces.ClientInterface;
import java.awt.CardLayout;
import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import serializable.Message;

/**
 *
 * @author Matteo Bogo
 */
public class MainWindow extends JFrame implements ClientConstants {
    
    /* Dati */
    private Data data;
    
    /* Threads */
    private Thread tConnectionSeeker;
    private DataInputHandler dataInputHandler;
    private Thread tDataInputHandlerUDP;
    private Thread tDataOutputHandlerUDP;
    private Thread tProxyHandler;
            
    /* Connessione a GOSSIP */
    private RMIConnection connection;
    
    /* Componenti Grafici Personalizzati */
    private CardLayout card;
    private Border redBorder;
    private Border blackBorder;
    
    /* Modelli per la gestione del contenuto delle JList */
    private DefaultListModel<String> inputListModel;
    private DefaultListModel<String> outputOfflineListModel;
    private DefaultListModel<String> outputOnlineListModel;
            
    public MainWindow() {
        
        /* Inizializzo i modelli */
        inputListModel = new DefaultListModel<String>();
        outputOfflineListModel = new DefaultListModel<String>();
        outputOnlineListModel = new DefaultListModel<String>();
        
        initComponents();
        initNetwork();
        initServicesComponents();
        
        /* Inizializzo Data */
        data = new Data();
                
        /* Mostro il pannello di Login */
        card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "loginPanel");
    }

    /* Inizializza i componenti grafici personalizzati */
    private void initServicesComponents() {
        
        /* Bordi personalizzati */
        redBorder = BorderFactory.createLineBorder(Color.RED, 1);
        blackBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    }

    /* Inizializza la connessione a GOSSIP */
    private void initNetwork() {
           
        /* Connessione a GOSSIP */
        this.connection = new RMIConnection();
                
        /* Thread ConnectionSeeker per controllare la connessione con GOSSIP */
        tConnectionSeeker = new ConnectionSeeker(connection,this);
        tConnectionSeeker.start();
    } 
    
    /* Restituisce le strutture dati e le informazioni di un utente */
    public Data getData() {
        
        return this.data;
    }
    
    /* Restituisce la connessione a GOSSIP */
    public RMIConnection getRMIConnection() {
        
        return this.connection;
    }
    
    /* Restituisce il DefaultListModel per aggiornare la lista dei contatti di ingresso */
    public DefaultListModel<String> getInputListModel() {
        
        return this.inputListModel;
    }
    
    /* Restituisce il DefaultListModel per aggiornare la lista dei contatti offline */
    public DefaultListModel<String> getOutputOfflineListModel() {
        
        return this.outputOfflineListModel;
    }
    
    /* Restituisce il DefaultListModel per aggiornare la lista dei contatti online */
    public DefaultListModel<String> getOutputOnlineListModel() {
        
        return this.outputOnlineListModel;
    }
    
    /* Restituisce la barra delle notifiche */
    public JLabel getServerStatus() {
        
        return this.serverStatusLabel;
    }
    
    /* Restituisce il Layout Manager */
    public CardLayout getCardLayout() {
        
        return this.card;
    }
    
    /* Restituisce il pannello principale */
    public JPanel getMainPanel() {
        
        return this.mainPanel;
    }
    
    /* Interrompe i Threads per la gestione dei dati in entrata/uscita */
    public synchronized void shutdownHandlers() {
        
        if(tDataInputHandlerUDP.isAlive() && tDataInputHandlerUDP != null) {
            
            this.dataInputHandler.stopHandler();
        }
        
        if(tDataOutputHandlerUDP.isAlive() && tDataOutputHandlerUDP != null) {
            
            this.tDataOutputHandlerUDP.interrupt();
        }
        
        if(tProxyHandler.isAlive() && tProxyHandler != null) {
            
            this.tProxyHandler.interrupt();
        }
        
        /* Rilascio le risorse */
        data.getFriendList().clear();
        data.getMessageQueue().clear();
        data.getProxyQueue().clear();
    }
    
    /* Genera un intero casuale in un intervallo prefissato */
    private static int getRandomNumberInterval(int limInf, int limSup) {
	
        return limInf + (int)(Math.random() * ((limSup - limInf) + 1));
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
        mainPanel = new javax.swing.JPanel();
        loginPanel = new javax.swing.JPanel();
        loginPanelLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        registerButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        registrationPanel = new javax.swing.JPanel();
        registrationPanelLabel = new javax.swing.JLabel();
        reg_usernameLabel = new javax.swing.JLabel();
        reg_usernameTextField = new javax.swing.JTextField();
        reg_passwordLabel = new javax.swing.JLabel();
        reg_passwordField = new javax.swing.JPasswordField();
        reg_rePasswordLabel = new javax.swing.JLabel();
        reg_rePasswordField = new javax.swing.JPasswordField();
        reg_registerButton = new javax.swing.JButton();
        reg_backButton = new javax.swing.JButton();
        friendsPanel = new javax.swing.JPanel();
        inputFriendListScrollPane = new javax.swing.JScrollPane();
        friends_inputFriendList = new javax.swing.JList();
        outputFriendListOfflineScrollPane = new javax.swing.JScrollPane();
        friends_outputFriendListOffline = new javax.swing.JList();
        outputFriendListOnlineScrollPane = new javax.swing.JScrollPane();
        friends_outputFriendListOnline = new javax.swing.JList();
        friendsPanelLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        offlineFriendsLabel = new javax.swing.JLabel();
        onlineFriendsLabel = new javax.swing.JLabel();
        friendRequestesLabel = new javax.swing.JLabel();
        manageFriendButton = new javax.swing.JButton();
        openChatButton = new javax.swing.JButton();
        addRemovePanel = new javax.swing.JPanel();
        friendsManagementPanelLabel = new javax.swing.JLabel();
        addRemoveTextField = new javax.swing.JTextField();
        addFriendButton = new javax.swing.JButton();
        removeFriendButton = new javax.swing.JButton();
        addFriendBackButton = new javax.swing.JButton();
        addRemoveInsertLabel = new javax.swing.JLabel();
        mainTitleLabel = new javax.swing.JLabel();
        serverStatusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel.setPreferredSize(new java.awt.Dimension(300, 340));
        mainPanel.setLayout(new java.awt.CardLayout());

        loginPanelLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        loginPanelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginPanelLabel.setText("[Login]");

        usernameLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        usernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        usernameLabel.setText("Username");

        usernameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usernameMouseClickedAction(evt);
            }
        });

        passwordLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        passwordLabel.setText("Password");

        passwordField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passwordMouseClickedAction(evt);
            }
        });

        loginButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        registerButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        registerButton.setText("Register");
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        exitButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(loginPanelLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                        .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                    .addComponent(passwordLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addComponent(loginPanelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        mainPanel.add(loginPanel, "loginPanel");

        registrationPanelLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        registrationPanelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        registrationPanelLabel.setText("[Registration]");

        reg_usernameLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        reg_usernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        reg_usernameLabel.setText("Username");

        reg_usernameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        reg_usernameTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regUsernameMouseClickedAction(evt);
            }
        });

        reg_passwordLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        reg_passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        reg_passwordLabel.setText("Password");

        reg_passwordField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        reg_passwordField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regPasswordMouseClickedAction(evt);
            }
        });

        reg_rePasswordLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        reg_rePasswordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        reg_rePasswordLabel.setText("Repeat Password");

        reg_rePasswordField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        reg_rePasswordField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regRePasswordMouseClickedAction(evt);
            }
        });

        reg_registerButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        reg_registerButton.setText("Register");
        reg_registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_registerButtonActionPerformed(evt);
            }
        });

        reg_backButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        reg_backButton.setText("Back");
        reg_backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout registrationPanelLayout = new javax.swing.GroupLayout(registrationPanel);
        registrationPanel.setLayout(registrationPanelLayout);
        registrationPanelLayout.setHorizontalGroup(
            registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(registrationPanelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reg_usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reg_passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(reg_usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reg_passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reg_rePasswordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reg_rePasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(reg_backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reg_registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        registrationPanelLayout.setVerticalGroup(
            registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addComponent(registrationPanelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(reg_usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reg_usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(reg_passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reg_passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(reg_rePasswordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reg_rePasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(reg_registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reg_backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 45, Short.MAX_VALUE))
        );

        mainPanel.add(registrationPanel, "registrationPanel");

        friends_inputFriendList.setModel(inputListModel);
        inputFriendListScrollPane.setViewportView(friends_inputFriendList);

        friends_outputFriendListOffline.setForeground(new java.awt.Color(255, 0, 0));
        friends_outputFriendListOffline.setModel(outputOfflineListModel);
        friends_outputFriendListOffline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                outputFriendListOfflineMouseClickedAction(evt);
            }
        });
        outputFriendListOfflineScrollPane.setViewportView(friends_outputFriendListOffline);

        friends_outputFriendListOnline.setForeground(new java.awt.Color(0, 255, 0));
        friends_outputFriendListOnline.setModel(outputOnlineListModel);
        friends_outputFriendListOnline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                outputFriendListOnlineMouseClickedAction(evt);
            }
        });
        outputFriendListOnlineScrollPane.setViewportView(friends_outputFriendListOnline);

        friendsPanelLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        friendsPanelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        friendsPanelLabel.setText("[Friends]");

        logoutButton.setFont(new java.awt.Font("Purisa", 0, 12)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        offlineFriendsLabel.setFont(new java.awt.Font("Purisa", 0, 12)); // NOI18N
        offlineFriendsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        offlineFriendsLabel.setText("Offline");

        onlineFriendsLabel.setFont(new java.awt.Font("Purisa", 0, 12)); // NOI18N
        onlineFriendsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        onlineFriendsLabel.setText("Online");

        friendRequestesLabel.setFont(new java.awt.Font("Purisa", 0, 12)); // NOI18N
        friendRequestesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        friendRequestesLabel.setText("Friend Requests");

        manageFriendButton.setFont(new java.awt.Font("Purisa", 0, 12)); // NOI18N
        manageFriendButton.setText("Manage");
        manageFriendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageFriendButtonActionPerformed(evt);
            }
        });

        openChatButton.setFont(new java.awt.Font("Purisa", 0, 12)); // NOI18N
        openChatButton.setText("Chat");
        openChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openChatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout friendsPanelLayout = new javax.swing.GroupLayout(friendsPanel);
        friendsPanel.setLayout(friendsPanelLayout);
        friendsPanelLayout.setHorizontalGroup(
            friendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(friendsPanelLayout.createSequentialGroup()
                .addComponent(friendsPanelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(friendsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(friendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(friendsPanelLayout.createSequentialGroup()
                        .addComponent(outputFriendListOfflineScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(outputFriendListOnlineScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(friendsPanelLayout.createSequentialGroup()
                        .addGroup(friendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(friendsPanelLayout.createSequentialGroup()
                                .addComponent(inputFriendListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addGroup(friendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(manageFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(openChatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(friendsPanelLayout.createSequentialGroup()
                                .addComponent(offlineFriendsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(onlineFriendsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(friendRequestesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        friendsPanelLayout.setVerticalGroup(
            friendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, friendsPanelLayout.createSequentialGroup()
                .addComponent(friendsPanelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(friendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(offlineFriendsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(onlineFriendsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(friendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outputFriendListOfflineScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outputFriendListOnlineScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(friendRequestesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(friendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputFriendListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(friendsPanelLayout.createSequentialGroup()
                        .addComponent(openChatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(manageFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        mainPanel.add(friendsPanel, "friendsPanel");

        friendsManagementPanelLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        friendsManagementPanelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        friendsManagementPanelLabel.setText("[Friends Management]");

        addRemoveTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        addRemoveTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addRemoveMouseClickedAction(evt);
            }
        });

        addFriendButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        addFriendButton.setText("Add");
        addFriendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFriendButtonActionPerformed(evt);
            }
        });

        removeFriendButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        removeFriendButton.setText("Remove");
        removeFriendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFriendButtonActionPerformed(evt);
            }
        });

        addFriendBackButton.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        addFriendBackButton.setText("Back");
        addFriendBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFriendBackButtonActionPerformed(evt);
            }
        });

        addRemoveInsertLabel.setFont(new java.awt.Font("Purisa", 0, 15)); // NOI18N
        addRemoveInsertLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addRemoveInsertLabel.setText("Insert Contact");

        javax.swing.GroupLayout addRemovePanelLayout = new javax.swing.GroupLayout(addRemovePanel);
        addRemovePanel.setLayout(addRemovePanelLayout);
        addRemovePanelLayout.setHorizontalGroup(
            addRemovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addRemovePanelLayout.createSequentialGroup()
                .addComponent(friendsManagementPanelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(addRemovePanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(addRemovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addRemoveInsertLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addRemoveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addFriendBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        addRemovePanelLayout.setVerticalGroup(
            addRemovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addRemovePanelLayout.createSequentialGroup()
                .addComponent(friendsManagementPanelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(addRemoveInsertLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addRemoveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(addFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(addFriendBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        mainPanel.add(addRemovePanel, "addRemovePanel");

        mainTitleLabel.setFont(new java.awt.Font("Purisa", 1, 20)); // NOI18N
        mainTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainTitleLabel.setText("GOSSIP");

        serverStatusLabel.setFont(new java.awt.Font("Purisa", 0, 10)); // NOI18N
        serverStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(serverStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        mainContainerLayout.setVerticalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainContainerLayout.createSequentialGroup()
                .addComponent(mainTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serverStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(300, 470));
    }// </editor-fold>//GEN-END:initComponents

    /* Esce dall'applicazione */
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed

        /* Esco dall'applicazione */
        System.out.println("Exit GOSSIP Client Chat");
        
        /* Termino il ConnectionSeeker Thread */
        tConnectionSeeker.interrupt();
        
        this.dispose();
                
        System.exit(0);        
    }//GEN-LAST:event_exitButtonActionPerformed

    /* Registra un utente */
    private void reg_registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_registerButtonActionPerformed
        
        /* Controllo che il campo Username non sia vuoto */
        if(reg_usernameTextField.getText().isEmpty()) {
            
            reg_usernameTextField.setBorder(redBorder);
            return;
        }
        
        /* Controllo che il campo Password non sia vuoto */
        if(reg_passwordField.getPassword().length == 0) {
            
            reg_passwordField.setBorder(redBorder);
            return;
        }
        
        /* Controllo che il campo Repeat Password non sia vuoto */
        if(reg_rePasswordField.getPassword().length == 0) {
            
            reg_rePasswordField.setBorder(redBorder);
            return;
        }
        
        /* Controllo che il campo Repeat Password combaci con campo Password */
        if(!Arrays.equals(reg_passwordField.getPassword(), reg_rePasswordField.getPassword())) {
            
            reg_passwordField.setBorder(redBorder);
            reg_rePasswordField.setBorder(redBorder);
            return;
        }
        
        /* Verifico connessione a GOSSIP */
        if(!connection.getConnectionStatus()) {
            
            return;
        }
            
        try {
                
            /* Username dell'utente da registrare */
            String register_username = reg_usernameTextField.getText();
            
            System.out.println("Checking username ["+register_username+"] on GOSSIP Server");
                        
            /* Verifico la disponibilita' dell'Username */
            boolean checkUsernameAvailability = connection.getServerObject().checkUsernameAvailable(register_username);
                
            if(!checkUsernameAvailability) {
                    
                /* Username non disponibile */
                serverStatusLabel.setForeground(Color.red);
                serverStatusLabel.setText("username already exist");
                return;
            }
                   
            /* Password */
            String password = new String(reg_passwordField.getPassword());
            
            System.out.println("Register ["+register_username+"] on GOSSIP Server");
                    
            /* Effettuo la Registrazione */
            boolean registrationStatus = connection.getServerObject().registerUser(register_username, password);
                    
            if(!registrationStatus) {
                        
                /* Registrazione fallita */
                serverStatusLabel.setForeground(Color.red);
                serverStatusLabel.setText("Registration failed");
                return;                        
            }
                        
            /* Registrazione eseguita */
            serverStatusLabel.setForeground(Color.green);
            serverStatusLabel.setText("Registration Success");
            
            System.out.println(register_username+" successfully registered");
                
        }catch(RemoteException e) {
                
            serverStatusLabel.setForeground(Color.red);
            serverStatusLabel.setText("Errore nel Server");
        }
    }//GEN-LAST:event_reg_registerButtonActionPerformed

    /* Passa al pannello registrazioni  */
    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        
        card.show(mainPanel, "registrationPanel");
        serverStatusLabel.setText("");
        reg_usernameTextField.setText("");
        reg_passwordField.setText("");
        reg_rePasswordField.setText("");
    }//GEN-LAST:event_registerButtonActionPerformed

    /* Torna al pannello di Login */
    private void reg_backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_backButtonActionPerformed
        
        card.show(mainPanel, "loginPanel");
        serverStatusLabel.setText("");
    }//GEN-LAST:event_reg_backButtonActionPerformed

    /* Login */
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        
        /* Username utente */
        String username = usernameTextField.getText();
        
        /* Password utente */
        String password = new String(passwordField.getPassword());        
        
        /* Controllo che il campo Username non sia vuoto */
        if(username.isEmpty()) {
            
            usernameTextField.setBorder(redBorder);
            return;
        }
        
        /* Controllo che il campo Password non sia vuoto */
        if(password.isEmpty()) {
            
            passwordField.setBorder(redBorder);
            return;
        }   
        
        /* Controllo che la connessione a GOSSIP sia stabilita */
        if(!connection.getConnectionStatus()) {
            
            return;
        }      
        
        /* Porta UDP Utente */
        int user_port = getRandomNumberInterval(UDP_PORT_MIN,UDP_PORT_MAX);
        
        /* Recupero l'indirizzo IP dell'utente */
        String address;
        
        try {
            
            address = InetAddress.getLocalHost().getHostAddress();
            
            System.out.println("Connection information: "+username+"@"+address+":"+user_port);
        
        }catch(UnknownHostException e) {
            
            serverStatusLabel.setForeground(Color.red);
            serverStatusLabel.setText("Connection Error");
            return;
        }
        
        try {
                
            /* Autenticazione con username/password */
            
            System.out.println("Authentication in progress");
            System.out.println("Generating Client Callback");
                    
            /* Genero la callback da inviare a GOSSIP */
            ClientInterface callbackObj = new ClientImpl(this);
            
            System.out.println("Exporting Client Stub");
                                        
            /* Esporto lo Stub */
            UnicastRemoteObject.unexportObject(callbackObj, true);
            ClientInterface stub = (ClientInterface) UnicastRemoteObject.exportObject((Remote) callbackObj, 0);
            
            /* Aggiorno i dati dell'utente */
            data.setUsername(username);
            data.setAddress(address);
            data.setPort(user_port);
                
            boolean authSuccess = connection.getServerObject().login(username,
                password, stub);
                    
            if(!authSuccess) {
                        
                /* Autenticazione fallita */
                serverStatusLabel.setForeground(Color.red);
                serverStatusLabel.setText("Login Failed");
                return;
            }
                        
            /* Autenticazione eseguita */
            System.out.println(username+" authenticated on GOSSIP"+"@"
                    +DEFAULT_RMI_GOSSIP_HOST+":"+DEFAULT_RMI_GOSSIP_PORT);
                        
            /* Visualizzo Pannello Amici */
            card.show(mainPanel, "friendsPanel");
            serverStatusLabel.setText("");
            
            /* Nome utente nel frame */
            this.setTitle(username);
                        
            /* Thread per la gestione dei pacchetti UDP in uscita */
            tDataOutputHandlerUDP = new Thread(new DataOutputHandler(data),"DataOutputHandler-Thread");
            tDataOutputHandlerUDP.start(); 
            
            /* Thread per la gestione dei pacchetti UDP in entrata */
            dataInputHandler = new DataInputHandler(data);
            tDataInputHandlerUDP = new Thread(dataInputHandler,"DataInputHandler-Thread");
            tDataInputHandlerUDP.start(); 
            
            /* Thread per la gestione dei messaggi scambiati con i Proxy */
            ProxyHandler proxyReceiver = new ProxyHandler(data);
            tProxyHandler = new Thread(proxyReceiver,"ProxyHandler-Thread");
            tProxyHandler.start();
            
            /* Recupero IP/Porta del mio Proxy */
            String proxy_info = connection.getServerObject().getProxyInformation(username);
            
            String[] parts = proxy_info.split(":");
            String proxy_address = parts[0];
            int proxy_port = Integer.parseInt(parts[1]);
            
            if(proxy_info.isEmpty()) {
                
                serverStatusLabel.setForeground(Color.red);
                serverStatusLabel.setText("Failed to retrieve offline messages");
                return;
            }
            
            System.out.println("Retrieving offline messages from Proxy@"+
                    proxy_address+":"+proxy_port);
            
            /* Messaggio di richiesta per scaricare i messaggi offline sul Proxy */
            Message offMsg = new Message("",PROXY_DOWNLOAD,username,"",
                    proxy_address,proxy_port);
            
            try {
                
                data.getProxyQueue().put(offMsg);
                
            }catch(InterruptedException e) {
                
                System.out.println("Failed to retrive offline messages from Proxy");
            }
                
        }catch(RemoteException e) {
            
            serverStatusLabel.setForeground(Color.red);
            serverStatusLabel.setText("Server Error");
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    /* Logout */
    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        
        System.out.println("Logout in progress");
        
        /* Chiudo tutte le Chat Aperte */
        System.out.println("Closing all open chats");
        
        for(Map.Entry<String,FriendHandler> entry : this.data.getFriendList().entrySet()) {
            
            FriendHandler friendHandler = entry.getValue();
            
            /* Controllo se la chat e' aperta */
            if(friendHandler.getChatIsVisible()) {
                
                System.out.println("Chat with "+friendHandler.getFriendUsername()+
                        " closed");
                
                friendHandler.shutdownChatWindow();
            }
        }
                
        /* Disconnessione da GOSSIP */
        try {
                    
            connection.getServerObject().logout(data.getUsername());
        
        }catch(RemoteException e) {
            
            System.out.println(data.getUsername()+" failed to disconnect");
        }
        
        System.out.println(data.getUsername()+" disconnected");
                
        System.out.println("Interrupting connection handlers");
        
        /* Interrompe i Threads per la gestione dei dati in entrata/uscita */
        shutdownHandlers();
        
        /* Torno al Pannello di Login */
        card.show(mainPanel, "loginPanel");
        serverStatusLabel.setText("");
        
        this.setTitle("");
    }//GEN-LAST:event_logoutButtonActionPerformed

    /* Passa al pannello di gestione dei contatti */
    private void manageFriendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageFriendButtonActionPerformed
        
        card.show(mainPanel, "addRemovePanel");
        addRemoveTextField.setForeground(Color.black);
        addRemoveTextField.setText("");
    }//GEN-LAST:event_manageFriendButtonActionPerformed

    /* Torna al pannello dei contatti */
    private void addFriendBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFriendBackButtonActionPerformed
        
        card.show(mainPanel, "friendsPanel");
    }//GEN-LAST:event_addFriendBackButtonActionPerformed

    /* Aggiunge un contatto */
    private void addFriendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFriendButtonActionPerformed
        
        /* Recupero l'username del contatto */
        String friend_username = addRemoveTextField.getText();
        
        /* Controllo che l'username non sia vuoto */
        if(friend_username.isEmpty()) {
            
            addRemoveTextField.setBorder(redBorder);
            return;
        }
        
        try {
            
            System.out.println("Trying to add "+friend_username);
            
            /* Aggiungi Amico */
            boolean addFriendResponse = connection.getServerObject().addFriend(
                this.data.getUsername(), friend_username);
                        
            if(addFriendResponse) {
                
                /* Amico aggiunto correttamente */
                serverStatusLabel.setForeground(Color.green);
                serverStatusLabel.setText(friend_username + " added");
            }
            else {
                
                serverStatusLabel.setForeground(Color.red);
                serverStatusLabel.setText("Friend not exist");
            }
        }catch(RemoteException e) {
            
            serverStatusLabel.setForeground(Color.red);
            serverStatusLabel.setText("Server Error");
        }
    }//GEN-LAST:event_addFriendButtonActionPerformed
 
    /* Rimuove un contatto */
    private void removeFriendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFriendButtonActionPerformed
        
        /* Recupero l'username del contatto */
        String friend_username = addRemoveTextField.getText();
        
        /* Controllo che l'username non sia vuoto */
        if(friend_username.isEmpty()) {
            
            addRemoveTextField.setForeground(Color.red);
            addRemoveTextField.setText("Inserire Username!");
            return;
        }
        
        try {
            
            System.out.println("Trying to remove "+friend_username);
            
            boolean removeFriendResponse = connection.getServerObject().removeFriend(
                    data.getUsername(), friend_username);
            
            if(removeFriendResponse) {
                
                serverStatusLabel.setForeground(Color.red);
                serverStatusLabel.setText(friend_username + " removed");
            }
            else {
                
                serverStatusLabel.setForeground(Color.red);
                serverStatusLabel.setText("Friend not exist");
            }
            
            System.out.println(friend_username+" removed");
        
        }catch(RemoteException e) {
            
            serverStatusLabel.setForeground(Color.red);
            serverStatusLabel.setText("Server Error");
        }        
    }//GEN-LAST:event_removeFriendButtonActionPerformed

    /* Apre una chat con un contatto */
    private void openChatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openChatButtonActionPerformed
                
        /* Controllo se ho selezionato un amico offline o online */
        if(friends_outputFriendListOffline.isSelectionEmpty() && 
                friends_outputFriendListOnline.isSelectionEmpty()) {
            
            serverStatusLabel.setForeground(Color.red);
            serverStatusLabel.setText("Select friend to start chat");
            return;
        }
        
        String friend_username = null;
        
        /* Ho selezionato un amico offline */
        if(!friends_outputFriendListOffline.isSelectionEmpty()) {
        
            friend_username = friends_outputFriendListOffline.getSelectedValue().toString();
        }
        
        /* Ho selezionato un amico online */
        if(!friends_outputFriendListOnline.isSelectionEmpty()) {
            
            friend_username = friends_outputFriendListOnline.getSelectedValue().toString();
        }
        
        System.out.println("Opening chat with "+friend_username);
                     
        /* Recupero l'istanza del contatto */
        System.out.println("Recovery informations about "+friend_username);
        
        FriendHandler friendHandler = data.getFriendList().get(friend_username);
        
        /* Contatto non trovato */
        if(friendHandler == null) {
            
            serverStatusLabel.setForeground(Color.red);
            serverStatusLabel.setText("Error start chat");
            return;
        }
        
        if(friendHandler.getChatIsOpen()) {
            
            /* Verifico se la chat e' aperta */
            if(friendHandler.getChatIsVisible()) {
                
                serverStatusLabel.setForeground(Color.red);
                serverStatusLabel.setText("Chat already in progress");
            }
            else {
                
                /* Riapro la chat */
                friendHandler.getChatWindow().setVisible(true);
                friendHandler.setChatIsVisible(true);
            }
        }
        else {
                        
            /* Avvio una nuova chat */
            friendHandler.startChatWindow();
        }
        
        System.out.println("Chat with "+friend_username+" started");
    }//GEN-LAST:event_openChatButtonActionPerformed

    /* Deseleziona la lista dei contatti offline */
    private void outputFriendListOnlineMouseClickedAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outputFriendListOnlineMouseClickedAction
        
        friends_outputFriendListOffline.clearSelection();
    }//GEN-LAST:event_outputFriendListOnlineMouseClickedAction

    /* Deseleziona la lista dei contatti online */
    private void outputFriendListOfflineMouseClickedAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outputFriendListOfflineMouseClickedAction
        
        /* Deseleziono la lista degli utenti online */
        friends_outputFriendListOnline.clearSelection();
    }//GEN-LAST:event_outputFriendListOfflineMouseClickedAction

    /* Cancella il contenuto del campo username nel pannello registrazioni al click */
    private void regUsernameMouseClickedAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regUsernameMouseClickedAction
       
        reg_usernameTextField.setText("");
        reg_usernameTextField.setBorder(blackBorder);
    }//GEN-LAST:event_regUsernameMouseClickedAction

    /* Cancella il contenuto del campo password nel pannello registrazioni al click */
    private void regPasswordMouseClickedAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regPasswordMouseClickedAction
        
        reg_passwordField.setText("");
        reg_passwordField.setBorder(blackBorder);
    }//GEN-LAST:event_regPasswordMouseClickedAction

    /* Cancella il contenuto del campo re-password nel pannello registrazioni al click */
    private void regRePasswordMouseClickedAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regRePasswordMouseClickedAction
        
        reg_rePasswordField.setText("");
        reg_rePasswordField.setBorder(blackBorder);
    }//GEN-LAST:event_regRePasswordMouseClickedAction

    /* Cancella il contenuto del campo username nel pannello login al click */
    private void usernameMouseClickedAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameMouseClickedAction
        
        usernameTextField.setText("");
        usernameTextField.setBorder(blackBorder);
    }//GEN-LAST:event_usernameMouseClickedAction

    /* Cancella il contenuto del campo password nel pannello login al click */
    private void passwordMouseClickedAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordMouseClickedAction
        
        passwordField.setText("");
        passwordField.setBorder(blackBorder);
    }//GEN-LAST:event_passwordMouseClickedAction

    /* Cancella il contenuto del campo username nel pannello gestione contatti al click */
    private void addRemoveMouseClickedAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addRemoveMouseClickedAction
        
        addRemoveTextField.setText("");
        addRemoveTextField.setBorder(blackBorder);
    }//GEN-LAST:event_addRemoveMouseClickedAction
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFriendBackButton;
    private javax.swing.JButton addFriendButton;
    private javax.swing.JLabel addRemoveInsertLabel;
    private javax.swing.JPanel addRemovePanel;
    private javax.swing.JTextField addRemoveTextField;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel friendRequestesLabel;
    private javax.swing.JLabel friendsManagementPanelLabel;
    private javax.swing.JPanel friendsPanel;
    private javax.swing.JLabel friendsPanelLabel;
    private javax.swing.JList friends_inputFriendList;
    private javax.swing.JList friends_outputFriendListOffline;
    private javax.swing.JList friends_outputFriendListOnline;
    private javax.swing.JScrollPane inputFriendListScrollPane;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel loginPanelLabel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel mainTitleLabel;
    private javax.swing.JButton manageFriendButton;
    private javax.swing.JLabel offlineFriendsLabel;
    private javax.swing.JLabel onlineFriendsLabel;
    private javax.swing.JButton openChatButton;
    private javax.swing.JScrollPane outputFriendListOfflineScrollPane;
    private javax.swing.JScrollPane outputFriendListOnlineScrollPane;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton reg_backButton;
    private javax.swing.JPasswordField reg_passwordField;
    private javax.swing.JLabel reg_passwordLabel;
    private javax.swing.JPasswordField reg_rePasswordField;
    private javax.swing.JLabel reg_rePasswordLabel;
    private javax.swing.JButton reg_registerButton;
    private javax.swing.JLabel reg_usernameLabel;
    private javax.swing.JTextField reg_usernameTextField;
    private javax.swing.JButton registerButton;
    private javax.swing.JPanel registrationPanel;
    private javax.swing.JLabel registrationPanelLabel;
    private javax.swing.JButton removeFriendButton;
    private javax.swing.JLabel serverStatusLabel;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables
}