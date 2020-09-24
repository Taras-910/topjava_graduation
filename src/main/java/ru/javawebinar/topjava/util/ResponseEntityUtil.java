package ru.javawebinar.topjava.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.net.URI;

public class ResponseEntityUtil {
    public static <T extends AbstractBaseEntity> ResponseEntity<T> getResponseEntity(T object  , String url) {
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(url + "/{id}")
                    .buildAndExpand(object.getId())
                    .toUri();
        ResponseEntity<T> responseEntity = ResponseEntity.created(uriOfNewResource).body(object);
        return responseEntity;
    }
}
