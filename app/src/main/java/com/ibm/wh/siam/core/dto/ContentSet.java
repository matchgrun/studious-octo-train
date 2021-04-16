/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;
import java.util.Date;

import com.ibm.wh.siam.core.annotations.SiamString;

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
public class ContentSet
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String contentSetId;

    @SiamString(maxLength=20)
    private String ownerType;

    private String ownerId;

    @SiamString(maxLength=40)
    private String contentSetDescriptorId;

    @SiamString(maxLength=20)
    private String contentSetType;

    @SiamString(maxLength=255)
    private String description;

    private Date startDate;
    private Date endDate;

    private String externalContentId;
    private String externalRoleName;
}
