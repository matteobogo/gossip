/**
 * MainClient.java
 */

package client.implementations;

import client.GUI.MainWindow;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

/**
 *
 * @author Matteo Bogo
 */
public class MainClient {
    
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
		
                try {
                    
                    /* Finestra Principale */
                    MainWindow loginFrame = new MainWindow();
                		
                    /* Autoposizionamento frame al centro dello schemo */
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    loginFrame.setLocation(dim.width/2-loginFrame.getSize().width/2, dim.height/2-loginFrame.getSize().height/2);
                    loginFrame.setResizable(false);
                    loginFrame.setVisible(true);
                    
                    System.out.println("Client GUI successfully launched");
                    
		}catch(Exception e) {
                    System.out.println("Error launch Client GUI");
                    System.exit(1);
		}
            }
	});
    }
}