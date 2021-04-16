/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.SecurityQuestionAnswerDAO;
import com.ibm.wh.siam.core.dto.SecurityQuestion;
import com.ibm.wh.siam.core.dto.SecurityQuestionAnswer;
import com.ibm.wh.siam.core.request.security.SecurityAnswersItem;
import com.ibm.wh.siam.core.request.security.SecurityQuestionAnswerSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerListResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionAnswerSaveResponse;
import com.ibm.wh.siam.core.response.security.SecurityQuestionListResponse;
import com.ibm.wh.siam.core.service.SecurityQuestionAnswerService;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 *
 */
@Component
public class SecurityQuestionAnswerServiceImpl
extends BaseSiamService
implements SecurityQuestionAnswerService
{
    private static final String ERRMSG_NOT_FOUND = "Security Question Answer not Found.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Security Question Answer.";
    private static final String ERRMSG_NOT_SPECIFIED = "Security Answers not Specified.";

    @SuppressWarnings("unused")
    private static String defaultEncryptionCode = "LEGACY";

    @Resource
    SecurityQuestionAnswerDAO sqaDao;

    @Autowired
    private Environment env;


    private static final Logger logger = LoggerFactory.getLogger( SecurityQuestionAnswerServiceImpl.class );

    @Override
    public SecurityQuestionAnswerListResponse findByCredential(final String credentialId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByCredential()");
            logger.info( "credentialId=" + credentialId );
        }
        ResponseStatus sts = null;
        SecurityQuestionAnswerListResponse response = new SecurityQuestionAnswerListResponse();
        List<SecurityQuestionAnswer> list = sqaDao.findByCredential(credentialId);
        response.setCredentialId(credentialId);
        response.setListAnswers(list);
        if( list == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public SecurityQuestionAnswerResponse findById(final String questionId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "questionId=" + questionId );
        }
        ResponseStatus sts = null;
        SecurityQuestionAnswerResponse response = new SecurityQuestionAnswerResponse();
        SecurityQuestionAnswer sqa = sqaDao.findById(questionId);
        response.setAnswer(sqa);
        if( sqa == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public SecurityQuestionAnswerResponse save(
            final RecordUpdaterIF recordUpdater,
            final SecurityQuestionAnswer objToSave)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "objToSave" + objToSave );
        }

        SecurityQuestionAnswerResponse response = new SecurityQuestionAnswerResponse();

        SecurityQuestionAnswer objSaved = null;
        String questionId = objToSave.getQuestionId();
        SecurityQuestionAnswer objFound = sqaDao.findById(questionId);

        if( logger.isInfoEnabled() ) {
            logger.info( "objFound=" + objFound );
        }

        // Perform validation
        ResponseStatus sts = null;
        List<ValidationError> listErrors = validateSecurityQuestionAnswer(recordUpdater, objToSave);
        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            response.setAnswer(objToSave);
            return response;
        }

        if( objFound == null ) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Inserting..." );
            }

            try {
                objSaved = sqaDao.insert(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusInsertFailed();
                }
                else {
                    sts = statusSuccess();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                sts = statusInsertFailed();
            }
        }
        else {
            // Update existing record
            if( logger.isInfoEnabled() ) {
                logger.info( "Updating..." );
            }
            try {
                objSaved = sqaDao.updateById(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusUpdateFailed();
                }
                else {
                    sts = statusSuccess();
                }
            }
            catch( Exception e) {
                sts = statusUpdateFailed();
            }
        }

        response.setStatus(sts);
        response.setAnswer(objSaved);

        return response;
    }

    @Override
    public SecurityQuestionAnswerResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String questionId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "questionId=" + questionId );
        }

        SecurityQuestionAnswerResponse response = new SecurityQuestionAnswerResponse();
        if( StringUtils.isEmpty( questionId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No question ID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        SecurityQuestionAnswer objDeleted = null;
        SecurityQuestionAnswer objFound = sqaDao.findById(questionId );
        if( objFound == null ) {
            response.setStatus(statusNotFound( ERRMSG_NOT_FOUND ));
        }
        else {
            objDeleted = sqaDao.deleteById( recordUpdater, objFound );
            if( objDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
            }
        }

        response.setStatus(sts);
        response.setAnswer(objDeleted);
        return response;
    }

    private List<ValidationError> validateSecurityQuestionAnswer(
            final RecordUpdaterIF recordUpdater,
            final SecurityQuestionAnswer sqa )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, sqa);
        return listErrors;
    }

    private List<ValidationError> validateSecurityQuestionAnswers(
            final RecordUpdaterIF recordUpdater,
            final SecurityAnswersItem securityItem )
    {
        FieldValidator fv = new FieldValidator();
        String credentialId = securityItem.getCredentialId();

        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        if( StringUtils.isEmpty(credentialId) ) {
            ValidationError vError = new ValidationError("credentialId",FieldValidator.MSG_EMPTY);
            listErrors.add(vError);
        }

        List<SecurityQuestionAnswer>listAnswers = securityItem.getListAnswers();
        for( SecurityQuestionAnswer sqa : listAnswers ) {
            listErrors.addAll( fv.validate(recordUpdater, sqa) );
        }
        return listErrors;
    }

    /**
     * Validate request object.
     * @param req   Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final SecurityQuestionAnswerSaveRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );
        return listErrors;
    }


    @Override
    public SecurityQuestionAnswerSaveResponse save(
            final RecordUpdaterIF recordUpdater,
            final SecurityQuestionAnswerSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "req" + req );
        }

        SecurityQuestionAnswerSaveResponse response = new SecurityQuestionAnswerSaveResponse();
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            ResponseStatus sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        SecurityAnswersItem securityItem = req.getSecurityItem();
        String credentialId = securityItem.getCredentialId();
        List<SecurityQuestionAnswer> listToSave = securityItem.getListAnswers();

        // Assign credentialId
        listToSave.forEach( ( item ) -> {
            item.setCredentialId(credentialId);
        });

        // Validate answers
        ResponseStatus sts = null;
        listErrors = validateSecurityQuestionAnswers(recordUpdater, securityItem);
        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            response.setCredentialId(credentialId);
            response.setListAnswers(listToSave);
            return response;
        }

        List<SecurityQuestionAnswer> listExisting = sqaDao.findByCredential(credentialId);

        // Resolve changes
        List<SecurityQuestionAnswer> listInsert = new ArrayList<SecurityQuestionAnswer>();
        List<SecurityQuestionAnswer> listModify = new ArrayList<SecurityQuestionAnswer>();
        List<SecurityQuestionAnswer> listDelete = new ArrayList<SecurityQuestionAnswer>();

        // Identify changes
        boolean haveChanges = resolveChanges(
                listToSave,
                listExisting,
                listInsert,
                listModify,
                listDelete);

        if( logger.isDebugEnabled() ) {
            logger.debug( "haveChanges=" + haveChanges );
            logger.debug( "listToSave=" + listToSave );
            logger.debug( "listExists=" + listExisting );
            logger.debug( "listInsert=" + listInsert );
            logger.debug( "listModify=" + listModify );
            logger.debug( "listDelete=" + listDelete );
        }

        // Assign default values
        assignDefaults(listInsert);

        // Save changes
        boolean haveErrors = processChanges(
                recordUpdater,
                listInsert,
                listModify,
                listDelete);
        if( haveErrors ) {
            response.setListAnswers(listToSave);
            sts = statusUpdateFailed();
        }
        else {
            List<SecurityQuestionAnswer> listSaved = sqaDao.findByCredential(credentialId);
            response.setListAnswers(listSaved);
            sts = statusSuccess();
        }

        response.setCredentialId(credentialId);
        response.setStatus(sts);

        return response;
    }

    /**
     * Resolve list of changes for credential ID.
     * @param listToSave    List of answers to save.
     * @param listExisting  List of existing answers.
     * @param listInsert    List of (new) answers to insert.
     * @param listModify    List of (existing) answers to modify.
     * @param listDelete    List of (existing) answers to delete.
     * @return  <i>true</i> if changes identified.
     */
    private boolean resolveChanges(
            final List<SecurityQuestionAnswer> listToSave,
            final List<SecurityQuestionAnswer> listExisting,
            final List<SecurityQuestionAnswer> listInsert,
            final List<SecurityQuestionAnswer> listModify,
            final List<SecurityQuestionAnswer> listDelete )
    {
        Set<String> setExisting = new HashSet<String>();
        Set<String> setToSave = new HashSet<String>();
        listExisting.forEach( ( item ) -> {
            setExisting.add( item.getQuestionId() );
        });
        listToSave.forEach( ( item ) -> {
            setToSave.add( item.getQuestionId() );
        });

        // Identify deletions
        listExisting.forEach( ( item ) -> {
            String questionId = item.getQuestionId();
            if( ! setToSave.contains(questionId) ) {
                listDelete.add(item);
            }
        });

        // Identify inserts/modify
        listToSave.forEach( ( item ) -> {
            String questionId = item.getQuestionId();
            if( StringUtils.isEmpty( questionId ) ) {
                listInsert.add(item);
            }
            else {
                if( setExisting.contains(questionId) ) {
                    listModify.add(item);
                }
            }
        });
        int sz = listInsert.size() + listModify.size() + listDelete.size();
        if( sz > 0 ) return true;
        return false;
    }

    /**
     * Assign default values for items to be inserted. In this case, we need to know
     * which encryption key is required for the new records.
     * @param listInsert List of inserts.
     */
    private void assignDefaults( final List<SecurityQuestionAnswer> listInsert ) {
        listInsert.forEach( ( item ) -> {
            item.setEncryptionCode(env.getProperty("siam.question.encryption.default"));
        });
    }

    /**
     * Process all database changes for this request.
     * @param recordUpdater Record updater.
     * @param listInsert    List of items to insert.
     * @param listModify    List of items to modify.
     * @param listDelete    List of items to delete.
     * @return
     */
    private boolean processChanges(
            final RecordUpdaterIF recordUpdater,
            final List<SecurityQuestionAnswer> listInsert,
            final List<SecurityQuestionAnswer> listModify,
            final List<SecurityQuestionAnswer> listDelete )
    {
        boolean haveErrors = false;

        // Process deletions
        for( SecurityQuestionAnswer item : listDelete ) {
            try {
                sqaDao.deleteById(recordUpdater, item);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                haveErrors = true;
            }
        }

        // Then inserts
        for( SecurityQuestionAnswer item : listInsert ) {
            try {
                sqaDao.insert(recordUpdater, item);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                haveErrors = true;
            }
        }

        // Finally modifies.
        for( SecurityQuestionAnswer item : listModify ) {
            try {
                sqaDao.updateById(recordUpdater, item);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                haveErrors = true;
            }
        }
        return haveErrors;
    }

    @Override
    public SecurityQuestionListResponse fetchQuestionsByCredential(String credentialId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByCredential()");
            logger.info( "credentialId=" + credentialId );
        }
        ResponseStatus sts = null;
        SecurityQuestionListResponse response = new SecurityQuestionListResponse();
        List<SecurityQuestion> list = sqaDao.findQuestionsByCredential(credentialId);
        response.setCredentialId(credentialId);
        response.setListSecurityQuestions(list);
        if( list == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

}
