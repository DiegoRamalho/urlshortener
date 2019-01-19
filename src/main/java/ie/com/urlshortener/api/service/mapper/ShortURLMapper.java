package ie.com.urlshortener.api.service.mapper;

import org.mapstruct.Mapper;

import ie.com.urlshortener.api.domain.ShortURL;
import ie.com.urlshortener.api.rest.dto.ShortURLDTO;

/**
 * Mapper for the entity ShortURL and its DTO.
 *
 * @author Diego
 * @since 19/01/2019
 */
@Mapper(componentModel = "spring")
public interface ShortURLMapper extends EntityMapper<ShortURLDTO, ShortURL> {

}
