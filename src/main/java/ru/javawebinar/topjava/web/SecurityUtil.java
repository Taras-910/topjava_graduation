package ru.javawebinar.topjava.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.javawebinar.topjava.AuthorizedUser;

import static java.util.Objects.requireNonNull;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        return requireNonNull(safeGet(), "No authorized user found");
    }

    /*    public static int authUserId() {
           int authId = get().getUser().id();
           return  authId;
       }*/

    public static int authUserId() {
        return START_SEQ;
    }
}
