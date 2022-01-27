package niconoggi.clientserver.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A very basic instanciateable extension of {@link AbstractSingleClientServer}.
 * It provides basic writing and reading of {@link String}s.
 * <p>
 * NOTE: If primitive types are wanted, either convert them to a String or extend
 * {@link AbstractSingleClientServer} and change the datatype of the data to write and read
 * @author epic-
 *
 */
public class SingleClientServer extends AbstractSingleClientServer{

	protected String dataToWrite;
	protected String readData;
	
	@Override
	public void write() throws IOException {
		final DataOutputStream out = new DataOutputStream(client.getOutputStream());
		out.writeUTF(dataToWrite);
		out.close();
		out.flush();
	}

	@Override
	public void read() throws IOException {
		final DataInputStream in = new DataInputStream(client.getInputStream());
		readData = in.readUTF();
		in.close();
	}
	
	/**
	 * sets the data that will be written later.
	 * <p>
	 * IMPORTANT: Ensure this is used <b>before</b> the write method is called.
	 * Otherwise potentially unintended or no data will be written
	 * @param dataToWrite
	 */
	public void setDataToWrite(final String dataToWrite) {
		this.dataToWrite = dataToWrite;
	}
	
	/**
	 * IMPORTANT: call this method <b>after</b> using the read method.
	 * Otherwise this method might return null.
	 * @return the read data
	 */
	public String getReadData() {
		return readData; 
	}

}
