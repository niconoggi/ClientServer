package niconoggi.clientserver.base;

import java.io.IOException;

/**
 * The interface providing the four necessary methods a
 * client or server should have: connecting, writing, reading,
 * and disconnecting
 * @author niconoggi
 */
public interface CommunicationComponent {

	/**
	 * connects this communicator
	 * @throws IOException an error occuring during connection
	 */
	void connect() throws IOException;
	
	/**
	 * writes data to communication partner(s)
	 * 
	 * @throws IOException an error occuring during the write process
	 */
	void write() throws IOException;
	
	/**
	 * reads data by communication partner(s)
	 * 
	 * @throws IOException an error occuring during the read process
	 */
	void read() throws IOException;
	
	/**
	 * disconnects either itself or other communication partners
	 *
	 * @throws IOException an error occuring disconnection
	 */
	void disconnect() throws IOException;
}
