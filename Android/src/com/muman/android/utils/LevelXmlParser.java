/*
Copyright 2011 Luke Yeager and Sam Bryan
$Id$

This file is part of MuMan.

MuMan is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

MuMan is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with MuMan.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
*/

package com.muman.android.utils;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.muman.android.components.Component;
import com.muman.android.components.Goal;
import com.muman.android.components.LaserBeam;
import com.muman.android.components.LaserSource;
import com.muman.android.components.Level;
import com.muman.android.components.Player;
import com.muman.android.components.Spiker;
import com.muman.android.components.Wall;

public class LevelXmlParser extends DefaultHandler {

	boolean currentElement = false;
	String currentValue = null;

	// The level must be parsed before the Components to set
	// level.components[][]
	boolean parsingLevel = false;
	// All component declarations should come within the <Components> node
	boolean parsingComponents = false;

	// Required nodes:
	boolean parsedLevel = false;
	boolean parsedPlayer = false;

	/**
	 * The Level into which the parsed data is being added
	 */
	private Level level;
	
	/**
	 * A list of lasers which have been created. At the end of parsing, these must be extended from the
	 * 		original LaserSource to include LaserBeams which extend until a solid surface is encountered.
	 */
	private ArrayList<Coordinate> lasers;
	
	/**
	 * Default constructor
	 * @param mLevel The Level into which the parsed data is being added
	 */
	public LevelXmlParser(Level mLevel) {
		super();
		level = mLevel;
		lasers = new ArrayList<Coordinate>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentElement = true;

		if (localName.equals("Level")) {
			startLevel(attributes);
		} else if (localName.equals("Player")) {
			parsePlayer(attributes);
		} else if (localName.equals("Components")) {
			startComponents(attributes);
		} else if (localName.equals("Wall")) {
			parseWall(attributes);
		} else if (localName.equals("Goal")) {
			parseGoal(attributes);
		} else if (localName.equals("Spiker")) {
			parseSpiker(attributes);
		} else if (localName.equals("Laser")) {
			parseLaser(attributes);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		if (localName.equals("Player")) {
			parsedPlayer = true;
		} else if (localName.equals("Level")) {
			parsedLevel = true;
			parsingLevel = false;
		}
	}

	// XXX: This never gets executed?
	@Override
	public void endDocument() throws SAXException {

		if (!parsedLevel) {
			throw new SAXException("Never parsed <Level>");
		} else if (!parsedPlayer) {
			throw new SAXException("Never parsed <Player>");
		}

		extendLasers();
	}	

	/**
	 * Called when a Level tag begins. This should be the rootNode of the XML
	 * file.
	 * 
	 * @param attrs
	 * @throws SAXException
	 */
	private void startLevel(Attributes attrs) throws SAXException {
		Integer width = null;
		Integer height = null;

		int length = attrs.getLength();
		for (int i = 0; i < length; i++) {
			String name = attrs.getLocalName(i);
			String value = attrs.getValue(i);
			if (name.equals("width")) {
				width = Integer.parseInt(value);
			} else if (name.equals("height")) {
				height = Integer.parseInt(value);
			}
		}

		if (width == null | height == null) {
			throw new SAXException("Error parsing <Level>");
		} else {
			parsingLevel = true;
			level.setDimensions(width, height);
		}

	}

	/**
	 * Parses the Player tag. All of this tag's data is within the attributes.
	 * 
	 * @param attrs
	 * @throws SAXException
	 */
	private void parsePlayer(Attributes attrs) throws SAXException {
		if (!parsingLevel) {
			throw new SAXException("<Player> node must be within <Level>");
		}
		Coordinate c = parseAttrsForPosition(attrs);
		
		parsedPlayer = true;
		level.player = new Player(c.x, c.y);
	}

	/**
	 * Called when a Level tag begins. This should be the root node for all
	 * component declarations.
	 * 
	 * @param attrs
	 * @throws SAXException
	 */
	private void startComponents(Attributes attrs) throws SAXException {
		if (!parsingLevel) {
			throw new SAXException("<Components> node must be within <Level>");
		}
		parsingComponents = true;
	}

	/**
	 * Parse a Wall component
	 * 
	 * @param attrs
	 * @throws SAXException
	 */
	private void parseWall(Attributes attrs) throws SAXException {
		if (!parsingComponents) {
			throw new SAXException("<Wall> node must be within <Components>");
		}

		Coordinate c = parseAttrsForPosition(attrs);
		level.addComponent(c.x, c.y, new Wall());
	}

	/**
	 * Parse a Goal component
	 * 
	 * @param attrs
	 * @throws SAXException
	 */
	private void parseGoal(Attributes attrs) throws SAXException {
		if (!parsingComponents) {
			throw new SAXException("<Goal> node must be within <Components>");
		}

		Coordinate c = parseAttrsForPosition(attrs);
		level.addComponent(c.x, c.y, new Goal());
	}

	/**
	 * Parse a Spiker component
	 * 
	 * @param attrs
	 * @throws SAXException
	 */
	private void parseSpiker(Attributes attrs) throws SAXException {
		if (!parsingComponents) {
			throw new SAXException("<Spiker> node must be within <Components>");
		}

		Coordinate c = parseAttrsForPosition(attrs);
		level.addComponent(c.x, c.y, new Spiker());
	}
	
	/**
	 * Parse a Laser component
	 * 
	 * @param attrs
	 * @throws SAXException
	 */
	private void parseLaser(Attributes attrs) throws SAXException {
		if (!parsingComponents) {
			throw new SAXException("<Laser> node must be within <Components>");
		}

		Coordinate c = parseAttrsForPosition(attrs);
		
		LaserSource.Direction dir = null;
		int length = attrs.getLength();
		for (int i = 0; i < length; i++) {
			String name = attrs.getLocalName(i);
			String value = attrs.getValue(i);
			if (name.equals("Direction")) {
				if (value.equalsIgnoreCase("UP")) {
					dir = LaserSource.Direction.UP;
				} else if (value.equalsIgnoreCase("DOWN")) {
					dir = LaserSource.Direction.DOWN;
				}  else if (value.equalsIgnoreCase("LEFT")) {
					dir = LaserSource.Direction.LEFT;
				}  else if (value.equalsIgnoreCase("RIGHT")) {
					dir = LaserSource.Direction.RIGHT;
				}
				break;
			}
		}
		
		if (dir == null) {
			throw new SAXException("You must specify a 'Direction' for a Laser (Up/Down/Left/Right)");
		}

		level.addComponent(c.x, c.y, new LaserSource(dir));
		lasers.add(c);
	}

	/**
	 * Parses a list of attributes and looks for a position definition. This
	 * method can be used for all component declarations.
	 * 
	 * @param attrs
	 * @return
	 * @throws SAXException 
	 */
	private Coordinate parseAttrsForPosition(Attributes attrs) throws SAXException {
		Integer x = null;
		Integer y = null;
		int length = attrs.getLength();
		for (int i = 0; i < length; i++) {
			String name = attrs.getLocalName(i);
			String value = attrs.getValue(i);
			if (name.equalsIgnoreCase("x")) {
				x = Integer.parseInt(value);
			} else if (name.equalsIgnoreCase("y")) {
				y = Integer.parseInt(value);
			}
		}
		
		if (x == null | y == null) {
			throw new SAXException("Could not parse position from attrs");
		} else {
			return new Coordinate(x-1,y-1);
		}
	}
	
	/**
	 * Called after XML file has been parsed, this function extends lasers beyond the LaserSource to include LaserBeams
	 * @throws SAXException
	 */
	private void extendLasers() throws SAXException {
		// Create Lasers
		int numLasers = lasers.size();
		for(int i=0; i<numLasers; i++) {
			Coordinate c = lasers.get(i);
			Component[] array = level.getComponents(c.x, c.y);
			
			// We'll just take the first one we encounter
			//	XXX: This means you can't put two laser sources in the same location for now
			LaserSource.Direction dir = null;
			for (int j=0; j<array.length; j++) {
				if (array[j].getClass() == LaserSource.class) {
					dir = ((LaserSource) array[j]).getDirection();
					break;
				}
			}
			
			// Update c
			switch (dir) {
			case UP:
				c.y--;
				break;
			case DOWN:
				c.y++;
				break;
			case LEFT:
				c.x--;
				break;
			case RIGHT:
				c.x++;
				break;
			default:
				throw new SAXException("Somehow the LaserSource orientation got thrown off during parsing");
			}
			
			while (c.x >= 0 && c.x < level.getWidth() && c.y >= 0 && c.y < level.getHeight()) {
				
				array = level.getComponents(c.x, c.y);
				boolean drawHere = true;
				for (int j=0; j<array.length; j++) {
					if (array[j].stopsLaser()) {
						drawHere = false;
						break;
					}
				}
				if (!drawHere)
					break;
				
				level.addComponent(c.x, c.y, new LaserBeam(dir)); 
				
				// Update c
				switch (dir) {
				case UP:
					c.y--;
					break;
				case DOWN:
					c.y++;
					break;
				case LEFT:
					c.x--;
					break;
				case RIGHT:
					c.x++;
					break;
				default:
				}
			}
		}
	}

}
