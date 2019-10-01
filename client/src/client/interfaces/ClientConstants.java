/**
 * ClientConstants.java
 */

package client.interfaces;

/**
 *
 * @author Matteo Bogo
 */
public interface ClientConstants {
    
    /* RMI PARAMETERS */
    final static int DEFAULT_RMI_GOSSIP_PORT = 10000;
    final static String DEFAULT_RMI_GOSSIP_STUB = "GOSSIP-Registry";
    final static String DEFAULT_RMI_GOSSIP_HOST = "127.0.0.1";
    
    /* CONNECTION ATTEMPTS PARAMETERS */
    final int SECONDS_BEFORE_CONNECTION_ATTEMPT = 30;
    final int CONNECTION_ATTEMPTS = 3;
    
    /* UDP */
    final static int UDP_PORT_MIN = 49152;
    final static int UDP_PORT_MAX = 65535;
    
    /* MESSAGE HEADER */
    final static String MESSAGE_HEADER = "MESSAGE";
        
    /* NOTIFICATIONS */
    final static String FRIEND_IS_ONLINE = " has come online";
    final static String FRIEND_IS_OFFLINE = " has gone offline";
    final static String MSG_TO_PROXY = "Messages will be delivered to Proxy";
    
    /* PROXY */
    final static String PROXY_DOWNLOAD = "DOWNLOAD";
    final static String PROXY_DOWNLOAD_OK = "DOWNLOAD_OK";
    final static String PROXY_END = "END";
    
    /* CONNECTION SEEKER */
    final static int CONNECTION_SEEKER_INTERVAL = 5000;
    final static int CONNECTION_SEEKER_SECOND = 1000;
    
    /* DATA INPUT HANDLER */
    final static int DATA_INPUT_HANDLER_DELAY = 1000;
    final static int DATA_INPUT_HANDLER_UDP_BUFFER = 1024;
    
    /* DATA OUTPUT HANDLER */
    final static int DATA_OUTPUT_HANDLER_DELAY = 1000;
    
    /* PROXY HANDLER */
    final static int PROXY_HANDLER_DELAY = 1000;
    final static int PROXY_HANDLER_TIMEOUT = 10000;
}