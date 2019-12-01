package ru.r2cloud.openapi;

import java.lang.reflect.Field;

import com.google.gson.FieldNamingStrategy;

public class NoUnderscoreFieldNaming implements FieldNamingStrategy {

	public static final NoUnderscoreFieldNaming INSTANCE = new NoUnderscoreFieldNaming();

	@Override
	public String translateName(Field f) {
		if (f.getName().startsWith("_")) {
			return f.getName().substring(1);
		}
		return f.getName();
	}

}
