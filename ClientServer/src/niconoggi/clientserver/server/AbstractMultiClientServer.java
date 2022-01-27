package niconoggi.clientserver.server;

import java.io.IOException;

import niconoggi.clientserver.base.AbstractServer;
import niconoggi.clientserver.base.SerializableSocket;
import niconoggi.clientserver.util.InstanceCopier;

public abstract class AbstractMultiClientServer extends AbstractServer {

	protected SerializableSocket[] clients;
	protected String[] rememberedAddresses;

	public AbstractMultiClientServer() {
	}

	public AbstractMultiClientServer(final int port) {
		super(port);
	}

	public AbstractMultiClientServer(final int port, final int clientAmount) {
		super(port);
		clients = new SerializableSocket[clientAmount];
		rememberedAddresses = new String[clientAmount];
	}

	@Override
	public void connect() throws IOException {
		if (clientsFull()) {
			return;
		}
		if (maxRetries != ServerValues.NO_CONNECTION_RETRY_FLAG && connectionRetries > maxRetries) {
			return;
		}
		server.setSoTimeout(connectTimeout);
		final SerializableSocket requestor = (SerializableSocket) server.accept();

		if (memoryAddressesFull()) {
			if (clientExpected(requestor)) {
				rePutClient(requestor);
			}
		}else {
			if(clientExpected(requestor)) {
				
			}
		}
		connectionRetries++;
		connect();
	}

	private void rePutClient(final SerializableSocket request) {
		for (int inArray = 0; inArray < rememberedAddresses.length; inArray++) {
			if (rememberedAddresses[inArray].equals(request.getInetAddress().toString())) {
				try {
					clients[inArray] = new InstanceCopier<SerializableSocket>().copyInstance(request);
					return;
				} catch (ClassNotFoundException | IOException e) {
					return;
				}
			}
		}
	}

	private boolean clientExpected(final SerializableSocket request) {
		for (int inArray = 0; inArray < rememberedAddresses.length; inArray++) {
			if (rememberedAddresses[inArray].equals(request.getInetAddress().toString())) {
				return true;
			}
		}

		return false;
	}

	private boolean clientsFull() {
		for (final SerializableSocket client : clients) {
			if (client == null || client.isClosed()) {
				return false;
			}
		}

		return true;
	}

	private boolean memoryAddressesFull() {
		for (final String address : rememberedAddresses) {
			if (address == null || address.isBlank()) {
				return false;
			}
		}
		return true;
	}
	
	private void addNextClient() {
		
	}
}
