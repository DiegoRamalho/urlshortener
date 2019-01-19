package ie.com.urlshortener.api.rest.dto;


import java.io.Serializable;
import java.time.Instant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The DTO for the ShortURL entity.
 *
 * @author Diego
 * @since 19/01/2019
 */
@ApiModel(description = "DTO that represents the Short Url.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortURLDTO implements Serializable {

    private static final long serialVersionUID = 971630144527985490L;
    @ApiModelProperty("Entity Identifier")
    private String id;
    @ApiModelProperty("Date that the entity was created")
    private Instant createdDate;
    @ApiModelProperty("Last date that the entity was modified")
    private Instant lastModifiedDate;
    @ApiModelProperty("Code of the shortened URL")
    private String code;
    @ApiModelProperty("Long URL")
    private String url;
}
