package niconoggi.clientserver.runner.serverrunner;

import java.io.IOException;
import java.io.Serializable;

import niconoggi.clientserver.server.SingleClientServer;
import niconoggi.clientserver.util.DataConverterUtil;

/**
 * An instanciatable extension of {@link AbstractServerRunner}
 * that represents a runner for {@link SingleClientServer}.
 * This assumes that the written generic data equals the read
 * generic data in type.
 * 
 * @author niconoggi
 *
 * @param <D> the type of data that is written and read
 */
public class GenericSingleClientServerRunner<D extends Serializable> extends AbstractServerRunner{

	private DataConverterUtil<D> converter;
	
	public GenericSingleClientServerRunner() {
		super();
		server = new SingleClientServer();
	}
	
	public GenericSingleClientServerRunner(final int port) {
		super(port);
		server = new SingleClientServer(port);
	}
	
	/**
	 * sets the data the server will write. It will
	 * be converted using {@link DataConverterUtil}, so in case
	 * an exception occurs, the data might not be set.
	 * @param dataToWrite the data that is written by the server
	 */
	public void setDataToWrite(final D dataToWrite) {
		try {
			((SingleClientServer) server).setDataToWrite(converter.convertToBytes(dataToWrite));
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
	public D getReadData() {
		try {
			return converter.convertFromByteArray(((SingleClientServer)server).getReadData());
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
		final GenericSingleClientServerRunner<D> other = (GenericSingleClientServerRunner<D>) obj;
		return converter.equals(other.converter);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + converter.hashCode();
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append("converter = ").append(converter.toString());
		return builder.toString();
	}
}
