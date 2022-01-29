package niconoggi.clientserver.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import niconoggi.clientserver.base.AbstractClient;

/**
 * A basic extension of {@link AbstractClient}.
 * It provides basic implementations for connect, disconnect,
 * read and write. written and read data is stored in a byte array
 * @author niconoggi
 *
 */
public class Client extends AbstractClient{

	protected byte[] dataToWrite;
	protected byte[] readData;
	
	@Override
	public void connect() throws IOException {
		socket = new Socket(host, port);
	}

	@Override
	public void write() throws IOException {
		final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		out.write(dataToWrite);
		out.flush();
		out.close();
	}

	@Override
	public void read() throws IOException {
		final DataInputStream in = new DataInputStream(socket.getInputStream());
		readData = in.readAllBytes();
		in.close();
	}

	@Override
	public void disconnect() throws IOException {
		if(socket != null && !socket.isClosed()) {
			socket.close();
		}
		socket = null;
	}
	
	public void setDataToWrite(final byte[] data) {
		dataToWrite = data;
	}
	
	public byte[] getReadData() {
		return readData;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		
		if(!(obj instanceof Client)) {
			return false;
		}
		
		final Client other = (Client) obj;
		
		return dataToWrite.equals(other.dataToWrite) && readData.equals(other.readData);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + dataToWrite.hashCode() + readData.hashCode();
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append("data to write: ").append(dataToWrite);
		builder.append(", read data: ").append(readData);
		return builder.toString();
	}
}
