package com.ll.drissonPage.base;

/**
 * 驱动
 *
 * @author 陆
 * @address <a href="https://t.me/blanksig"/>click
 * @original DrissionPage
 */

@Deprecated
public class Driver_Netty {
//    /**
//     * 标签id
//     */
//    @Getter
//    private final String id;
//    /**
//     * 浏览器连接地址
//     */
//    @Getter
//    private final String address;
//    /**
//     * 标签页类型
//     */
//    @Getter
//    private final String type;
//    @Getter
//    @Setter
//    private boolean debug;
//    @Getter
//    private final String websocketUrl;
//    private final AtomicInteger curId;
//
//    /**
//     * 会话返回值
//     */
//    private final LinkedBlockingQueue<String> webSocketMsg = new LinkedBlockingQueue<>();
//    private final Thread recvThread;
//    private final Thread handleEventThread;
//    @Getter
//    private AtomicBoolean stopped;
//    private final BlockingQueue<TreeMap<String, Object>> eventQueue;
//    private final BlockingQueue<TreeMap<String, Object>> immediateEventQueue;
//    private final TreeMap<String, MyRunnable> eventHandlers;
//    private final TreeMap<String, MyRunnable> immediateEventHandlers;
//    private final TreeMap<Integer, BlockingQueue<TreeMap<String, Object>>> methodResults;
//    /**
//     * 创建这个驱动的对象
//     */
//    @Getter
//    @Setter
//    private Occupant occupant;
//    private boolean alertFlag;
//    private SocketClient socketClient;
//    private Thread handleImmediateEventThread;
//
//    public Driver_Netty(String tabId, String tabType, String address) {
//        this(tabId, tabType, address, null);
//    }
//
//    /**
//     * 驱动
//     *
//     * @param tabId   标签id
//     * @param tabType 标签页类型
//     * @param address 浏览器连接地址
//     */
//    public Driver_Netty(String tabId, String tabType, String address, Occupant occupant) {
//        this.id = tabId;
//        this.address = address;
//        this.type = tabType;
//        this.occupant = occupant;
//        this.debug = false;
//        this.alertFlag = false;
//        this.websocketUrl = "ws://" + address + "/devtools/" + tabType + "/" + tabId;
//        this.curId = new AtomicInteger(0);
//
//
//        this.recvThread = new Thread(this::recvLoop);
//        this.handleEventThread = new Thread(this::handleEventLoop);
//        this.recvThread.setDaemon(true);
//        this.handleEventThread.setDaemon(true);
//        this.handleImmediateEventThread = null;
//
//        this.stopped = new AtomicBoolean();
//
//        this.eventHandlers = new TreeMap<>();
//        this.immediateEventHandlers = new TreeMap<>();
//        this.methodResults = new TreeMap<>();
//        this.eventQueue = new LinkedBlockingQueue<>();
//        this.immediateEventQueue = new LinkedBlockingQueue<>();
//        start();
//    }
//
//    /**
//     * 发送信息到浏览器，并返回浏览器返回的信息
//     *
//     * @param message 发送给浏览器的数据
//     * @param timeout 超时时间，为null表示无时间
//     * @return 浏览器返回的数据
//     */
//    private JSONObject send(Map<String, Object> message, double timeout) {
//        message = new HashMap<>(message);
//        int wsId = curId.incrementAndGet();
//        message.put("id", wsId);
//
//        if (debug) System.out.println("发->" + message);
//        //计算等待时间
//        long endTime = (long) (System.currentTimeMillis() + timeout * 1000L);
//        LinkedBlockingQueue<TreeMap<String, Object>> value = new LinkedBlockingQueue<>();
//        methodResults.put(wsId, value);
//        try {
//            SendMsg.send(socketClient.getChannel(), message);
//            if (timeout == 0) {
//                methodResults.remove(wsId);
//                return new JSONObject(Map.of("id", wsId, "result", Map.of()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            methodResults.remove(wsId);
//            return new JSONObject(Map.of("error", Map.of("message", "connection disconnected"), "type", "connection_error"));
//        }
//        int i = 5;
//        long endTimes = System.currentTimeMillis() + 1000L;
//        while (!stopped.get()) {
//            try {
//                Map<String, Object> result = methodResults.get(wsId).poll(5, TimeUnit.SECONDS);
//                if (result == null) {
//                    if (!stopped.get() && System.currentTimeMillis() < endTimes) continue;
//                    if (!stopped.get() && i > 0 && System.currentTimeMillis() > endTimes) {
//                        i--;
//                        endTimes = System.currentTimeMillis() + 1000L;
//                        System.out.println("超时或者丢包，重新发送:->" + message);
//                        SendMsg.send(socketClient.getChannel(), message);
//                        continue;
//                    }
//                }
//                methodResults.remove(wsId);
//                if (result == null) throw new NullPointerException();
//                return new JSONObject(result);
//            } catch (InterruptedException | NullPointerException | IllegalArgumentException e) {
////                e.printStackTrace();
//                String string = message.get("method").toString();
//                if (alertFlag && string.startsWith("Input.") || string.startsWith("Runtime.")) {
//                    return new JSONObject(Map.of("error", Map.of("message", "alert exists."), "type", "alert_exists"));
//                }
//                if (timeout > 0 && System.currentTimeMillis() > endTime) {
//                    methodResults.remove(wsId);
//                    return alertFlag ? new JSONObject(Map.of("error", Map.of("message", "alert exists."), "type", "alert_exists")) : new JSONObject(Map.of("error", Map.of("message", "timeout"), "type", "timeout"));
//                }
//            }
//        }
//
//        return new JSONObject(Map.of("error", Map.of("message", "connection disconnected"), "type", "connection_error"));
//    }
//
//    /**
//     * 接收浏览器信息的守护线程方法
//     */
//    private void recvLoop() {
//        while (!stopped.get()) {
//            JSONObject msg;
//            try {
//                String andSet = webSocketMsg.getAndSet(null);
//                if (andSet != null) {
//                    msg = JSONObject.parseObject(andSet);
//                } else continue;
//            } catch (Exception e) {
//                if (stop()) return;
//                return;
//
//            }
//            if (debug) System.out.println("<-收" + msg);
//
//            if (msg.containsKey("method")) {
//                if (msg.getString("method").startsWith("Page.javascriptDialog")) {
//                    alertFlag = msg.getString("method").endsWith("Opening");
//                }
//                MyRunnable function = immediateEventHandlers.get(msg.getString("method"));
//                if (function != null) {
//                    this.handleImmediateEvent(function, msg.getOrDefault("params", new HashMap<>()));
//                } else {
//                    try {
//                        eventQueue.put(new TreeMap<>(msg));
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            } else {
//                Integer integer = msg.getInteger("id");
//                if (integer != null && methodResults.containsKey(integer)) {
//                    try {
//                        this.methodResults.get(integer).put(new TreeMap<>(msg));
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                } else if (debug) {
//                    System.out.println("未知错误->" + msg);
//                }
//            }
//
//        }
//    }
//
//    /**
//     * 当接收到浏览器信息，执行已绑定的方法
//     */
//    private void handleEventLoop() {
//        while (!stopped.get()) {
//            Map<String, Object> event;
//            try {
//                event = eventQueue.poll(1, TimeUnit.SECONDS);
//                if (event == null) continue;
//            } catch (InterruptedException e) {
//                continue;
//            } catch (IllegalMonitorStateException e) {
//                return;
//            }
//            MyRunnable function = eventHandlers.get(event.get("method").toString());
//            if (function != null) {
//                function.setMessage(event.get("params"));
//                function.run();
//            }
//        }
//    }
//
//    private void handleImmediateEventLoop() {
//        while (!stopped.get() && !immediateEventQueue.isEmpty()) {
//            Map<String, Object> event;
//            try {
//                event = immediateEventQueue.poll(2, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                continue;
//            }
//            if (event != null) {
//                MyRunnable function = (MyRunnable) event.get("method");
//                if (function != null) {
//                    function.setMessage(event.get("params"));
//                    function.run();
//                }
//
//            }
//
//        }
//    }
//
//    /**
//     * 处理立即执行的动作
//     *
//     * @param function 要运行下方法
//     * @param params   方法参数
//     */
//    private void handleImmediateEvent(MyRunnable function, Object params) {
//        TreeMap<String, Object> func = new TreeMap<>();
//        func.put("method", function);
//        func.put("params", params);
//        immediateEventQueue.add(func);
//
//        if (handleImmediateEventThread == null || !handleImmediateEventThread.isAlive()) {
//            handleImmediateEventThread = new Thread(this::handleImmediateEventLoop);
//            handleImmediateEventThread.setDaemon(true);
//            handleImmediateEventThread.start();
//        }
//    }
//
//    /**
//     * 执行cdp方法
//     *
//     * @param method 方法
//     * @return 执行结果
//     */
//    public Object run(String method) {
//        return run(method, new HashMap<>());
//    }
//
//    /**
//     * 执行cdp方法
//     *
//     * @param method 方法
//     * @param params 参数
//     * @return 执行结果
//     */
//    public Object run(String method, Map<String, Object> params) {
//        if (stopped.get())
//            return JSON.toJSONString(Map.of("error", "connection disconnected", "type", "connection_error"));
//        params = new HashMap<>(params);
//        Object timeout1 = params.remove("_timeout");
//        double timeout = timeout1 != null ? Float.parseFloat(timeout1.toString()) : Settings.cdpTimeout;
//
//        JSONObject result = this.send(Map.of("method", method, "params", params), timeout);
//        if (!result.containsKey("result") && result.containsKey("error")) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("error", result.getJSONObject("error").get("message"));
//            map.put("type", result.getOrDefault("type", "call_method_error"));
//            map.put("method", method);
//            map.put("args", params);
//            map.put("timeout", timeout);
//            return JSON.toJSONString(map);
//        } else {
//            return JSON.toJSONString(result.get("result"));
//        }
//    }
//
//
//    /**
//     * 启动连接
//     */
//    private void start() {
//        this.stopped.set(false);
//        try {
//            socketClient = new SocketClient(new URI(this.websocketUrl));
//            socketClient.connect();
//            socketClient.setMessageReceiver(webSocketMsg);
//
//            recvThread.start();
//            handleEventThread.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//            stop();
//        }
//    }
//
//    /**
//     * 中断连接
//     */
//    public boolean stop() {
//        stop1();
//        while (this.recvThread.isAlive() || this.handleEventThread.isAlive()) {
//            try {
//                this.handleEventThread.stop();
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 中断连接
//     */
//    private void stop1() {
//        if (debug) System.out.println("关闭");
//        if (stopped.get()) return;
//        stopped.set(!stopped.get());
//        if (socketClient != null) {
//            socketClient.stop();
//            socketClient = null;
//        }
//
//        try {
//            while (!eventQueue.isEmpty()) {
//                Map<String, Object> event = eventQueue.poll();
//                MyRunnable method = eventHandlers.get(event.get("method").toString());
//                if (method != null) {
//                    method.setMessage(event.get("params"));
//                    method.run();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        eventHandlers.clear();
//        methodResults.clear();
//        eventQueue.clear();
//        if (occupant != null) occupant.onDisconnect();
//    }
//
//    public void setCallback(String event, MyRunnable callback) {
//        setCallback(event, callback, false);
//    }
//
//    /**
//     * 绑定cdp event和回调方法
//     *
//     * @param event     方法名称
//     * @param callback  绑定到cdp event的回调方法
//     * @param immediate 是否要立即处理的动作
//     */
//    public void setCallback(String event, MyRunnable callback, boolean immediate) {
//        TreeMap<String, MyRunnable> handler = immediate ? immediateEventHandlers : eventHandlers;
//        if (callback != null) handler.put(event, callback);
//        else handler.remove(event);
//    }

}