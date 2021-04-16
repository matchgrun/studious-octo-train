/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.ibm.wh.siam.core.common.RecordUpdater;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.exc.NullArgumentException;
import com.ibm.wh.siam.core.exc.UsernameException;

/**
 * @author Match Grun
 *
 */
public class TestRecordUpdaterUtil {

    private static final String TEST_USERNAME = "userName";
    private static final String TEST_APPLICATION_NAME = "applicationName";

    private static final String MSG_EMPTY_ARGS = "Failed to validate empty arguments.";
    private static final String MSG_VALID_ARGS = "Failed to validate with valid arguments.";

    private static final Logger logger = LoggerFactory.getLogger( TestRecordUpdaterUtil.class );

    @Test
    public void testValidate01() {
        final String methodName = "aMethod";
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator01()");
        }

        RecordUpdater updater = new RecordUpdater();

        try {
            RecordUpdaterUtil.validate(updater, methodName);
            fail( MSG_EMPTY_ARGS );
        }
        catch (NullArgumentException e) {
            if( logger.isDebugEnabled() ) {
                logger.debug( "e=" + e.getMessage());
            }
        }
    }

    @Test
    public void testValidate02() {
        final String methodName = "aMethod";
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator02()");
        }

        RecordUpdater updater = new RecordUpdater();
        updater.setName( TEST_USERNAME );

        try {
            RecordUpdaterUtil.validate(updater, methodName);
            fail( MSG_EMPTY_ARGS );
        }
        catch (NullArgumentException e) {
            String msg = e.getMessage();
            if( logger.isDebugEnabled() ) {
                logger.debug( "e=" + e.getMessage());
            }
            assertTrue( msg.contains( "No applicationName supplied" ), "Failed to find 'applicationName'" );
        }
    }

    @Test
    public void testValidate03() {
        final String methodName = "aMethod";
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator03()");
        }

        RecordUpdater updater = new RecordUpdater();
        updater.setApplication( TEST_APPLICATION_NAME );

        try {
            RecordUpdaterUtil.validate(updater, methodName);
            fail( MSG_EMPTY_ARGS );
        }
        catch (NullArgumentException e) {
            String msg = e.getMessage();
            if( logger.isDebugEnabled() ) {
                logger.debug( "e=" + e.getMessage());
            }
            assertTrue( msg.contains( "No updaterName supplied" ), "Failed to find 'updaterName'" );
        }
    }

    @Test
    public void testValidate04() {
        final String methodName = "aMethod";
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator04()");
        }

        RecordUpdater updater = new RecordUpdater();
        updater.setName( TEST_USERNAME );
        updater.setApplication( TEST_APPLICATION_NAME );

        try {
            RecordUpdaterUtil.validate(updater, methodName);
        }
        catch (NullArgumentException e) {
            fail( MSG_VALID_ARGS );
        }
    }

    @Test
    public void testValidate05() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator05()");
        }

        HttpHeaders httpHeaders = new HttpHeaders();

        try {
            RecordUpdaterIF updater = RecordUpdaterUtil.createRecordUpdater(httpHeaders);
            if( logger.isDebugEnabled() ) {
                logger.debug( "updater=" + updater );
            }
            fail( MSG_EMPTY_ARGS );
        }
        catch ( UsernameException e ) {
        }
    }

    @Test
    public void testValidate06() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator06()");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add( RecordUpdaterUtil.HDR_USER, TEST_USERNAME );

        try {
            RecordUpdaterIF updater = RecordUpdaterUtil.createRecordUpdater(httpHeaders);
            if( logger.isDebugEnabled() ) {
                logger.debug( "updater=" + updater );
            }
            fail( MSG_EMPTY_ARGS );
        }
        catch ( UsernameException e ) {
            String msg = e.getMessage();
            if( logger.isDebugEnabled() ) {
                logger.debug( "e=" + e.getMessage());
            }
            assertTrue( msg.contains( "No applicationName supplied" ), "Failed to find 'applicationName'" );
        }
    }

    @Test
    public void testValidate07() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator07()");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add( RecordUpdaterUtil.HDR_APPLICATION, TEST_APPLICATION_NAME );

        try {
            RecordUpdaterIF updater = RecordUpdaterUtil.createRecordUpdater(httpHeaders);
            if( logger.isDebugEnabled() ) {
                logger.debug( "updater=" + updater );
            }
            fail( MSG_EMPTY_ARGS );
        }
        catch ( UsernameException e ) {
            String msg = e.getMessage();
            if( logger.isDebugEnabled() ) {
                logger.debug( "e=" + e.getMessage());
            }
            assertTrue( msg.contains( "No updaterName supplied" ), "Failed to find 'updaterName'" );
        }
    }

    @Test
    public void testValidate08() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator08()");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add( RecordUpdaterUtil.HDR_USER, TEST_USERNAME );
        httpHeaders.add( RecordUpdaterUtil.HDR_APPLICATION, TEST_APPLICATION_NAME );

        try {
            RecordUpdaterIF updater = RecordUpdaterUtil.createRecordUpdater(httpHeaders);
            if( logger.isDebugEnabled() ) {
                logger.debug( "updater=" + updater );
            }
            assertTrue( updater.getName().equals( TEST_USERNAME ), "Failed to find 'updaterName'" );
            assertTrue( updater.getApplication().equals( TEST_APPLICATION_NAME ), "Failed to find 'applicationName'" );
        }
        catch ( UsernameException e ) {
            fail( MSG_VALID_ARGS );
        }
    }


}
