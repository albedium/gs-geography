/*
 * Copyright 2006 - 2012 
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

import java.util.ArrayList;
import java.util.List;

import org.graphstream.geography.index.SpatialIndexPoint;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * A point.
 * 
 * The goal of this class is to represent in the simpler manner possible a point
 * element. Its main features are an ID, a position and a set of attributes
 * copied from its original format (potentially filtered).
 * 
 * @author Merwan Achibet
 */
public class Point extends Element {

	/**
	 * The Cartesian position of the point in the studied space.
	 */
	private Coordinate position;

	/**
	 * Instantiate a new point.
	 * 
	 * @param id
	 *            The point ID.
	 */
	public Point(String id) {
		this(id, null, false);
	}

	/**
	 * Instantiate a new point and assign it to a specific category of elements.
	 * 
	 * @param id
	 *            The point ID.
	 * @param category
	 *            The point category.
	 */
	public Point(String id, String category) {

		this(id, category, false);
	}

	/**
	 * Instantiate a new point and assign it to a specific category of elements.
	 * 
	 * @param id
	 *            The point ID.
	 * @param category
	 *            The point category.
	 * @param diff
	 *            True if the point is a diff, false otherwise.
	 */
	public Point(String id, String category, boolean diff) {
		super(id, category, diff);

		this.type = Type.POINT;
		this.position = new Coordinate();
	}

	/**
	 * Instantiate a new point by deep-copying another one.
	 * 
	 * @param other
	 *            The point to copy.
	 */
	public Point(Point other) {
		super(other);

		this.type = Type.POINT;
		this.position = new Coordinate(other.getPosition());
	}

	/**
	 * Give the Cartesian position of the point.
	 * 
	 * @return The point position.
	 */
	public Coordinate getPosition() {

		return new Coordinate(this.position);
	}

	public double getX() {

		return this.position.x;
	}

	public double getY() {

		return this.position.y;
	}

	/**
	 * Change the Cartesian position of the point.
	 * 
	 * @param x
	 *            The x-axis coordinate.
	 * @param y
	 *            The y-axis coordinate.
	 */
	public void setPosition(double x, double y) {

		this.position.x = x;
		this.position.y = y;
	}

	@Override
	public List<SpatialIndexPoint> toSpatialIndexPoints() {

		List<SpatialIndexPoint> spatialIndexPoints = new ArrayList<SpatialIndexPoint>();

		spatialIndexPoints.add(new SpatialIndexPoint(this, this.getId(), this.position.x, this.position.y));

		return spatialIndexPoints;
	}

}
