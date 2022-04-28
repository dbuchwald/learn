package net.dbuchwald.k8s.apps.api.repositories;

import net.dbuchwald.k8s.apps.api.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
}
