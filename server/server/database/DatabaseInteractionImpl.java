/*
 * DatabaseInteractionImpl.java
 */

package server.database;

import java.sql.*;
import java.util.ArrayList;
import server.interfaces.ServerConstants;

/**
 *
 * @author Matteo Bogo
 */
public class DatabaseInteractionImpl implements DatabaseInteraction, ServerConstants {
    
    @Override
    public void initDatabase() {
        
        System.out.println("DB: Starting database");
        
        /* Risorse SQL */
        Connection con;
        Statement stmt;
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            /* Tabella UtentiRegistrati */
            
            String sql = "CREATE TABLE IF NOT EXISTS UtentiRegistrati" +
                         "(ID               INTEGER     PRIMARY KEY," +
                         " username         TEXT        NOT NULL UNIQUE," +
                         " password         TEXT        NOT NULL)";
            
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            
            /* Tabella UtentiContattiIngresso */
            
            sql = "CREATE TABLE IF NOT EXISTS UtentiContattiIngresso" +
                  "(ID              INTEGER     PRIMARY KEY," +  
                  " username        TEXT        NOT NULL," +
                  " input_contact   TEXT        )";
            
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            
            /* Tabella UtentiContattiUscita */
            
            sql = "CREATE TABLE IF NOT EXISTS UtentiContattiUscita" +
                  "(ID              INTEGER     PRIMARY KEY," +
                  " username        TEXT        NOT NULL," +
                  " output_contact  TEXT        )";
            
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
                        
            stmt.close();
            con.close();
            
        }catch(Exception e) {
            System.out.println("DB: error initialize database");
            System.exit(0);
        }
        
