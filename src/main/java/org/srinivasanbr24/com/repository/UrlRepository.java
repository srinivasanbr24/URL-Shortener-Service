package org.srinivasanbr24.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.srinivasanbr24.com.Model.UrlMapping;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, String> {
    Optional<UrlMapping> findByShortUrl(String shortUrl);
}
