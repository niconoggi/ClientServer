package niconoggi.clientserver.runner.serverrunner;

import java.io.IOException;
import java.io.Serializable;

import niconoggi.clientserver.server.SingleClientServer;
import niconoggi.clientserver.util.DataConverterUtil;

/**
 * An instanciable extension of {@link AbstractServerRunner}
 * that represents a runner for {@link SingleClientServer}s.
 * This assumes that the type of written data and the type of
 * read data are different.
 * @author niconoggi
 *
 * @param <W> the type of written data
 * @param <R> the type of read data
 */
public class MultipleGenericSingleClientServerRunner<W extends Serializable, R extends Serializable> extends AbstractServerRunner{

	private DataConverterUtil<W> writeConverter;
	private DataConverterUtil<R> readConverter;
	
	public MultipleGenericSingleClientServerRunner() {
		super();
		server = new SingleClientServer();
	}
	
	public MultipleGenericSingleClientServerRunner(final int port) {
		super(port);
		server = new SingleClientServer(port);
	}
	
	/**
	 * sets the data the server will write. It will
	 * be converted using {@link DataConverterUtil}, so in case
	 * an exception occurs, the data might not be set.
	 * @param dataToWrite the data that is written by the server
	 */
	public void setDataToWrite(final W dataToWrite) {
		try {
			((SingleClientServer) server).setDataToWrite(writeConverter.convertToBytes(dataToWrite));
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
			return readConverter.convertFromByteArray(((SingleClientServer)server).getReadData());
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
		
		if(!(obj instanceof MultipleGenericSingleClientServerRunner)) {
			return false;
		}
		
		final MultipleGenericSingleClientServerRunner<W,R> other = (MultipleGenericSingleClientServerRunner<W,R>) obj;
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
