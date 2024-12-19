import server.UDPServer;

public class Main {
    public static void main(String[] args) {
        UDPServer server = new UDPServer(12345);
        server.start();
    }
}
