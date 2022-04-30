package net.dbuchwald.k8s.apps.api;

import net.dbuchwald.k8s.apps.api.models.Greeting;
import net.dbuchwald.k8s.apps.api.models.GreetingId;
import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.models.constants.GreetingCode;
import net.dbuchwald.k8s.apps.api.models.constants.LanguageIdentifier;
import net.dbuchwald.k8s.apps.api.services.ReferenceDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("derby")
public class DerbyProfileIT extends AbstractPersistenceIT{
}
