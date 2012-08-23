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

package org.graphstream.geography.shp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.graphstream.geography.AttributeFilter;
import org.graphstream.geography.ElementDescriptor;
import org.graphstream.geography.Element;
import org.graphstream.geography.GeoSource;
import org.graphstream.geography.Line;
import org.graphstream.geography.Point;
import org.graphstream.geography.Polygon;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Descriptor for generic shapefiles.
 * 
 * @author Merwan Achibet
 */
public class ElementDescriptorSHP extends ElementDescriptor {

	/**
	 * Instantiate a new descriptor.
	 * 
	 * @param source
	 *            The source using this descriptor.
	 * @param category
	 *            The category associated with matching elements.
	 * @param filter
	 *            The filter that will reduce the attributes of matching
	 *            elements.
	 */
	public ElementDescriptorSHP(GeoSource source, String category, AttributeFilter filter) {
		super(source, category, filter);
	}

	@Override
	protected boolean isPoint(Object o) {

		// Cast the object to a GeoTools feature.

		SimpleFeature feature = (SimpleFeature)o;

		// TODO: A better way to do this?

		return feature.getType().getGeometryDescriptor().getType().getBinding() == com.vividsolutions.jts.geom.Point.class;

	}

	@Override
	protected boolean isLine(Object o) {

		// Cast the object to a GeoTools feature.

		SimpleFeature feature = (SimpleFeature)o;

		// TODO: A better way to do this?

		Class<?> binding = feature.getType().getGeometryDescriptor().getType().getBinding();

		return binding == com.vividsolutions.jts.geom.MultiLineString.class || binding == com.vividsolutions.jts.geom.LineString.class;
	}

	@Override
	protected boolean isPolygon(Object o) {

		// Cast the object to a GeoTools feature.

		SimpleFeature feature = (SimpleFeature)o;

		// TODO: A better way to do this?

		Class<?> binding = feature.getType().getGeometryDescriptor().getType().getBinding();

		return binding == com.vividsolutions.jts.geom.Polygon.class;
	}

	@Override
	protected boolean hasKey(String key, Object o) {

		SimpleFeature feature = (SimpleFeature)o;

		Collection<Property> properties = feature.getProperties();

		for(Property property : properties)
			if(property.getName().toString().equals(key))
				return true;

		return false;
	}

	@Override
	protected boolean hasKeyValue(String key, Object value, Object o) {

		SimpleFeature feature = (SimpleFeature)o;

		Collection<Property> properties = feature.getProperties();

		for(Property property : properties)
			if(property.getName().toString().equals(key) && property.getValue().equals(value))
				return true;

		return false;
	}

	@Override
	public String getElementId(Object o) {

		// Cast the object to a GeoTools SimpleFeature.

		SimpleFeature feature = (SimpleFeature)o;

		// Return its ID.
		
		return feature.getID();
	}

	@Override
	protected Point newPoint(Object o) {

		// Cast the object to a GeoTools SimpleFeature.

		SimpleFeature feature = (SimpleFeature)o;

		// Retrieve its ID.

		String id = feature.getID();

		// Instantiate a new point.

		Point point = new Point(id, getCategory());

		// Set the position.

		Coordinate[] coord = ((Geometry)feature.getDefaultGeometry()).getCoordinates();

		point.setPosition(coord[0].x, coord[0].y);

		// Bind the other attributes according to the filter.

		bindAttributesToElement(feature, point);

		return point;
	}
	
	@Override
	protected Point newPointDiff(Element previousVersion, Object o) {

		// Cast the object to a GeoTools SimpleFeature.

		SimpleFeature feature = (SimpleFeature)o;

		// Retrieve its ID.

		String id = feature.getID();

		// Instantiate a new point.

		Point point = new Point(id, getCategory());

		// Bind the other attributes according to the filter.

		bindAttributesToElementDiff(previousVersion, feature, point);

		return point;
	}
	
