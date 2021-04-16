/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.tree;

/**
 * Defines an exception that is caused when attempting to create duplicate node
 * in a tree hierarchy.
 *
 * @author Match Grun
 *
 */
public class DuplicateNodeException
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
    public DuplicateNodeException() {
        super();
    }

    /**
     * Creates an exception with a message.
     *
     * @param message The exception message.
     */
    public DuplicateNodeException( final String message ) {
        super( message );
    }

    /**
     * Creates an exception with a nested exception.
     *
     * @param cause The nested exception
     */
    public DuplicateNodeException( final Exception cause ) {
        super( cause );
    }

    /**
     * Creates an Exception with a message and a nested exception.
     *
     * @param message The exception message.
     * @param cause   The nested exception.
     */
    public DuplicateNodeException(
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
