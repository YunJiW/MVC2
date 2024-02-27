package hello.itemservice.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME ="mySessionId";

    //동시 요청을 위한 ConcurrentHashMap 사용
    private Map<String,Object> sessionStore = new ConcurrentHashMap<>();


    /**
     *
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response){

        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);


        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);

        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie == null){
            return null;
        }


        return sessionStore.get(sessionCookie.getValue());
    }


    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie sessonCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessonCookie != null){
            sessionStore.remove(sessonCookie.getValue());
        }

    }

    public Cookie findCookie(HttpServletRequest request,String CookieName){
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(CookieName))
                .findAny()
                .orElse(null);
    }
}
