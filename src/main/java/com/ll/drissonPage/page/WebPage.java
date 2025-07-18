package com.ll.drissonPage.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ll.drissonPage.base.BasePage;
import com.ll.drissonPage.base.Browser;
import com.ll.drissonPage.base.By;
import com.ll.drissonPage.base.DrissionElement;
import com.ll.drissonPage.config.ChromiumOptions;
import com.ll.drissonPage.config.SessionOptions;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.element.SessionElement;
import com.ll.drissonPage.functions.Web;
import com.ll.drissonPage.units.setter.WebPageSetter;
import com.ll.drissonPage.units.waiter.PageWaiter;
import lombok.Getter;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author 陆
 * @address <a href="https://t.me/blanksig"/>click
 */
public class WebPage extends BasePage<DrissionElement<?, ?>> {
    @Getter
    protected final SessionPage sessionPage;
    @Getter
    protected final ChromiumPage chromiumPage;
    private WebPageSetter setter;
    private WebMode mode;
    @Getter
    private boolean hasDriver;
    @Getter
    private boolean hasSession;
    private final Lock lock;

    /**
     * 初始化函数 默认是d模式
     */
    public WebPage() {
        this(WebMode.NULL);
    }

    /**
     * 初始化函数
     *
     * @param mode 'd' 或 's'，即driver模式和session模式
     */
    public WebPage(WebMode mode) {
        this(mode, -1.0);
    }

    /**
     * 初始化函数
     *
     * @param sessionOptions 设置配置，默认是s模式
     */
    public WebPage(SessionOptions sessionOptions) {
        this(WebMode.s, sessionOptions);
    }

    /**
     * 初始化函数
     *
     * @param chromiumOptions 设置配置，默认是d模式
     */
    public WebPage(ChromiumOptions chromiumOptions) {
        this(WebMode.d, chromiumOptions);
    }

    /**
     * 初始化函数
     *
     * @param mode 'd' 或 's'，即driver模式和session模式
     */
    public WebPage(WebMode mode, SessionOptions sessionOptions) {
        this(mode, null, false, sessionOptions);
    }

    public WebPage(WebMode mode, ChromiumOptions chromiumOptions) {
        this(mode, null, chromiumOptions, false);
    }

    /**
     * 初始化函数
     *
     * @param mode    'd' 或 's'，即driver模式和session模式
     * @param timeout 超时时间（秒），d模式时为寻找元素时间，s模式时为连接时间，默认10秒
     */
    public WebPage(WebMode mode, Double timeout) {
        this(mode, timeout < 0 ? null : timeout, false);
    }

    /**
     * 初始化函数
     *
     * @param mode            'd' 或 's'，即driver模式和session模式
     * @param timeout         超时时间（秒），d模式时为寻找元素时间，s模式时为连接时间，默认10秒
     * @param chromiumOptions Driver对象，只使用s模式时应传入False
     */
    public WebPage(WebMode mode, Double timeout, boolean chromiumOptions) {
        this(mode, timeout, chromiumOptions, false);
    }

    /**
     * 初始化函数
     *
     * @param mode             'd' 或 's'，即driver模式和session模式
     * @param timeout          超时时间（秒），d模式时为寻找元素时间，s模式时为连接时间，默认10秒
     * @param chromiumOptions  Driver对象，只使用s模式时应传入False
     * @param sessionOrOptions Session对象或SessionOptions对象，只使用d模式时应传入False
     */
    public WebPage(WebMode mode, Double timeout, boolean chromiumOptions, boolean sessionOrOptions) {
        this(mode, timeout, chromiumOptions, sessionOrOptions, false);
    }

    /**
     * 初始化函数
     *
     * @param mode             'd' 或 's'，即driver模式和session模式
     * @param timeout          超时时间（秒），d模式时为寻找元素时间，s模式时为连接时间，默认10秒
     * @param chromiumOptions  Driver对象，只使用s模式时应传入False
     * @param sessionOrOptions Session对象或SessionOptions对象，只使用d模式时应传入False
     */
    public WebPage(WebMode mode, Double timeout, boolean chromiumOptions, SessionOptions sessionOrOptions) {
        this(mode, timeout, chromiumOptions, sessionOrOptions, false);
    }

    /**
     * 初始化函数
     *
     * @param mode            'd' 或 's'，即driver模式和session模式
     * @param timeout         超时时间（秒），d模式时为寻找元素时间，s模式时为连接时间，默认10秒
     * @param chromiumOptions Driver对象，只使用s模式时应传入False
     */
    public WebPage(WebMode mode, Double timeout, ChromiumOptions chromiumOptions) {
        this(mode, timeout, chromiumOptions, false);
    }

