package niconoggi.clientserver.runner.base;

import niconoggi.clientserver.base.CommunicationComponent;

/**
 * A basic definition of what a "Runner" class for
 * server or client looks like. As it is a runner,
 * the only method it provides is {@code run()}
 * @author niconoggi
 *
 */
public interface CommunicationRunner {

	/**
	 * The implementation of this run method should cause the
	 * underlying {@link CommunicationComponent} to first read and
	 * then write
	 */
	void runReadFirst();
	
	/**
	 * The implementation of this run method should cause the
	 * underlying {@link CommunicationComponent} to first write and
	 * then read
	 */
	void runWriteFirst();
	
}
