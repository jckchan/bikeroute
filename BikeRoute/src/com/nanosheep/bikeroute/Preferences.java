/**
 * 
 */
package com.nanosheep.bikeroute;

import com.nanosheep.bikeroute.R;
import com.nanosheep.bikeroute.utility.dialog.DialogFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * 
 * This file is part of BikeRoute.
 * 
 * Copyright (C) 2011  Jonathan Gray
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * @author jono@nanosheep.net
 * @version Jul 5, 2010
 */
public class Preferences extends PreferenceActivity {
        private Preference tts;

		@Override
        protected void onCreate(final Bundle savedState) {
                super.onCreate(savedState);
                
                //Check for TTS
                Intent checkIntent = new Intent();
				checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
				startActivityForResult(checkIntent, R.id.tts_check);
				
                addPreferencesFromResource(R.xml.preferences);
                tts = findPreference("tts");
        }
        
        @Override
    	public final boolean onPrepareOptionsMenu(final Menu menu) {
    		final MenuItem steps = menu.findItem(R.id.directions);

    		steps.setVisible(false);
    		if (((BikeRouteApp)getApplication()).getRoute() != null) {
    			steps.setVisible(true);
    		}
    		return super.onPrepareOptionsMenu(menu);
    	}
        
        /**
    	 * Create the options menu.
    	 * @return true if menu created.
    	 */

    	@Override
    	public final boolean onCreateOptionsMenu(final Menu menu) {
    		final MenuInflater inflater = getMenuInflater();
    		inflater.inflate(R.menu.options_menu, menu);
    		return true;
    	}
    	
    	/**
    	 * Handle option selection.
    	 * @return true if option selected.
    	 */
    	@Override
    	public boolean onOptionsItemSelected(final MenuItem item) {
    		Intent intent;
    		switch(item.getItemId()) {
    		case R.id.navigate:
    			intent = new Intent(this, Navigate.class);
    			startActivityForResult(intent, R.id.trace);
    			break;
    		case R.id.directions:
    			intent = new Intent(this, DirectionsView.class);
    			startActivityForResult(intent, R.id.trace);
    			break;
    		case R.id.map:
    			intent = new Intent(this, LiveRouteMap.class);
    			//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    			startActivityForResult(intent, R.id.trace);
    			break;
    		case R.id.stop_nav:
    			finishActivity(R.id.trace);
    			setResult(1);
    			this.finish();
    			break;
    		case R.id.about:
    			showDialog(R.id.about);
    			break;
    		}
    		return true;
    	}
    	
    	@Override
		protected void onActivityResult(
		        final int requestCode, final int resultCode, final Intent data) {
		    if (requestCode == R.id.tts_check) {
		        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
		        	tts.setEnabled(true);
		        } else {
		            final Intent installIntent = new Intent();
		            installIntent.setAction(
		                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
		            startActivity(installIntent);
		        }
		    }
		   
		    if ((requestCode == R.id.trace) && (resultCode == 1)) {
		    	setResult(1);
		    	finish();
		    }
		}
    	
    	/**
    	 * Creates dialogs for loading, on errors, alerts.
    	 * Available dialogs:
    	 * Planning progress, planning error.
    	 * @return the approriate Dialog object
    	 */
    	
    	@Override
		public Dialog onCreateDialog(final int id) {
    		return DialogFactory.getAboutDialog(this);
    	}
    	
    	
}