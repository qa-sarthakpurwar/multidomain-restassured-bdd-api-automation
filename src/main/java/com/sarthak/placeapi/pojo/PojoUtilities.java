package com.sarthak.placeapi.pojo;

import java.util.ArrayList;
import java.util.List;

public class PojoUtilities {

	public AddPlace addPlacePayload(String name, String address, String language) {
		AddPlace addPlaceObject = new AddPlace();

		Location locationObject = new Location();
		locationObject.setLat(-38.383494);
		locationObject.setLng(33.427362);

		addPlaceObject.setLocation(locationObject);
		addPlaceObject.setAccuracy(50);
		addPlaceObject.setName(name);
		addPlaceObject.setPhone_number("(+91) 983 893 3937");
		addPlaceObject.setAddress(address);

		List<String> typesList = new ArrayList<String>();
		typesList.add("shoe park");
		typesList.add("shop");

		addPlaceObject.setTypes(typesList);
		addPlaceObject.setWebsite("http://google.com");
		addPlaceObject.setLanguage(language);

		return addPlaceObject;
	}

	public UpdatePlace updatePlacePayload(String place_id, String address, String key) {
		UpdatePlace updatePlaceObject = new UpdatePlace();

		updatePlaceObject.setPlace_id(place_id);
		updatePlaceObject.setAddress(address);
		updatePlaceObject.setKey(key);

		return updatePlaceObject;
	}

	public DeletePlace deletePlacePayload(String place_id) {
		DeletePlace deletePlaceObject = new DeletePlace();
		deletePlaceObject.setPlace_id(place_id);
		return deletePlaceObject;
	}

}