    /**
     * 初始化函数
     *
     * @param mode             'd' 或 's'，即driver模式和session模式
     * @param timeout          超时时间（秒），d模式时为寻找元素时间，s模式时为连接时间，默认10秒
     * @param chromiumOptions  Driver对象，只使用s模式时应传入False
     * @param sessionOrOptions Session对象或SessionOptions对象，只使用d模式时应传入False
     */
    public WebPage(WebMode mode, Double timeout, ChromiumOptions chromiumOptions, boolean sessionOrOptions) {
        this(mode, timeout, chromiumOptions, sessionOrOptions, false);
    }

    /**
     * 初始化函数
     *
     * @param mode             'd' 或 's'，即driver模式和session模式
     * @param timeout          超时时间（秒），d模式时为寻找元素时间，s模式时为连接时间，默认10秒
     * @param chromiumOptions  Driver对象，只使用s模式时应传入False
     * @param sessionOrOptions Session对象或SessionOptions对象，只使用d模式时应传入False
     */
    public WebPage(WebMode mode, Double timeout, ChromiumOptions chromiumOptions, SessionOptions sessionOrOptions) {
        this(mode, timeout, chromiumOptions, sessionOrOptions, false);
    }


    /**
     * 初始化函数
     *
     * @param mode             'd' 或 's'，即driver模式和session模式
     * @param timeout          超时时间（秒），d模式时为寻找元素时间，s模式时为连接时间，默认10秒
     * @param chromiumOptions  Driver对象，只使用s模式时应传入False
     * @param sessionOrOptions Session对象或SessionOptions对象，只使用d模式时应传入False
     */
    private WebPage(WebMode mode, Double timeout, Object chromiumOptions, Object sessionOrOptions, boolean ignoredFlag) {
        this.mode = mode;
        this.lock = new ReentrantLock();
        sessionPage = sessionOrOptions instanceof SessionOptions ? new SessionPage((SessionOptions) sessionOrOptions) : new SessionPage();
        if (chromiumOptions == null || Objects.equals(chromiumOptions, false))
            chromiumOptions = new ChromiumOptions(true, null).setTimeouts(sessionPage.timeout(), null, null).setPaths(sessionPage.downloadPath());
        ChromiumPage instance;
        if (chromiumOptions instanceof String) {
            instance = ChromiumPage.getInstance(String.valueOf(chromiumOptions), timeout);
        } else if (chromiumOptions instanceof ChromiumOptions) {
            instance = ChromiumPage.getInstance((ChromiumOptions) chromiumOptions, timeout);
        } else if (chromiumOptions instanceof Integer) {
            instance = ChromiumPage.getInstance((Integer) chromiumOptions, timeout);
        } else if (chromiumOptions == null) {
            instance = ChromiumPage.getInstance("", timeout);
        } else {
            throw new IllegalArgumentException("chromiumOptions类型只能为 String , ChromiumOptions, Integer, null");
        }
        this.chromiumPage = instance;
        this.setType("WebPage");

    }
    //------------挂件

    /**
     * @return 返回用于设置的对象
     */
    public WebPageSetter set() {
        if (setter == null) setter = new WebPageSetter(this);
        return setter;
    }

    /**
     * @return
     */
    public PageWaiter waits() {
        return this.chromiumPage != null ? this.chromiumPage.waits() : null;
    }

    /**
     * @return 返回当前标签页id
     */
    public String tabId() {
        return this.chromiumPage != null ? this.chromiumPage.tabId() : null;
    }

    /**
     * @return 返回用于控制浏览器cdp的driver
     */
    public Browser browser() {
        return this.chromiumPage != null ? this.chromiumPage.browser() : null;
    }

    /**
     * @return 返回标签页数量 如果是-1则是{@link  ChromiumPage}没有创建
     */
    public int tabsCount() {
        return this.chromiumPage != null ? this.chromiumPage.tabsCount() : -1;
    }

    /**
     * @return 返回所有标签页id组成的列表
     */
    public List<String> tabIds() {
        return this.chromiumPage != null ? this.chromiumPage.browser().tabs() : null;

    }

    /**
     * @return 返回最新的标签页，最新标签页指最后创建或最后被激活的
     */
    public WebPageTab latestTab() {
        return this.chromiumPage != null ? new WebPageTab(this, this.chromiumPage.latestTab().tabId()) : null;

    }

    /**
     * @return 返回浏览器进程id
     */
    public Integer processId() {
        return this.chromiumPage != null ? this.chromiumPage.processId() : null;
    }

    /**
     * @return 返回所控制的浏览器版本号
     */
    public String browserVersion() {
        return this.chromiumPage != null ? this.chromiumPage.browserVersion() : null;
    }

    /**
     * @return 返回当前url
     */
    @Override
    public String url() {
        switch (mode) {
            case d:
                return this.browserUrl();
            case s:
                return this.sessionUrl();
            default:
                return null;
        }
    }

    /**
     * @return 返回浏览器当前url
     */
    protected String browserUrl() {
        return this.chromiumPage.driver() != null ? this.sessionPage.url() : null;
    }

    /**
     * @return 返回当前页面title
     */
    public String title() {
        switch (mode) {
            case d:
                return chromiumPage.title();
            case s:
                return sessionPage.title();
            default:
                return null;
        }
    }

