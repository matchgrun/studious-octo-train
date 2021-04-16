/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.tree;

/**
 * Defines an exception when a node could not be found.
 *
 * @author Match Grun
 *
 */
public class NodeNotFoundException
extends TreeException
{
// -----------------------------------------------------------------------------
// Constants.
// -----------------------------------------------------------------------------
   /**
    * Serialized ID.
    */
    private static final long serialVersionUID = 1L;

// -----------------------------------------------------------------------------
// Member variables.
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
// Constructors.
// -----------------------------------------------------------------------------
    /**
     * Default constructor.
     *
     */
    public NodeNotFoundException() {
        super();
    }

    /**
     * Creates an exception with a message.
     *
     * @param message The exception message.
     */
    public NodeNotFoundException( final String message ) {
        super( message );
    }

    /**
     * Creates an exception with a nested exception.
     *
     * @param cause The nested exception
     */
    public NodeNotFoundException( final Exception cause ) {
        super( cause );
    }

    /**
     * Creates an Exception with a message and a nested exception.
     *
     * @param message The exception message.
     * @param cause   The nested exception.
     */
    public NodeNotFoundException(
            final String message,
            final Exception cause )
    {
        super( message, cause );
    }

// -----------------------------------------------------------------------------
// Methods.
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
// Properties.
// -----------------------------------------------------------------------------

}

// -----------------------------------------------------------------------------
// End of Source.
// -----------------------------------------------------------------------------
//