        System.out.println("DB: database initialization is successful");
    }

    @Override
    public boolean registerUser(String username, String password) {
        
        System.out.println("DB: ["+username+"] --> registerUser");
        
        /* Risorse SQL */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            /* Gestione manuale delle transazioni */
            con.setAutoCommit(false);
            
            /* Controllo disponibilita' username */
            String sql = "SELECT username FROM UtentiRegistrati WHERE username=?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            boolean found = false;
            while(rs.next()) {
                
                found = true;
            }
                        
            rs.close();
            pstmt.close();
            
            /* Username gia' in uso */
            if(found) {
                
                con.close();
                return false;
            }
            else {
                
                /* Inserisco username/password nel database */
                
                sql = "INSERT INTO UtentiRegistrati (username,password) VALUES (?,?)";
                
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                
                pstmt.executeUpdate();
                
                /* Eseguo la transazione */
                con.commit();
                
                pstmt.close();
                con.close();
                
                System.out.println("DB: registerUser is successful");
                
                return true;
            }
            
        }catch(Exception ex) {
            
            System.out.println("DB: error registerUser failed ");
            
            /* Annullo la transazione */
            try {
                con.rollback();
                return false;
            }catch(SQLException e) {
                throw new RuntimeException(ex);
            }
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }
    }
    
    @Override
    public ArrayList<String> getRegisteredUsers() {
        
        System.out.println("DB: getRegisterUsers");
        
        /* Risorse SQL */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            ArrayList<String> registeredUsers = new ArrayList<String>();
            
            /* Recupero tutti gli utenti registrati a GOSSIP */
            String sql = "SELECT username FROM UtentiRegistrati";
            
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                
                registeredUsers.add(rs.getString(1));
            }
            
            rs.close();
            pstmt.close();
            con.close();
            
            return registeredUsers;
            
        }catch(Exception e) {
            
            System.out.println("DB: error getRegisterUsers failed");
            throw new RuntimeException(e);
            
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }
    }
    
    @Override
    public boolean checkUser(String username) {
        
        System.out.println("DB: checkUser --> ["+username+"]");
        
        /* Risorse SQL */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            /* Controllo le credenziali dell'utente */
            String sql = "SELECT username FROM UtentiRegistrati WHERE username=?";
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            boolean found = false;
            while(rs.next()) {
                
                found = true;
            }
            
            rs.close();
            pstmt.close();
            con.close();
            
            System.out.println("DB: checkUser is successful");
            
            return found;
            
        }catch(Exception e) {
            
            System.out.println("DB: error checkUser failed");
            throw new RuntimeException(e);
            
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }
    }
    
    @Override
    public boolean userAuthentication(String username, String password) {
        
        System.out.println("DB: ["+username+"] --> userAuthentication");
        
        /* Risorse SQL */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            /* Controllo le credenziali dell'utente */
            String sql = "SELECT * FROM UtentiRegistrati WHERE username=? AND password=?";
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            
            boolean found = false;
            while(rs.next()) {
                
                found = true;
            }
            
            rs.close();
            pstmt.close();
            con.close();
            
            if(found) {
            
                System.out.println("DB: userAuthentication is successful");
            }
            else {
                
                System.out.println("DB: userAuthentication failed");
            }
            
            return found;
            
        }catch(Exception e) {
            
            System.out.println("DB: error userAuthentication failed");
            throw new RuntimeException(e);
            
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }
    }
    
    @Override
    public int checkUserOnFriendList(String username, String friend_username) {
        
        System.out.println("DB: ["+username+"] --> checkUserOnFriendList --> ["+friend_username+"]");
        
        /* Risorse SQL */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        boolean found = false;
        int idFriendList = USER_NOT_IN_LIST_ID;
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
                        
            /* Controllo sulla lista dei contatti di ingresso */
            String sql = "SELECT * FROM UtentiContattiIngresso WHERE username=? AND input_contact=?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, friend_username);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                
                idFriendList = USER_ON_INPUT_LIST_ID;
                found = true;
            }
            
            if(found) {
            
                /* Contatto nella lista dei contatti di ingresso */
                
                rs.close();
                pstmt.close();
                con.close();
                
                System.out.println("DB: checkUserOnFriendList is successful: ["+
                        friend_username+"] on [UtentiContattiIngresso]");
                
                return idFriendList;
            }
                                                        
            /* Controllo sulla lista dei contatti di uscita */
            sql = "SELECT * FROM UtentiContattiUscita WHERE username=? AND output_contact=?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, friend_username);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                
                idFriendList = USER_ON_OUTPUT_LIST_ID;
                found = true;
            }
            
            if(found) {
            
                /* Contatto sulla lista dei contatti di uscita */
                
                rs.close();
                pstmt.close();
                con.close();
                
                System.out.println("DB: checkUserOnFriendList is successful: ["+
                        friend_username+"] on [UtentiContattiUscita]");
                
                return idFriendList;
            }
            
            return idFriendList;
                        
        }catch(Exception e) {
            
            System.out.println("DB: error checkUserOnFriendList failed");
            throw new RuntimeException(e);
            
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }
    }
    
    @Override
    public ArrayList<String> getInputFriendList(String username) {
        
        System.out.println("DB: ["+username+"] --> getInputFriendList");
        
        /* Risorse SQL */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        /* Lista dei contatti di ingresso dell'utente */
        ArrayList<String> inputFriendList = new ArrayList<String>();
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            /* Recupero la lista contatti in ingresso */
            String sql = "SELECT input_contact FROM UtentiContattiIngresso uci, UtentiRegistrati ur"
                    + " WHERE ur.username=? AND ur.username=uci.username";
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                
                inputFriendList.add(rs.getString(1));
            }
            
        System.out.println("DB: getInputFriendList is successful");
        
        return inputFriendList;
        
        }catch(Exception e) {
            
            System.out.println("DB: error getInputFriendList failed");
            throw new RuntimeException(e);
            
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }
    }
    
    @Override
    public synchronized ArrayList<String> getFriendList(String username) {
        
        System.out.println("DB: ["+username+"] --> getFriendLists");
        
        /* Risorse SQL */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        /* Lista dei contatti di uscita */
        ArrayList<String> friendList = new ArrayList<String>();
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            /* Recupero la lista dei contatti di uscita dell'utente */
            String sql = "SELECT output_contact FROM UtentiContattiUscita ucu, UtentiRegistrati ur"
                    + " WHERE ur.username=? AND ur.username=ucu.username";
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                    
                friendList.add(rs.getString(1));
            }
                        
            System.out.println("DB: getFriendLists is successful");
        
            return friendList;
            
        }catch(Exception e) {
            
            System.out.println("DB: error getFriendLists failed");
            throw new RuntimeException(e);
            
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }
    }
        
    @Override
    public boolean addFriend(String username, String friend_username) {
        
        System.out.println("DB: ["+username+"] --> addFriend --> ["+friend_username+"]");
        
        /* Risorse SQL */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            /* Gestione manuale delle transazioni */
            con.setAutoCommit(false);
            
            /* Controllo che l'username del contatto esista nel sistema */
            String sql = "SELECT username FROM UtentiRegistrati WHERE username=?";
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, friend_username);
            rs = pstmt.executeQuery();
                        
            boolean userExist = false;
            while(rs.next()) {
                
                userExist = true;
            }
            
            rs.close();
            pstmt.close();
            
            if(!userExist) {
                
                con.close();
                return false;
            }
            else {
                
                /* Se il contatto mi ha a sua volta aggiunto nella sua lista
                 * dei contatti di ingresso, allora lo sposto direttamente
                 * nella lista dei contatti di uscita.
                 */
                
                /* Controllo che il contatto mi abbia aggiunto nella sua lista 
                 * dei contatti di ingresso.
                 */
                sql = "SELECT * FROM UtentiContattiIngresso WHERE username=? AND input_contact=?";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, friend_username);
                pstmt.setString(2, username);
                rs = pstmt.executeQuery();
                
                boolean contactHasMeAsFriend = false;
                while(rs.next()) {
                
                    contactHasMeAsFriend = true;
                }
                
                rs.close();
                
                if(contactHasMeAsFriend) {
                    
                    /* Il Contatto mi ha aggiunto a sua volta come amico, lo sposto
                     * nella mia lista di contatti in uscita.
                     */
                    sql = "INSERT INTO UtentiContattiUscita(username,output_contact) VALUES (?,?)";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, username);
                    pstmt.setString(2, friend_username);
                    
                    pstmt.executeUpdate();
                    
                    /* Dato che siamo entrambi amici, adesso anche il contatto mi sposta
                     * nella sua lista dei contatti in uscita.
                     */
                    
                    /* Mi cancello dalla sua lista dei contatti di ingresso e mi sposto nella
                     * sua lista dei contatti di uscita.
                     */
                    
                    sql = "DELETE FROM UtentiContattiIngresso WHERE username=? AND input_contact=?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, friend_username);
                    pstmt.setString(2, username);
                    
                    pstmt.executeUpdate();
                    
                    sql = "INSERT INTO UtentiContattiUscita(username,output_contact) VALUES (?,?)";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, friend_username);
                    pstmt.setString(2, username);
                    
                    pstmt.executeUpdate();
                }
                else {
                    
                    /* Il Contatto NON mi ha aggiunto a sua volta come amico,
                     * allora lo aggiungo nella lista dei contatti di ingresso.
                     */
                    
                    sql = "INSERT INTO UtentiContattiIngresso(username,input_contact) VALUES (?,?)";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, username);
                    pstmt.setString(2, friend_username);
                
                    pstmt.executeUpdate();
                }
                
                /* Eseguo la transazione */
                con.commit();
                
                pstmt.close();
                con.close();
                
                System.out.println("DB: addFriend is successful");
                
                return true;                
            }
            
        }catch(Exception ex) {
            
            System.out.println("DB: error addFriend failed");
            
            /* Annullo la transazione */
            try {
                con.rollback();
                return false;
            }catch(SQLException e) {
                throw new RuntimeException(ex);
            }
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }  
    }
    
    @Override
    public int removeFriend(String username, String friend_username) {
        
        System.out.println("DB: ["+username+"] --> removeFriend --> ["+friend_username+"]");
        
        /* Risorse SQL */       
        Connection con = null;
        PreparedStatement pstmt = null; 
        ResultSet rs = null;
        
        try {
            
            con = DatabaseInteractionImpl.getDBConnection(SQLLITE_PATH);
            
            int i = USER_NOT_IN_LIST_ID;
            
            /* Controllo se il contatto si trova nella lista dei contatti di ingresso */
            String sql = "DELETE FROM UtentiContattiIngresso WHERE username=? AND input_contact=?";
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, friend_username);
            i = pstmt.executeUpdate();
                
            pstmt.close();  
            
            if(i > 0) {
                
                /* Ho modificato la lista dei contatti di ingresso */
                con.close();
                
                System.out.println("DB: removeFriend is successful on [UtentiContattiIngresso]");
                return USER_ON_INPUT_LIST_ID;
            }
            
            /* Controllo se il contatto si trova nella lista dei contatti di uscita */
            sql = "DELETE FROM UtentiContattiUscita WHERE username=? AND output_contact=?";
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, friend_username);
            i = pstmt.executeUpdate();
            
            pstmt.close();
            
            if(i > 0) {
                
                /* Ho modificato la lista dei contatti di uscita dell'utente */
                
                /* Cancello l'utente anche dalla lista dei contatti di uscita del contatto */
                sql = "DELETE FROM UtentiContattiUscita WHERE username=? AND output_contact=?";
                
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, friend_username);
                pstmt.setString(2, username);
                pstmt.executeUpdate();
                
                pstmt.close();

                System.out.println("DB: removeFriend is successful on [UtentiContattiUscita]");
                return USER_ON_OUTPUT_LIST_ID;
            }
            
            /* Se sono arrivato qui, il contatto non esiste */
            con.close();
            
            return i;
 
        }catch(Exception ex) {
            
            System.out.println("DB: error removeFriend failed");
            throw new RuntimeException(ex);
                    
        }finally {
            
            /* Rilascio le risorse */
            if(pstmt != null)
                try {
                    pstmt.close();
                }catch(Exception e) {
                    System.out.println("DB: closing statement failed");
                }
            
            if(con != null)
                try {
                    con.close();
                }catch(Exception e) {
                    System.out.println("DB: closing connection failed");
                }
            if(rs != null)
                try {
                    rs.close();
                }catch(Exception e) {
                    System.out.println("DB: closing resultset failed");
                }
        }
    }
    
    /* Stabilisce una connessione con la base di dati */
    private static Connection getDBConnection(String SQLLITE_PATH) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        
        /* Caricamento classe driver JDBC */
        Class.forName(SQLLITE_DRIVER).newInstance();
        
        /* Connessione al Database */
        Connection connectionDB = DriverManager.getConnection(SQLLITE_PATH);
        
        if(connectionDB == null) {
            
            System.out.println("DB: connection to DB failed");
        }
        else
            System.out.println("DB: connection to DB is successful");
        
        /* Default: Ogni operazione viene riportata immediatamente sulla base di dati */
        connectionDB.setAutoCommit(true);
        
        return connectionDB;
    }
}