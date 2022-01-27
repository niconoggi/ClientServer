package niconoggi.clientserver.base;

import java.io.Serializable;
import java.net.Socket;

import niconoggi.clientserver.util.DataConverterUtil;
import niconoggi.clientserver.util.InstanceCopier;

/**
 * The same as a {@link Socket}, but for the purpose of making
 * it copyable and convertable using {@link InstanceCopier} or
 * {@link DataConverterUtil}, it also implements {@link Serializable}
 * @author niconoggi
 *
 */
public class SerializableSocket extends Socket implements Serializable{

	/**
	 */
	private static final long serialVersionUID = -3603226685604508210L;

}
