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
 * @created 2012
 */
package org.owasp.goatdroid.herdfinancial.request;

import org.owasp.goatdroid.herdfinancial.http.AuthenticatedRestClient;
import org.owasp.goatdroid.herdfinancial.http.RequestMethod;
import org.owasp.goatdroid.herdfinancial.misc.Utils;
import org.owasp.goatdroid.herdfinancial.response.StatementResponse;
import org.owasp.goatdroid.herdfinancial.responseobjects.Statement;

import android.content.Context;

public class StatementsRequest {

	Context context;
	String destinationInfo;

	public StatementsRequest(Context context) {

		this.context = context;
		destinationInfo = Utils.getDestinationInfo(context);
	}

	public Statement getStatement(String sessionToken,
			String accountNumber, String startDate, String endDate)
			throws Exception {

		AuthenticatedRestClient client = new AuthenticatedRestClient("https://"
				+ destinationInfo + "/herdfinancial/api/v1/priv/statements/"
				+ accountNumber + "/" + startDate + "/" + endDate);
		client.Execute(RequestMethod.GET, context);
		return StatementResponse.parseStatusAndStatement(client.getResponse());
	}

	public Statement getStatementUpdate(
			String sessionToken, String accountNumber) throws Exception {

		AuthenticatedRestClient client = new AuthenticatedRestClient(
				"https://"
						+ destinationInfo
						+ "/herdfinancial/api/v1/priv/statements/poll-statement-updates/"
						+ accountNumber);
		client.Execute(RequestMethod.GET, context);
		return StatementResponse.parseStatusAndStatement(client.getResponse());
	}
}
