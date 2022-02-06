package niconoggi.clientserver.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import niconoggi.clientserver.util.DataConverterUtil;
import niconoggi.clientserver.util.InstanceCopier;

/**
 * A very basic instanciateable extension of {@link AbstractSingleClientServer}.
 * It provides basic writing and reading of bytes. This is to create compatibility
 * with {@link InstanceCopier} and {@link DataConverterUtil}
 * @author niconoggi
 *
 */
public class SingleClientServer extends AbstractSingleClientServer{

	protected byte[] dataToWrite;
	protected byte[] readData;
	
	public SingleClientServer() {
		super();
	}
	
	public SingleClientServer(final int port) {
		super(port);
	}
	
	@Override
	public void write() throws IOException {
		final DataOutputStream out = new DataOutputStream(client.getOutputStream());
		out.write(dataToWrite);
		out.close();
		out.flush();
	}

	@Override
	public void read() throws IOException {
		final DataInputStream in = new DataInputStream(client.getInputStream());
		readData = in.readAllBytes();
		in.close();
	}
	
	/**
	 * sets the data that will be written later.
	 * <p>
	 * IMPORTANT: Ensure this is used <b>before</b> the write method is called.
	 * Otherwise potentially unintended or no data will be written
	 * @param dataToWrite
	 */
	public void setDataToWrite(final byte[] dataToWrite) {
		this.dataToWrite = dataToWrite;
	}
	
	/**
	 * IMPORTANT: call this method <b>after</b> using the read method.
	 * Otherwise this method might return null.
	 * @return the read data
	 */
	public byte[] getReadData() {
		return readData; 
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		
		if(!(obj instanceof SingleClientServer)) { 
			return false;
		}
		
		final SingleClientServer other = (SingleClientServer) obj;
		return dataToWrite.equals(other.dataToWrite) && readData.equals(other.readData);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + dataToWrite.hashCode() + readData.hashCode();
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append("data to write = ").append(dataToWrite.toString());
		builder.append(", read data = ").append(readData.toString());
		return builder.toString();
	}
}
