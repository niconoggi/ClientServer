package niconoggi.clientserver.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Util class for converting types defined by the generic
 * to bytes and vice verca. 
 * <p>
 * Could be used to copy values from one instance to another
 * or similarly.
 * @author niconoggi
 *
 * @param <D> the data object
 */
public class DataConverterUtil<D extends Serializable> {

	/**
	 * converts an instance of the given generic type to
	 * a byte array
	 * @param data the instance to get the bytes from
	 * @return the converted bytes
	 * @throws IOException an exception that could be thrown by {@link ObjectOutputStream}
	 */
	public byte[] convertToBytes(final D data) throws IOException {
		final ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		final ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut);
		objectOut.writeObject(data);
		
		return byteArrayOut.toByteArray();
	}
	
	/**
	 * converts an array of bytes to an object of the given type generic
	 * @param bytes the byte array to convert
	 * @return the converted object
	 * @throws IOException an Exception that could be thrown by {@link ObjectInputStream}
	 * @throws ClassNotFoundException an Exception that could be thrown by {@link ObjectInputStream}
	 */
	@SuppressWarnings("unchecked")
	public D convertFromByteArray(final byte[] bytes) throws IOException, ClassNotFoundException{
		final ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(bytes);
		final ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn);
		return (D) objectIn.readObject();
	}
}
