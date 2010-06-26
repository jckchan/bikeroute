package com.nanosheep.bikeroute;

import android.app.Activity;
import android.content.SharedPreferences;
import com.google.android.maps.GeoPoint;

/**
 * A class to handle parking a bike.
 * 
 * Stores and retrieves parking location in sharedpreferences.
 * 
 * @author jono@nanosheep.net
 * 
 */

public class Parking {
	private static final String PREFS_NAME = "bikepark_location";
	private static final String LAT = "lat";
	private static final String LNG = "lng";
	private final SharedPreferences settings;
	private final SharedPreferences.Editor editor;

	public Parking(final Activity activity) {
		settings = activity.getSharedPreferences(PREFS_NAME, 0);
		editor = settings.edit();
	}

	public final void park(final int latitude, final int longitude) {
		park(new GeoPoint(latitude, longitude));
	}

	/**
	 * 'Park' at the location given. Set values for lng & lat in
	 * sharedpreferences.
	 * 
	 * @param p
	 *            Location to park at.
	 */

	public final void park(final GeoPoint p) {
		editor.putInt(LAT, p.getLatitudeE6());
		editor.putInt(LNG, p.getLongitudeE6());
		editor.commit();
	}

	/**
	 * Unpark from the current parking location.
	 */

	public final void unPark() {
		editor.remove(LAT);
		editor.remove(LNG);
		editor.commit();
	}

	/**
	 * Check if parking is set.
	 * 
	 * @return a boolean indicating if values are set for lat & lng
	 */

	public final boolean isParked() {
		return settings.contains(LAT) && settings.contains(LNG);
	}

	/**
	 * Get the location of the current parking spot.
	 * 
	 * @return a Geopoint for the location. Sets lat & lng to -1 if values not
	 *         found.
	 */

	public final GeoPoint getLocation() {
		final int lat = settings.getInt(LAT, -1);
		final int lng = settings.getInt(LNG, -1);
		return new GeoPoint(lat, lng);
	}

}