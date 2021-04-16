/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.cache;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Defines a cached item.
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CachedItem
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cache key.
     */
    private String cacheKey;

    /**
     * Time stamp created.
     */
    private long timeCreated;

    /**
     * Time stamp time last accessed.
     */
    private long timeAccessed;

    /**
     * Cached item.
     */
    private Object cachedObject;

    /**
     * Create cached item.
     * @param cachedObj
     */
    public CachedItem( final Object cachedObj ) {
        timeCreated = timeAccessed = System.currentTimeMillis();
        cachedObject = cachedObj;
    }

    /**
     * Clear object.
     */
    public void clear() {
        cachedObject = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String clsName = this.getClass().getName();
        int pos = clsName.lastIndexOf( "." );
        sb.append( clsName.substring( 1 + pos ) );
        sb.append( ": key/created/accessed/class: {" );
        sb.append( cacheKey ); sb.append( "/" );
        sb.append( timeCreated ); sb.append( "/" );
        sb.append( timeAccessed ); sb.append( "/" );
        if( cachedObject == null ) {
            sb.append( "-" );
        }
        else {
            sb.append( cachedObject.getClass().getName() );
        }
        sb.append( "}" );
        return sb.toString();
     }

}