    /**
     * @return 返回页码原始数据数据
     */
    public Object rawData() {
        switch (mode) {
            case d:
                if (this.hasDriver) {
                    return chromiumPage.html();
                } else {
                    return "";
                }
            case s:
                return sessionPage.rawData();
            default:
                return null;
        }
    }

    @Override
    public String html() {
        switch (mode) {
            case d:
                if (this.hasDriver) {
                    return chromiumPage.html();
                } else {
                    return "";
                }
            case s:
                return sessionPage.html();
            default:
                return null;
        }
    }

    @Override
    public JSONObject json() {
        switch (mode) {
            case d:
                return chromiumPage.json();

            case s:
                return sessionPage.json();
            default:
                return null;
        }
    }

    /**
     * @return 返回 s 模式获取到的 Response 对象
     */
    public Response response() {
        return sessionPage.response();
    }

    /**
     * @return 返回当前模式，'s'或'd'
     */
    public WebMode mode() {
        return this.mode;
    }

    /**
     * @return 返回user agent
     */
    public String ua() {
        return userAgent();

    }

    /**
     * @return 返回user agent
     */
    @Override
    public String userAgent() {
        switch (mode) {
            case d:
                return chromiumPage.userAgent();
            case s:
                return sessionPage.userAgent();
            default:
                return null;
        }
    }

    /**
     * @return 返回Session对象，如未初始化则按配置信息创建
     */
    public OkHttpClient session() {
        if (sessionPage.session == null) this.sessionPage.createSession();
        return sessionPage.session;
    }

    /**
     * @return 返回 session 保存的url
     */
    private String sessionUrl() {
        try (Response response = this.sessionPage.response()) {
            if (response == null) return null;
            return response.request().url().toString();
        }
    }

    /**
     * @return 返回通用timeout设置
     */
    public Double timeout() {
        return chromiumPage.timeouts.getBase();
    }

    /**
     * 把当前页面保存为文件，如果path和name参数都为null，只返回文本
     *
     * @param path 保存路径，为null且name不为null时保存在当前路径
     * @param name 文件名，为null且path不为null时用title属性值
     * @return asPdf为True时返回bytes，否则返回文件文本
     */

    public Object save(String path, String name) {
        return save(path, name, false);
    }

    /**
     * 把当前页面保存为文件，如果path和name参数都为null，只返回文本
     *
     * @param path  保存路径，为null且name不为null时保存在当前路径
     * @param name  文件名，为null且path不为null时用title属性值
     * @param asPdf 为Ture保存为pdf，否则为mhtml且忽略params参数
     * @return asPdf为True时返回bytes，否则返回文件文本
     */

    public Object save(String path, String name, boolean asPdf) {
        return save(path, name, asPdf, new HashMap<>());
    }

    /**
     * 把当前页面保存为文件，如果path和name参数都为null，只返回文本
     *
     * @param path   保存路径，为null且name不为null时保存在当前路径
     * @param name   文件名，为null且path不为null时用title属性值
     * @param asPdf  为Ture保存为pdf，否则为mhtml且忽略params参数
     * @param params pdf生成参数
     * @return asPdf为True时返回bytes，否则返回文件文本
     */

    public Object save(String path, String name, boolean asPdf, Map<String, Object> params) {
        return this.chromiumPage != null ? asPdf ? ChromiumBase.getPdf(this.chromiumPage, path, name, params) : ChromiumBase.getMHtml(this.chromiumPage, path, name) : null;
    }

    @Override
    public Boolean get(String url, boolean showErrMsg, Integer retry, Double interval, Double timeout, Map<String, Object> params) {
        switch (mode) {
            case d:
                return chromiumPage.get(url, showErrMsg, retry, interval, timeout, params);
            case s:
                timeout = timeout == null ? this.hasDriver ? chromiumPage.timeouts.getPageLoad() : this.timeout() : timeout;
                return sessionPage.get(url, showErrMsg, retry, interval, timeout, params);
            default:
                return null;
        }
    }

    /**
     * 用post方式跳转到url 会切换到s模式
     *
     * @param url 目标url
     * @return s模式时返回url是否可用，d模式时返回获取到的Response对象
     */

    public Object post(@NotNull String url) {
        return this.post(url, null);

    }

    /**
     * 用post方式跳转到url 会切换到s模式
     *
     * @param url    目标url
     * @param params 连接参数
     * @return s模式时返回url是否可用，d模式时返回获取到的Response对象
     */

    public Object post(@NotNull String url, Map<String, Object> params) {
        return this.post(url, false, params);

    }

    /**
     * 用post方式跳转到url 会切换到s模式
     *
     * @param url        目标url
     * @param showErrMsg 是否显示和抛出异常
     * @param params     连接参数
     * @return s模式时返回url是否可用，d模式时返回获取到的Response对象
     */

