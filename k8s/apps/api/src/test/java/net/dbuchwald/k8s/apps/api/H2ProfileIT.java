package net.dbuchwald.k8s.apps.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("h2")
public class H2ProfileIT extends AbstractPersistenceIT {
}
