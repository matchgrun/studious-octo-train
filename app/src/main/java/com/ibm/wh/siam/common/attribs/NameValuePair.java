/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.attribs;

import java.io.Serializable;

/**
 * @author Match Grun
 *
 */
public class NameValuePair
implements Serializable, NameValueIF
{
    private static final long serialVersionUID = -1L;

    private String name;
    private String value;

    public NameValuePair(
            final String name,
            final String value )
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder( 100 );
       String clsName = this.getClass().getName();
       int pos = clsName.lastIndexOf( "." );
       sb.append( clsName.substring( 1 + pos ) ).append( " {" );
       sb.append( "name/value: " );
       sb.append( name ).append( ";" );
       sb.append( value ).append( " }" );
       return sb.toString();

    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName( final String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue( final String value) {
        this.value = value;
    }

}
