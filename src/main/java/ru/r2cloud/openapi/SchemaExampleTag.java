package ru.r2cloud.openapi;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;

public class SchemaExampleTag extends TagSupport {

	private static final long serialVersionUID = -5848704381004159286L;

	private Schema<?> value;
	private OpenAPI openapi;

	@Override
	public int doEndTag() throws JspException {
		Writer w = pageContext.getOut();
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().setFieldNamingStrategy(NoUnderscoreFieldNaming.INSTANCE).create();
			w.append(gson.toJson(convertSchema(value)));
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	private Object convertSchema(Schema<?> schema) {
		if (schema == null) {
			return null;
		}
		if (schema.getType() != null && schema.getType().equals("array")) {
			ArraySchema arraySchema = (ArraySchema) schema;
			List<Object> result = new ArrayList<>();
			result.add(convertSchema(arraySchema.getItems()));
			return result;
		} else if (schema.get$ref() != null) {
			return convertModel(schema.get$ref());
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Object> convertModel(String ref) {
		Schema<?> schema = findByRef(ref);
		Map<String, Object> result = new HashMap<>();
		for (Entry<String, Schema> cur : schema.getProperties().entrySet()) {
			result.put(cur.getKey(), convertPrimitiveType(cur.getValue()));
		}
		return result;
	}

	private Object convertPrimitiveType(Schema<?> propSchema) {
		Object propValue;
		if (propSchema.get$ref() != null) {
			propValue = convertModel(propSchema.get$ref());
		} else if (propSchema.getType().equals("integer")) {
			if (propSchema.getExample() != null) {
				propValue = propSchema.getExample();
			} else {
				propValue = 0;
			}
		} else if (propSchema.getType().equals("boolean")) {
			propValue = false;
		} else if (propSchema.getType().equals("string")) {
			if (propSchema.getExample() != null) {
				propValue = propSchema.getExample();
			} else if (propSchema.getEnum() != null) {
				propValue = propSchema.getEnum().get(0);
			} else {
				propValue = "string";
			}
		} else if (propSchema.getType().equals("number")) {
			if (propSchema.getExample() != null) {
				propValue = propSchema.getExample();
			} else {
				propValue = 0.0;
			}
		} else if (propSchema.getType().equals("array")) {
			List<Object> array = new ArrayList<>();
			ArraySchema arraySchema = (ArraySchema) propSchema;
			array.add(convertPrimitiveType(arraySchema.getItems()));
			propValue = array;
		} else {
			propValue = "unknown";
		}
		return propValue;
	}

	private Schema<?> findByRef(String ref) {
		int index = ref.lastIndexOf('/');
		if (index == -1) {
			return null;
		}
		String schemaName = ref.substring(index + 1);
		return openapi.getComponents().getSchemas().get(schemaName);
	}

	public void setValue(Schema<?> value) {
		this.value = value;
	}

	public void setOpenapi(OpenAPI openapi) {
		this.openapi = openapi;
	}
}
