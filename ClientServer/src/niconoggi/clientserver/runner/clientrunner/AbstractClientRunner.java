package niconoggi.clientserver.runner.clientrunner;

import java.io.IOException;

import niconoggi.clientserver.base.AbstractClient;
import niconoggi.clientserver.runner.base.AbstractRunner;

/**
 * An abstract extension of {@link AbstractRunner} representing the runner
 * class for clients
 * @author niconoggi
 *
 */
public abstract class AbstractClientRunner extends AbstractRunner{

	protected AbstractClient client;
	protected String host;
	protected int port;
	
	public AbstractClientRunner() {}
	
	public AbstractClientRunner(final String host) {
		this.host = host;
	}

	public AbstractClientRunner(final int port) {
		this.port = port;
	}
	
	public AbstractClientRunner(final String host, final int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void runReadFirst() {
		try {
			client.connect();
			client.read();
			client.disconnect();
			client.connect();
			client.write();
			client.disconnect();
		} catch (IOException e) {
			handleErrors(e);
		}
	}
	
	@Override
	public void runWriteFirst() {
		try {
			client.connect();
			client.write();
			client.disconnect();
			client.connect();
			client.read();
			client.disconnect();
		} catch (IOException e) {
			handleErrors(e);
		}
	}
	
	/**
	 * disconnects the client. The client instance will not be set null,
	 * however the clients socket will be set null
	 */
	public void stopClient() {
		try {
			client.disconnect();
		} catch (IOException e) {
			handleErrors(e);
		}
	}
	
	public void setHost(final String host) {
		this.host = host;
		client.setHost(host);
	}
	
	public void setPort(final int port) {
		this.port = port;
		client.setPort(port);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!(obj instanceof AbstractClientRunner)) {
			return false;
		}
		
		final AbstractClientRunner other = (AbstractClientRunner) obj;
		
		return client.equals(other.client) && host.equals(other.host) && port == other.port;
	}
	
	@Override
	public int hashCode() {
		return client.hashCode() + host.hashCode() + port;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("client = ").append(client.toString());
		builder.append(", host = ").append(host);
		builder.append(", port = ").append(port);
		return builder.toString();
	}
}
