/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.dto;

import java.io.Serializable;

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
public class AttributeNameValueLW
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String attributeId;
    private String attributeDescriptorId;
    private String attributeName;
    private String attributeValue;
}
