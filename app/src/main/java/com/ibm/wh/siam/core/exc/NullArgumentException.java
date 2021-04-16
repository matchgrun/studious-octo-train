package com.ibm.wh.siam.core.exc;

/**
 * Implements a generic null argument exception.
 *
 * @author: Match Grun
 */
public class NullArgumentException
extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NullArgumentException() {
        super();
    }

    public NullArgumentException( final String message ) {
        super( message );
    }


    public NullArgumentException( final Exception cause ) {
        super( cause );
    }


    public NullArgumentException(
            final String message,
            final Exception cause )
    {
        super( message, cause );
    }

}
