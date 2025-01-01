package org.srinivasanbr24.com.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srinivasanbr24.com.Model.UrlMapping;
import org.srinivasanbr24.com.exception.UrlShortenerException;
import org.srinivasanbr24.com.repository.UrlRepository;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlShortenerService {

   @Autowired
   UrlRepository repository;

    private static final String SHA256 = "SHA-256";
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Transactional
    public String shortenUrl(String longUrl) throws UrlShortenerException {
    String shortUrl = generateUniqueKey(longUrl);
        Optional<UrlMapping> existShortUrl = repository.findByShortUrl(shortUrl);
        if(existShortUrl.isPresent()){
            shortUrl = generateUniqueKey(longUrl+ UUID.randomUUID().toString());
        }
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setLongUrl(longUrl);
        repository.save(urlMapping);
        return shortUrl;
    }

    public static String generateUniqueKey(String s) throws UrlShortenerException {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA256);
            byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
             return bytesToBase62(hash).substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            throw new UrlShortenerException(e.getMessage());
        }
    }

    private static String bytesToBase62(byte[] bytes) {
        BigInteger number = new BigInteger(1, bytes);
        StringBuilder base62 = new StringBuilder();
        while (number.compareTo(BigInteger.ZERO) > 0) {
            base62.insert(0, BASE62.charAt(number.mod(BigInteger.valueOf(62)).intValue()));
            number = number.divide(BigInteger.valueOf(62));
        }
        return base62.toString();
    }

    public String getLongUrl(String url) {
        Optional<UrlMapping> longUrl =repository.findByShortUrl(url);
        return longUrl.map(UrlMapping::getLongUrl).orElse(null);
    }

}
