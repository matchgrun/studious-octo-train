/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.validator;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.annotations.SiamInteger;
import com.ibm.wh.siam.core.annotations.SiamLong;
import com.ibm.wh.siam.core.annotations.SiamString;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dto.BaseSiamStatus;
import com.ibm.wh.siam.core.util.StatusUtil;

/**
 * @author Match Grun
 *
 */
public class FieldValidator
{
    private static final Logger logger = LoggerFactory.getLogger( FieldValidator.class );

    private static final String FLDNAME_STS = "status";
    private static final String FLDNAME_UPDATER = "updaterName";
    private static final String FLDNAME_APPLICATION = "applicationName";

    private static final String FLD_NULL = "IS-NULL";
    public static final String MSG_NULL_OBJECT = "Object not supplied.";

    public static final String MSG_EMPTY = "Field cannot be empty.";
    private static final String MSG_SHORT = "Field too short (minimum %d)";
    private static final String MSG_LONG = "Field too long (maximum %d)";

    private static final String MSG_RANGE = "Value out of range (%d - %d)";

    private static final String FMT_UPDATER = "No %s supplied (null/empty).";
    private static final String MSG_UPDATER = String.format(FMT_UPDATER, FLDNAME_UPDATER);
    private static final String MSG_APPLICATION = String.format(FMT_UPDATER, FLDNAME_APPLICATION);

    private static final int FLDLEN_STS = 1;

    private static final String FMT_STATUS = "Status value invalid; value must be: %s.";


