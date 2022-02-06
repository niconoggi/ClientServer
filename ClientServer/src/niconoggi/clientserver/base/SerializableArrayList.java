package niconoggi.clientserver.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import niconoggi.clientserver.util.DataConverterUtil;
import niconoggi.clientserver.util.InstanceCopier;

/**
 * A serialized version of {@link ArrayList}.
 * Can be used as {@link List} substitute in {@link DataConverterUtil}
 * or {@link InstanceCopier}
 * @author niconoggi
 *
 * @param <D> the {@link List} generic
 */
public class SerializableArrayList<D> extends ArrayList<D> implements Serializable{

	/**
	 */
	private static final long serialVersionUID = 3269847863919046696L;

}