	@Override
	protected Line newLine(Object o) {

		// Cast the object to a GeoTools SimpleFeature.

		SimpleFeature feature = (SimpleFeature)o;

		// Retrieve its ID.

		String id = feature.getID();

		// Instantiate a new line.

		Line line = new Line(id, getCategory());

		// Shape the line.

		Coordinate[] coords = ((Geometry)feature.getDefaultGeometry()).getCoordinates();

		// TODO same for DescriptorOSM
		if(this.onlyLineEndPointsConsidered) {
			line.addPoint(null, coords[0].x, coords[0].y);
			line.addPoint(null, coords[coords.length - 1].x, coords[coords.length - 1].y);
		}
		else
			for(int i = 0; i < coords.length; ++i)
				line.addPoint(null, coords[i].x, coords[i].y);

		// Bind the attributes according to the filter.

		bindAttributesToElement(feature, line);

		return line;
	}
	
	@Override
	protected Line newLineDiff(Element previousVersion, Object o) {

		// Cast the object to a GeoTools SimpleFeature.

		SimpleFeature feature = (SimpleFeature)o;

		// Retrieve its ID.

		String id = feature.getID();

		// Instantiate a new line.

		Line line = new Line(id, getCategory());

		// Bind the attributes according to the filter.

		bindAttributesToElementDiff(previousVersion, feature, line);

		return line;
	}

	@Override
	protected Polygon newPolygon(Object o) {

		// Cast the object to a GeoTools SimpleFeature.

		SimpleFeature feature = (SimpleFeature)o;

		// Retrieve its ID.

		String id = feature.getID();

		// Instantiate a new line.

		Polygon polygon = new Polygon(id, getCategory());

		// Shape the line.

		Coordinate[] coords = ((Geometry)feature.getDefaultGeometry()).getCoordinates();

		for(int i = 0; i < coords.length; ++i)
			polygon.addPoint(null, coords[i].x, coords[i].y);

		// Bind the attributes according to the filter.

		bindAttributesToElement(feature, polygon);

		return polygon;
	}

	@Override
	protected Polygon newPolygonDiff(Element previousVersion, Object o) {

		// Cast the object to a GeoTools SimpleFeature.

		SimpleFeature feature = (SimpleFeature)o;

		// Retrieve its ID.

		String id = feature.getID();

		// Instantiate a new line.

		Polygon polygon = new Polygon(id, getCategory());

		// Bind the attributes according to the filter.

		bindAttributesToElementDiff(previousVersion, feature, polygon);

		return polygon;
	}

	/**
	 * Copy the attributes of an element from its input format to the simple
	 * geometric format.
	 * 
	 * This is where an optional attribute filter is applied.
	 * 
	 * @param feature
	 *            The GeoTools feature where attributes are copied from.
	 * @param element
	 *            The simple geometric element where filtered attributes are
	 *            copied to.
	 */
	protected void bindAttributesToElement(SimpleFeature feature, Element element) {

		Collection<Property> properties = feature.getProperties();

		for(Property property : properties)
			if(this.filter == null || this.filter.isKept(property.getName().toString()))
				element.setAttribute(property.getName().toString(), property.getValue());
	}

	protected void bindAttributesToElementDiff(Element previousVersion, SimpleFeature feature, Element element) {
		
		Collection<Property> newVersionProperties = feature.getProperties();
		HashMap<String, Object> newVersionAttributes = new HashMap<String, Object>();
		for(Property property : newVersionProperties)
			newVersionAttributes.put(property.getName().toString(), property.getValue());
		
		HashMap<String, Object> previousVersionAttributes = previousVersion.getAttributes();
		
		for(Entry<String, Object> entry : previousVersionAttributes.entrySet())
			if(!newVersionAttributes.containsKey(entry.getKey()))
				element.addRemovedAttribute(entry.getKey());
		
		for(Entry<String, Object> entry : newVersionAttributes.entrySet())
			if(!previousVersionAttributes.containsKey(entry.getKey()))
				element.setAttribute(entry.getKey(), entry.getValue());
		
		for(Entry<String, Object> entry : newVersionAttributes.entrySet())
			if(previousVersionAttributes.containsKey(entry.getKey()) && !previousVersionAttributes.get(entry.getKey()).equals(entry.getValue()))
				element.setAttribute(entry.getKey(), entry.getValue());
	}
	
}