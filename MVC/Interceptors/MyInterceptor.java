Interceptor implementujący `preHandle` dla wszystkich odwołań REST; dane z interceptora do kontrolera przekazywane przez 
statyczne pola i metody UserContext (w ramach parametrów ThreadLocal). 



@Component
public class MyInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ss = request.getParameter("pass");
        if (ss==null) return true;
        if (ss.equals("111")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        //interceptor passes value of UserContext.USERID to all controllers (readable by static methods/fields of UserContext)
        if (ss.equals("222")) {
            UserContext.USERID.set(222);
        }
//        request.setAttribute("token", UUID.randomUUID()); //niewiele pomaga
        return true;
    }
}
public class UserContext {
    public static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();
    public static final ThreadLocal<Integer> USERID = new ThreadLocal<>();
}



//-----------------------------------
Usage (Controller):
 @RequestMapping(value = {"/grupy/test"})
    @ResponseBody
    public Rest testRequestBody(@RequestBody Tuu t
    ) {
        Rest r = new Rest();
        Integer passedId = UserContext.USERID.get();
        if (passedId!=null && passedId.equals(222)) t.setY(222);
        r.setResult(t);
        return r;
    }
//--------------------------------------
Call (angularJS):
  grupySrv.testIt = function (xx, yy, pas) {
            var rootUrl = 'http://localhost:8090/GrupyService/';
            var tuu = {
                x:xx,
                y:yy
            };
            alert('!!');
            return $http({
                url: rootUrl + 'grupy/test',
                method: 'POST',
                params: {pass: pas},
                data: tuu
            });
        };
