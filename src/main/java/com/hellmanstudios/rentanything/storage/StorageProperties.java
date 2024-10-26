package com.hellmanstudios.rentanything.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

	private String location = "uploads";

	public String getLocation() {

		String homeDir = System.getenv("HOME");

		return homeDir + "/" + location;

	}

	public void setLocation(String location) {
		this.location = location;
	}

}