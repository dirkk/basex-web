package org.basex.web.xquery;

import static org.basex.data.DataText.*;
import static org.basex.io.MimeTypes.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.core.BaseXException;
import org.basex.core.Prop;
import org.basex.core.cmd.Set;
import org.basex.io.out.ArrayOutput;
import org.basex.io.serial.SerializerProp;
import org.basex.server.LocalSession;
import org.basex.server.Query;
import org.basex.util.Token;
import org.basex.util.TokenBuilder;
import org.basex.util.Util;
import org.basex.web.servlet.util.ResultPage;
import org.basex.web.session.SessionFactory;

/**
 * Provides static methods to access BaseX.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public final class BaseXContext {
    /**
     * Default Output Method, set via Maven
     */
    final static String defaultOutputMethod = System.getProperty("org.basex.web.default.outputmethod") == null ?
           "xhtml" : System.getProperty("org.basex.web.default.outputmethod");

    /** Thread local resultpage. */
    private static final ThreadLocal<ResultPage> RESULT_PAGE =
    new ThreadLocal<ResultPage>() {
        @Override
        protected ResultPage initialValue() {
            return ResultPage.getEmpty();
        }
    };
    /** A per Thread Session. */
    private static final ThreadLocal<LocalSession> SESS =
    new ThreadLocal<LocalSession>() {
        @Override
        protected LocalSession initialValue() {
            final LocalSession ls = new LocalSession(SessionFactory.get());
            final TokenBuilder tb = new TokenBuilder();
            tb.addExt(SerializerProp.S_METHOD[0]).add("=")
                .add(defaultOutputMethod);
            try {
                ls.execute(new Set(Prop.SERIALIZER,tb ));
            } catch (IOException e) {
                Util.notexpected(e);
            }
            return ls;
        }
    };


    /** Do not construct me. */
    private BaseXContext() { /* void */
    }



    /**
     * Executes a query string.
     *
     * @param qu query string
     * @param get GET map
     * @param post POST map
     * @param rp response object
     * @param rq request object
     * @param buff the result buffer
     * @return the query result.
     * @throws IOException on error
     */
    public static synchronized ResultPage exec(final String qu,
            final String get, final String post, final HttpServletResponse rp,
            final HttpServletRequest rq, final ArrayOutput buff) throws IOException {

        setReqResp(rp, rq);
        try {
            SESS.get().setOutputStream(buff);
            final Query q = SESS.get().query(qu);
            bind(get, post, rq.getSession(true).getId(), q);

            q.execute();
            initResponse(new SerializerProp(q.options()), rp);

            assert null != RESULT_PAGE.get().getBody() :
                "Query Result must not be ''";
            return RESULT_PAGE.get();
        } catch (BaseXException e) {
            return err(rp, rq, e);
        }
    }

    /**
     * Set Request/Response.
     *
     * @param rp response
     * @param rq request
     */
    private static void setReqResp(final HttpServletResponse rp,
            final HttpServletRequest rq) {
        RESULT_PAGE.get().setReq(rq);
        RESULT_PAGE.get().setResp(rp);
    }

    /**
     * Binds the GET & POST Parameters. Binds the SESSION ID to $SESSION
     *
     * @param get get
     * @param post post
     * @param sess session id
     * @param q the query
     * @throws IOException on error.
     */
    private static void bind(final String get, final String post,
            final String sess, final Query q) throws IOException {
        q.bind("SESSION", sess);
        q.bind("GET", get, "json");
        q.bind("POST", post, "json");
    }

    /**
     * Returns a ResultPage containing an Error Message.
     *
     * @param rp response
     * @param rq request
     * @param e error
     * @return ResultPage with error message
     */
    private static ResultPage err(final HttpServletResponse rp,
            final HttpServletRequest rq, final Exception e) {
        System.err.println(e.getMessage());
        return new ResultPage("<div class=\"error\">" + e.getMessage()
                + "</div>", rp, rq);
    }


    /**
     * Sets the default output method and encoding.
     * N.B. contrary do the default behaviour basex-web uses M_XHTML if nothing
     *      else is specified.
     * N.B.2. the XHTML mime type is set according to rfc3236
     * @param sprop Serializer Properties
     * @param rp the response object
     */
    static void  initResponse(final SerializerProp sprop,
            final HttpServletResponse rp) {
      // set encoding *TODO* set only when not raw
      rp.setCharacterEncoding(sprop.get(SerializerProp.S_ENCODING));

      // set content type
      String type = sprop.get(SerializerProp.S_MEDIA_TYPE);
      if(type.isEmpty()) {
        // determine content type dependent on output method
        final String method = sprop.get(SerializerProp.S_METHOD);
        if(method.equals(M_RAW)) {
          type = APP_OCTET;
        }
        else if(method.equals(M_TEXT)){
            type = "text/plain";
        }
        else if(method.equals(M_XML)) {
            type= APP_XML;
        } else if(Token.eq(method, M_JSON, M_JSONML)) {
          type = APP_JSON;
        } else if(Token.eq(method, M_HTML)) {
          type = TEXT_HTML;
        } else if(Token.eq(method, M_XHTML)) {
          type= "application/xhtml+xml";
        }
      }
      rp.setContentType(type);
    }

    /**
     * Returns the response.
     *
     * @return response
     */
    static HttpServletResponse getResp() {
        return RESULT_PAGE.get().getResp();
    }

    /**
     * Returns the request object.
     *
     * @return request
     */
    static HttpServletRequest getReq() {
        return RESULT_PAGE.get().getReq();
    }

}