    /**
     * Validate specified request object for insert/update.
     * @param recordUpdater Record updater.
     * @param obj           Object to validate.
     * @return  List of validation errors.
     */
    public List<ValidationError> validate(
            final RecordUpdaterIF recordUpdater,
            final Object obj )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "FieldValidator::validate" );
            logger.debug( "obj IS-A: " + obj.getClass().getName() );
        }

        // Check for null object
        if( obj == null ) {
            List<ValidationError> listErrors = new ArrayList<ValidationError>();
            ValidationError vError = new ValidationError(FLD_NULL,MSG_NULL_OBJECT);
            listErrors.add(vError);
            return listErrors;
        }

        // Check record validator.
        List<ValidationError> listErrors = validate(recordUpdater);

        // Check for Siam Base Object.
        List<Field> listFields = getAllFields(obj);
        for( Iterator<Field> it = listFields.iterator(); it.hasNext(); ) {
            Field fld = it.next();
            Type gt = fld.getGenericType();
            /*
            if( logger.isDebugEnabled() ) {
                logger.debug( "fld.getName()=" + fld.getName() );
                logger.debug( "gt.getTypeName()=" + gt.getTypeName() );
            }
            */
            String typeName = gt.getTypeName();
            if( typeName.equals( String.class.getName() ) ) {
                validateString(obj, fld, listErrors);
            }
            if( typeName.equals( Integer.class.getName() ) ) {
                validateInteger(obj, fld, listErrors);
            }
            if( typeName.equals( Long.class.getName() ) ) {
                validateLong(obj, fld, listErrors);
            }

        }

        if( obj instanceof BaseSiamStatus ) {
            listErrors.addAll( validateFields( ( BaseSiamStatus ) obj ) );
        }

        return listErrors;
    }

    /**
     * Return list of all fields, included inherited.
     * @param obj   Object to examine.
     * @return  List of fields.
     */
    private List<Field> getAllFields( final Object obj ) {
        List<Field> listFields = new ArrayList<Field>();
        for (Class<?> c = obj.getClass(); c != null; c = c.getSuperclass()) {
            listFields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return listFields;
    }

    private ValidationError errorFieldEmpty( final String fieldName ) {
        return new ValidationError( fieldName, MSG_EMPTY);
    }

    /**
     * Validate specified request object for insert/update.
     * @param obj Siam Base Object to validate.
     * @return  List of validation errors.
     */
    public List<ValidationError> validateFields(
            final BaseSiamStatus obj )
    {
        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        if( obj == null ) {
            ValidationError vError = new ValidationError(FLD_NULL,MSG_NULL_OBJECT);
            listErrors.add(vError);
            return listErrors;
        }

        String sts = obj.getStatus();
        if( StringUtils.isEmpty( sts ) ) {
            ValidationError vError = errorFieldEmpty(FLDNAME_STS);
            listErrors.add(vError);
        }
        else {
            if( sts.length() > FLDLEN_STS ) {
                ValidationError vError = new ValidationError(FLDNAME_STS);
                vError.setErrorMessage(String.format(MSG_LONG, FLDLEN_STS));
                listErrors.add(vError);
            }
            if( ! StatusUtil.isValid(sts) ) {
                ValidationError vError = new ValidationError(FLDNAME_STS);
                vError.setErrorMessage(String.format(FMT_STATUS, StatusUtil.valuesAre() ));

                listErrors.add(vError);
            }
            // sts = StatusUtil.defaultValue(sts);
        }

        return listErrors;
    }

    private void validateString(
            final Object obj,
            Field fld,
            List<ValidationError> listErrors )
    {
        SiamString sAnnot = fld.getAnnotation(SiamString.class);
        if( sAnnot == null ) return;

//        if( logger.isDebugEnabled() ) {
//            logger.debug( "------------" );
//            logger.debug( "fld.getName()=" + fld.getName() );
//            logger.debug("sAnnot=" + sAnnot );
//        }

        String fieldName = null;
        if( sAnnot.fieldName().length() < 1 ) {
            fieldName = fld.getName();
        }
        else {
            // Use customized field name
            fieldName = sAnnot.fieldName();
        }

        String sVal = null;
        try {
            fld.setAccessible(true);
            sVal = ( String ) fld.get(obj);
            /*
            if( logger.isDebugEnabled() ) {
                logger.debug("sVal=" + sVal );
            }s
            */

            boolean flagEmpty = false;
            boolean flagLength = false;
            boolean flagShort = false;
            if( sVal == null  ) {
                flagEmpty = true;
            }
            else {
                int ilen = sVal.length();
                if( ilen < 1 ) {
                    flagEmpty = true;
                }
                else {
                    if( ilen > sAnnot.maxLength() ) {
                        flagLength = true;
                    }
                    if( ilen < sAnnot.minLength() ) {
                        flagShort = true;
                    }
                }
            }
            if( flagEmpty ) {
                if( ! sAnnot.empty() ) {
                    ValidationError vError = errorFieldEmpty(fieldName);
                    listErrors.add(vError);
                }
            }
            if( sAnnot.empty() ) {
                if( flagShort ) {
                    if( sAnnot.minLength() > -1 ) {
                        ValidationError vError = new ValidationError(fieldName);
                        vError.setErrorMessage(String.format(MSG_SHORT, sAnnot.minLength()));
                        listErrors.add(vError);
                    }
                }
                if( flagLength ) {
                    if( sAnnot.maxLength() > -1 ) {
                        ValidationError vError = new ValidationError(fieldName);
                        vError.setErrorMessage(String.format(MSG_LONG, sAnnot.maxLength()));
                        listErrors.add(vError);
                    }
                }
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        if( logger.isDebugEnabled() ) {
//            logger.debug( "============================" );
//        }

    }

    private void validateInteger(
            final Object obj,
            Field fld,
            List<ValidationError> listErrors )
    {
        SiamInteger sAnnot = fld.getAnnotation(SiamInteger.class);
        if( sAnnot == null ) return;

        String fieldName = null;
        if( sAnnot.fieldName().length() < 1 ) {
            fieldName = fld.getName();
        }
        else {
            // Use customized field name
            fieldName = sAnnot.fieldName();
        }

        Integer iVal = null;
        try {
            fld.setAccessible(true);
            iVal = ( Integer ) fld.get(obj);
            /*
            if( logger.isDebugEnabled() ) {
                logger.debug("iVal=" + iVal );
            }
            */

            boolean flagEmpty = false;
            boolean flagRange = false;
            if( iVal == null  ) {
                flagEmpty = true;
            }
            else {
                if( sAnnot.range() ) {
                    flagRange = inRange(iVal, sAnnot);
                }
            }
            if( flagEmpty ) {
                if( ! sAnnot.empty() ) {
                    ValidationError vError = errorFieldEmpty(fieldName);
                    listErrors.add(vError);
                }
            }
            else {
                if( ! flagRange ) {
                    ValidationError vError = new ValidationError(fieldName);
                    vError.setErrorMessage(String.format(MSG_RANGE, sAnnot.minValue(), sAnnot.maxValue() ));
                    listErrors.add(vError);
                }
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void validateLong(
            final Object obj,
            Field fld,
            List<ValidationError> listErrors )
    {
        SiamLong sAnnot = fld.getAnnotation(SiamLong.class);
        if( sAnnot == null ) return;

        String fieldName = null;
        if( sAnnot.fieldName().length() < 1 ) {
            fieldName = fld.getName();
        }
        else {
            // Use customized field name
            fieldName = sAnnot.fieldName();
        }

        Long lVal = null;
        try {
            fld.setAccessible(true);
            lVal = ( Long ) fld.get(obj);
            /*
            if( logger.isDebugEnabled() ) {
                logger.debug("lVal=" + lVal );
            }
            */

            boolean flagEmpty = false;
            boolean flagRange = false;
            if( lVal == null  ) {
                flagEmpty = true;
            }
            else {
                if( sAnnot.range() ) {
                    flagRange = inRange(lVal, sAnnot);
                }
            }
            if( flagEmpty ) {
                if( ! sAnnot.empty() ) {
                    ValidationError vError = errorFieldEmpty(fieldName);
                    listErrors.add(vError);
                }
            }
            else {
                if( ! flagRange ) {
                    ValidationError vError = new ValidationError(fieldName);
                    vError.setErrorMessage(String.format(MSG_RANGE, sAnnot.minValue(), sAnnot.maxValue() ));
                    listErrors.add(vError);
                }
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean inRange( final Integer iVal, final SiamInteger sAnnot ) {
        if( iVal >= sAnnot.minValue() ) {
            if( ( iVal <= sAnnot.maxValue() ) ) return true;
        }
        return false;
    }

    private boolean inRange( final Long lVal, final SiamLong sAnnot ) {
        if( lVal >= sAnnot.minValue() ) {
            if( ( lVal <= sAnnot.maxValue() ) ) return true;
        }
        return false;
    }

    public List<ValidationError> validate( final RecordUpdaterIF recordUpdater )
    {
        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        if( StringUtils.isEmpty(recordUpdater.getName() ) ) {
            ValidationError vError = new ValidationError(FLDNAME_UPDATER);
            vError.setErrorMessage(MSG_UPDATER);
            listErrors.add(vError);
        }
        if( StringUtils.isEmpty(recordUpdater.getApplication() ) ) {
            ValidationError vError = new ValidationError(FLDNAME_APPLICATION);
            vError.setErrorMessage(MSG_APPLICATION);
            listErrors.add(vError);
        }
        return listErrors;
    }

    /**
     * Validate specified request object.
     * @param req   Request to validate.
     * @return  List of validation errors.
     */
    public List<ValidationError> validateRequest( final Object req )
    {
        List<ValidationError> listErrors = new ArrayList<ValidationError>();

        // Check for null object
        if( req == null ) {
            ValidationError vError = new ValidationError(FLD_NULL,MSG_NULL_OBJECT);
            listErrors.add(vError);
            return listErrors;
        }

        // Check for Siam Base Object.
        List<Field> listFields = getAllFields(req);
        for( Iterator<Field> it = listFields.iterator(); it.hasNext(); ) {
            Field fld = it.next();
            /*
            Type gt = fld.getGenericType();
            if( logger.isInfoEnabled() ) {
                logger.info( "fld.getName()=" + fld.getName() );
                logger.info( "gt.getTypeName()=" + gt.getTypeName() );
            }
            */

            try {
                fld.setAccessible(true);
                Object obj = fld.get(req);
                if( obj == null ) {
                    ValidationError vError = new ValidationError(FLD_NULL,MSG_NULL_OBJECT);
                    listErrors.add(vError);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listErrors;
    }
}
