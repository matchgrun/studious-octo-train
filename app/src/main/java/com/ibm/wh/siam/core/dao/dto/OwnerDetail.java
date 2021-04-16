/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
public class OwnerDetail
implements Serializable
{
    private static final long serialVersionUID = -1L;
    private String ownerId;
    private String ownerType;
    private String ownerCode;
    private String ownerName;
}
