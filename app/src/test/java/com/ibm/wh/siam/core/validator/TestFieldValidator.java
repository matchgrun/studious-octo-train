/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.validator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wh.siam.core.common.RecordUpdater;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.Product;
import com.ibm.wh.siam.core.dto.SecurityQuestionAnswer;

/**
 * @author Match Grun
 *
 */
public class TestFieldValidator
{
    private static final Logger logger = LoggerFactory.getLogger( TestFieldValidator.class );

    private String LONG_QUESTION = "Qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq";
    private String LONG_ANSWER = "Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private String LONG_CREDENTIAL_ID = "fb8ac778-483a-4c62-bb9d-a7edf521fe31-ENDS-HERE";

    private String MSG_FAIL_ALL = "Failed all validation tests.";
    private String MSG_FAIL_VALID = "Failed test with valid fields.";
    private String MSG_FAIL_MSG = "Failed to find error message.";
    private String MSG_FAIL_TOO_LONG = "Field too long";


    private RecordUpdaterIF recordUpdater;

    @BeforeEach
    private void setup() {
        recordUpdater = new RecordUpdater();
        recordUpdater.setName("Test");
        recordUpdater.setApplication("Test");
    }

    private void dumpErrorList( List<ValidationError> listErrors ) {
        if( logger.isDebugEnabled() ) {
            if(listErrors.size() > 0 ) {
                logger.debug( "Errors: {");
                listErrors.forEach( ve -> {
                    logger.debug( "  " + ve );
                });
                logger.debug( "}");
            }
            else {
                logger.debug( "Errors: {NONE}" );
            }
        }
    }

