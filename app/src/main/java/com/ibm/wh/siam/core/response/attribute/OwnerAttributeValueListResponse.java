/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.attribute;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.core.dto.AttributeNameValueList;
import com.ibm.wh.siam.core.response.BaseSiamResponse;

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
public class OwnerAttributeValueListResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String ownerId;
    private String selector;
    private String selectorValue;
    private List<AttributeNameValueList> listAttributes;

}
