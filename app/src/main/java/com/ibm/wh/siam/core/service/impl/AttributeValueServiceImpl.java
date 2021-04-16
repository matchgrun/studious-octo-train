/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.CommonConstants;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.common.SiamOwnerTypes;
import com.ibm.wh.siam.core.dao.AttributeDescriptorDAO;
import com.ibm.wh.siam.core.dao.AttributeValueDAO;
import com.ibm.wh.siam.core.dao.CoreCommonDAO;
import com.ibm.wh.siam.core.dao.dto.AttributeNameValueLW;
import com.ibm.wh.siam.core.dao.dto.OwnerDescriptorItem;
import com.ibm.wh.siam.core.dto.AttributeNameValue;
import com.ibm.wh.siam.core.dto.AttributeNameValueList;
import com.ibm.wh.siam.core.request.attribute.AttributeValueItem;
import com.ibm.wh.siam.core.request.attribute.AttributeValueSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.attribute.AttributeValueSaveResponse;
import com.ibm.wh.siam.core.response.attribute.OwnerAttributeValueListResponse;
import com.ibm.wh.siam.core.response.attribute.OwnerAttributeValueResponse;
import com.ibm.wh.siam.core.response.attribute.OwnersWithAttributeNameResponse;
import com.ibm.wh.siam.core.service.AttributeValueService;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 * @param <E>
 *
 */
