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
package org.owasp.goatdroid.fourgoats.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.owasp.goatdroid.fourgoats.R;
import org.owasp.goatdroid.fourgoats.activities.ViewFriendRequestActivity;
import org.owasp.goatdroid.fourgoats.request.FriendRequest;
import org.owasp.goatdroid.fourgoats.responseobjects.PendingFriendRequest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class PendingFriendRequests extends SherlockFragment {

	Bundle bundle;
	ListView listView;
	TextView noPendingFriendRequestsTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.pending_friend_requests, container,
				false);

		noPendingFriendRequestsTextView = (TextView) v
				.findViewById(R.id.noPendingFriendRequestsTextView);
		listView = (ListView) v.findViewById(R.id.pendingFriendsListView);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				String selectedFromList = (String) (listView
						.getItemAtPosition(myItemInt));
				String[] splitList = selectedFromList.split("\n");
				Intent intent = new Intent(getActivity(),
						ViewFriendRequestActivity.class);
				Bundle profileBundle = new Bundle();
				profileBundle.putString("userName", splitList[0]);
				profileBundle.putString("fullName", splitList[1]);
				intent.putExtras(profileBundle);
				startActivity(intent);
			}
		});

		GetPendingFriendRequests getFriendRequests = new GetPendingFriendRequests();
		getFriendRequests.execute(null, null);
		return v;
	}

	public String[] bindListView(PendingFriendRequest request) {

		// userName, firstName, lastName
		ArrayList<String> userArray = new ArrayList<String>();
		for (HashMap<String, String> friendRequest : request
				.getPendingFriendRequests()) {
			if ((friendRequest.get("userName") != null)
					&& (friendRequest.get("firstName") != null)
					&& (friendRequest.get("lastName") != null))

				userArray.add(friendRequest.get("userName") + "\n"
						+ friendRequest.get("firstName") + " "
						+ friendRequest.get("lastName"));
		}
		String[] friendRequests = new String[userArray.size()];
		friendRequests = userArray.toArray(friendRequests);
		return friendRequests;
	}

	private class GetPendingFriendRequests extends
			AsyncTask<Void, Void, String[]> {
		protected String[] doInBackground(Void... params) {

			PendingFriendRequest pendingRequests = new PendingFriendRequest();
			FriendRequest rest = new FriendRequest(getActivity());

			try {
				pendingRequests = rest.getPendingFriendRequests();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pendingRequests.getPendingFriendRequests().size() > 0) {
				if (pendingRequests.isSuccess()) {
					return bindListView(pendingRequests);
				} else
					return new String[0];
			} else
				return new String[0];
		}

		public void onPostExecute(String[] requests) {

			if (getActivity() != null) {
				if (requests.length > 0) {
					listView.setAdapter(new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_list_item_1, requests));
				} else
					noPendingFriendRequestsTextView.setVisibility(1);
			}
		}
	}
}