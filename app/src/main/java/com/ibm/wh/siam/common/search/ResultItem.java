/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResultItem
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Item found
     */
    private Object item;

    /**
     * Level.
     */
    private int level;

    /**
     * List of child items.
     */
    private List<ResultItem> children;

    public void addChild(
            final int pos,
            final ResultItem item )
    {
        if( item != null ) {
            if( children == null ) {
                children = new ArrayList<ResultItem>();
            }
            children.add( pos, item );
            item.setLevel( 1 + level );
        }
    }

    public void addChild( final ResultItem item )
    {
        if( item != null ) {
            if( children == null ) {
                children = new ArrayList<ResultItem>();
            }
            children.add( item );
            item.setLevel( 1 + level );
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String clsName = this.getClass().getName();
        int pos = clsName.lastIndexOf( "." );
        sb.append( clsName.substring( 1 + pos ) );
        sb.append( ": lvl/obj/" );
        sb.append( level ); sb.append( "/" );
        if( item == null ) {
            sb.append( "-" );
        }
        else {
            sb.append( item.toString() );
        }
        sb.append( "/Children {" );
        if( children != null ) {
            children.forEach( it -> {
                sb.append( "\n" );
                for( int j = 0; j < it.getLevel(); j++ ) {
                    sb.append( "  " );
                }
                sb.append( "- " );
                sb.append( it.toString() );
            });
            sb.append( "\n" );
            for( int j = 0; j < level; j++ ) {
                sb.append( "  " );
            }
        }
        sb.append( "}" );
        return sb.toString();
     }

}
