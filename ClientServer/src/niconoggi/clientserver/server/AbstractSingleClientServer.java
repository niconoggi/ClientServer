package niconoggi.clientserver.server;

import java.io.IOException;
import java.net.Socket;

import niconoggi.clientserver.base.AbstractServer;
import niconoggi.clientserver.base.SerializableSocket;
import niconoggi.clientserver.util.InstanceCopier;

/**
 * An abstract class representing a Server that accepts only one client. It
 * provides some utility by also remembering the client that connected to it
 * 
 * @author niconoggi
 *
 */
public abstract class AbstractSingleClientServer extends AbstractServer {

	protected SerializableSocket client;

	protected String rememberedClientAddress;

	@Override
	public void connect() throws IOException {
		if (maxRetries != ServerValues.NO_CONNECTION_RETRY_FLAG && connectionRetries > maxRetries) {
			return;
		}
		server.setSoTimeout(connectTimeout);
		final SerializableSocket requestor = (SerializableSocket) server.accept();
		if (isExpectedRequestor(requestor)) {
			try {
				client = new InstanceCopier<SerializableSocket>().copyInstance(requestor);
				return;
			} catch (ClassNotFoundException e) {
				client = null;
				return;
			}
		}

		connectionRetries++;
		connect();
	}

	@Override
	public void disconnect() throws IOException {
		if (client != null) {
			client.close();
		}
		client = null;
	}

	/**
	 * sets the remembered address used as helper when connecting null
	 */
	public void forgetClient() {
		rememberedClientAddress = null;
	}

	public String getRememberedClientAddress() {
		return rememberedClientAddress;
	}

	public void setRememberedClientAddress(final String address) {
		rememberedClientAddress = address;
	}

	/**
	 * checks if the requestor defined in the parameters matches the memorized one
	 * by its {@link Socket#getInetAddress()}
	 * 
	 * @param requestor
	 * @return
	 */
	private boolean isExpectedRequestor(final Socket requestor) {
		if (requestor == null) {
			return false;
		}
		if (rememberedClientAddress == null || rememberedClientAddress.isBlank()) {
			return true;
		}

		return rememberedClientAddress.equals(requestor.getInetAddress().toString());
	}

	@Override
	public boolean equals(final Object obj) {
		if (!super.equals(obj)) {
			return false;
		}

		if (!(obj instanceof AbstractSingleClientServer)) {
			return false;
		}

		final AbstractSingleClientServer other = (AbstractSingleClientServer) obj;
		return client.equals(other.client) && rememberedClientAddress.equals(other.rememberedClientAddress);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + client.hashCode() + rememberedClientAddress.hashCode();
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append("client: ").append(client.toString());
		builder.append(", remembered client address: ").append(rememberedClientAddress);
		return builder.toString();
	}
}
