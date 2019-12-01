package ru.r2cloud.openapi;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.v3.oas.models.media.Schema;

public class SchemaTag extends TagSupport {

	private static final long serialVersionUID = -5848704381004159285L;

	private Schema<?> value;

	@Override
	public int doEndTag() throws JspException {
		Writer w = pageContext.getOut();
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().setFieldNamingStrategy(NoUnderscoreFieldNaming.INSTANCE).create();
			w.append(gson.toJson(value.getProperties()));
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	public void setValue(Schema<?> value) {
		this.value = value;
	}

}
