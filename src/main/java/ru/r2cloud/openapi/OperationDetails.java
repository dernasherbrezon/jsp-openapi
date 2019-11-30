package ru.r2cloud.openapi;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

public class OperationDetails {

	private String name;
	private Operation operation;
	private PathItem path;
	private String methodName;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public PathItem getPath() {
		return path;
	}

	public void setPath(PathItem path) {
		this.path = path;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
