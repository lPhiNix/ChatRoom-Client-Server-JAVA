package common.socket;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * La clase {@code UDPUtil} proporciona utilidades para manejar operaciones comunes
 * relacionadas con la comunicación de red a través del protocolo UDP.
 *
 * Contiene métodos para obtener la dirección IP local, el nombre del host,
 * crear sockets UDP y resolver nombres de host a direcciones IP.
 */
public class UDPUtil {

    /**
     * Obtiene la dirección IP local de la máquina.
     *
     * @return La dirección IP local de la máquina como una cadena de texto.
     * @throws UnknownHostException Si ocurre un error al obtener la dirección IP.
     */
    public static String getLocalIP() throws UnknownHostException {
        // Devuelve la dirección IP local del host
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Obtiene el nombre del host local.
     *
     * @return El nombre del host local.
     * @throws UnknownHostException Si ocurre un error al obtener el nombre del host.
     */
    public static String getLocalHostName() throws UnknownHostException {
        // Devuelve el nombre del host local
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * Crea un nuevo socket UDP sin especificar un puerto.
     *
     * @return Un objeto {@code DatagramSocket} creado sin puerto especificado.
     * @throws SocketException Si ocurre un error al crear el socket.
     */
    public static DatagramSocket createSocket() throws SocketException {
        // Crea y devuelve un nuevo socket UDP
        return new DatagramSocket();
    }

    /**
     * Crea un nuevo socket UDP en el puerto especificado.
     *
     * @param port El puerto en el que se creará el socket UDP.
     * @return Un objeto {@code DatagramSocket} creado en el puerto especificado.
     * @throws SocketException Si ocurre un error al crear el socket.
     */
    public static DatagramSocket createSocket(int port) throws SocketException {
        // Crea y devuelve un nuevo socket UDP en el puerto especificado
        return new DatagramSocket(port);
    }

    /**
     * Resuelve un nombre de host a su dirección IP correspondiente.
     *
     * @param host El nombre del host a resolver.
     * @return La dirección IP asociada con el nombre de host.
     * @throws UnknownHostException Si ocurre un error al resolver el nombre del host.
     */
    public static InetAddress getInetAddressByName(String host) throws UnknownHostException {
        // Resuelve y devuelve la dirección IP correspondiente al nombre de host
        return InetAddress.getByName(host);
    }
}
