/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.exc;

/**
 * @author Match Grun
 *
 */
public class UsernameException
extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UsernameException() {
        super();
    }

    public UsernameException( final String message ) {
        super( message );
    }


    public UsernameException( final Exception cause ) {
        super( cause );
    }


    public UsernameException(
            final String message,
            final Exception cause )
    {
        super( message, cause );
    }

}