    public Object post(@NotNull String url, boolean showErrMsg, Map<String, Object> params) {
        return this.post(url, showErrMsg, null, params);

    }

    /**
     * 用post方式跳转到url 会切换到s模式
     *
     * @param url        目标url
     * @param showErrMsg 是否显示和抛出异常
     * @param retry      重试次数，为None时使用页面对象retry_times属性值
     * @param params     连接参数
     * @return s模式时返回url是否可用，d模式时返回获取到的Response对象
     */
    public Object post(@NotNull String url, boolean showErrMsg, Integer retry, Map<String, Object> params) {
        return this.post(url, showErrMsg, retry, null, params);
    }

    /**
     * 用post方式跳转到url 会切换到s模式
     *
     * @param url        目标url
     * @param showErrMsg 是否显示和抛出异常
     * @param retry      重试次数，为None时使用页面对象retry_times属性值
     * @param interval   重试间隔（秒），为None时使用页面对象retry_interval属性值
     * @param params     连接参数
     * @return s模式时返回url是否可用，d模式时返回获取到的Response对象
     */

    public Object post(@NotNull String url, boolean showErrMsg, Integer retry, Double interval, Map<String, Object> params) {
        if (Objects.equals(mode, WebMode.d)) {
            this.cookiesToSession();
            sessionPage.post(url, showErrMsg, retry, interval, params);
            return this.response();
        } else {
            return sessionPage.post(url, showErrMsg, retry, interval, params);
        }
    }


    /**
     * 返回第一个符合条件的元素、属性或节点文本
     *
     * @param by      元素的定位信息，可以是元素对象，by，或查询字符串
     * @param index   获取第几个，从1开始，可传入负数获取倒数第几个
     * @param timeout 查找元素超时时间（秒），默认与页面等待时间一致
     * @return 元素对象
     */
    public DrissionElement<?, ?> ele(By by, int index, Double timeout) {
        switch (mode) {
            case d:
                return chromiumPage.ele(by, index, timeout);
            case s:
                return sessionPage.ele(by, index, timeout);
            default:
                return null;
        }
    }

    /**
     * 返回第一个符合条件的元素、属性或节点文本
     *
     * @param locator 元素的定位信息，可以是元素对象，by，或查询字符串
     * @param index   获取第几个，从1开始，可传入负数获取倒数第几个
     * @param timeout 查找元素超时时间（秒），默认与页面等待时间一致
     * @return 元素对象
     */
    public DrissionElement<?, ?> ele(String locator, int index, Double timeout) {
        switch (mode) {
            case d:
                return chromiumPage.ele(locator, index, timeout);
            case s:
                return sessionPage.ele(locator, index, timeout);
            default:
                return null;
        }
    }

    /**
     * 返回页面中所有符合条件的元素
     *
     * @param locator 元素的定位信息，可以是by，或查询字符串
     * @param timeout 查找元素超时时间（秒），默认与页面等待时间一致
     * @return 元素对象的列表
     */
    public List<DrissionElement<?, ?>> eles(String locator, Double timeout) {
        switch (mode) {
            case d:
                List<ChromiumElement> chromiumElements = chromiumPage.eles(locator, timeout);
                return chromiumElements != null ? new ArrayList<>(chromiumElements) : null;
            case s:
                List<SessionElement> sessionElements = sessionPage.eles(locator, timeout);
                return sessionElements != null ? new ArrayList<>(sessionElements) : null;
            default:
                return null;
        }
    }

    /**
     * 返回页面中所有符合条件的元素
     *
     * @param by      元素的定位信息，可以是by，或查询字符串
     * @param timeout 查找元素超时时间（秒），默认与页面等待时间一致
     * @return 元素对象的列表
     */
    public List<DrissionElement<?, ?>> eles(By by, Double timeout) {
        switch (mode) {
            case d:
                List<ChromiumElement> chromiumElements = chromiumPage.eles(by, timeout);
                return chromiumElements != null ? new ArrayList<>(chromiumElements) : null;
            case s:
                List<SessionElement> sessionElements = sessionPage.eles(by, timeout);
                return sessionElements != null ? new ArrayList<>(sessionElements) : null;
            default:
                return null;
        }
    }

    /**
     * 查找第一个符合条件的元素以SessionElement形式返回，d模式处理复杂页面时效率很高
     *
     * @param by    查询元素
     * @param index 获取第几个，从1开始，可传入负数获取倒数第几个
     * @return SessionElement对象
     */
    @Override
    public SessionElement sEle(By by, Integer index) {
        switch (mode) {
            case d:
                return chromiumPage.sEle(by, index);
            case s:
                return sessionPage.sEle(by, index);
            default:
                return null;
        }
    }

    /**
     * 查找第一个符合条件的元素以SessionElement形式返回，d模式处理复杂页面时效率很高
     *
     * @param loc   查询元素
     * @param index 获取第几个，从1开始，可传入负数获取倒数第几个
     * @return SessionElement对象
     */
    @Override
    public SessionElement sEle(String loc, Integer index) {
        switch (mode) {
            case d:
                return chromiumPage.sEle(loc, index);
            case s:
                return sessionPage.sEle(loc, index);
            default:
                return null;
        }
    }

