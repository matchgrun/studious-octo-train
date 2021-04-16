/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.tree;

/**
 * Defines common behaviour for all exceptions.
 *
 * @author Match Grun
 *
 */
public class TreeException
extends Exception
{
// -----------------------------------------------------------------------------
// Constants.
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
// Member variables.
// -----------------------------------------------------------------------------
   /**
    * Serialized ID.
    */
    private static final long serialVersionUID = 1L;

// -----------------------------------------------------------------------------
// Constructors.
// -----------------------------------------------------------------------------
   /**
    * Default constructor.
    *
    */
    public TreeException() {
        super();
    }

    /**
     * Creates an exception with a message.
     *
     * @param message The exception message.
     */
    public TreeException( final String message ) {
        super( message );
    }

    /**
     * Creates an exception with a nested exception.
     *
     * @param cause The nested exception
     */
    public TreeException( final Exception cause ) {
        super( cause );
    }

    /**
     * Creates an Exception with a message and a nested exception.
     *
     * @param message The exception message.
     * @param cause   The nested exception.
     */
    public TreeException(
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
