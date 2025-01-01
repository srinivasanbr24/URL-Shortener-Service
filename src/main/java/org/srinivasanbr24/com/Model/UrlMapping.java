package org.srinivasanbr24.com.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table (name = "URLMapping")
public class UrlMapping {
    @Id
    private String id = UUID.randomUUID().toString();
    private String shortUrl;
    private String longUrl;

}
