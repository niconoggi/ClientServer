package niconoggi.clientserver.server;

import java.io.IOException;
import java.net.ServerSocket;

import niconoggi.clientserver.base.AbstractServer;
import niconoggi.clientserver.base.CommunicationComponent;
import niconoggi.clientserver.base.SerializableSocket;
import niconoggi.clientserver.util.InstanceCopier;

/**
 * An abstract extension of {@link AbstractServer} which
 * provides basic implementations of {@link CommunicationComponent#connect()}
 * and {@link CommunicationComponent#disconnect()} for multiple clients at once.
 * <p>
 * Clients in this sense are represented by {@link SerializableSocket}
 * @author niconoggi
 *
 */
public abstract class AbstractMultiClientServer extends AbstractServer {

	protected SerializableSocket[] clients;
	protected String[] rememberedAddresses;
	
	/**
	 * default constructor
	 */
	public AbstractMultiClientServer() {
	}

	/**
	 *  {@inheritDoc}
	 */
	public AbstractMultiClientServer(final int port) {
		super(port);
	}

	/**
	 * onstructor that sets the port for the {@link ServerSocket} and the amound
	 * of clients used when initialized
	 * @param port the port the {@link ServerSocket} will be bound to
	 * @param clientAmount the amount of clients this instance expects to communicate with
	 */
	public AbstractMultiClientServer(final int port, final int clientAmount) {
		super(port);
		setClientAmount(clientAmount);
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

		if(clientExpected(requestor)) {
			rePutClient(requestor);
		}
		if(!memoryAddressesFull()) {
			addNextClient(requestor);
		}
		connectionRetries++;
		connect();
	}
	
	@Override
	public void disconnect() throws IOException{
		for(int client = 0; client < clients.length; client++) {
			if(clients[client] != null && !clients[client].isClosed()) {
				clients[client].close();
			}
			clients[client] = null;
		}
	}
	
	/**
	 * Sets the amount of clients expected.
	 * <p>
	 * <b>WARNING:</b> use with caution.<p>
	 * If this method is called after clients already connected
	 * or the remembered addresses already contain values, the results
	 * may be unintended. Make sure to set the client amount either via constructor
	 * or via this method <b>ONCE</b> if you want to ensure no unintended behaviour
	 * @param amount the client amount
	 */
	public void setClientAmount(final int amount) {
		clients = new SerializableSocket[amount];
		rememberedAddresses = new String[amount];
	}
	
	/**
	 * @return the length of the client array
	 */
	public int getInitialClientAmount() {
		return clients.length;
	}
	
	/**
	 * the actually connected clients. A client counts
	 * as connected if they are not null and not closed
	 * @return
	 */
	public int getActualClientAmount() {
		int amount = 0;
		for(int client = 0; client < clients.length; client++) {
			if(clients[client] != null && !clients[client].isClosed()) {
				amount++;
			}
		}
		return amount;
	}
	
	/**
	 * sets all the client addresses that are in memory to null
	 */
	public void forgetAllAddresses() {
		for(int address = 0; address < rememberedAddresses.length; address++) {
			rememberedAddresses[address] = null;
		}
	}
	
	/**
	 * @return the memorised addresses of the clients
	 */
	public String[] getCurrentAddressMemory() {
		return rememberedAddresses;
	}

	/**
	 * Tries to put the given {@link SerializableSocket} at
	 * the array position its inet address is in the remembered
	 * addresses array
	 * @param request the {@link SerializableSocket} that is tried to add
	 */
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

	/**
	 * checks if the given clients address is already in the memorized address
	 * list, thus was one of the previous connected ones that now is expected
	 * @param request the client to check on existing inet address
	 * @return true, if the client by above definition is expected. false otherwise
	 */
	private boolean clientExpected(final SerializableSocket request) {
		for (int inArray = 0; inArray < rememberedAddresses.length; inArray++) {
			if (rememberedAddresses[inArray].equals(request.getInetAddress().toString())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return true, if no {@link SerializableSocket} in the internal array
	 * is null or closed. false otherwise
	 */
	private boolean clientsFull() {
		for (final SerializableSocket client : clients) {
			if (client == null || client.isClosed()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @return true, if no remembered address is null or blank. false otherwise
	 */
	private boolean memoryAddressesFull() {
		for (final String address : rememberedAddresses) {
			if (address == null || address.isBlank()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Tries to find the next empty spot in the client array, defined
	 * by either a null value or a closed {@link SerializableSocket}.
	 * That value is replaced with the parameter client. 
	 * If any errors occur during copyig the {@link SerializableSocket}
	 * the method just stops, so if you want any handling for that, 
	 * add logic in an extended Subclass for that
	 * @param nextClient the {@link SerializableSocket} that will be added
	 */
	protected void addNextClient(final SerializableSocket nextClient) {
		for(int inArray = 0; inArray < clients.length; inArray++) {
			if(clients[inArray] == null || clients[inArray].isClosed()) {
				try {
					clients[inArray] = new InstanceCopier<SerializableSocket>().copyInstance(nextClient);
					rememberedAddresses[inArray] = clients[inArray].getInetAddress().toString();
					return;
				} catch (ClassNotFoundException | IOException e) {
					return;
				}
			}
		}
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		
		if(!(obj instanceof AbstractMultiClientServer)) {
			return false;
		}
		
		final AbstractMultiClientServer other = (AbstractMultiClientServer) obj;
		return clientsEqual(other.clients) && addressesEqual(other.rememberedAddresses);
	}
	
	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		for(int inArray = 0; inArray < clients.length; inArray++) {
			if(clients[inArray] != null && !clients[inArray].isClosed()) {
				hashCode += clients[inArray].hashCode();
			}
			if(rememberedAddresses[inArray] != null) {
				hashCode += rememberedAddresses[inArray].hashCode();
			}
		}
		return hashCode;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append("Clients: ");
		for(final SerializableSocket client : clients) {
			if(!client.equals(clients[0])) {
				builder.append(", ");
			}
			builder.append(client.toString());
		}
		builder.append("; remembered addresses: ");
		for(final String address : rememberedAddresses) {
			if(!address.equals(rememberedAddresses[0])) {
				builder.append(", ");
			}
			builder.append(address);
		}
		
		return builder.toString();
	}
	
	/**
	 * iterates over the client array to see if the arrays
	 * of this instance and the given instance equal
	 * @param other the other array of {@link SerializableSocket} 
	 * @return true, if both arrays equal. false otherwise
	 */
	private boolean clientsEqual(final SerializableSocket[] other) {
		for(int inArray = 0; inArray < clients.length; inArray++) {
			if(!clients[inArray].equals(other[inArray])) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * iterates over the addresses to see if the arrays
	 * of this instance and the given instance equal
	 * @param other the other array of addresses
	 * @return true, if both arrays equal. false otherwise
	 */
	private boolean addressesEqual(final String[] other) {
		for(int inArray = 0; inArray < rememberedAddresses.length; inArray++) {
			if(!rememberedAddresses[inArray].equals(other[inArray])) {
				return false;
			}
		}
		
		return true;
	}
	
}
