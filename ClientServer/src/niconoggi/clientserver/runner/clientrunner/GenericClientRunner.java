package niconoggi.clientserver.runner.clientrunner;

import java.io.IOException;
import java.io.Serializable;

import niconoggi.clientserver.client.Client;
import niconoggi.clientserver.util.DataConverterUtil;

/**
 * an instanciateable extension of {@link AbstractClientRunner}
 * that represents a client communicating with generic object data.
 * @author niconoggi
 *
 * @param <D> the object type that is written and read
 */
public class GenericClientRunner<D extends Serializable> extends AbstractClientRunner {

	private DataConverterUtil<D> converter;

	public GenericClientRunner() {
		client = new Client();
		converter = new DataConverterUtil<D>();
	}

	public GenericClientRunner(final String host) {
		super(host);
		client = new Client(host);
		converter = new DataConverterUtil<D>();
	}

	public GenericClientRunner(final int port) {
		super(port);
		client = new Client(port);
		converter = new DataConverterUtil<D>();
	}

	public GenericClientRunner(final String host, final int port) {
		super(host, port);
		client = new Client(host, port);
		converter = new DataConverterUtil<D>();
	}
	
	public void setDataToWrite(final D dataToWrite) {
		try {
			((Client) client).setDataToWrite(converter.convertToBytes(dataToWrite));
		} catch (IOException e) {
			handleErrors(e);
		}
	}
	
	public D getReadData() {
		try {
			return converter.convertFromByteArray(((Client) client).getReadData());
		} catch (ClassNotFoundException | IOException e) {
			handleErrors(e);
			return null;
		}
	}

	@Override
	protected void handleErrors() {
		// handling errors more specifically has to be implemented
		// individually. In this basic example, it is only printed out
		// by the system
		System.out.println("an error occured while running the client");
	}

	@Override
	protected void handleErrors(Exception ex) {
		// handling errors more specifically has to be implemented
		// individually. In this basic example, it is only printed out
		// by the system
		System.out.println("an error occured while running the client: " + ex);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		
		if(!(obj instanceof GenericClientRunner)) {
			return false;
		}
		
		final GenericClientRunner<D> other = (GenericClientRunner<D>) obj;
		
		return converter.equals(other.converter);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + converter.hashCode();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append(", converter = ").append(converter.toString());
		return builder.toString();
	}
}
