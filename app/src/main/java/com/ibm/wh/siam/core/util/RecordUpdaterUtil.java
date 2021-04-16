package com.ibm.wh.siam.core.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.RecordUpdater;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.exc.NullArgumentException;
import com.ibm.wh.siam.core.exc.UsernameException;

public class RecordUpdaterUtil {

    protected static final String HDR_USER = "X-Application-User";
    protected static final String HDR_APPLICATION = "X-Application-Name";
    private static final String MSG_NO_USERNAME = "No updaterName supplied (null/empty).";
    private static final String MSG_NO_APPLICATION = "No applicationName supplied (null/empty).";

    private static final Logger logger = LoggerFactory.getLogger( RecordUpdaterUtil.class );


    public static void validate(
            final RecordUpdaterIF updater,
            final String methodName )
    throws NullArgumentException
    {
        if( StringUtils.isEmpty(updater.getName() ) ) {
            throw new NullArgumentException( formatMessage(methodName, MSG_NO_USERNAME ));
        }
        if( StringUtils.isEmpty(updater.getApplication() ) ) {
            throw new NullArgumentException( formatMessage(methodName, MSG_NO_APPLICATION ));
        }
    }

    /**
     * Format message
     * @param methodName
     * @param msg
     * @return Formatted message.
     */
    private static String formatMessage(
            final String methodName,
            final String msg )
    {
        StringBuffer sb = new StringBuffer( 50 );
        if( methodName != null )
        {
           if( methodName.length() > 0 )
           {
              sb.append( methodName ).append( "/" );
           }
        }
        sb.append( msg );
        return sb.toString();
    }

    /**
     * Create record updater object from HTTP headers.
     * @param headers   HTTP headers.
     * @return  Record updater.
     */
    public static RecordUpdaterIF createRecordUpdater( final HttpHeaders headers )
    throws UsernameException
    {
        final String methodName = "createRecordUpdater()";
        RecordUpdaterIF updater = null;
        if( logger.isDebugEnabled() ) {
            logger.debug("createRecordUpdater()...");
        }
        try {
            String userName = parseValue( headers, HDR_USER );
            String appName = parseValue( headers, HDR_APPLICATION );

            updater = new RecordUpdater( userName, appName );
            if( logger.isDebugEnabled() ) {
                logger.debug("updater=" + updater );
            }

            RecordUpdaterUtil.validate(updater, methodName );
        }
        catch (NullArgumentException e) {
            logger.error( e.getMessage() );
            throw new UsernameException( e.getMessage(), e );
        }

        return updater;
    }

    /**
     * Parse header value.
     * @param headers   Headers.
     * @param hdrName   Header name.
     * @return Value of header.
     */
    private static String parseValue(
            final HttpHeaders headers,
            final String hdrName )
    {
        String value = null;
        List<String> listValues = headers.get( hdrName );
        if( listValues != null ) {
            value = listValues.get(0);
        }
        return value;
    }

}
