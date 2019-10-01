/**
 * ServerConstants.java
 */

package server.interfaces;

/**
 *
 * @author Matteo Bogo
 */
public interface ServerConstants {
    
    /* Database */
    final static int USER_ON_INPUT_LIST_ID = 0;
    final static int USER_ON_OUTPUT_LIST_ID = 1;
    final static int USER_NOT_IN_LIST_ID = -1;
    final static String SQLLITE_DRIVER = "org.sqlite.JDBC";
    final static String SQLLITE_PATH = "jdbc:sqlite:data.db";
    
    /* TimeoutChecker */
    final static int CHECKING_INTERVAL = 10000;

    /* RMI Parameters */
    final static String RMI_SKELETON_NAME = "GOSSIP-Registry";
    final static int RMI_PORT = 10000;
    
    /* Proxy */
    final static int PROXY_DELAY = 1000;
    final static int PROXY_WORKER_TIMEOUT = 8000;
    final static String PROXY_DOWNLOAD = "DOWNLOAD";
    final static String PROXY_DOWNLOAD_OK = "DOWNLOAD_OK";
    final static String PROXY_END = "END";
    final static String MESSAGE_HEADER = "MESSAGE";
}