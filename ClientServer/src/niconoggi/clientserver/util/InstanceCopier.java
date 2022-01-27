package niconoggi.clientserver.util;

import java.io.IOException;
import java.io.Serializable;

/**
 * Util class for copying values of an instance to another one,
 * providing generic support
 * @author niconoggi
 *
 * @param <T> the type of the cloned instance
 */
public class InstanceCopier<T extends Serializable> {

	/**
	 * copies the target instance using {@link DataConverterUtil}
	 * @param copyFrom the instance that is copied
	 * @return a copy of the given instance
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public T copyInstance(final T copyFrom) throws ClassNotFoundException, IOException {
		final DataConverterUtil<T> converter = new DataConverterUtil<T>();
		return converter.convertFromByteArray(converter.convertToBytes(copyFrom));
	}
}
