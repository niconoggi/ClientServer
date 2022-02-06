package niconoggi.clientserver.runner.base;


/**
 * An abstract class implementing {@link CommunicationRunner}
 * while also providing an abstract method for handling errors.
 * This can also do nothing, it is only provided so it can be used
 * if necessary
 * @author niconoggi
 *
 */
public abstract class AbstractRunner implements CommunicationRunner{

	/**
	 * A method that in server or client runner sense should be called
	 * when an error at any given time during communication occurs.
	 */
	protected abstract void handleErrors();
	
	/**
	 * A method that in server or client runner sense should be called
	 * when an error at any given time during communication occurs.
	 * @param ex represents the exception that occured when this method is used
	 */
	protected abstract void handleErrors(final Exception ex);
}
