/**
 * OWASP GoatDroid Project
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * GoatDroid project. For details, please see
 * https://www.owasp.org/index.php/Projects/OWASP_GoatDroid_Project
 *
 * Copyright (c) 2012 - The OWASP Foundation
 * 
 * GoatDroid is published by OWASP under the GPLv3 license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author Jack Mannino (Jack.Mannino@owasp.org https://www.owasp.org/index.php/User:Jack_Mannino)
 * @author Walter Tighzert
 * @created 2012
 */
package org.owasp.goatdroid.fourgoats.request;

import org.owasp.goatdroid.fourgoats.http.AuthenticatedRestClient;
import org.owasp.goatdroid.fourgoats.http.RequestMethod;
import org.owasp.goatdroid.fourgoats.misc.Utils;
import org.owasp.goatdroid.fourgoats.response.AddVenueResponse;
import org.owasp.goatdroid.fourgoats.responseobjects.GenericResponseObject;

import android.content.Context;

public class AddVenueRequest {

	String destinationInfo;
	Context context;

	public AddVenueRequest(Context context) {

		this.context = context;
		destinationInfo = Utils.getDestinationInfo(context);
	}

	public GenericResponseObject doAddVenue(String venueName,
			String venueWebsite, String latitude, String longitude)
			throws Exception {

		AuthenticatedRestClient client = new AuthenticatedRestClient("https://"
				+ destinationInfo + "/fourgoats/api/v1/priv/venues/add",
				context);

		client.AddParam("venueName", venueName);
		client.AddParam("venueWebsite", venueWebsite);
		client.AddParam("latitude", latitude);
		client.AddParam("longitude", longitude);
		client.Execute(RequestMethod.POST, context);

		return AddVenueResponse.parseAddVenueResponse(client.getResponse());
	}

}