    /**
     * 查找所有符合条件的元素以SessionElement形式返回，d模式处理复杂页面时效率很高
     *
     * @param by 元素的定位信息 查询元素
     * @return SessionElement对象集合
     */
    @Override
    public List<SessionElement> sEles(By by) {
        switch (mode) {
            case d:
                return chromiumPage.sEles(by);
            case s:
                return sessionPage.sEles(by);
            default:
                return null;
        }
    }

    /**
     * 查找所有符合条件的元素以SessionElement形式返回，d模式处理复杂页面时效率很高
     *
     * @param loc 元素的定位信息 查询元素
     * @return SessionElement对象集合
     */
    @Override
    public List<SessionElement> sEles(String loc) {
        switch (mode) {
            case d:
                return chromiumPage.sEles(loc);
            case s:
                return sessionPage.sEles(loc);
            default:
                return null;
        }
    }

    /**
     * 切换模式，接收's'或'd'，除此以外的字符串会切换为 d 模式
     * 如copy_cookies为True，切换时会把当前模式的cookies复制到目标模式
     * 切换后，如果go是True，调用相应的get函数使访问的页面同步
     *
     * @param mode 模式
     */
    public void changeMode(WebMode mode) {
        changeMode(mode, true);
    }

    /**
     * 切换模式，接收's'或'd'，除此以外的字符串会切换为 d 模式
     * 如copy_cookies为True，切换时会把当前模式的cookies复制到目标模式
     * 切换后，如果go是True，调用相应的get函数使访问的页面同步
     *
     * @param mode 模式
     * @param go   是否跳转到原模式的url
     */
    public void changeMode(WebMode mode, boolean go) {
        changeMode(mode, go, true);
    }

    /**
     * 切换模式，接收's'或'd'，除此以外的字符串会切换为 d 模式
     * 如copy_cookies为True，切换时会把当前模式的cookies复制到目标模式
     * 切换后，如果go是True，调用相应的get函数使访问的页面同步
     *
     * @param mode        模式
     * @param go          是否跳转到原模式的url
     * @param copyCookies 是否复制cookies到目标模式
     */
    public void changeMode(WebMode mode, boolean go, boolean copyCookies) {
        if (mode != null && Objects.equals(mode.mode, this.mode.mode)) return;
        this.mode = mode;
        //s模式转d模式
        if (this.mode.equals(WebMode.d)) {
            if (this.chromiumPage.driver == null) {
                this.chromiumPage.connectBrowser(null);
                this.url = this.hasDriver ? null : sessionPage.url();
                this.hasDriver = true;
            }
            String s = this.sessionUrl();
            if (s != null) {
                if (copyCookies) this.cookiesToBrowser();
                if (go) this.get(this.sessionUrl());
            }
            //d模式转s模式
        } else if (this.mode.equals(WebMode.s)) {
            this.hasSession = true;
            this.url = this.sessionUrl();
            if (this.hasDriver) {
                if (copyCookies) this.cookiesToSession();
                if (go && !this.get(sessionPage.url())) {
                    throw new IllegalArgumentException("s模式访问失败，请设置go=False，自行构造连接参数进行访问。");
                }
            }
        }
    }


    /**
     * 把driver对象的cookies复制到session对象
     */
    public void cookiesToSession() {
        cookiesToSession(true);
    }

    /**
     * 把driver对象的cookies复制到session对象
     *
     * @param copyUserAgent 是否复制ua信息
     */
    public void cookiesToSession(boolean copyUserAgent) {
        if (!this.hasSession) return;
        if (copyUserAgent) {
            String o = JSON.parseObject(this.chromiumPage.runCdp("Runtime.evaluate", Map.of("expression", "navigator.userAgent;")).toString()).getJSONObject("result").getString("value");
            this.sessionPage.headers.put("User-Agent", o);
        }
        Web.setBrowserCookies(this.getChromiumPage(), chromiumPage.cookies());

    }

    /**
     * 把session对象的cookies复制到浏览器
     */
    public void cookiesToBrowser() {
        if (!this.hasDriver) return;
        Web.setBrowserCookies(this.getChromiumPage(), sessionPage.cookies());

    }

