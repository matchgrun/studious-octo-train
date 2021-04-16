/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;

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
public class Group
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String groupId;

    @SiamString(empty=false, maxLength=30)
    private String groupCode;
    @SiamString(empty=false, maxLength=30)
    private String groupName;
    @SiamString(empty=false, maxLength=50)
    private String groupType;

    @SiamString(maxLength=255)
    private String description;
    @SiamString(maxLength=30)
    private String accountGroup;
    @SiamString(maxLength=30)
    private String legacyGroupId;
    @SiamString(maxLength=50)
    private String legacyGroupName;

    private String ldapEntryDN;
    private String adExternalDN;

}

