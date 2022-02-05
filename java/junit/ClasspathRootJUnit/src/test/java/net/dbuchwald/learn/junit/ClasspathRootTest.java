package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClasspathRootTest {

    @Test
    @DisplayName("classpath root should be correctly identified")
    void classpathRootShouldBeCorrectlyIdentified() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:");
        URL rootURL = resource.getURL();
        System.out.println(rootURL);
        assertTrue(true);
    }
}
