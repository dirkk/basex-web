package org.basex.web.servlet.impl;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.io.IO;
import org.basex.io.in.TextInput;
import org.basex.io.out.ArrayOutput;
import org.basex.util.Token;
import org.basex.web.servlet.PrepareParamsServlet;
import org.basex.web.servlet.util.ResultPage;
import org.basex.web.xquery.BaseXContext;
import org.eclipse.jetty.http.HttpException;

import com.google.common.base.Objects;

/**
 * Handles all that fancy MVC stuff.
 *
 * @author Michael Seiferle, <ms@basex.org>
 */
public class Xails extends PrepareParamsServlet {

    /** XQuery controllers/action.xq in charge. */
    private File view;
    /** XQuery controllers/action.xq in charge. */
    private File controller;

    @Override
    public void get(final HttpServletResponse response,
            final HttpServletRequest req, final File f, final String get,
            final String post) throws IOException {
        // TODO Auto-generated method stub

        initMVC(req);

        final ArrayOutput buff = new ArrayOutput();
        buildResult(response, req, get, post, buff);
        assert buff.size() > 0;
        if (!response.containsHeader("Location")) {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        final String ct =  Objects.firstNonNull(response.getContentType(), "");
        if(!renderSimpleTemplate(req, ct)){
//         response.getOutputStream().write(buff.toArray());
         final TextInput ti = new TextInput(IO.get(fPath +"/layouts/default.html"));
         response.getWriter().write(Token.string(ti.content()).replace("{{$content}}", buff.toString()));
         // fillPageBuffer("/layouts/default.html").toString().replace("{{$content}}",

        }else{
            response.getOutputStream().write(buff.toArray());
        }


//                ? "/layouts/ajax.html" : "/layouts/default.html";
//            fillPageBuffer(pageBuffer, file);
//            response.getOutputStream().write(Token.token("Hello"));
//        response.getWriter().write(
//                pageBuffer.toString().replace("{{$content}}", queryResult));
//            response.getOutputStream().flush();
            response.flushBuffer();
    }

    /**
     * Decides whether or not to render a simple template.
     * @param req request
     * @param ct Content Type
     * @return true if the response is AJAX / JSON / TEXT
     */
    private boolean renderSimpleTemplate(final HttpServletRequest req,
            final String ct) {
        return req.getHeader("X-Requested-With") != null ||
                ct.startsWith("application/json")||
                ct.startsWith("text/plain") ||
                ct.startsWith("image/") ||
                ct.startsWith("application/oc") ||
                ct.startsWith("application/xm");
    }

    /**
     * Builds the resulting XQuery file in memory and evaluates the reslt.
     *
     * @param resp the response
     * @param req request reference
     * @param get get variables Map
     * @param post post variables Map
     * @param buff
     * @param out the output stream
     * @return the evaluated result
     * @throws IOException on error.
     */
    private String buildResult(final HttpServletResponse resp,
            final HttpServletRequest req, final String get, final String post,
            final ArrayOutput buff)
            throws IOException {
        final StringBuilder qry = prepareQuery();
        final TextInput tio = new TextInput(IO.get(view.getCanonicalPath()));
        qry.append(Token.string(tio.content()));

        final ResultPage queryResult = BaseXContext.exec(qry.toString(), get,
                post, resp, req, buff);
        return "";
    }

    /**
     * Adds the controller import to the view file.
     *
     * @return controller import String
     * @throws IOException if file not found.
     */
    private StringBuilder prepareQuery() throws IOException {
        final StringBuilder qry = new StringBuilder(128);
        if (controller == null)
            return qry;
        final String controllername = dbname(controller.getName());

        qry.append(String.format("import module namespace "
                + "%s=\"http://www.basex.org/myapp/%s\" " + "at \"%s\";\n",
                controllername, controllername, controller.getCanonicalPath()));
        return qry;
    }

    /**
     * Gets only the filename without suffix.
     *
     * @param n filename
     * @return chopped filename.
     */
    public final String dbname(final String n) {
        final int i = n.lastIndexOf(".");
        return (i != -1 ? n.substring(0, i) : n).replaceAll("[^\\w-]", "");
    }

    /**
     * Sets the controller and action based on the sent request. Initializes the
     * current Page Buffer.
     *
     * @param req request.
     * @throws HttpException on error
     */
    private void initMVC(final HttpServletRequest req) throws HttpException {
        final String cntr = Objects.firstNonNull(
                req.getAttribute("xails.controller"), "page").toString();
        assert null != cntr : "Error no controller set";

        final String ac = Objects.firstNonNull(
                req.getAttribute("xails.action"), "index").toString();
        assert null != ac : "Error no action set";

        final String vpath = String.format("views/%s/%s.xq", cntr, ac);
        final String cpath = String.format("controllers/%s.xq", cntr);

        try {
            controller = super.requestedFile(cpath);
        } catch (final HttpException e) { ;}
        view = super.requestedFile(vpath);
    }

    /**
     * Reads the layout into the page Buffer.
     *
     * @param fname layout to fill
     * @return StringBuilder containing the Layout
     * @throws IOException ex
     */
    private StringBuilder fillPageBuffer( final String fname)
            throws IOException {
        final StringBuilder pb = new StringBuilder();
        final TextInput ti = new TextInput(IO.get(fPath + fname));
        pb.append(Token.string(ti.content()));
        return pb;
    }
}
