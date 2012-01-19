package org.basex.web.xquery;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.core.BaseXException;
import org.basex.query.QueryProcessor;
import org.basex.server.Query;
import org.basex.util.Util;
import org.basex.web.servlet.util.ResultPage;

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

    /** Do not construct me. */
    private BaseXContext() { /* void */
    }

    /**
     * Executes a query string after binding the request parameters.
     *
     * @throws IOException on error
     */
    public static void exec() throws IOException {

        try {
            getQuery().execute();
        } catch (BaseXException e) {
            Util.notexpected(e);
        }
    }

    /**
     * Prepares a query string and returns a Query object. Preparing includes: *
     * Setting ThreadLocales for {@link HttpServletRequest} and
     * {@link HttpServletResponse} * Setting the OutputStream of the
     * {@link QueryProcessor} to that of jetty://
     *
     * @param q query string
     * @param rp response
     * @param rq request
     * @param get parameters as string
     * @param post parameters as string
     * @throws IOException on error
     */
    public static void prepare(final String q, final HttpServletResponse rp,
            final HttpServletRequest rq, final String get, final String post)
            throws IOException {
        setReqResp(rp, rq);

        RESULT_PAGE.get().getSession().setOutputStream(rp.getOutputStream());
        final Query qu = RESULT_PAGE.get().getSession().query(q);
        RESULT_PAGE.get().setQuery(qu);
        bind(get, post, getReq().getSession(true).getId());
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
     * @throws IOException on error.
     */
    private static void bind(final String get, final String post,
            final String sess) throws IOException {
        RESULT_PAGE.get().getQuery().bind("SESSION", sess);
        RESULT_PAGE.get().getQuery().bind("GET", get, "json");
        RESULT_PAGE.get().getQuery().bind("POST", post, "json");
    }

    /**
     * Returns the response.
     * @return response
     */
    static HttpServletResponse getResp() {
        return RESULT_PAGE.get().getResp();
    }

    /**
     * Returns a ThreadLocal Query.
     *
     * @return Query Object
     */
    public static Query getQuery() {
        return RESULT_PAGE.get().getQuery();
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