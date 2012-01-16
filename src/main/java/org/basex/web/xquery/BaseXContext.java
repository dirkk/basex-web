package org.basex.web.xquery;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.core.BaseXException;
import org.basex.server.Query;
import org.basex.server.Session;
import org.basex.web.servlet.util.ResultPage;
import org.basex.web.session.SessionFactory;

/**
 * Provides static methods to access BaseX.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public final class BaseXContext {
    /** Thread local resultpage. */
    private static final ThreadLocal<ResultPage> RESULT_PAGE =
    new ThreadLocal<ResultPage>() {
        @Override
        protected ResultPage initialValue() {
            return ResultPage.getEmpty();
        }
    };

    /** Session. */
    static final Session SESS = SessionFactory.get();

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
     * @return the query result.
     * @throws IOException on error
     */
    public static synchronized ResultPage exec(final String qu,
            final String get, final String post, final HttpServletResponse rp,
            final HttpServletRequest rq) throws IOException {

        setReqResp(rp, rq);
        try {
            final Query q = SESS.query(qu);

            bind(get, post, rq.getSession(true).getId(), q);

            RESULT_PAGE.get().setBody(q.execute());
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
        return new ResultPage("<div class=\"error\">" + e.getMessage()
                + "</div>", rp, rq);
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