    @Test
    public void testValidator01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator01()");
        }
        Product prod = new Product();
        prod.setProductCode("This is a long field.");
        prod.setDescription("A long description");
        prod.setStatus("Too Long");
        prod.setLdapEntryDN("Too Short");

        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, prod);
        dumpErrorList(listErrors);

        int sz = listErrors.size();
        assertTrue( sz > 1, MSG_FAIL_ALL);

        ValidationError vError0 = listErrors.get(0);
        ValidationError vError1 = listErrors.get(1);
        assertTrue( vError0.getFieldName().equalsIgnoreCase( "status" ), "Failed to find 'status' field.");
        assertTrue( vError1.getFieldName().equalsIgnoreCase( "status" ), "Failed to find 'status' field.");

        String msg0 = vError0.getErrorMessage();
        if( msg0.contains( MSG_FAIL_TOO_LONG ) ) {
            return;
        }
        String msg1 = vError1.getErrorMessage();
        if( msg1.contains( MSG_FAIL_TOO_LONG ) ) {
            return;
        }
        fail( MSG_FAIL_MSG );
    }

    @Test
    public void testValidator02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator02()");
        }
        Product prod = new Product();
        prod.setProductCode("This is a long field.");
        prod.setDescription("A long description");
        prod.setStatus( "a" );

        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, prod);
        dumpErrorList(listErrors);
        int sz = listErrors.size();
        assertTrue( sz < 1, "Failed to validate valid 'status' code.");

        prod.setStatus( "Q" );
        listErrors = fv.validate(recordUpdater, prod);
        dumpErrorList(listErrors);
        sz = listErrors.size();
        assertTrue( sz > 0, "Failed to validate invalid 'status' code.");
    }

    @Test
    public void testValidator03() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator03()");
        }
        SecurityQuestionAnswer sqa = new SecurityQuestionAnswer();
        sqa.setCredentialId("I-did-this");
        sqa.setQuestion("This is a long field.");
        sqa.setAnswer("A long description");
        sqa.setStatus("A");
        sqa.setDisplaySequence(0);

        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, sqa);
        dumpErrorList(listErrors);

        int sz = listErrors.size();
        assertTrue( sz > 0, MSG_FAIL_ALL );

        ValidationError vError0 = listErrors.get(0);
        assertTrue( vError0.getFieldName().equalsIgnoreCase( "displaySequence" ), "Failed to find 'displaySequence' field.");

        String msg = vError0.getErrorMessage();
        assertTrue( msg.contains("Value out of range"), MSG_FAIL_MSG);
    }

    @Test
    public void testValidator04() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator04()");
        }
        SecurityQuestionAnswer sqa = new SecurityQuestionAnswer();
        sqa.setCredentialId("I-did-this");
        sqa.setQuestion(LONG_QUESTION);
        sqa.setAnswer(LONG_ANSWER);
        sqa.setStatus("A");
        sqa.setDisplaySequence(2);

        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, sqa);
        dumpErrorList(listErrors);

        int sz = listErrors.size();
        assertTrue( sz > 1, MSG_FAIL_ALL );

        ValidationError vError0 = listErrors.get(0);
        String msg0 = vError0.getErrorMessage();
        if( msg0.contains( MSG_FAIL_TOO_LONG ) ) {
            return;
        }

        ValidationError vError1 = listErrors.get(1);
        String msg1 = vError1.getErrorMessage();
        if( msg1.contains( MSG_FAIL_TOO_LONG ) ) {
            return;
        }
        fail( MSG_FAIL_MSG );
    }

    @Test
    public void testValidator05() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator05()");
        }
        SecurityQuestionAnswer sqa = new SecurityQuestionAnswer();
        sqa.setCredentialId("I-did-this");
        sqa.setQuestion(LONG_QUESTION);
        sqa.setAnswer(LONG_ANSWER);
        sqa.setStatus("A");
        sqa.setDisplaySequence(1);

        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, sqa);
        dumpErrorList(listErrors);

        int sz = listErrors.size();
        assertTrue( sz > 1, MSG_FAIL_ALL);

        ValidationError vError0 = listErrors.get(0);
        String msg0 = vError0.getErrorMessage();
        if( msg0.contains( MSG_FAIL_TOO_LONG ) ) {
            return;
        }

        ValidationError vError1 = listErrors.get(1);
        String msg1 = vError1.getErrorMessage();
        if( msg1.contains( MSG_FAIL_TOO_LONG ) ) {
            return;
        }
    }

    @Test
    public void testValidator06() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator06()");
        }
        SecurityQuestionAnswer sqa = new SecurityQuestionAnswer();
        sqa.setCredentialId("I-did-this");
        sqa.setQuestion(LONG_QUESTION);
        sqa.setAnswer(LONG_ANSWER);
        sqa.setStatus("A");
        sqa.setDisplaySequence(10);

        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, sqa);
        dumpErrorList(listErrors);

        int sz = listErrors.size();
        assertTrue( sz > 1, MSG_FAIL_ALL);

        sqa.setQuestion("Short Question");
        sqa.setAnswer( "Short Answer");
        sqa.setDisplaySequence(11);

        fv = new FieldValidator();
        listErrors = fv.validate(recordUpdater, sqa);
        dumpErrorList(listErrors);

        sz = listErrors.size();
        assertTrue( sz > 0, MSG_FAIL_ALL);

        ValidationError vError0 = listErrors.get(0);
        assertTrue( vError0.getFieldName().equalsIgnoreCase( "displaySequence" ), "Failed to find 'displaySequence' field.");

        String msg = vError0.getErrorMessage();
        assertTrue( msg.contains("Value out of range"), MSG_FAIL_MSG);
    }

    @Test
    public void testValidator07() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator07()");
        }
        SecurityQuestionAnswer sqa = new SecurityQuestionAnswer();
        sqa.setQuestion("Short Question");
        sqa.setAnswer( "Short Answer");
        sqa.setStatus("A");

        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, sqa);
        dumpErrorList(listErrors);

        int sz = listErrors.size();
        assertTrue( sz < 1, MSG_FAIL_VALID );
    }

    @Test
    public void testValidator08() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testValidator08()");
        }
        SecurityQuestionAnswer sqa = new SecurityQuestionAnswer();
        sqa.setCredentialId(LONG_CREDENTIAL_ID);
        sqa.setQuestion("This is a long field.");
        sqa.setAnswer("A long description");
        sqa.setStatus("A");
        sqa.setEncryptionCode("SECRET");

        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, sqa);
        dumpErrorList(listErrors);
        int sz = listErrors.size();
        assertTrue( sz < 1, MSG_FAIL_VALID );
    }

}
