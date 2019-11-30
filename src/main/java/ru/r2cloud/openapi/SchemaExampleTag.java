package ru.r2cloud.openapi;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

public class SchemaExampleTag extends TagSupport {

	private static final long serialVersionUID = -5848704381004159286L;

	private Schema<?> value; 
	private OpenAPI openapi;
	
	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().print("this is test: " + value);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	public void setValue(Schema<?> value) {
		this.value = value;
	}
	
	public void setOpenapi(OpenAPI openapi) {
		this.openapi = openapi;
	}
}
