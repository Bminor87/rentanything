package com.hellmanstudios.rentanything.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

	@Value("${UPLOAD_DIR:uploads}")
	private String location;

	public String getLocation() {

		String homeDir = System.getenv("HOME");

		return homeDir + "/" + location;

	}

	public void setLocation(String location) {
		this.location = location;
	}

}