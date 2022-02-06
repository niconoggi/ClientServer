package niconoggi.clientserver.runner.clientrunner;

import java.io.IOException;
import java.io.Serializable;

import niconoggi.clientserver.client.Client;
import niconoggi.clientserver.util.DataConverterUtil;

/**
 * An instanciatable extension of {@link AbstractClientRunner}
 * that represents a client communicating with different generic data.
 * The data is split in written generic data and read generic data.
 * @author niconoggi
 *
 * @param <W> the object type to write
 * @param <R> the object type that is read
 */
public class MultipleGenericClientRunner<W extends Serializable, R extends Serializable> extends AbstractClientRunner {

	private DataConverterUtil<W> writeConverter;
	private DataConverterUtil<R> readConverter;

	public MultipleGenericClientRunner() {
		client = new Client();
		writeConverter = new DataConverterUtil<W>();
		readConverter = new DataConverterUtil<R>();
	}

	public MultipleGenericClientRunner(final String host) {
		super(host);
		client = new Client(host);
		writeConverter = new DataConverterUtil<W>();
		readConverter = new DataConverterUtil<R>();
	}

	public MultipleGenericClientRunner(final int port) {
		super(port);
		client = new Client(port);
		writeConverter = new DataConverterUtil<W>();
		readConverter = new DataConverterUtil<R>();
	}

	public MultipleGenericClientRunner(final String host, final int port) {
		super(host, port);
		client = new Client(host, port);
		writeConverter = new DataConverterUtil<W>();
		readConverter = new DataConverterUtil<R>();
	}
	
	public void setDataToWrite(final W dataToWrite) {
		try {
			((Client) client).setDataToWrite(writeConverter.convertToBytes(dataToWrite));
		} catch (IOException e) {
			handleErrors(e);
		}
	}
	
	public R getReadData() {
		try {
			return readConverter.convertFromByteArray(((Client) client).getReadData());
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
		
		if(!(obj instanceof MultipleGenericClientRunner)) {
			return false;
		}
		
		final MultipleGenericClientRunner<W,R> other = (MultipleGenericClientRunner<W,R>) obj;
		
		return writeConverter.equals(other.writeConverter) && readConverter.equals(other.readConverter);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + writeConverter.hashCode() + readConverter.hashCode();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append(", write converter = ").append(writeConverter.toString());
		builder.append(", read converter = ").append(readConverter.toString());
		return builder.toString();
	}
}
