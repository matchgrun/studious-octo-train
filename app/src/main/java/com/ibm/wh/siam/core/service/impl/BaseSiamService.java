/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import com.ibm.wh.siam.core.response.ResponseStatus;

/**
 * @author Match Grun
 *
 */
public class BaseSiamService
{
    protected static final int ACTION_INSERT = 0;
    protected static final int ACTION_UPDATE = 1;
    protected static final int ACTION_DUPLICATE = -1;

    protected static final String STATUS_SUCCESS = "Ok";

    protected ResponseStatus statusSuccess() {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("");
        sts.setStatus(STATUS_SUCCESS);
        sts.setMessage("Success");
        return sts;
    }

    protected ResponseStatus statusNotFound( final String msg ) {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("NF");
        sts.setStatus("Not-Found");
        sts.setMessage(msg);
        return sts;
    }

    protected ResponseStatus statusInsertFailed() {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("IF");
        sts.setStatus("Failure");
        sts.setMessage("Save failed.");
        return sts;
    }

    protected ResponseStatus statusUpdateFailed() {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("UF");
        sts.setStatus("Failure");
        sts.setMessage("Save failed.");
        return sts;
    }

    protected ResponseStatus statusDuplicateKey() {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("UF");
        sts.setStatus("Failure");
        sts.setMessage("Save failed: Duplicate key.");
        return sts;
    }

    protected ResponseStatus statusDeleteFailed() {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("DF");
        sts.setStatus("Failure");
        sts.setMessage("Delete failed.");
        return sts;
    }

    protected ResponseStatus statusInvalid( final String msg ) {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("NV");
        sts.setStatus("Failure");
        sts.setMessage( msg);
        return sts;
    }

    protected ResponseStatus statusInvalid() {
        return statusInvalid( "Invalid." );
    }

    protected ResponseStatus validationErrors() {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("VE");
        sts.setStatus("Failure");
        sts.setMessage("Data contains validation errors.");
        return sts;
    }

    protected ResponseStatus statusSaveFailed() {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("IF");
        sts.setStatus("Failure");
        sts.setMessage("Save failed.");
        return sts;
    }

    protected boolean isSuccessful( final ResponseStatus sts ) {
        boolean retVal = false;
        if( sts.getStatus().equals(STATUS_SUCCESS)) {
            if( sts.getErrorCode().length() < 1 ) {
                retVal = true;
            }
        }
        return retVal;
    }

    protected ResponseStatus searchFailed( final String msg ) {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("SF");
        sts.setStatus("Failure");
        sts.setMessage( msg);
        return sts;
    }

    protected ResponseStatus validationErrorObject( final String msg ) {
        ResponseStatus sts = new ResponseStatus();
        sts.setErrorCode("VE");
        sts.setStatus("Failure-Object");
        sts.setMessage( msg );
        return sts;
    }

}