@Component
public class AttributeValueServiceImpl
extends BaseSiamService
implements AttributeValueService
{
    @Resource
    AttributeDescriptorDAO attribDescriptorDao;

    @Resource
    AttributeValueDAO attributeValueDao;

    @Resource
    CoreCommonDAO coreCommonDao;

    private static final String ERRMSG_NOT_FOUND = "Attribute Value not Found.";
    @SuppressWarnings("unused")
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Attribute Value.";

    private static final String ERRMSG_ATTRIB_NAMES_NOT_RECOGNIZED = "Attribute Name(s) not recognized:";

    private static final String ERRMSG_INVALID_OWNER_TYPE = "Invalid Owner Type Specified.";
    private static final String ERRMSG_INVALID_OWNER_ID = "Invalid Owner ID Specified.";

    private static final String FLD_OWNER_TYPE = "ownerType";
    private static final String FLD_OWNER_ID = "ownerId";
    private static final String FLD_ATTRIBUTE_NAME= "attributeName";

    private static final String CHAR_DELIM = ":";

    private static final Logger logger = LoggerFactory.getLogger( AttributeValueServiceImpl.class );

    @Override
    public OwnerAttributeValueResponse findForOwnerId( final String ownerId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOwnerID()");
            logger.info( "ownerId=" + ownerId );
        }

        ResponseStatus sts = null;
        OwnerAttributeValueResponse response = new OwnerAttributeValueResponse();
        response.setOwnerId(ownerId);
        response.setSelector(SELECTOR_ALL);
        response.setSelectorValue("");
        List<AttributeNameValue> list = attributeValueDao.findByOwner(ownerId);
        response.setListAttributeNameValues(list);
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
    public OwnerAttributeValueResponse findForOwnerByDescriptorName(
            final String ownerId,
            final String descriptorName )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findForOwnerByDescriptorName()");
            logger.info( "ownerId=" + ownerId );
            logger.info( "descriptorName=" + descriptorName );
        }

        ResponseStatus sts = null;
        OwnerAttributeValueResponse response = new OwnerAttributeValueResponse();
        response.setOwnerId(ownerId);
        response.setSelector(SELECTOR_DESCRIPTOR_NAME);
        response.setSelectorValue(descriptorName);
        List<AttributeNameValue> list = attributeValueDao.findForOwnerByDescriptorName(ownerId, descriptorName);
        response.setListAttributeNameValues(list);
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
    public OwnerAttributeValueResponse findForOwnerByDescriptorId(
            final String ownerId,
            final String descriptorId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findForOwnerByDescriptorId()");
            logger.info( "ownerId=" + ownerId );
            logger.info( "descriptorId=" + descriptorId );
        }

        ResponseStatus sts = null;
        OwnerAttributeValueResponse response = new OwnerAttributeValueResponse();
        response.setOwnerId(ownerId);
        response.setSelector( SELECTOR_DESCRIPTOR_ID );
        response.setSelectorValue(descriptorId);
        List<AttributeNameValue> list = attributeValueDao.findForOwnerByDescriptorId(ownerId, descriptorId);
        response.setListAttributeNameValues(list);
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
    public OwnerAttributeValueListResponse findAttributeListForOwner( final String ownerId ) {
        if( logger.isInfoEnabled() ) {
            logger.info( "findAttributeListByOwner()");
            logger.info( "ownerId=" + ownerId );
        }

        ResponseStatus sts = null;
        OwnerAttributeValueListResponse response = new OwnerAttributeValueListResponse();
        response.setOwnerId(ownerId);
        response.setSelector(SELECTOR_ALL);
        response.setSelectorValue("");

        List<AttributeNameValueLW> listLW = attributeValueDao.findALWForOwner(ownerId);
        if( listLW == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            List<AttributeNameValueList> listAttribs = buildListAttributeValues(listLW);
            response.setListAttributes(listAttribs);
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }


    private List<AttributeNameValueList> buildListAttributeValues( final List<AttributeNameValueLW> listLW )
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "buildListAttributeValues()");
        }

        List<AttributeNameValueList> listAttributes = new ArrayList<AttributeNameValueList>();

        Map<String, AttributeNameValueList> map = new HashMap<String, AttributeNameValueList>();
        listLW.forEach( item -> {
            String name = item.getAttributeName();
            AttributeNameValueList valueList = map.get(name);
            if( valueList == null ) {
                valueList = new AttributeNameValueList();
                valueList.setAttributeDescriptorId(item.getAttributeDescriptorId());
                valueList.setAttributeName(item.getAttributeName());
                map.put(name, valueList);

                listAttributes.add(valueList);
            }
            List<String> listValues = valueList.getListValues();
            if( listValues == null ) {
                listValues = new ArrayList<String>();
                valueList.setListValues(listValues);
            }
            listValues.add(item.getAttributeValue());
        });
        return listAttributes;
    }

    @Override
    public OwnerAttributeValueListResponse findAttributeListForOwnerByName(
            final String ownerId,
            final String descriptorName )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findAttributeListForOwnerByName()");
            logger.info("ownerId=" + ownerId );
            logger.info("descriptorName=" + descriptorName );
        }

        ResponseStatus sts = null;
        OwnerAttributeValueListResponse response = new OwnerAttributeValueListResponse();
        response.setOwnerId(ownerId);
        response.setSelector(SELECTOR_DESCRIPTOR_NAME);
        response.setSelectorValue(descriptorName);

        List<AttributeNameValueLW> listLW =
                attributeValueDao.findALWForOwnerByDescriptorName(ownerId, descriptorName);
        if( listLW == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            List<AttributeNameValueList> listAttribs = buildListAttributeValues(listLW);
            response.setListAttributes(listAttribs);
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }
    @Override
    public OwnerAttributeValueListResponse findAttributeListForOwnerById(
            final String ownerId,
            final String descriptorId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findAttributeListForOwnerById()");
            logger.info("ownerId=" + ownerId );
            logger.info("descriptorId=" + descriptorId );
        }

        ResponseStatus sts = null;
        OwnerAttributeValueListResponse response = new OwnerAttributeValueListResponse();
        response.setOwnerId(ownerId);
        response.setSelector( SELECTOR_DESCRIPTOR_ID );
        response.setSelectorValue(descriptorId);

        List<AttributeNameValueLW> listLW = attributeValueDao.findALWForOwnerByDescriptorId(ownerId, descriptorId);
        if( listLW == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            List<AttributeNameValueList> listAttribs = buildListAttributeValues(listLW);
            response.setListAttributes(listAttribs);
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }

    @Override
    public AttributeValueSaveResponse save(
            final RecordUpdaterIF recordUpdater,
            final AttributeValueSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "save()" );
        }

        ResponseStatus sts = null;
        AttributeValueSaveResponse response = new AttributeValueSaveResponse();
        List<ValidationError> listErrors = validateRequest(
                recordUpdater,
                req);
        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        AttributeValueItem avItem = req.getAttributeValue();
        String ownerId = avItem .getOwnerId();
        String ownerType = avItem.getOwnerType();
        if( logger.isInfoEnabled() ) {
            logger.info( "ownerId=" + ownerId );
            logger.info( "ownerType=" + ownerType );
        }
        if( ownerType != null ) ownerType = ownerType.toUpperCase();

        List<AttributeNameValueList> listToSave = avItem.getListAttributes();
        if( logger.isInfoEnabled() ) {
            dumpList( "listToSave", listToSave );
        }

        List<String> listAttribNames = buildListUniqueAttributeNames( avItem );
        Map<String, String> mapDescriptors = attribDescriptorDao.findMapAttributeId(listAttribNames);
        if( logger.isDebugEnabled() ) {
            mapDescriptors.forEach( (n,v) -> {
                logger.debug( "[" + n + "]==>[" + v + "]");
            });
        }

        listErrors = validateAttributes(
                recordUpdater,
                req,
                listAttribNames,
                mapDescriptors );

        // Validate owner ID, only if no other errors found. Performance boost.
        if( listErrors.size() < 1 ) {
            if( ! coreCommonDao.validateOwner(ownerType, ownerId) ) {
                ValidationError vError = new ValidationError( FLD_OWNER_ID, ERRMSG_INVALID_OWNER_ID );
                listErrors.add(vError);
            }
        }

        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            response.setOwnerId(ownerId);
            response.setOwnerType(ownerType);
            response.setListAttributes(listToSave);
            return response;
        }

        // Remove duplicate values
        dedupeValues(listToSave);
        if( logger.isInfoEnabled() ) {
            dumpList( "listToSave", listToSave );
        }

        List<AttributeNameValue> listExisting = attributeValueDao.findByOwner(ownerId);
        if( logger.isInfoEnabled() ) {
            dumpListANV( "listExisting", listExisting );
        }

        List<AttributeNameValue> listInsert = new ArrayList<AttributeNameValue>();
        List<AttributeNameValue> listDelete = new ArrayList<AttributeNameValue>();

        // Identify changes
        resolveChanges(
                listToSave,
                listExisting,
                listInsert,
                listDelete );

        if( logger.isInfoEnabled() ) {
            dumpListANV( "listInsert", listInsert );
            dumpListANV( "listDelete", listDelete );
            logger.info( "names=" + listAttribNames );
        }

        if( listInsert.size() > 0 ) {
            if( StringUtils.isEmpty(ownerType)) {
                // Attempt to locate an existing owner type
                ownerType = identifyOwnerType(ownerId, listExisting);
                if( logger.isInfoEnabled() ) {
                    logger.info( "ownerType=" + ownerType );
                }
            }

            // Owner type should already be valid
            /*
            if( ! AttributeUtil.isValid(ownerType) ) {
            }
            */

            assignValues(listInsert, ownerType, ownerId, mapDescriptors );
        }

        if( logger.isInfoEnabled() ) {
            dumpListANV( "listInsert", listInsert );
        }

        boolean haveErrors = processChanges(
                recordUpdater,
                listInsert,
                listDelete );

        if( haveErrors ) {
            response.setListAttributes(listToSave);
            sts = statusUpdateFailed();
        }
        else {

            List<AttributeNameValueLW> listLW = attributeValueDao.findALWForOwner(ownerId);
            if( listLW == null ) {
                sts = statusNotFound( ERRMSG_NOT_FOUND );
            }
            else {
                List<AttributeNameValueList> listAttribs = buildListAttributeValues(listLW);
                response.setListAttributes(listAttribs);
                sts = statusSuccess();
            }
            response.setStatus(sts);

            sts = statusSuccess();
        }

        response.setOwnerId(ownerId);
        response.setOwnerType(ownerType);
        response.setStatus(sts);

        return response;
    }

    /**
     * Remove duplicate values from specified list.
     * @param list  List to process.
     */

    private void dedupeValues( final List<AttributeNameValueList> list ) {
        Set<String> set = new HashSet<String>();
        list.forEach( item -> {
            List<String> listValues = item.getListValues();
            List<String> listClean = new ArrayList<String>();
            set.clear();
            listValues.forEach( value -> {
                if( ! set.contains( value ) ) {
                    listClean.add(value);
                }
                set.add(value);
            });
            item.setListValues(listClean);
        });
    }

    private List<String> buildListUniqueAttributeNames( final AttributeValueItem avItem ) {
        Set<String> setNames = new HashSet<String>();
        List<String> listNames = new ArrayList<String>();
        List<AttributeNameValueList> listAttribs = avItem.getListAttributes();
        listAttribs.forEach(item -> {
            String name = item.getAttributeName();
            if( ! setNames.contains(name)) {
                listNames.add(name);
            }
            setNames.add(name);
        });
        return listNames;
    }

    private void dumpList(
            final String msg,
            final List<AttributeNameValueList> list )
    {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + " {");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    private void dumpListANV(
            final String msg,
            final List<AttributeNameValue> list )
    {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + " {");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    private String buildKey(
            final StringBuilder sb,
            final String name,
            final String value )
    {
        sb.setLength(0);
        sb.append( name ).append( CHAR_DELIM ).append( value );
        return sb.toString();
    }

    private void resolveChanges(
            final List<AttributeNameValueList> listToSave,
            final List<AttributeNameValue> listExisting,
            final List<AttributeNameValue> listInsert,
            final List<AttributeNameValue> listDelete )
    {
        Map<String,String> mapExisting = new HashMap<String,String>();
        Map<String,String> mapSave = new HashMap<String,String>();
        Map<String,String> mapDescriptors = new HashMap<String,String>();

        StringBuilder sb = new StringBuilder( 100 );
        listExisting.forEach(item -> {
            String key = buildKey( sb, item.getAttributeName(), item.getAttributeValue() );
            mapExisting.put(key, item.getAttributeId() );
            mapDescriptors.put( item.getAttributeName(), item.getAttributeDescriptorId() );
        });

        listToSave.forEach( item -> {
            String name = item.getAttributeName();
            List<String> listValues = item.getListValues();
            listValues.forEach( value -> {
                String key = buildKey( sb, name, value );
                String placeHolder = mapSave.get( key );
                if( placeHolder == null ) {
                    mapSave.put( key, "*" );
                }
            });
        });

        // Identify items to insert
        listToSave.forEach( item -> {
            String name = item.getAttributeName();
            List<String> listValues = item.getListValues();
            listValues.forEach( value -> {
                String key = buildKey( sb, name, value );
                String attributeId = mapExisting.get( key );
                if( attributeId == null ) {
                    AttributeNameValue attrib = new AttributeNameValue();
                    attrib.setAttributeName(name);
                    attrib.setAttributeValue(value);
                    String descriptorId = mapDescriptors.get(name);
                    if( descriptorId != null ) {
                        attrib.setAttributeDescriptorId(descriptorId);
                    }
                    listInsert.add(attrib);
                }
            });
        });

        // Identify items to delete
        listExisting.forEach( item -> {
            String key = buildKey( sb, item.getAttributeName(), item.getAttributeValue() );
            String placeHolder = mapSave.get( key );
            if( placeHolder == null ) {
                listDelete.add( item );
            }
        });
    }

    /**
     * Identify owner type by examining existing attribute name-value pairs.
     * If no existing attribute values specified, check owner Id with database.
     * @param ownerId       Owner Id.
     * @param listExisting  List of attribute name-value pairs.
     * @return Owner Id.
     */
    private String identifyOwnerType(
            final String ownerId,
            final List<AttributeNameValue> listExisting )
    {
        for( AttributeNameValue item : listExisting ) {
            return item.getOwnerType();
        }
        return coreCommonDao.identifyOwnerType(ownerId);
    }

    /**
     * Assign default values to each attribute being inserted.
     *
     * @param listInsert    List of attributes to be inserted.
     * @param ownerType     Owner type.
     * @param ownerId       Owner Id.
     * @param mapValidDescriptors   Map of attribute descriptors.
     */
    private void assignValues(
            final List<AttributeNameValue> listInsert,
            final String ownerType,
            final String ownerId,
            final Map<String, String> mapValidDescriptors )
    {
        listInsert.forEach( item -> {
            String name = item.getAttributeName().trim();
            String descriptorId = mapValidDescriptors.get(name);
            item.setOwnerId(ownerId);
            item.setOwnerType(ownerType);
            item.setAttributeDescriptorId(descriptorId);
            item.setStatus(CommonConstants.STATUS_ACTIVE);
        });
    }

    /**
     * Validate specified request.
     * @param recordUpdater     Record updater.
     * @param req               Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final RecordUpdaterIF recordUpdater,
            final AttributeValueSaveRequest  req )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            AttributeValueItem avItem = req.getAttributeValue();
            listErrors.addAll( fv.validate(recordUpdater, avItem ) );
        }
        return listErrors;
    }

    /**
     * Validate request.
     * @param recordUpdater     Record updater.
     * @param req               Request.
     * @param listAttribNames   List of attribute names to be checked.
     * @param mapValidDescriptors   Map of attribute descriptors.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateAttributes(
            final RecordUpdaterIF recordUpdater,
            final AttributeValueSaveRequest req,
            final List<String> listAttribNames,
            final Map<String, String> mapValidDescriptors )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, req );

        StringBuilder sb = new StringBuilder( 100 );
        boolean flagError = false;
        for( String name : listAttribNames ) {
            name = name.trim();
            if( ! mapValidDescriptors.containsKey( name ) ) {
                if( flagError ) {
                    sb.append( "; " );
                }
                else {
                    sb.append( ERRMSG_ATTRIB_NAMES_NOT_RECOGNIZED ).append( " { ");
                }
                sb.append( name );
                flagError = true;
            }
        }

        if( flagError ) {
            sb.append( " }" );
            ValidationError vError = new ValidationError( FLD_ATTRIBUTE_NAME, sb.toString() );
            listErrors.add(vError);
        }

        return listErrors;
    }

    private boolean processChanges(
            final RecordUpdaterIF recordUpdater,
            final List<AttributeNameValue> listInsert,
            final List<AttributeNameValue> listDelete )
    {
        boolean haveErrors = false;

        // Process deletions
        for( AttributeNameValue item : listDelete ) {
            try {
                attributeValueDao.deleteById(recordUpdater, item);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                haveErrors = true;
            }
        }
        for( AttributeNameValue item : listInsert ) {
            try {
                attributeValueDao.insert(recordUpdater, item);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                haveErrors = true;
            }
        }
        return haveErrors;
    }

    @Override
    public OwnersWithAttributeNameResponse findOwnersWithAttributeName(
            final String ownerType, final String descriptorName )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findOwnersWithAttributeName()");
            logger.info( "ownerType=" + ownerType );
            logger.info( "descriptorName=" + descriptorName );
        }

        String ownerUC = ownerType.toUpperCase();

        ResponseStatus sts = null;
        OwnersWithAttributeNameResponse response = new OwnersWithAttributeNameResponse();

        response.setOwnerType(ownerUC);
        response.setDescriptorName(descriptorName);

        // Validate owner type
        if( ! SiamOwnerTypes.isValid(ownerUC)) {
            List<ValidationError> listErrors = new ArrayList<ValidationError>();
            ValidationError vError = new ValidationError( FLD_OWNER_TYPE, ERRMSG_INVALID_OWNER_TYPE );
            listErrors.add(vError);
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
        }
        else {
            Iterable<OwnerDescriptorItem> listOwners =
                    attributeValueDao.findOwnersWithAttributeName(
                            ownerUC,
                            descriptorName );
            response.setListOwners(listOwners);

            if( listOwners == null ) {
                sts = statusNotFound( ERRMSG_NOT_FOUND );
            }
            else {
                sts = statusSuccess();
            }
        }

        response.setStatus(sts);

        return response;
    }


}
