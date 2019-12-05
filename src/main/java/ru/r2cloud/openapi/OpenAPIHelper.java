package ru.r2cloud.openapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

public class OpenAPIHelper {

	private static final String DEFAULT_BADGE = "badge-secondary";

	public static List<OperationDetails> getMethodsByTag(OpenAPI openapi, String tagName) {
		List<OperationDetails> result = new ArrayList<>();
		for (Entry<String, PathItem> cur : openapi.getPaths().entrySet()) {
			PathItem item = cur.getValue();
			for (Entry<String, Operation> curOperation : getOperationsByType(item).entrySet()) {
				if (curOperation.getValue().getTags().contains(tagName)) {
					OperationDetails detail = new OperationDetails();
					detail.setMethodName(curOperation.getKey());
					detail.setOperation(curOperation.getValue());
					detail.setName(cur.getKey());
					detail.setPath(item);
					result.add(detail);
				}
			}
		}
		return result;
	}

	public static String getColorByMethod(OperationDetails method) {
		if (Boolean.TRUE.equals(method.getOperation().getDeprecated())) {
			return DEFAULT_BADGE;
		}
		String methodName = method.getMethodName();
		if (methodName == null) {
			return DEFAULT_BADGE;
		}
		if (methodName.equals("post")) {
			return "badge-success";
		} else if (methodName.equals("get")) {
			return "badge-primary";
		} else if (methodName.equals("put")) {
			return "badge-warning";
		} else if (methodName.equals("delete")) {
			return "badge-danger";
		} else if (methodName.equals("trace")) {
			return "badge-dark";
		} else if (methodName.equals("patch")) {
			return "badge-info";
		} else {
			return DEFAULT_BADGE;
		}
	}

	private static Map<String, Operation> getOperationsByType(PathItem item) {
		Map<String, Operation> result = new HashMap<>();
		if (item.getGet() != null) {
			result.put("get", item.getGet());
		}
		if (item.getPut() != null) {
			result.put("put", item.getPut());
		}
		if (item.getHead() != null) {
			result.put("head", item.getHead());
		}
		if (item.getPost() != null) {
			result.put("post", item.getPost());
		}
		if (item.getDelete() != null) {
			result.put("delete", item.getDelete());
		}
		if (item.getPatch() != null) {
			result.put("patch", item.getPatch());
		}
		if (item.getOptions() != null) {
			result.put("options", item.getOptions());
		}
		if (item.getTrace() != null) {
			result.put("trace", item.getTrace());
		}
		return result;
	}

	private OpenAPIHelper() {
		// do nothing
	}
}
