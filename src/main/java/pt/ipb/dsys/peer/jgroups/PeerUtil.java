package pt.ipb.dsys.peer.jgroups;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PeerUtil {

  /**
   * By checking if the @gossipHostname hostname is set (which happens only inside docker)
   * we are able to check if this is a peer inside docker
   *
   * @param gossipHostname The name of the gossip router inside docker
   * @return Whether this is a peer running inside docker
   */
  public static boolean isInsideDocker(String gossipHostname) {
    try {
      InetAddress.getByName(gossipHostname);
      return true;
    } catch (UnknownHostException e) {
      return false;
    }
  }

  /**
   * If not running inside docker, bind to address 127.0.0.1.
   * This prevents network problems that happen (mostly) in windows
   * See: <a href="https://docs.jboss.org/jbossas/docs/Clustering_Guide/beta422/html/ch07s07s07.html">https://docs.jboss.org/jbossas/docs/Clustering_Guide/beta422/html/ch07s07s07.html</a>
   */
  public static void localhostFix(String gossipHostname) {
    if (!isInsideDocker(gossipHostname))
      System.setProperty("jgroups.bind_addr", "127.0.0.1");
  }

}
