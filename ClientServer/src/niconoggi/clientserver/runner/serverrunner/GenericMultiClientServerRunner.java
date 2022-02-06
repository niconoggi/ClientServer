package niconoggi.clientserver.runner.serverrunner;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import niconoggi.clientserver.base.SerializableArrayList;
import niconoggi.clientserver.server.MultiClientServer;
import niconoggi.clientserver.server.SingleClientServer;
import niconoggi.clientserver.util.DataConverterUtil;

/**
 * An instanciatable extension of {@link AbstractServerRunner}
 * that represents a server communicating with different write and read
 * object data. This should be the general consense when it comes to
 * {@link MultiClientServer}s, because these deal with writing single
 * object data while reading a {@link List} of bytes.
 * That is also the reason why one of the generics needs to be a subclass
 * of {@link SerializableArrayList}, which is just an {@link ArrayList} that
 * implements {@link Serializable}. 
 * @author niconoggi
 *
 * @param <W> The generic data that is written
 * @param <R> the generic data that is read
 * 
 */
public class GenericMultiClientServerRunner<W extends Serializable, R extends SerializableArrayList<W>> extends AbstractServerRunner{

	private DataConverterUtil<W> writeConverter;
	private DataConverterUtil<W> readConverter;
	
	public GenericMultiClientServerRunner() {
		super();
		server = new MultiClientServer();
	}
	
	public GenericMultiClientServerRunner(final int port) {
		super(port);
		server = new MultiClientServer(port);
	}
	
	public GenericMultiClientServerRunner(final int port, final int clientAmount) {
		super(port);
		server = new MultiClientServer(port, clientAmount);
	}
	
	/**
	 * sets the data the server will write. It will
	 * be converted using {@link DataConverterUtil}, so in case
	 * an exception occurs, the data might not be set.
	 * @param dataToWrite the data that is written by the server
	 */
	public void setDataToWrite(final W dataToWrite) {
		try {
			((MultiClientServer) server).setDataToWrite(writeConverter.convertToBytes(dataToWrite));
		} catch (IOException e) {
			handleErrors(e);
		}
	}
	
	/**
	 * returns the data read by the server.<p>
	 * If an error occured during converting the data
	 * using {@link DataConverterUtil}, null is returned
	 * @return the read data by the server or null in case of an exception
	 */
	public R getReadData() {
		try {
			final List<byte[]> readByServer = ((MultiClientServer)server).getReadData();
			final SerializableArrayList<byte[]> convertable = (SerializableArrayList<byte[]>) readByServer;
			final SerializableArrayList<W> converted = new SerializableArrayList<W>();
			for(final byte[] toConvert : convertable) {
				converted.add(readConverter.convertFromByteArray(toConvert));
			}
			return (R) converted;
		} catch (ClassNotFoundException | IOException e) {
			handleErrors(e);
			return null;
		}
	}
	
	@Override
	protected void handleErrors() {
		//handling Errors should be an implementation
		//done by yourselfs, so this will only sysout
		//that an error occured
		System.out.println("an error occured while running the server");
	}

	@Override
	protected void handleErrors(Exception ex) {
		//handling Errors should be an implementation
		//done by yourselfs, so this will only sysout
		//that an error occured
		System.out.println("an error occured while running the server " + ex);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		
		final GenericMultiClientServerRunner<W, R> other = (GenericMultiClientServerRunner<W,R>) obj;
		return writeConverter.equals(other.writeConverter) && readConverter.equals(other.readConverter);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + writeConverter.hashCode() + readConverter.hashCode();
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append("write converter = ").append(writeConverter.toString());
		builder.append(", read converter = ").append(readConverter.toString());
		return builder.toString();
	}
}
