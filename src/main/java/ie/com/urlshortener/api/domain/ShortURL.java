package ie.com.urlshortener.api.domain;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Short URL entity.
 *
 * @author Diego
 * @since 19/01/2019
 */
@Document(collection = "shorten_url")
@Data
@NoArgsConstructor
public class ShortURL implements Serializable {

    private static final long serialVersionUID = 971630144527985490L;

    /**
     * The Instant that the entity was created.
     */
    @CreatedDate
    @Field("created_date")
    private Instant createdDate;

    /**
     * The last Instant that the entity was modified.
     */
    @LastModifiedDate
    @Field("last_modified_date")
    private Instant lastModifiedDate;

    /**
     * The entity identifier.
     */
    @Id
    private String id;

    /**
     * Code of the shortened URL.
     */
    @Field("code")
    private String code;

    /**
     * Long URL.
     */
    @Field("url")
    private String url;
}
