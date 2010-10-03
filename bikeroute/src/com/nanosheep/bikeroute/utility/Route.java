package com.nanosheep.bikeroute.utility;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

//import com.google.android.maps.GeoPoint;
import org.andnav.osm.util.GeoPoint;

/**
 * @author jono@nanosheep.net
 * @version Jun 22, 2010
 */
public class Route implements Parcelable{
	private String name;
	private final List<GeoPoint> points;
	private List<Segment> segments;
	private String copyright;
	private String warning;
	private String country;
	private int length;
	private XYSeries elevations;
	private String polyline;
	
	public Route() {
		points = new ArrayList<GeoPoint>();
		segments = new ArrayList<Segment>();
		elevations = new XYSeries("Elevation");
	}
	
	 public Route(final Parcel in) {
         points = new ArrayList<GeoPoint>();
         elevations = new XYSeries("Elevation");
         readFromParcel(in);
	 }
	
	 /* (non-Javadoc)
      * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
      */
     @Override
     public void writeToParcel(final Parcel dest, final int flags) {
             dest.writeString(name);
             dest.writeTypedList(segments);
             dest.writeString(copyright);
             dest.writeString(warning);
             dest.writeString(country);
             dest.writeString(polyline);
             dest.writeInt(length);
             int pointSize = points.size();
             dest.writeInt(pointSize);
             for (int i = 0; i < pointSize; i++) {
                     dest.writeInt(points.get(i).getLatitudeE6());
                     dest.writeInt(points.get(i).getLongitudeE6());
             }
             int elevSize = elevations.getItemCount();
             dest.writeInt(elevSize);
             for (int i = 0; i < elevSize; i++) {
            	 dest.writeDouble(elevations.getX(i));
            	 dest.writeDouble(elevations.getY(i));
             }
     }
     
     public void readFromParcel(final Parcel in) {
             name = in.readString();
             segments = new ArrayList<Segment>();
             in.readTypedList(segments, Segment.CREATOR);
             copyright = in.readString();
             warning = in.readString();
             country = in.readString();
             polyline = in.readString();
             length = in.readInt();
             int pointSize = in.readInt();
             for (int i = 0; i < pointSize; i++) {
                     points.add(new GeoPoint(in.readInt(), in.readInt()));
             }
             int elevSize = in.readInt();
             for (int i = 0; i < elevSize; i++) {
            	 elevations.add(in.readDouble(), in.readDouble());
             }
     }

	public void addPoint(final GeoPoint p) {
		points.add(p);
	}
	
	public void addPoints(final List<GeoPoint> points) {
		this.points.addAll(points);
	}
	
	public List<GeoPoint> getPoints() {
		return points;
	}
	
	public void addSegment(final Segment s) {
		segments.add(s);
	}
	
	public List<Segment> getSegments() {
		return segments;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param string the copyright to set
	 */
	public void setCopyright(String string) {
		this.copyright = string;
	}

	/**
	 * @return the copyright string id
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}

	/**
	 * @return the warning
	 */
	public String getWarning() {
		return warning;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Get the elevations as a set of series' that can be displayed by the
	 * achart lib.
	 * @return an XYMultipleSeriesDataset that contains the elevation/distance series.
	 */
	
	public XYMultipleSeriesDataset getElevations() {
		XYMultipleSeriesDataset elevationSet = new XYMultipleSeriesDataset();
		elevationSet.addSeries(elevations);
	    return elevationSet;
	}
	
	/**
	 * An an elevation and distance (in metres) to the elevation series for
	 * this route.
	 * @param elevation in metres.
	 * @param dist in metres.
	 */
	
	public void addElevation(final double elevation, final double dist) {
		elevations.add(dist / 1000, elevation);
	}
	
	/**
	 * Get a renderer for drawing the elevation chart.
	 * @return an XYMultipleSeriesRenderer configured for metric.
	 */
	
	public XYMultipleSeriesRenderer getChartRenderer() {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    r.setColor(Color.BLUE);
	    r.setPointStyle(PointStyle.POINT);
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    renderer.setAxesColor(Color.DKGRAY);
	    renderer.setLabelsColor(Color.LTGRAY);
	    renderer.setYTitle("m");
	    renderer.setXTitle("km");
	    return renderer;
	  }

	/**
	 * @param polyline the polyline to set
	 */
	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}

	/**
	 * @return the polyline
	 */
	public String getPolyline() {
		return polyline;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 public static final Parcelable.Creator CREATOR =
	        new Parcelable.Creator() {
	            public Route createFromParcel(final Parcel in) {
	                return new Route(in);
	            }

	                        @Override
	                        public Route[] newArray(final int size) {
	                                return new Route[size];
	                        }
	        };

}
