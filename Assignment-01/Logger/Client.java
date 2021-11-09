import java.net.*;
import java.util.*;

public class Client {
    static final int PORT = 1337; // choose some port that is not in use
    static final int bufferSize = 10240; // max 10KB message

    public static InetAddress getBroadcastAddress() {
        try {
            // get network cards
            var netIntFaces = NetworkInterface.getNetworkInterfaces();

            // for each network card do
            while(netIntFaces.hasMoreElements()) {
                NetworkInterface netIntFace = netIntFaces.nextElement();

                if(netIntFace == null) {
                    continue;
                } 
                
                // if network card can actually be used
                if(netIntFace.isUp() && !netIntFace.isLoopback()) {
                    Iterator<InterfaceAddress> it = netIntFace.getInterfaceAddresses().iterator();
                    while (it.hasNext()) {
                        // get possible network addresses
                        InterfaceAddress address = it.next();
                        
                        if(address == null) {
                            continue;
                        }

                        // get broadcast addresses and check for null
                        InetAddress broadcast = address.getBroadcast();
                        if(broadcast != null) {
                            return broadcast;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        //find broadcast address
        InetAddress broadcast = getBroadcastAddress();
        System.out.println("Broadcast Addr: " + broadcast);
        if(broadcast != null) {
            //create a udp socket to the broadcast server
            connectToServer(broadcast, args);
        }
    }

    static void connectToServer(InetAddress broadcast, String[] messages) {
        try {
        DatagramSocket datasock = new DatagramSocket();
        datasock.setBroadcast(true);
        byte[] data = new byte[bufferSize];
        int actualDataLength = 0;
        // write String[] messages => datagramPacket.data[]
        for(String msg : messages) {
            for(int i = 0; i < msg.length(); i++) {
                int c = actualDataLength;
                data[c] = (byte) msg.charAt(i);
                // restrict max size
                if(actualDataLength++ > bufferSize) break;
            }
            if(actualDataLength > bufferSize) break;
        }
        // send the packet
        DatagramPacket pack = new DatagramPacket(data, actualDataLength, broadcast, PORT);
        datasock.send(pack);

        datasock.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
