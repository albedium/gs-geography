/*
 * Copyright 2006 - 2011 
 *     Julien Baudry	<julien.baudry@graphstream-project.org>
 *     Antoine Dutot	<antoine.dutot@graphstream-project.org>
 *     Yoann Pigné		<yoann.pigne@graphstream-project.org>
 *     Guilhelm Savin	<guilhelm.savin@graphstream-project.org>
 * 
 * This file is part of GraphStream <http://graphstream-project.org>.
 * 
 * GraphStream is a library whose purpose is to handle static or dynamic
 * graph, create them from scratch, file or any source and display them.
 * 
 * This program is free software distributed under the terms of two licenses, the
 * CeCILL-C license that fits European law, and the GNU Lesser General Public
 * License. You can  use, modify and/ or redistribute the software under the terms
 * of the CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
 * URL <http://www.cecill.info> or under the terms of the GNU LGPL as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C and LGPL licenses and that you accept their terms.
 */

package org.graphstream.geography;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract geometric element.
 * 
 * This class serves as a base for Point, Line and Polygon. These are used as
 * intermediary representations for any kind of geographical features coming
 * from an external source. The original input formats can be quite
 * heterogeneous as the data they contain is library-dependent and we don't want
 * force the user to learn GeoTools, XOM or whatever other library.
 * 
 * An Element consists of an identifier and a list of attributes copied from the
 * original format of the element (potentially filtered).
 * 
 * @author Merwan Achibet
 */
public abstract class Element {

	public static enum Type {
		POINT, LINE, POLYGON
	};

	/**
	 * The ID of the feature.
	 */
	protected String id;

	/**
	 * The category of the feature (attributed by the descriptor that
	 * instantiated the object from a matching feature).
	 */
	protected String category;

	/**
	 * A key/value mapping of attributes.
	 */
	protected Map<String, Object> attributes;

	/**
	 * Instantiate a new element.
	 * 
	 * @param id
	 *            The identifier of the element.
	 */
	public Element(String id, String category) {

		this.id = id;
		this.category = category;

		this.attributes = new HashMap<String, Object>();
	}

	/**
	 * Give the ID of the element.
	 * 
	 * @return The ID.
	 */
	public String getId() {

		return new String(this.id);
	}

	/**
	 * Give the category of the element.
	 * 
	 * @return The category.
	 */
	public String getCategory() {

		return new String(this.category);
	}

	/**
	 * Give the value of the attribute which name is supplied.
	 * 
	 * @param key
	 *            The key of the attribute.
	 * @return The value of the attribute or null if it does not exist.
	 */
	public Object getAttribute(String key) {

		return this.attributes.get(key);
	}

	/**
	 * Check if the element possesses the supplied attribute.
	 * 
	 * @param key
	 *            The key of the attribute.
	 * @return True if the attribute is contained within the element, false
	 *         otherwise.
	 */
	public boolean hasAttribute(String key) {

		return this.attributes.containsKey(key);
	}

	/**
	 * Check if the element possesses the supplied attribute AND if it equals
	 * the supplied value.
	 * 
	 * @param key
	 *            The key of the attribute.
	 * @param value
	 *            The value that the attribute should have.
	 * @return True if the attribute matches.
	 */
	public boolean hasAttribute(String key, Object value) {

		return this.attributes.containsKey(key) && this.attributes.get(key).equals(value);
	}

	/**
	 * Add an key/value pair to the element as an attribute.
	 * 
	 * @param key
	 *            The key of the attribute.
	 * @param value
	 *            The value of the attribute.
	 */
	public void addAttribute(String key, Object value) {

		this.attributes.put(key, value);
	}

	/**
	 * Remove an attribute from the element.
	 * 
	 * @param key
	 *            The key of the attribute to remove.
	 */
	public void removeAttribute(String key) {

		this.attributes.remove(key);
	}
	
	public boolean isType(Type type) {
		
		if(type == Type.POINT)
			return isPoint();
		
		if(type == Type.LINE)
			return isLine();
		
		// TODO polygon
		
		return false;
	}

	/**
	 * Check if the element is a point.
	 * 
	 * @return True if the element is a point, false otherwise.
	 */
	public boolean isPoint() {

		return this instanceof Point;
	}

	/**
	 * Check if the element is a line.
	 * 
	 * @return True if the element is a line, false otherwise.
	 */
	public boolean isLine() {

		return this instanceof Line;
	}

	// Abstract

	/**
	 * Check if the element is placed at a given position.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @return True if the element is at position (x,y), false otherwise.
	 */
	public abstract boolean at(double x, double y);

}
