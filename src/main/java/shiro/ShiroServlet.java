package shiro;

import FinderStrategies.BigIntFinder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import servlet.Finder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

/**
 * Servlet finds Primes using a Finder in a thread and generates the html code
 * Author: Ari Michael Ayvazyan
 * Date: 14.03.14
 */
public class ShiroServlet extends HttpServlet {
    private Finder finder;
    private Date startDate;

    /**
     * @see javax.servlet.http.HttpServlet
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.sendRedirect("/search");
        /*response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("Do a Get request!");*/
        doGet(request, response);
    }

    /**
     * @see javax.servlet.http.HttpServlet
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.sendRedirect("/primes/searcher");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            //collect user principals and credentials in a gui specific manner
            //such as username/password html form, X509 certificate, OpenID, etc.
            //We'll use the username/password example here since it is the most common.
            //(do you know what movie this is from? ;)
            UsernamePasswordToken token = new UsernamePasswordToken(request.getParameter("login"), request.getParameter("password"));
            //this is all you have to do to support 'remember me' (no config - built in!):
            token.setRememberMe(true);
            try {
                currentUser.login(token);
//                if no exception, that's it, we're done!
            } catch (UnknownAccountException uae) {
                //username wasn't in the system, show them an error message?
            } catch (IncorrectCredentialsException ice) {
                //password didn't match, try again?
            } catch (LockedAccountException lae) {
                //account for that username is locked - can't login.  Show them a message?
            } catch (AuthenticationException ae) {
                //unexpected condition - error?
            }
        }
        System.out.println("User [" + currentUser.getPrincipal() + "] logged in successfully.");


        //This line is Important to generate a Valid HTML Form
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \n" +
                "  \"http://www.w3.org/TR/html4/loose.dtd\">");
        out.println("<html><head><title></title></head><body>");
        //this is where the fun part starts
        //Login form
        out.println("<h1>Login to Web App</h1>\n" +
                        "      <form method=\"post\">\n" +
                        "        <p><input type=\"text\" name=\"login\" value=\"\" placeholder=\"Username\"></p>\n" +
                        "        <p><input type=\"password\" name=\"password\" value=\"\" placeholder=\"Password\"></p>\n" +
                        "        <p class=\"submit\"><input type=\"submit\" name=\"commit\" value=\"Login\"></p>\n" +
                        "      </form>"
        );


        //The title
        out.println("<div align=\"center\"><h1>Prime Searcher" + "</h1>");
        //The upper border
        out.println("<hr style=\"color:blue; background-color:blue; height:15px; width:80%;\">");
        //out.println("Found: "+this.finder.getCounter()+" Primes <br>");
        if (currentUser.isPermitted("primeSearcher:view")) {
            //The start date
            out.println("Started at " + this.startDate.toString() + "<br/>");
            out.println("<h1>"+"Hello " + currentUser.getPrincipal() + "<br></h1>");
            out.println("The last prime discovered was " + this.finder.getLastPrime().toString() + " at " + new Date(System.currentTimeMillis()).toString() + " <br> ");

        } else {
            out.println("<h1>You are not worthy to view the prime counter.<br></h1>");
        }
        //Another Border
        out.println("<hr style=\"color:blue; background-color:blue; height:15px; width:80%;\">");
        //Closing open Tags and the center div
        out.println("</div></body></html>");
        currentUser.logout();
    }

    /**
     * This Function gets called when this servlet gets initialized.
     * Using Tomcat it gets called at the first request Sent to it.
     *
     * @see javax.servlet.http.HttpServlet
     */
    public void init() throws ServletException {
        //
        // Initializing Shiro
        //

        // 1.
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        // 2.
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();

        // 3.
        SecurityUtils.setSecurityManager(securityManager);

        //
        // Done
        //
        this.startDate = new Date(System.currentTimeMillis());
        super.init();
        this.finder = new Finder(new BigIntFinder());
        Thread t = new Thread(this.finder);
        t.start();
        String[] args = {};
    }

    /**
     * @see javax.servlet.http.HttpServlet
     */
    @Override
    public void destroy() {
        this.finder.stop();
        super.destroy();
    }
}
