/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.exc;

/**
 * @author Match Grun
 *
 */
public class ValidationException
extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ValidationException() {
        super();
    }

    public ValidationException( final String message ) {
        super( message );
    }


    public ValidationException( final Exception cause ) {
        super( cause );
    }


    public ValidationException(
            final String message,
            final Exception cause )
    {
        super( message, cause );
    }

}
