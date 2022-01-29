package niconoggi.clientserver.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import niconoggi.clientserver.base.SerializableSocket;

/**
 * A basic extension of {@link AbstractMultiClientServer}
 * providing write and read functionality for multiple connected
 * clients. 
 * <p>
 * <b>IMPORTANT FOR USE:</b> The data which is written is represented as
 * a byte array meaning that all clients will get the same data (if each
 * client should get different data, please extend this or the super class 
 * yourself).<p>
 * The read data on the other hand is stored in a {@link List} of byte arrays. 
 * @author niconoggi
 *
 */
public class MultiClientServer extends AbstractMultiClientServer{

	private byte[] dataToWrite;
	private List<byte[]> readData;
	
	@Override
	public void write() throws IOException {
		for(final SerializableSocket client : clients) {
			if(client != null && !client.isClosed()) {
				final DataOutputStream out = new DataOutputStream(client.getOutputStream());
				out.write(dataToWrite);
				out.flush();
				out.close();
			}
		}
	}

	@Override
	public void read() throws IOException {
		readData.clear();
		for(final SerializableSocket client : clients) {
			if(client != null && !client.isClosed()) {
				final DataInputStream in = new DataInputStream(client.getInputStream());
				readData.add(in.readAllBytes());
				in.close();
			}
		}
	}
	
	public void setDataToWrite(final byte[] dataToWrite) {
		this.dataToWrite = dataToWrite;
	}
	
	public List<byte[]> getReadData(){
		return readData;
	}

	public byte[] getSpecificReadData(final int clientNumber) {
		return readData.get(clientNumber);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		
		if(!(obj instanceof MultiClientServer)) {
			return false;
		}
		
		final MultiClientServer other = (MultiClientServer) obj;
		return dataToWrite.equals(other.dataToWrite) && readData.equals(other.readData);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + dataToWrite.hashCode() + readData.hashCode();
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append("dataToWrite: ").append(dataToWrite);
		builder.append(", readData: ").append(readData.toString());
		return builder.toString();
	}
}
