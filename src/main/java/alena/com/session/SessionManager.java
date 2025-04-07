package alena.com.session;
import alena.com.dto.RequestResponseCol;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class SessionManager {
    private static final Map<String, RequestResponseCol> sessionStore = new ConcurrentHashMap<>();
    public static void storeSession(String sessionId, RequestResponseCol reqres) {
        sessionStore.put(sessionId, reqres);
    }
    public static RequestResponseCol getSession(String sessionId) {
        return sessionStore.get(sessionId);
    }
    public static void removeSession(String sessionId) {
        sessionStore.remove(sessionId);
    }
}