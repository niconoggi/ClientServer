package niconoggi.clientserver.base;

import java.io.IOException;
import java.net.ServerSocket;

import niconoggi.clientserver.server.ServerValues;

/**
 * an abstract class providing the basics a server needs,
 * such as a {@link ServerSocket} and a start and stop method
 * @author niconoggi
 *
 */
public abstract class AbstractServer implements CommunicationComponent{
	
	protected ServerSocket server;
	protected int port;
	
	protected int connectTimeout = ServerValues.DEFAULT_CONNECTION_TIMEOUT;
	protected int maxRetries = ServerValues.DEFAULT_CONNECTION_RETRIES;
	protected int connectionRetries;
	
	public AbstractServer() {}
	
	/**
	 * Constructor that also sets the port
	 * @param port the port the server will be bound to
	 */
	public AbstractServer(final int port) {
		setPort(port);
	}
	
	/**
	 * Starts the server by initializing the {@link ServerSocket}.
	 * <p>
	 * NOTE: This start method assumes the port is already set
	 * @throws IOException an error when instanciating the {@link ServerSocket}.
	 * 						could happen if the given port is already bound
	 */
	public void start() throws IOException{
		server = new ServerSocket(port);
	}
	
	/**
	 * starts the server after setting the port
	 * @param port the port to bind to
	 * @throws IOException an error when instanciating the {@link ServerSocket}.
	 * 						Could happen if the given port is already bound
	 */
	public void start(final int port) throws IOException{
		setPort(port);
		start();
	}
	
	/**
	 * disconnects the server and closes the {@link ServerSocket}
	 * @throws IOException an error occuring while trying to disconnect or close
	 */
	public void stop() throws IOException{
		disconnect();
		server.close();
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(final int port) {
		this.port = port;
	}
	
	public int getMaxRetries() {
		return maxRetries;
	}
	
	public void setMaxRetries(final int maxRetries) {
		this.maxRetries = maxRetries;
	}
	
	public int getConnectionRetries() {
		return connectionRetries;
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}
	
	public void setConnectTimeout(final int timeout) {
		connectTimeout = timeout;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(!(obj instanceof AbstractServer)) {
			return false;
		}
		
		AbstractServer other = (AbstractServer) obj;
		
		return server.equals(other.server) && port == other.port;
	}
	
	@Override
	public int hashCode() {
		return server.hashCode() + port;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ServerSocket: {");
		builder.append(server.toString()).append("}");
		builder.append(", port = ").append(port);
		return builder.toString();
	}
}
