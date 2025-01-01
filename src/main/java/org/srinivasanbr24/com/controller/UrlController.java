package org.srinivasanbr24.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import org.srinivasanbr24.com.exception.UrlShortenerException;
import org.srinivasanbr24.com.service.UrlShortenerService;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String longUrl) {
        if(!StringUtils.hasText(longUrl) || !StringUtils.hasLength(longUrl)
                || !StringUtils.trimAllWhitespace(longUrl).isEmpty()){
            throw new RuntimeException("Please enter valid inputs");
        }
        try {
            return service.shortenUrl(longUrl);
        } catch (UrlShortenerException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirectToLongUrl(@PathVariable String shortUrl) {
        String longUrl = service.getLongUrl(shortUrl);
        if(longUrl == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found");
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(longUrl);
        return redirectView;
    }
}

