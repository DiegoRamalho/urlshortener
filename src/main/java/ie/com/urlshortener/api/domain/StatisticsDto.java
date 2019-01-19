package ie.com.urlshortener.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Diego
 * @since 19/01/2019
 */
@Data
@NoArgsConstructor
public class StatisticsDto {

    private String key;
    private Long value;
}
