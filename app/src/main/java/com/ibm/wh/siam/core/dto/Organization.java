package com.ibm.wh.siam.core.dto;


import java.io.Serializable;

import com.ibm.wh.siam.core.annotations.SiamString;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Organization
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String organizationId;

    @SiamString(empty=false, maxLength=30)
    private String organizationCode;
    @SiamString(empty=false, maxLength=50)
    private String organizationName;
    @SiamString(empty=false, maxLength=30)
    private String organizationType;
    @SiamString(maxLength=255)
    private String description;
    @SiamString(empty=false, maxLength=30)
    private String accountNumber;
    @SiamString(maxLength=30)
    private String accountGroup;
    @SiamString(maxLength=30)
    private String legacyEntityId;
    @SiamString(maxLength=30)
    private String legacyEntityShortName;
    @SiamString(maxLength=30)
    private String marketSegment;

    private String ldapEntryDN;
    private String adExternalDn;

}
