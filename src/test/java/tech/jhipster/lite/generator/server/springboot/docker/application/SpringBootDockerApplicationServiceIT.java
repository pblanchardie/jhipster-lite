package tech.jhipster.lite.generator.server.springboot.docker.application;

import static tech.jhipster.lite.TestUtils.*;
import static tech.jhipster.lite.generator.project.domain.Constants.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.jhipster.lite.IntegrationTest;
import tech.jhipster.lite.generator.project.domain.Project;
import tech.jhipster.lite.module.infrastructure.secondary.TestJHipsterModules;

@IntegrationTest
class SpringBootDockerApplicationServiceIT {

  @Autowired
  private SpringBootDockerApplicationService springBootDockerApplicationService;

  @Test
  void shouldAddJib() {
    Project project = tmpProject();
    TestJHipsterModules.applyInit(project);
    TestJHipsterModules.applyMaven(project);

    springBootDockerApplicationService.addJib(project);

    assertFileExist(project, "src/main/docker/jib/entrypoint.sh");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.version>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.version>");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.image>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.image>");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.architecture>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.architecture>");

    assertFileContent(project, POM_XML, mavenJibPlugin(project));
  }

  @Test
  void shouldAddJibFiles() {
    Project project = tmpProject();
    TestJHipsterModules.applyInit(project);
    TestJHipsterModules.applyMaven(project);

    springBootDockerApplicationService.addJibFiles(project);

    assertFileExist(project, "src/main/docker/jib/entrypoint.sh");

    assertFileContent(project, "src/main/docker/jib/entrypoint.sh", "com.mycompany.myapp.JhipsterApp");
  }

  @Test
  void shouldAddJibPlugin() {
    Project project = tmpProject();
    TestJHipsterModules.applyInit(project);
    TestJHipsterModules.applyMaven(project);

    springBootDockerApplicationService.addJibPlugin(project);

    assertFileContent(project, POM_XML, "<jib-maven-plugin.version>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.version>");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.image>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.image>");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.architecture>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.architecture>");

    assertFileContent(project, POM_XML, mavenJibPlugin(project));
  }

  @Test
  void shouldAddDockerfile() {
    Project project = tmpProject();
    TestJHipsterModules.applyInit(project);

    springBootDockerApplicationService.addDockerfile(project);

    assertFileExist(project, "Dockerfile");
    assertFileContent(project, "Dockerfile", "EXPOSE 8080");
  }

  @Test
  void shouldAddDockerfileWithDifferentPort() {
    Project project = tmpProject();
    project.addConfig("serverPort", 7419);
    TestJHipsterModules.applyInit(project);

    springBootDockerApplicationService.addDockerfile(project);

    assertFileExist(project, "Dockerfile");
    assertFileContent(project, "Dockerfile", "EXPOSE 7419");
  }

  private List<String> mavenJibPlugin(Project project) {
    return List.of(
      "<plugin>",
      "<groupId>com.google.cloud.tools</groupId>",
      "<artifactId>jib-maven-plugin</artifactId>",
      "<version>${jib-maven-plugin.version}</version>",
      "<configuration>",
      "<from>",
      "<image>${jib-maven-plugin.image}</image>",
      "<platforms>",
      "<platform>",
      "<architecture>${jib-maven-plugin.architecture}</architecture>",
      "<os>linux</os>",
      "</platform>",
      "</platforms>",
      "</from>",
      "<to>",
      "<image>" + project.getBaseName().orElse("jhipster") + ":latest</image>",
      "</to>",
      "<container>",
      "<entrypoint>",
      "<shell>bash</shell>",
      "<option>-c</option>",
      "<arg>/entrypoint.sh</arg>",
      "</entrypoint>",
      "<ports>",
      "<port>" + project.getServerPort() + "</port>",
      "</ports>",
      "<environment>",
      "<SPRING_OUTPUT_ANSI_ENABLED>ALWAYS</SPRING_OUTPUT_ANSI_ENABLED>",
      "<JHIPSTER_SLEEP>0</JHIPSTER_SLEEP>",
      "</environment>",
      "<creationTime>USE_CURRENT_TIMESTAMP</creationTime>",
      "<user>1000</user>",
      "</container>",
      "<extraDirectories>",
      "<paths>src/main/docker/jib</paths>",
      "<permissions>",
      "<permission>",
      "<file>/entrypoint.sh</file>",
      "<mode>755</mode>",
      "</permission>",
      "</permissions>",
      "</extraDirectories>",
      "</configuration>",
      "</plugin>"
    );
  }
}
