    1   /*
    2    * Copyright (c) 2003 The Visigoth Software Society. All rights
    3    * reserved.
    4    *
    5    * Redistribution and use in source and binary forms, with or without
    6    * modification, are permitted provided that the following conditions
    7    * are met:
    8    *
    9    * 1. Redistributions of source code must retain the above copyright
   10    *    notice, this list of conditions and the following disclaimer.
   11    *
   12    * 2. Redistributions in binary form must reproduce the above copyright
   13    *    notice, this list of conditions and the following disclaimer in
   14    *    the documentation and/or other materials provided with the
   15    *    distribution.
   16    *
   17    * 3. The end-user documentation included with the redistribution, if
   18    *    any, must include the following acknowledgement:
   19    *       "This product includes software developed by the
   20    *        Visigoth Software Society (http://www.visigoths.org/)."
   21    *    Alternately, this acknowledgement may appear in the software itself,
   22    *    if and wherever such third-party acknowledgements normally appear.
   23    *
   24    * 4. Neither the name "FreeMarker", "Visigoth", nor any of the names of the 
   25    *    project contributors may be used to endorse or promote products derived
   26    *    from this software without prior written permission. For written
   27    *    permission, please contact visigoths@visigoths.org.
   28    *
   29    * 5. Products derived from this software may not be called "FreeMarker" or "Visigoth"
   30    *    nor may "FreeMarker" or "Visigoth" appear in their names
   31    *    without prior written permission of the Visigoth Software Society.
   32    *
   33    * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
   34    * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
   35    * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
   36    * DISCLAIMED.  IN NO EVENT SHALL THE VISIGOTH SOFTWARE SOCIETY OR
   37    * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   38    * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
   39    * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
   40    * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
   41    * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
   42    * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
   43    * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
   44    * SUCH DAMAGE.
   45    * ====================================================================
   46    *
   47    * This software consists of voluntary contributions made by many
   48    * individuals on behalf of the Visigoth Software Society. For more
   49    * information on the Visigoth Software Society, please see
   50    * http://www.visigoths.org/
   51    */
   52   
   53   package freemarker.ext.servlet;
   54   
   55   import java.io.File;
   56   import java.io.FileNotFoundException;
   57   import java.io.IOException;
   58   import java.text.SimpleDateFormat;
   59   import java.util.Calendar;
   60   import java.util.Enumeration;
   61   import java.util.GregorianCalendar;
   62   
   63   import javax.servlet.ServletContext;
   64   import javax.servlet.ServletException;
   65   import javax.servlet.http.HttpServlet;
   66   import javax.servlet.http.HttpServletRequest;
   67   import javax.servlet.http.HttpServletResponse;
   68   import javax.servlet.http.HttpSession;
   69   
   70   import freemarker.cache.ClassTemplateLoader;
   71   import freemarker.cache.FileTemplateLoader;
   72   import freemarker.cache.TemplateLoader;
   73   import freemarker.cache.WebappTemplateLoader;
   74   import freemarker.core.Configurable;
   75   import freemarker.ext.jsp.TaglibFactory;
   76   import freemarker.log.Logger;
   77   import freemarker.template.Configuration;
   78   import freemarker.template.ObjectWrapper;
   79   import freemarker.template.Template;
   80   import freemarker.template.TemplateException;
   81   import freemarker.template.TemplateExceptionHandler;
   82   import freemarker.template.TemplateModel;
   83   import freemarker.template.TemplateModelException;
   84   import freemarker.template.utility.StringUtil;
   85   import java.util.Locale;
   86   
   87   /**
   88    * <p>This is a general-purpose FreeMarker view servlet.</p>
   89    * 
   90    * <p>The main features are:
   91    * 
   92    * <ul>
   93    * 
   94    * <li>It makes all request, request parameters, session, and servlet
   95    * context attributes available to templates through <code>Request</code>,
   96    * <code>RequestParameters</code>, <code>Session</code>, and <code>Application</code>
   97    * variables.
   98    * 
   99    * <li>The scope variables are also available via automatic scope discovery. That is,
  100    * writing <code>Application.attrName</code>, <code>Session.attrName</code>,
  101    * <code>Request.attrName</code> is not mandatory; it's enough to write <code>attrName</code>,
  102    * and if no such variable was created in the template, it will search the
  103    * variable in <code>Request</code>, and then in <code>Session</code>,
  104    * and finally in <code>Application</code>.  
  105    * 
  106    * <li>It creates a variable with name <code>JspTaglibs</code>, that can be used
  107    * to load JSP taglibs. For example:<br>
  108    * <code>&lt;#assign tiles=JspTaglibs["/WEB-INF/struts-tiles.tld"]></code>
  109    * 
  110    * </ul>
  111    * 
  112    * <p>The servlet will rethrow the errors occurring during template processing,
  113    * wrapped into <code>ServletException</code>, except if the class name of the
  114    * class set by the <code>template_exception_handler</code> contains the
  115    * substring <code>"Debug"</code>. If it contains <code>"Debug"</code>, then it
  116    * is assumed that the template exception handler prints the error message to the
  117    * page, thus <code>FreemarkerServlet</code> will suppress the  exception, and
  118    * logs the problem with the servlet logger
  119    * (<code>javax.servlet.GenericServlet.log(...)</code>). 
  120    * 
  121    * <p>Supported init-params are:</p>
  122    * 
  123    * <ul>
  124    * 
  125    * <li><strong>TemplatePath</strong> specifies the location of the templates.
  126    * By default, this is interpreted as web application directory relative URI.<br>
  127    * Alternatively, you can prepend it with <tt>file://</tt> to indicate a literal
  128    * path in the file system (i.e. <tt>file:///var/www/project/templates/</tt>). 
  129    * Note that three slashes were used to specify an absolute path.<br>
  130    * Also, you can prepend it with <tt>class://path/to/template/package</tt> to
  131    * indicate that you want to load templates from the specified package
  132    * accessible through the classloader of the servlet.<br>
  133    * Default value is <tt>class://</tt> (that is, the root of the class hierarchy).
  134    * <i>Note that this default is different than the default in FreeMarker 1.x, when
  135    * it defaulted <tt>/</tt>, that is to loading from the webapp root directory.</i></li>
  136    * 
  137    * <li><strong>NoCache</strong> if set to true, generates headers in the response
  138    * that advise the HTTP client not to cache the returned page.
  139    * The default is <tt>false</tt>.</li>
  140    * 
  141    * <li><strong>ContentType</strong> if specified, response uses the specified
  142    * Content-type HTTP header. The value may include the charset (e.g.
  143    * <tt>"text/html; charset=ISO-8859-1"</tt>). If not specified, <tt>"text/html"</tt>
  144    * is used. If the charset is not specified in this init-param, then the charset
  145    * (encoding) of the actual template file will be used (in the response HTTP header
  146    * and for encoding the output stream). Note that this setting can be overridden
  147    * on a per-template basis by specifying a custom attribute named 
  148    * <tt>content_type</tt> in the <tt>attributes</tt> parameter of the 
  149    * <tt>&lt;#ftl></tt> directive. 
  150    * </li>
  151    * 
  152    * <li>The following init-params are supported only for backward compatibility, and
  153    * their usage is discouraged: TemplateUpdateInterval, DefaultEncoding,
  154    * ObjectWrapper, TemplateExceptionHandler. Use setting init-params such as
  155    * object_wrapper instead. 
  156    * 
  157    * <li>Any other init-param will be interpreted as <code>Configuration</code>
  158    * level setting. See {@link Configuration#setSetting}</li>
  159    * 
  160    * </ul>
  161    * 
  162    * @author Attila Szegedi
  163    * @version $Id: FreemarkerServlet.java,v 1.82.2.5 2006/06/21 13:02:01 ddekany Exp $
  164    */
  165   
  166   public class FreemarkerServlet extends HttpServlet
  167   {
  168       private static final Logger logger = Logger.getLogger("freemarker.servlet");
  169       
  170       public static final long serialVersionUID = -2440216393145762479L;
  171   
  172       private static final String INITPARAM_TEMPLATE_PATH = "TemplatePath";
  173       private static final String INITPARAM_NOCACHE = "NoCache";
  174       private static final String INITPARAM_CONTENT_TYPE = "ContentType";
  175       private static final String DEFAULT_CONTENT_TYPE = "text/html";
  176       private static final String INITPARAM_DEBUG = "Debug";
  177       
  178       private static final String DEPR_INITPARAM_TEMPLATE_DELAY = "TemplateDelay";
  179       private static final String DEPR_INITPARAM_ENCODING = "DefaultEncoding";
  180       private static final String DEPR_INITPARAM_OBJECT_WRAPPER = "ObjectWrapper";
  181       private static final String DEPR_INITPARAM_WRAPPER_SIMPLE = "simple";
  182       private static final String DEPR_INITPARAM_WRAPPER_BEANS = "beans";
  183       private static final String DEPR_INITPARAM_WRAPPER_JYTHON = "jython";
  184       private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER = "TemplateExceptionHandler";
  185       private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_RETHROW = "rethrow";
  186       private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_DEBUG = "debug";
  187       private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_HTML_DEBUG = "htmlDebug";
  188       private static final String DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_IGNORE = "ignore";
  189       private static final String DEPR_INITPARAM_DEBUG = "debug";
  190   
  191       public static final String KEY_REQUEST = "Request";
  192       public static final String KEY_REQUEST_PRIVATE = "__FreeMarkerServlet.Request__";
  193       public static final String KEY_REQUEST_PARAMETERS = "RequestParameters";
  194       public static final String KEY_SESSION = "Session";
  195       public static final String KEY_APPLICATION = "Application";
  196       public static final String KEY_APPLICATION_PRIVATE = "__FreeMarkerServlet.Application__";
  197       public static final String KEY_JSP_TAGLIBS = "JspTaglibs";
  198   
  199       // Note these names start with dot, so they're essentially invisible from
  200       // a freemarker script.
  201       private static final String ATTR_REQUEST_MODEL = ".freemarker.Request";
  202       private static final String ATTR_REQUEST_PARAMETERS_MODEL =
  203           ".freemarker.RequestParameters";
  204       private static final String ATTR_SESSION_MODEL = ".freemarker.Session";
  205       private static final String ATTR_APPLICATION_MODEL =
  206           ".freemarker.Application";
  207       private static final String ATTR_JSP_TAGLIBS_MODEL =
  208           ".freemarker.JspTaglibs";
  209   
  210       private static final String EXPIRATION_DATE;
  211   
  212       static {
  213           // Generate expiration date that is one year from now in the past
  214           GregorianCalendar expiration = new GregorianCalendar();
  215           expiration.roll(Calendar.YEAR, -1);
  216           SimpleDateFormat httpDate =
  217               new SimpleDateFormat(
  218                   "EEE, dd MMM yyyy HH:mm:ss z",
  219                   java.util.Locale.US);
  220           EXPIRATION_DATE = httpDate.format(expiration.getTime());
  221       }
  222   
  223       private String templatePath;
  224       private boolean nocache;
  225       protected boolean debug;
  226       private Configuration config;
  227       private ObjectWrapper wrapper;
  228       private String contentType;
  229       private boolean noCharsetInContentType;
  230       
  231       public void init() throws ServletException {
  232           try {
  233               config = createConfiguration();
  234               
  235               // Set defaults:
  236               config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
  237               contentType = DEFAULT_CONTENT_TYPE;
  238               
  239               // Process object_wrapper init-param out of order: 
  240               wrapper = createObjectWrapper();
  241               if (logger.isDebugEnabled()) {
  242                   logger.debug("Using object wrapper of class " + wrapper.getClass().getName());
  243               }
  244               config.setObjectWrapper(wrapper);
  245               
  246               // Process TemplatePath init-param out of order:
  247               templatePath = getInitParameter(INITPARAM_TEMPLATE_PATH);
  248               if (templatePath == null)
  249                   templatePath = "class://";
  250               config.setTemplateLoader(createTemplateLoader(templatePath));
  251   
  252               // Process all other init-params:
  253               Enumeration initpnames = getServletConfig().getInitParameterNames();
  254               while (initpnames.hasMoreElements()) {
  255                   String name = (String) initpnames.nextElement();
  256                   String value = getInitParameter(name);
  257                   
  258                   if (name == null) {
  259                       throw new ServletException(
  260                               "init-param without param-name. "
  261                               + "Maybe the web.xml is not well-formed?");
  262                   }
  263                   if (value == null) {
  264                       throw new ServletException(
  265                               "init-param without param-value. "
  266                               + "Maybe the web.xml is not well-formed?");
  267                   }
  268                   
  269                   if (name.equals(DEPR_INITPARAM_OBJECT_WRAPPER)
  270                           || name.equals(Configurable.OBJECT_WRAPPER_KEY)
  271                           || name.equals(INITPARAM_TEMPLATE_PATH)) {
  272                       // ignore: we have already processed these
  273                   } else if (name.equals(DEPR_INITPARAM_ENCODING)) { // BC
  274                       if (getInitParameter(Configuration.DEFAULT_ENCODING_KEY) != null) {
  275                           throw new ServletException(
  276                                   "Conflicting init-params: "
  277                                   + Configuration.DEFAULT_ENCODING_KEY + " and "
  278                                   + DEPR_INITPARAM_ENCODING);
  279                       }
  280                       config.setDefaultEncoding(value);
  281                   } else if (name.equals(DEPR_INITPARAM_TEMPLATE_DELAY)) { // BC
  282                       if (getInitParameter(Configuration.TEMPLATE_UPDATE_DELAY_KEY) != null) {
  283                           throw new ServletException(
  284                                   "Conflicting init-params: "
  285                                   + Configuration.TEMPLATE_UPDATE_DELAY_KEY + " and "
  286                                   + DEPR_INITPARAM_TEMPLATE_DELAY);
  287                       }
  288                       try {
  289                           config.setTemplateUpdateDelay(Integer.parseInt(value));
  290                       } catch (NumberFormatException e) {
  291                           // Intentionally ignored
  292                       }
  293                   } else if (name.equals(DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER)) { // BC
  294                       if (getInitParameter(Configurable.TEMPLATE_EXCEPTION_HANDLER_KEY) != null) {
  295                           throw new ServletException(
  296                                   "Conflicting init-params: "
  297                                   + Configurable.TEMPLATE_EXCEPTION_HANDLER_KEY + " and "
  298                                   + DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER);
  299                       }
  300   
  301                       if (DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_RETHROW.equals(value)) {
  302                           config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  303                       } else if (DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_DEBUG.equals(value)) {
  304                           config.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
  305                       } else if  (DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_HTML_DEBUG.equals(value)) {
  306                           config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
  307                       } else if  (DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER_IGNORE.equals(value)) {
  308                           config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
  309                       } else {
  310                           throw new ServletException(
  311                                   "Invalid value for servlet init-param "
  312                                   + DEPR_INITPARAM_TEMPLATE_EXCEPTION_HANDLER + ": " + value);
  313                       }
  314                   } else if (name.equals(INITPARAM_NOCACHE)) {
  315                       nocache = StringUtil.getYesNo(value);
  316                   } else if (name.equals(DEPR_INITPARAM_DEBUG)) { // BC
  317                       if (getInitParameter(INITPARAM_DEBUG) != null) {
  318                           throw new ServletException(
  319                                   "Conflicting init-params: "
  320                                   + INITPARAM_DEBUG + " and "
  321                                   + DEPR_INITPARAM_DEBUG);
  322                       }
  323                       debug = StringUtil.getYesNo(value);
  324                   } else if (name.equals(INITPARAM_DEBUG)) {
  325                       debug = StringUtil.getYesNo(value);
  326                   } else if (name.equals(INITPARAM_CONTENT_TYPE)) {
  327                       contentType = value;
  328                   } else {
  329                       config.setSetting(name, value);
  330                   }
  331               } // while initpnames
  332               
  333               noCharsetInContentType = true;
  334               int i = contentType.toLowerCase().indexOf("charset=");
  335               if (i != -1) {
  336                   char c = ' ';
  337                   i--;
  338                   while (i >= 0) {
  339                       c = contentType.charAt(i);
  340                       if (!Character.isWhitespace(c)) break;
  341                       i--;
  342                   }
  343                   if (i == -1 || c == ';') {
  344                       noCharsetInContentType = false;
  345                   }
  346               }
  347           } catch (ServletException e) {
  348               throw e;
  349           } catch (Exception e) {
  350               throw new ServletException(e);
  351           }
  352       }
  353   
  354       /**
  355        * Create the template loader. The default implementation will create a
  356        * {@link ClassTemplateLoader} if the template path starts with "class://",
  357        * a {@link FileTemplateLoader} if the template path starts with "file://",
  358        * and a {@link WebappTemplateLoader} otherwise.
  359        * @param templatePath the template path to create a loader for
  360        * @return a newly created template loader
  361        * @throws IOException
  362        */
  363       protected TemplateLoader createTemplateLoader(String templatePath) throws IOException
  364       {
  365           if (templatePath.startsWith("class://")) {
  366               // substring(7) is intentional as we "reuse" the last slash
  367               return new ClassTemplateLoader(getClass(), templatePath.substring(7));
  368           } else {
  369               if (templatePath.startsWith("file://")) {
  370                   templatePath = templatePath.substring(7);
  371                   return new FileTemplateLoader(new File(templatePath));
  372               } else {
  373                   return new WebappTemplateLoader(this.getServletContext(), templatePath);
  374               }
  375           }
  376       }
  377   
  378       public void doGet(HttpServletRequest request, HttpServletResponse response)
  379           throws ServletException, IOException
  380       {
  381           process(request, response);
  382       }
  383   
  384       public void doPost(
  385           HttpServletRequest request,
  386           HttpServletResponse response)
  387           throws ServletException, IOException
  388       {
  389           process(request, response);
  390       }
  391   
  392       private void process(
  393           HttpServletRequest request,
  394           HttpServletResponse response)
  395           throws ServletException, IOException
  396       {
  397           // Give chance to subclasses to perform preprocessing
  398           if (preprocessRequest(request, response)) {
  399               return;
  400           }
  401   
  402           String path = requestUrlToTemplatePath(request);
  403   
  404           if (debug) {
  405               log("Requested template: " + path);
  406           }
  407   
  408           Template template = null;
  409           try {
  410               template = config.getTemplate(
  411                       path,
  412                       deduceLocale(path, request, response));
  413           } catch (FileNotFoundException e) {
  414               response.sendError(HttpServletResponse.SC_NOT_FOUND);
  415               return;
  416           }
  417           
  418           Object attrContentType = template.getCustomAttribute("content_type");
  419           if(attrContentType != null) {
  420               response.setContentType(attrContentType.toString());
  421           }
  422           else {
  423               if (noCharsetInContentType) {
  424                   response.setContentType(
  425                           contentType + "; charset=" + template.getEncoding());
  426               } else {
  427                   response.setContentType(contentType);
  428               }
  429           }
  430   
  431           // Set cache policy
  432           setBrowserCachingPolicy(response);
  433   
  434           ServletContext servletContext = getServletContext();
  435           try {
  436               TemplateModel model = createModel(wrapper, servletContext, request, response);
  437   
  438               // Give subclasses a chance to hook into preprocessing
  439               if (preTemplateProcess(request, response, template, model)) {
  440                   try {
  441                       // Process the template
  442                       template.process(model, response.getWriter());
  443                   } finally {
  444                       // Give subclasses a chance to hook into postprocessing
  445                       postTemplateProcess(request, response, template, model);
  446                   }
  447               }
  448           } catch (TemplateException te) {
  449               if (config.getTemplateExceptionHandler()
  450                           .getClass().getName().indexOf("Debug") != -1) {
  451                   this.log("Error executing FreeMarker template", te);
  452               } else {
  453                   ServletException e = new ServletException(
  454                           "Error executing FreeMarker template", te);
  455                   // Attempt to set init cause, but don't freak out if the method
  456                   // is not available (i.e. pre-1.4 JRE). This is required as the
  457                   // constructor-passed throwable won't show up automatically in
  458                   // stack traces.
  459                   try {
  460                       e.getClass().getMethod("initCause",
  461                               new Class[] { Throwable.class }).invoke(e,
  462                               new Object[] { te });
  463                   } catch (Exception ex) {
  464                       // Can't set init cause, we're probably running on a pre-1.4
  465                       // JDK, oh well...
  466                   }
  467                   throw e;
  468               }
  469           }
  470       }
  471       
  472       /**
  473        * Returns the locale used for the 
  474        * {@link Configuration#getTemplate(String, Locale)} call.
  475        * The base implementation simply returns the locale setting of the
  476        * configuration. Override this method to provide different behaviour, i.e.
  477        * to use the locale indicated in the request.
  478        */
  479       protected Locale deduceLocale(
  480               String templatePath, HttpServletRequest request, HttpServletResponse response) {
  481           return config.getLocale();
  482       }
  483   
  484       protected TemplateModel createModel(ObjectWrapper wrapper,
  485                                           ServletContext servletContext,
  486                                           HttpServletRequest request,
  487                                           HttpServletResponse response) throws TemplateModelException {
  488           try {
  489               AllHttpScopesHashModel params = new AllHttpScopesHashModel(wrapper, servletContext, request);
  490       
  491               // Create hash model wrapper for servlet context (the application)
  492               ServletContextHashModel servletContextModel =
  493                   (ServletContextHashModel) servletContext.getAttribute(
  494                       ATTR_APPLICATION_MODEL);
  495               if (servletContextModel == null)
  496               {
  497                   servletContextModel = new ServletContextHashModel(this, wrapper);
  498                   servletContext.setAttribute(
  499                       ATTR_APPLICATION_MODEL,
  500                       servletContextModel);
  501                   TaglibFactory taglibs = new TaglibFactory(servletContext);
  502                   servletContext.setAttribute(
  503                       ATTR_JSP_TAGLIBS_MODEL,
  504                       taglibs);
  505                   initializeServletContext(request, response);
  506               }
  507               params.putUnlistedModel(KEY_APPLICATION, servletContextModel);
  508               params.putUnlistedModel(KEY_APPLICATION_PRIVATE, servletContextModel);
  509               params.putUnlistedModel(KEY_JSP_TAGLIBS, (TemplateModel)servletContext.getAttribute(ATTR_JSP_TAGLIBS_MODEL));
  510               // Create hash model wrapper for session
  511               HttpSessionHashModel sessionModel;
  512               HttpSession session = request.getSession(false);
  513               if(session != null) {
  514                   sessionModel = (HttpSessionHashModel) session.getAttribute(ATTR_SESSION_MODEL);
  515                   if (sessionModel == null || sessionModel.isZombie()) {
  516                       sessionModel = new HttpSessionHashModel(session, wrapper);
  517                       session.setAttribute(ATTR_SESSION_MODEL, sessionModel);
  518                       if(!sessionModel.isZombie()) {
  519                           initializeSession(request, response);
  520                       }
  521                   }
  522               }
  523               else {
  524                   sessionModel = new HttpSessionHashModel(this, request, response, wrapper);
  525               }
  526               params.putUnlistedModel(KEY_SESSION, sessionModel);
  527       
  528               // Create hash model wrapper for request
  529               HttpRequestHashModel requestModel =
  530                   (HttpRequestHashModel) request.getAttribute(ATTR_REQUEST_MODEL);
  531               if (requestModel == null || requestModel.getRequest() != request)
  532               {
  533                   requestModel = new HttpRequestHashModel(request, response, wrapper);
  534                   request.setAttribute(ATTR_REQUEST_MODEL, requestModel);
  535                   request.setAttribute(
  536                       ATTR_REQUEST_PARAMETERS_MODEL,
  537                       createRequestParametersHashModel(request));
  538               }
  539               params.putUnlistedModel(KEY_REQUEST, requestModel);
  540               params.putUnlistedModel(KEY_REQUEST_PRIVATE, requestModel);
  541       
  542               // Create hash model wrapper for request parameters
  543               HttpRequestParametersHashModel requestParametersModel =
  544                   (HttpRequestParametersHashModel) request.getAttribute(
  545                       ATTR_REQUEST_PARAMETERS_MODEL);
  546               params.putUnlistedModel(KEY_REQUEST_PARAMETERS, requestParametersModel);
  547               return params;
  548           } catch (ServletException e) {
  549               throw new TemplateModelException(e);
  550           } catch (IOException e) {
  551               throw new TemplateModelException(e);
  552           }
  553       }
  554   
  555       /**
  556        * Maps the request URL to a template path that is passed to 
  557        * {@link Configuration#getTemplate(String, Locale)}. You can override it
  558        * (i.e. to provide advanced rewriting capabilities), but you are strongly
  559        * encouraged to call the overridden method first, then only modify its
  560        * return value. 
  561        * @param request the currently processed request
  562        * @return a String representing the template path
  563        */
  564       protected String requestUrlToTemplatePath(HttpServletRequest request)
  565       {
  566           // First, see if it is an included request
  567           String includeServletPath  = (String) request.getAttribute("javax.servlet.include.servlet_path");
  568           if(includeServletPath != null)
  569           {
  570               // Try path info; only if that's null (servlet is mapped to an
  571               // URL extension instead of to prefix) use servlet path.
  572               String includePathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
  573               return includePathInfo == null ? includeServletPath : includePathInfo;
  574           } 
  575           // Seems that the servlet was not called as the result of a 
  576           // RequestDispatcher.include(...). Try pathInfo then servletPath again,
  577           // only now directly on the request object:
  578           String path = request.getPathInfo();
  579           if (path != null) return path;
  580           path = request.getServletPath();
  581           if (path != null) return path;
  582           // Seems that it is a servlet mapped with prefix, and there was no extra path info.
  583           return "";
  584       }
  585   
  586       /**
  587        * Called as the first step in request processing, before the templating mechanism
  588        * is put to work. By default does nothing and returns false. This method is
  589        * typically overridden to manage serving of non-template resources (i.e. images)
  590        * that reside in the template directory.
  591        * @param request the HTTP request
  592        * @param response the HTTP response
  593        * @return true to indicate this method has processed the request entirely,
  594        * and that the further request processing should not take place.
  595        */
  596       protected boolean preprocessRequest(
  597           HttpServletRequest request,
  598           HttpServletResponse response)
  599               throws ServletException, IOException {
  600           return false;
  601       }
  602   
  603       /**
  604        * This method is called from {@link #init()} to create the
  605        * FreeMarker configuration object that this servlet will use
  606        * for template loading. This is a hook that allows you
  607        * to custom-configure the configuration object in a subclass.
  608        * The default implementation returns a new {@link Configuration}
  609        * instance.
  610        */
  611       protected Configuration createConfiguration() {
  612           return new Configuration();
  613       }
  614       
  615       /**
  616        * This method is called from {@link #init()} to create the
  617        * FreeMarker object wrapper object that this servlet will use
  618        * for adapting request, session, and servlet context attributes into 
  619        * template models.. This is a hook that allows you
  620        * to custom-configure the wrapper object in a subclass.
  621        * The default implementation returns a wrapper that depends on the value
  622        * of <code>ObjectWrapper</code> init parameter. If <code>simple</code> is
  623        * specified, {@link ObjectWrapper#SIMPLE_WRAPPER} is used; if <code>jython</code>
  624        * is specified, {@link freemarker.ext.jython.JythonWrapper} is used. In
  625        * every other case {@link ObjectWrapper#DEFAULT_WRAPPER} is used.
  626        */
  627       protected ObjectWrapper createObjectWrapper() {
  628           String wrapper = getServletConfig().getInitParameter(DEPR_INITPARAM_OBJECT_WRAPPER);
  629           if (wrapper != null) { // BC
  630               if (getInitParameter(Configurable.OBJECT_WRAPPER_KEY) != null) {
  631                   throw new RuntimeException("Conflicting init-params: "
  632                           + Configurable.OBJECT_WRAPPER_KEY + " and "
  633                           + DEPR_INITPARAM_OBJECT_WRAPPER);
  634               }
  635               if (DEPR_INITPARAM_WRAPPER_BEANS.equals(wrapper)) {
  636                   return ObjectWrapper.BEANS_WRAPPER;
  637               }
  638               if(DEPR_INITPARAM_WRAPPER_SIMPLE.equals(wrapper)) {
  639                   return ObjectWrapper.SIMPLE_WRAPPER;
  640               }
  641               if(DEPR_INITPARAM_WRAPPER_JYTHON.equals(wrapper)) {
  642                   // Avoiding compile-time dependency on Jython package
  643                   try {
  644                       return (ObjectWrapper) Class.forName("freemarker.ext.jython.JythonWrapper")
  645                               .newInstance();
  646                   } catch (InstantiationException e) {
  647                       throw new InstantiationError(e.getMessage());
  648                   } catch (IllegalAccessException e) {
  649                       throw new IllegalAccessError(e.getMessage());
  650                   } catch (ClassNotFoundException e) {
  651                       throw new NoClassDefFoundError(e.getMessage());
  652                   }
  653               }
  654   //            return BeansWrapper.getDefaultInstance();
  655               return ObjectWrapper.DEFAULT_WRAPPER;
  656           } else {
  657               wrapper = getInitParameter(Configurable.OBJECT_WRAPPER_KEY);
  658               if (wrapper == null) {
  659   //                return BeansWrapper.getDefaultInstance();
  660                   return ObjectWrapper.DEFAULT_WRAPPER;
  661               } else {
  662                   try {
  663                       config.setSetting(Configurable.OBJECT_WRAPPER_KEY, wrapper);
  664                   } catch (TemplateException e) {
  665                       throw new RuntimeException(e.toString());
  666                   }
  667                   return config.getObjectWrapper();
  668               }
  669           }
  670       }
  671       
  672       protected ObjectWrapper getObjectWrapper() {
  673           return wrapper;
  674       }
  675       
  676       protected final String getTemplatePath() {
  677           return templatePath;
  678       }
  679   
  680       protected HttpRequestParametersHashModel createRequestParametersHashModel(HttpServletRequest request) {
  681           return new HttpRequestParametersHashModel(request);
  682       }
  683   
  684       /**
  685        * Called when servlet detects in a request processing that
  686        * application-global (that is, ServletContext-specific) attributes are not yet
  687        * set.
  688        * This is a generic hook you might use in subclasses to perform a specific
  689        * action on first request in the context. By default it does nothing.
  690        * @param request the actual HTTP request
  691        * @param response the actual HTTP response
  692        */
  693       protected void initializeServletContext(
  694           HttpServletRequest request,
  695           HttpServletResponse response)
  696               throws ServletException, IOException {
  697       }
  698   
  699       /**
  700        * Called when servlet detects in a request processing that session-global 
  701        * (that is, HttpSession-specific) attributes are not yet set.
  702        * This is a generic hook you might use in subclasses to perform a specific
  703        * action on first request in the session. By default it does nothing. It
  704        * is only invoked on newly created sessions; it is not invoked when a
  705        * replicated session is reinstantiated in another servlet container.
  706        * 
  707        * @param request the actual HTTP request
  708        * @param response the actual HTTP response
  709        */
  710       protected void initializeSession(
  711           HttpServletRequest request,
  712           HttpServletResponse response)
  713           throws ServletException, IOException
  714       {
  715       }
  716   
  717       /**
  718        * Called before the execution is passed to template.process().
  719        * This is a generic hook you might use in subclasses to perform a specific
  720        * action before the template is processed. By default does nothing.
  721        * A typical action to perform here is to inject application-specific
  722        * objects into the model root
  723        *
  724        * <p>Example: Expose the Serlvet context path as "baseDir" for all templates:
  725        *
  726        *<pre>
  727        *    ((SimpleHash) data).put("baseDir", request.getContextPath() + "/");
  728        *    return true;
  729        *</pre>
  730        *
  731        * @param request the actual HTTP request
  732        * @param response the actual HTTP response
  733        * @param template the template that will get executed
  734        * @param data the data that will be passed to the template. By default this will be
  735        *        an {@link AllHttpScopesHashModel} (which is a {@link freemarker.template.SimpleHash} subclass).
  736        *        Thus, you can add new variables to the data-model with the
  737        *        {@link freemarker.template.SimpleHash#put(String, Object)} subclass) method.
  738        * @return true to process the template, false to suppress template processing.
  739        */
  740       protected boolean preTemplateProcess(
  741           HttpServletRequest request,
  742           HttpServletResponse response,
  743           Template template,
  744           TemplateModel data)
  745           throws ServletException, IOException
  746       {
  747           return true;
  748       }
  749   
  750       /**
  751        * Called after the execution returns from template.process().
  752        * This is a generic hook you might use in subclasses to perform a specific
  753        * action after the template is processed. It will be invoked even if the
  754        * template processing throws an exception. By default does nothing.
  755        * @param request the actual HTTP request
  756        * @param response the actual HTTP response
  757        * @param template the template that was executed
  758        * @param data the data that was passed to the template
  759        */
  760       protected void postTemplateProcess(
  761           HttpServletRequest request,
  762           HttpServletResponse response,
  763           Template template,
  764           TemplateModel data)
  765           throws ServletException, IOException
  766       {
  767       }
  768       
  769       /**
  770        * Returns the {@link freemarker.template.Configuration} object used by this servlet.
  771        * Please don't forget that {@link freemarker.template.Configuration} is not thread-safe
  772        * when you modify it.
  773        */
  774       protected Configuration getConfiguration() {
  775           return config;
  776       }
  777   
  778       /**
  779        * If the parameter "nocache" was set to true, generate a set of headers
  780        * that will advise the HTTP client not to cache the returned page.
  781        */
  782       private void setBrowserCachingPolicy(HttpServletResponse res)
  783       {
  784           if (nocache)
  785           {
  786               // HTTP/1.1 + IE extensions
  787               res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, "
  788                       + "post-check=0, pre-check=0");
  789               // HTTP/1.0
  790               res.setHeader("Pragma", "no-cache");
  791               // Last resort for those that ignore all of the above
  792               res.setHeader("Expires", EXPIRATION_DATE);
  793           }
  794       }
  795   }
