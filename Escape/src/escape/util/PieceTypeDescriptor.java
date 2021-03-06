/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Copyright ©2016-2020 Gary F. Pollice
 *******************************************************************************/
package escape.util;

import java.util.*;

import escape.required.EscapePiece.*;

/**
 * A JavaBean that represents a complete piece type description. This file
 * is provided as an example that can be used to initialize instances of a GameManager
 * via the EscapeBuilder. You do not have to use this, but are encouraged to do so.
 *
 * However, you do need to be able to load the appropriate named data from the 
 * configuration file in order to create a game correctly.
 * 
 * MODIFIABLE: YES
 * MOVEABLE: YES
 * REQUIRED: NO
 */
public class PieceTypeDescriptor
{
	private PieceName pieceName;
    private MovementPattern movementPattern;
    private PieceAttribute[] attributes;
    
    public PieceTypeDescriptor() {}

    public PieceTypeDescriptor(PieceName pieceName, MovementPattern movementPattern, PieceAttribute[] attributes) {
        this.pieceName = pieceName;
        this.movementPattern = movementPattern;
        this.attributes = attributes;
    }

// TODO : maybe make these methods public


    public int getMovementValue()
    {
        PieceAttribute distance = getAttribute(PieceAttributeID.DISTANCE);
        if(distance == null)
            distance = getAttribute(PieceAttributeID.FLY);
        return distance.getValue();
//        if(distance != null) return distance.getValue();
//        return getAttribute(PieceAttributeID.FLY).getValue();

    }

    public int getValue()
    {
        PieceAttribute value = getAttribute(PieceAttributeID.VALUE);
        if(value == null) return 1;
        return value.getValue();

    }
    /**
     * @return the pieceName
     */
    public PieceName getPieceName()
    {
        return pieceName;
    }
    /**
     * @param pieceName the pieceName to set
     */
    public void setPieceName(PieceName pieceName)
    {
        this.pieceName = pieceName;
    }
    /**
     * @return the movementPattern
     */
    public MovementPattern getMovementPattern()
    {
        return movementPattern;
    }
    /**
     * @param movementPattern the movementPattern to set
     */
    public void setMovementPattern(MovementPattern movementPattern)
    {
        this.movementPattern = movementPattern;
    }
    /**
     * @return the attributes
     */
    public PieceAttribute[] getAttributes()
    {
        return attributes;
    }
    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(PieceAttribute ... attributes)
    {
        this.attributes = attributes;
    }
    
    /**
	 * See if this descriptor has the specified attribute
	 * @param id the attribute ID
	 * @return the attribute or null if the piece has none
	 */
	public PieceAttribute getAttribute(PieceAttributeID id)
	{
		Optional<PieceAttribute> attr = 
			Arrays.stream(attributes)
				.filter(a -> a.getId().equals(id))
				.findFirst();
		return attr.isPresent() ? attr.get() : null;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
//	@Override
//	public String toString()
//	{
//		return "PieceTypeInitializer [pieceName=" + pieceName + ", movementPattern="
//		    + movementPattern + ", attributes=" + Arrays.toString(attributes) + "]";
//	}

    @Override
    public boolean equals(Object otherPiece)
    {
        if(this.getClass() != otherPiece.getClass()) return false;

        PieceTypeDescriptor otherGamePiece = (PieceTypeDescriptor) otherPiece;

        for(PieceAttribute otherAttribute : otherGamePiece.attributes)
        {
            boolean anyMatch = Arrays.stream(this.attributes).anyMatch(thisAttribute -> thisAttribute.equals(otherAttribute));
            if(!anyMatch) return false;
        }

        return this.pieceName.equals(otherGamePiece.pieceName) && this.movementPattern.equals(otherGamePiece.movementPattern);
    }
}
