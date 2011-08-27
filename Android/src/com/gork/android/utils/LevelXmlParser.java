/*
Copyright 2011 Luke Yeager and Sam Bryan
$Id$

This file is part of Gork.

Gork is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gork is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gork.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
*/

package com.gork.android.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gork.android.components.Goal;
import com.gork.android.components.Level;
import com.gork.android.components.Player;
import com.gork.android.components.Wall;

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

	private Level level;

	public LevelXmlParser(Level mLevel) {
		super();
		level = mLevel;
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

	@Override
	public void endDocument() throws SAXException {

		if (!parsedLevel) {
			throw new SAXException("Never parsed <Level>");
		} else if (!parsedPlayer) {
			throw new SAXException("Never parsed <Player>");
		}
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
		level.components[c.x][c.y] = new Wall();
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
		level.components[c.x][c.y] = new Goal(level);
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

}
