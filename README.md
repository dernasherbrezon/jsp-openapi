# jsp-openapi [![Build Status](https://travis-ci.com/dernasherbrezon/jsp-openapi.svg?branch=master)](https://travis-ci.com/dernasherbrezon/jsp-openapi) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ru.r2cloud.openapi%3Ajsp-openapi&metric=alert_status)](https://sonarcloud.io/dashboard?id=ru.r2cloud.openapi%3Ajsp-openapi)

Tag lib for rendering openapi specification. CSS and javascript libraries are not included.

* "bootstrap4-openapi". Render using bootstrap4.

## Usage

For a complete reference see the [test](https://github.com/dernasherbrezon/jsp-openapi/blob/master/src/test/resources/webapp/index.jsp).

### Step by step guide

Include dependency:

```xml
<dependency>
	<groupId>ru.r2cloud.openapi</groupId>
	<artifactId>jsp-openapi</artifactId>
	<version>1.0</version>
</dependency>
```

Setup controller (for example Spring MVC):

```java
@RequestMapping("/api")
public ModelAndView load() throws Exception {
	OpenAPI openapi = new OpenAPIV3Parser().read(LoadApi.class.getClassLoader().getResource("openapi.json").getFile());
	Map<String, Object> model = new HashMap<String, Object>();
	model.put("entity", openapi);
	return new ModelAndView("api", model);
}
```

> Note: if API specification cannot be changed in runtime, then it is better to cache it on startup.

> Note: io.swagger.v3.parser.OpenAPIV3Parser is a swagger parser for v3 specification. You can implement your own parser or use any compatible. Parser should be able to construct io.swagger.v3.oas.models.OpenAPI model. 

Configure taglib:

```
<%@ taglib prefix="openapi" uri="https://github.com/dernasherbrezon/jsp-openapi" %>
```

Use on the page:

```html
<body>
	<div class="container">
		<openapi:bootstrap4-openapi openapi="${entity}"/>
	</div>
</body>
```
