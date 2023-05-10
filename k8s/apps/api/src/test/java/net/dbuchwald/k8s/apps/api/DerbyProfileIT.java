package net.dbuchwald.k8s.apps.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("derby")
public class DerbyProfileIT extends AbstractPersistenceIT{
}
