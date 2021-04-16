/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.exc;

/**
 * @author Match Grun
 *
 */
public class NotFoundException
extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super();
    }

    public NotFoundException( final String message ) {
        super( message );
    }


    public NotFoundException( final Exception cause ) {
        super( cause );
    }


    public NotFoundException(
            final String message,
            final Exception cause )
    {
        super( message, cause );
    }

}
