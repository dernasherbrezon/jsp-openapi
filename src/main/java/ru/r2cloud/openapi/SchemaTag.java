package ru.r2cloud.openapi;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.v3.oas.models.media.Schema;

public class SchemaTag extends TagSupport {

	private static final long serialVersionUID = -5848704381004159285L;

	private Schema<?> value;
	
	@Override
	public int doEndTag() throws JspException {
		Writer w = pageContext.getOut();
		try {
			JsonFactory jsonFactory = new JsonFactory();
			jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
			ObjectMapper mapper = new ObjectMapper(jsonFactory);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.writeValue(w, value.getProperties());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	public void setValue(Schema<?> value) {
		this.value = value;
	}

}
