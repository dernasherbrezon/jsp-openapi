package ru.r2cloud.openapi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.jetty.http.HttpTester;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.JarResource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;

@RunWith(Parameterized.class)
public class Bootstrap4OpenapiTest {

	private Server server;
	private LocalConnector localConnector;
	private OpenAPI openapi;
	private final String pageName;

	public Bootstrap4OpenapiTest(String pageName) {
		this.pageName = pageName;
	}

	@Test
	public void testRendering() throws Exception {
		HttpTester.Request request = HttpTester.newRequest();
		HttpTester.Response response;

		request.setMethod("GET");
		request.setVersion("HTTP/1.0");
		request.setHeader("Host", "tester");
		request.setURI("/" + pageName + ".jsp");
		response = HttpTester.parseResponse(HttpTester.from(localConnector.getResponse(request.generate())));

		try (BufferedWriter w = new BufferedWriter(new FileWriter("./src/test/resources/expected/" + pageName + ".html"))) {
			w.write(response.getContent());
		}
		// StringBuilder expected = new StringBuilder();
		// try (BufferedReader r = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/expected/" + pageName + ".txt")))) {
		// String curLine = null;
		// while ((curLine = r.readLine()) != null) {
		// expected.append(curLine).append("\n");
		// }
		// }
		// assertEquals(expected.toString().trim(), response.getContent());
	}

	@Before
	public void setUp() throws Exception {
		openapi = new OpenAPIV3Parser().read(Bootstrap4OpenapiTest.class.getClassLoader().getResource("openapi.json").getFile());

		ResourceCollection resources = new ResourceCollection(new String[] { "./src/test/resources/webapp", "./src/main/resources" });

		Locale.setDefault(Locale.ENGLISH);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		server = new Server();
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setBaseResource(resources);

		webapp.getMetaData().addWebInfJar(JarResource.newResource(new File("./src/main/resources/")));
		webapp.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/.*jsp-api-[^/]*\\.jar$|.*/.*jstl-[^/]*\\.jar$|.*/.*taglibs[^/]*\\.jar$");
		webapp.setAttribute("entity", openapi);
		Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
		classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.webapp.MetaInfConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");
		server.setHandler(webapp);
		server.start();
		localConnector = new LocalConnector(server);
		localConnector.start();
	}

	@After
	public void tearDown() throws Exception {
		if (localConnector != null) {
			localConnector.stop();
		}
		if (server != null) {
			server.stop();
		}
	}

	@Parameters
	public static Collection<Object[]> data() {
		File[] f = new File("./src/test/resources/webapp").listFiles();
		Collection<Object[]> result = new ArrayList<>();
		for (File cur : f) {
			if (!cur.isFile()) {
				continue;
			}
			result.add(new Object[] { getFileNameWithoutExtension(cur) });
		}
		return result;
	}

	private static String getFileNameWithoutExtension(File f) {
		int index = f.getName().lastIndexOf('.');
		if (index == -1) {
			return f.getName();
		}
		return f.getName().substring(0, index);
	}
}
