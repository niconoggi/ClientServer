package niconoggi.clientserver.runner.serverrunner;

import java.io.IOException;

import niconoggi.clientserver.base.AbstractServer;
import niconoggi.clientserver.runner.base.AbstractRunner;

/**
 * An abstract extension of {@link AbstractRunner}
 * that represents a runner class to run servers
 * @author niconoggi
 *
 */
public abstract class AbstractServerRunner extends AbstractRunner{

	protected AbstractServer server;
	protected int port;
	
	public AbstractServerRunner() {}
	
	public AbstractServerRunner(final int port) {
		setPort(port);
	}
	
	@Override
	public void runWriteFirst() {
		try {
			server.connect();
			server.write();
			//reconnecting is important due to closing
			// the streams resulting in closing the client
			server.disconnect();
			server.connect();
			server.read();
			server.disconnect();
		} catch (IOException e) {
			stopServer();
			handleErrors(e);
		}
	}
	
	@Override
	public void runReadFirst() {
		try {
			server.connect();
			server.read();
			//reconnecting is important due to closing
			// the streams resulting in closing the client
			server.disconnect();
			server.connect();
			server.write();
			server.disconnect();
		} catch (IOException e) {
			stopServer();
			handleErrors(e);
		}
	}
	
	/**
	 * starts the server. If an error occures during that process,
	 * the server may not be started as intended
	 */
	public void startServer() {
		try {
			server.start();
		} catch (IOException e) {
			handleErrors(e);
		}
	}
	
	/**
	 * stops the server. If an error occures during that process,
	 * the server may not be stopped as intended
	 */
	public void stopServer() {
		try {
			server.stop();
		} catch (IOException e) {
			handleErrors(e);
		}
	}
	
	public void setPort(final int port) {
		this.port = port;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof AbstractServerRunner)) {
			return false;
		}
		final AbstractServerRunner other = (AbstractServerRunner) obj;
		return server.equals(other.server) && port == other.port;
	}
	
	@Override
	public int hashCode() {
		return server.hashCode() + port;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("server = ").append(server.toString());
		builder.append(", port =").append(port);
		return builder.toString();
	}
}
