/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;
import java.util.List;

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
public class AttributeNameValueList
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String attributeDescriptorId;
    private String attributeName;
    private List<String> listValues;
}
