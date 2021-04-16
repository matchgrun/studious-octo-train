/********************************************************* {COPYRIGHT-TOP} ***
* IBM Confidential
* OCO Source Materials
* *** thidsutilities ***
*
* (C) Copyright IBM Corporation 2018
*
* The source code for this program is not published or otherwise
* divested of its trade secrets, irrespective of what has been
* deposited with the U.S. Copyright Office.
********************************************************* {COPYRIGHT-END} **/
/*
 * Class Name:  TreeNodeSequenceComparator.java
 * Description: Implements a Tree Node Comparator that orders by sequence.
 *
 * Copyright 2012 - Feb 17, 2012: Thomson Reuters Global Resources.
 * All Rights Reserved. Proprietary and Confidential information of TRGR.
 * Disclosure without the written authorization of TRGR is prohibited.
 * Created on Feb 17, 2012.
 */
package com.ibm.wh.siam.common.tree;

import java.util.Comparator;

/**
 * Implements a Tree Node Comparator that orders by sequence.
 *
 * @author Match Grun
 */
public class TreeNodeSequenceComparator
implements Comparator<Object>
{
// -----------------------------------------------------------------------------
// Constants.
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
// Member variables.
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
// Constructors.
// -----------------------------------------------------------------------------
   /**
    * Default constructor.
    */
   public TreeNodeSequenceComparator()
   {
   }

// -----------------------------------------------------------------------------
// Methods.
// -----------------------------------------------------------------------------
   /**
    * Comparison function.
    * @param obj1 The first object.
    * @param obj2 The second object.
    * @return Negative, zero, position integer.
    * @see Comparator
    * @exception ClassCastException if objects are incorrect type.
    */
    @Override
    public int compare( Object obj1, Object obj2 )
    throws ClassCastException
    {
        SingleParentNode n1 = null;
        SingleParentNode n2 = null;
        if( obj1 instanceof SingleParentNode )
        {
            if( obj2 instanceof SingleParentNode ) {
                n1 = ( SingleParentNode ) obj1;
                n2 = ( SingleParentNode ) obj2;
           }
           else {
               throw new ClassCastException( "Object 2 must be an SingleParentNode." );
           }
        }
        else {
            throw new ClassCastException( "Object 1 must be an SingleParentNode." );
        }

        return n1.getSequence() - n2.getSequence();
    }

// -----------------------------------------------------------------------------
// Properties.
// -----------------------------------------------------------------------------

}

// =============================================================================
// End of Source.
// =============================================================================
//
