package springboot.aviation;

import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DockerConnectionTest {

    @Test
    void testDockerIsAvailable() {

        boolean dockerAvailable = DockerClientFactory.instance().isDockerAvailable();
        System.out.println(">>> RESULTADO COM API 1.44: " + dockerAvailable);
        assertTrue(dockerAvailable);
    }
}