/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.content;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.ContentSet;
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
public class ContentSetListResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Iterable<ContentSet> listContentSets;

}