    @Override
    public List<Cookie> cookies(boolean asMap, boolean allDomains, boolean allInfo) {
        switch (mode) {
            case d:
                return chromiumPage.cookies(asMap, allDomains, allInfo);
            case s:
                return sessionPage.cookies(asMap, allDomains, allInfo);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab() {
        return getTab(null);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param id 要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(String id) {
        return getTab(id, null);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param id    要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @param title 要匹配title的文本，模糊匹配，为None则匹配所有
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(String id, String title) {
        return getTab(id, title, null);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param id    要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @param title 要匹配title的文本，模糊匹配，为None则匹配所有
     * @param url   要匹配url的文本，模糊匹配，为None则匹配所有
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(String id, String title, String url) {
        return getTab(id, title, url, null);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param id      要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @param title   要匹配title的文本，模糊匹配，为None则匹配所有
     * @param url     要匹配url的文本，模糊匹配，为None则匹配所有
     * @param tabType tab类型，可用列表输入多个，如 'page', 'iframe' 等，为None则匹配所有
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(String id, String title, String url, List<String> tabType) {
        return getTab(id, title, url, tabType, true);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param num 要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(int num) {
        return getTab(num, null);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param num   要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @param title 要匹配title的文本，模糊匹配，为None则匹配所有
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(int num, String title) {
        return getTab(num, title, null);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param num   要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @param title 要匹配title的文本，模糊匹配，为None则匹配所有
     * @param url   要匹配url的文本，模糊匹配，为None则匹配所有
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(int num, String title, String url) {
        return getTab(num, title, url, null);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param num     要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @param title   要匹配title的文本，模糊匹配，为None则匹配所有
     * @param url     要匹配url的文本，模糊匹配，为None则匹配所有
     * @param tabType tab类型，可用列表输入多个，如 'page', 'iframe' 等，为None则匹配所有
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(int num, String title, String url, List<String> tabType) {
        return getTab(num, title, url, tabType, false);
    }

    /**
     * 获取一个标签页对象，id_or_num不为None时，后面几个参数无效
     *
     * @param idOrNum 要获取的标签页id或序号，序号从1开始，可传入负数获取倒数第几个，不是视觉排列顺序，而是激活顺序
     * @param title   要匹配title的文本，模糊匹配，为None则匹配所有
     * @param url     要匹配url的文本，模糊匹配，为None则匹配所有
     * @param tabType tab类型，可用列表输入多个，如 'page', 'iframe' 等，为None则匹配所有
     * @return {@link  WebPageTab}对象
     */
    public WebPageTab getTab(Object idOrNum, String title, String url, List<String> tabType, boolean ignored) {
        String tabId = null;
        if (idOrNum != null) {
            if (idOrNum instanceof String) {
                tabId = (String) idOrNum;
            } else if (idOrNum instanceof Integer) {
                tabId = this.chromiumPage.tabIds().get((Integer) idOrNum > 0 ? (Integer) idOrNum - 1 : (Integer) idOrNum);
            } else if (idOrNum instanceof WebPageTab) {
                return (WebPageTab) idOrNum;
            }
        } else if (title == null && url == null && (tabType == null || tabType.isEmpty())) {
            tabId = this.chromiumPage.tabId();
        } else {
            List<Map<String, Object>> tabs = this.chromiumPage.browser().findTabs(title, url, tabType);
            if (tabs != null) {
                tabId = tabs.get(0).get("id").toString();
            } else {
                return null;
            }
        }
        this.lock.lock();
        try {
            return new WebPageTab(this, tabId);
        } finally {
            this.lock.unlock();
        }

    }

    /**
     * 查找符合条件的tab，返回它们组成的列表
     *
     * @return {@link  WebPageTab}对象组成的列表
     */
    public List<WebPageTab> getTabs() {
        return getTabs(null);
    }

    /**
     * 查找符合条件的tab，返回它们组成的列表
     *
     * @param title 要匹配title的文本，模糊匹配，为null则匹配所有
     * @return {@link  WebPageTab}对象组成的列表
     */
    public List<WebPageTab> getTabs(String title) {
        return getTabs(title, null);
    }

    /**
     * 查找符合条件的tab，返回它们组成的列表
     *
     * @param title 要匹配title的文本，模糊匹配，为null则匹配所有
     * @param url   要匹配url的文本，模糊匹配，为null则匹配所有
     * @return {@link  WebPageTab}对象组成的列表
     */
    public List<WebPageTab> getTabs(String title, String url) {
        return getTabs(title, url, null);
    }

    /**
     * 查找符合条件的tab，返回它们组成的列表
     *
     * @param title   要匹配title的文本，模糊匹配，为null则匹配所有
     * @param url     要匹配url的文本，模糊匹配，为null则匹配所有
     * @param tabType tab类型，可用列表输入多个，如 'page', 'iframe' 等，为None则匹配所有
     * @return {@link  WebPageTab}对象组成的列表
     */
    public List<WebPageTab> getTabs(String title, String url, List<String> tabType) {
        this.lock.lock();
        try {
            return this.chromiumPage.getBrowser().findTabs(title, url, tabType).stream().map(tab -> new WebPageTab(this, tab.get("id").toString())).collect(Collectors.toList());
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 新建一个标签页
     *
     * @return 新标签页对象
     */
    public WebPageTab newTab() {
        return newTab(null);
    }

    /**
     * 新建一个标签页
     *
     * @param url 新标签页跳转到的网址
     * @return 新标签页对象
     */
    public WebPageTab newTab(String url) {
        return newTab(url, false);
    }

    /**
     * 新建一个标签页
     *
     * @param url       新标签页跳转到的网址
     * @param newWindow 是否在新窗口打开标签页
     * @return 新标签页对象
     */
    public WebPageTab newTab(String url, boolean newWindow) {
        return newTab(url, newWindow, false);
    }

    /**
     * 新建一个标签页
     *
     * @param url        新标签页跳转到的网址
     * @param newWindow  是否在新窗口打开标签页
     * @param background 是否不激活新标签页，如newWindow为True则无效
     * @return 新标签页对象
     */
    public WebPageTab newTab(String url, boolean newWindow, boolean background) {
        return newTab(url, newWindow, background, false);
    }

    /**
     * 新建一个标签页
     *
     * @param url        新标签页跳转到的网址
     * @param newWindow  是否在新窗口打开标签页
     * @param background 是否不激活新标签页，如newWindow为True则无效
     * @param newContext 是否创建新的上下文
     * @return 新标签页对象
     */
    public WebPageTab newTab(String url, boolean newWindow, boolean background, boolean newContext) {
        WebPageTab webPageTab = new WebPageTab(this, chromiumPage._newTab(newWindow, background, newContext));
        if (url != null) webPageTab.get(url);
        return webPageTab;

    }

    /**
     * 关闭driver及浏览器  并且切换到d模式
     */
    public void closeDriver() {
        if (this.hasDriver) {
            this.changeMode(WebMode.s);
            try {
                this.chromiumPage.runCdp("Browser.close");
            } catch (Exception ignored) {
            }
            this.chromiumPage.driver.stop();
            this.chromiumPage.driver = null;
            this.hasDriver = false;
        }
    }

    /**
     * 关闭session  并且切换到s模式
     */
    public void closeSession() {
        if (this.hasDriver) {
            this.changeMode(WebMode.d);
            Response response = this.sessionPage.response;
            if (response != null) try {
                response.close();
            } catch (Exception ignored) {
            }
            this.sessionPage.session = null;
            this.sessionPage.response = null;
            this.hasSession = false;
        }
    }

    /**
     * 关闭标签页和Session
     */

    public void close() {
        if (this.hasDriver) this.chromiumPage.quit();
        if (this.sessionPage.session != null) {
            Response response = this.sessionPage.response;
            if (response != null) try {
                response.close();
            } catch (Exception ignored) {
            }
        }

    }

    /**
     * 断开连接
     */
    public void stop() {
        if (this.hasDriver) this.chromiumPage.stop();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (this.hasDriver) this.chromiumPage.disconnect();
    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     */
    public void closeTabs() {
        closeTabs(new String[]{});
    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     *
     * @param ids 要关闭的标签页对象或id，可传入列表或元组，为None时关闭当前页
     */
    public void closeTabs(String[] ids) {
        closeTabs(ids, false);
    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     *
     * @param ids 要关闭的标签页对象或id，可传入列表或元组，为None时关闭当前页
     */
    public void closeTabs(String ids) {
        closeTabs(ids, false);
    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     *
     * @param ids    要关闭的标签页对象或id，可传入列表或元组，为None时关闭当前页
     * @param others 是否关闭指定标签页之外的
     */
    public void closeTabs(String[] ids, boolean others) {
        if (ids.length == 0) ids = new String[]{this.tabId()};
        List<String> tabs = Arrays.asList(ids);
        if (this.chromiumPage != null) this.chromiumPage.closeTabs(others, tabs);
    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     *
     * @param ids    要关闭的标签页对象或id，可传入列表或元组，为None时关闭当前页
     * @param others 是否关闭指定标签页之外的
     */
    public void closeTabs(String ids, boolean others) {
        if (ids == null) ids = this.tabId();
        List<String> tabs = Collections.singletonList(ids);
        if (this.chromiumPage != null) this.chromiumPage.closeTabs(others, tabs);


    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     *
     * @param chromiumTabs 要关闭的标签页对象或id，可传入列表或元组，为None时关闭当前页
     */
    public void closeTabs(ChromiumTab[] chromiumTabs) {
        closeTabs(chromiumTabs, false);

    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     *
     * @param chromiumTab 要关闭的标签页对象或id，可传入列表或元组，为None时关闭当前页
     */
    public void closeTabs(ChromiumTab chromiumTab) {
        closeTabs(chromiumTab, false);
    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     *
     * @param chromiumTabs 要关闭的标签页对象或id，可传入列表或元组，为None时关闭当前页
     * @param others       是否关闭指定标签页之外的
     */
    public void closeTabs(ChromiumTab[] chromiumTabs, boolean others) {
        List<String> tabs = new ArrayList<>();
        if (chromiumTabs.length == 0) tabs.add(this.tabId());
        for (ChromiumTab chromiumTab : chromiumTabs) tabs.add(chromiumTab.tabId());
        if (this.chromiumPage != null) this.chromiumPage.closeTabs(others, tabs);

    }

    /**
     * 关闭传入的标签页，默认关闭当前页。可传入多个
     *
     * @param chromiumTab 要关闭的标签页对象或id，可传入列表或元组，为None时关闭当前页
     * @param others      是否关闭指定标签页之外的
     */
    public void closeTabs(ChromiumTab chromiumTab, boolean others) {
        List<String> tabs = new ArrayList<>();
        if (this.chromiumPage != null) {
            tabs.add((chromiumTab == null ? this.chromiumPage : chromiumTab).tabId());
            this.chromiumPage.closeTabs(others, tabs);
        }
    }

    /**
     * 关闭浏览器
     */
    public void quit() {
        this.quit(5.0);
    }

    /**
     * 关闭浏览器
     *
     * @param timeout 等待浏览器关闭超时时间（秒）
     */
    public void quit(double timeout) {
        this.quit(timeout, true);
    }

    /**
     * 关闭浏览器
     *
     * @param timeout 等待浏览器关闭超时时间（秒）
     * @param force   关闭超时是否强制终止进程
     */
    public void quit(double timeout, boolean force) {
        if (this.chromiumPage != null) this.chromiumPage.getBrowser().quit(timeout, force);
    }

    /**
     * 返回页面中符合条件的元素、属性或节点文本，默认返回第一个
     *
     * @param by       查询元素
     * @param timeout  查找超时时间（秒）
     * @param index    获取第几个，从1开始，可传入负数获取倒数第几个 如果是null则是返回全部
     * @param relative WebPage用的表示是否相对定位的参数
     * @param raiseErr 找不到元素是是否抛出异常，为null时根据全局设置
     * @return 元素对象
     */
    @Override
    protected List<DrissionElement<?, ?>> findElements(By by, Double timeout, Integer index, Boolean relative, Boolean raiseErr) {
        switch (mode) {
            case d:
                List<ChromiumElement> chromiumElements = chromiumPage.findElements(by, timeout, index, relative, raiseErr);
                return chromiumElements != null ? new ArrayList<>(chromiumElements) : null;
            case s:
                List<SessionElement> elements = sessionPage.findElements(by, timeout, index, relative, raiseErr);
                return elements != null ? new ArrayList<>(elements) : null;
            default:
                return null;
        }
    }

    /**
     * 返回页面中符合条件的元素、属性或节点文本，默认返回第一个
     *
     * @param loc      查询元素
     * @param timeout  查找超时时间（秒）
     * @param index    获取第几个，从1开始，可传入负数获取倒数第几个 如果是null则是返回全部
     * @param relative WebPage用的表示是否相对定位的参数
     * @param raiseErr 找不到元素是是否抛出异常，为null时根据全局设置
     * @return 元素对象
     */
    @Override
    protected List<DrissionElement<?, ?>> findElements(String loc, Double timeout, Integer index, Boolean relative, Boolean raiseErr) {
        switch (mode) {
            case d:
                List<ChromiumElement> chromiumElements = chromiumPage.findElements(loc, timeout, index, relative, raiseErr);
                return chromiumElements != null ? new ArrayList<>(chromiumElements) : null;
            case s:
                List<SessionElement> elements = sessionPage.findElements(loc, timeout, index, relative, raiseErr);
                return elements != null ? new ArrayList<>(elements) : null;
            default:
                return null;
        }
    }

    /**
     * 关闭浏览器和Session
     *
     * @param timeout 等待浏览器关闭超时时间
     * @param force   关闭超时是否强制终止进程
     */
    public void quit(Double timeout, boolean force) {
        if (this.hasSession) {
            this.sessionPage.close();
            this.sessionPage.session = null;
            this.sessionPage.response = null;
            this.hasSession = false;
        }
        if (this.hasDriver) {
            this.chromiumPage.quit(timeout, force);
            this.chromiumPage.driver = null;
            this.hasDriver = false;
        }
    }

    @Override
    public String toString() {
        return "<WebPage browserId=" + this.chromiumPage.browser().getId() + " tabId=" + this.chromiumPage.tabId() + ">";
    }

    /**
     * 克隆
     *
     * @param cloneNumber 克隆数量
     * @return 集合
     */
    public List<WebPage> copy(int cloneNumber) {
        return IntStream.range(0, cloneNumber < 0 ? 1 : cloneNumber).mapToObj(i -> copy()).collect(Collectors.toList());
    }

    /**
     * 克隆
     *
     * @return 单个
     */
    public WebPage copy() {
        ChromiumOptions chromiumOptions = this.chromiumPage.getChromiumOptions().copy();
        chromiumOptions.autoPort(true, chromiumOptions.getTmpPath() + UUID.randomUUID().toString().substring(0, 5));
        WebPage webPage = new WebPage(this.mode, this.timeout(), chromiumOptions.copy(), this.sessionPage.sessionOptions.copy());
        String url1 = this.url();
        if (url1 != null) webPage.get(url1);
        return webPage;
    }
}
