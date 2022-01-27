package niconoggi.clientserver.base;

import java.net.Socket;

/**
 * An abstract implementation of the {@link CommunicationComponent} interface.
 * It provides a {@link Socket} and information about host ip and port
 * @author niconoggi
 *
 */
public abstract class AbstractClient implements CommunicationComponent{

	/** this represents the client */
	protected Socket socket;
	protected String host;
	protected int port;
	
	public AbstractClient() {}
	
	public AbstractClient(final String host) {
		setHost(host);
	}
	
	public AbstractClient(final int port) {
		setPort(port);
	}
	
	public AbstractClient(final String host, final int port) {
		setHost(host);
		setPort(port);
	} 
	
	public String getHost() {
		return host;
	}
	
	public void setHost(final String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(final int port) {
		this.port = port;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(!(obj instanceof AbstractClient)) {
			return false;
		}
		
		final AbstractClient other = (AbstractClient) obj;
		
		return socket.equals(other.socket) && host.equals(other.host) && port == other.port;
	}
	
	@Override
	public int hashCode() {
		return socket.hashCode() + (host == null ? 0 : host.hashCode()) + port;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Socket: {").append(socket.toString()).append("}");
		builder.append(", host = ").append(host);
		builder.append(", port = ").append(port);
		return builder.toString();
	}
}
