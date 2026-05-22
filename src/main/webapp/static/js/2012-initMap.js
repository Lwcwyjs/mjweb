    var T,
        baidu = T = baidu || {version: "1.3.9"}; 

    //提出guid，防止在与老版本Tangram混用时
    //在下一行错误的修改window[undefined]
    baidu.guid = "$BAIDU$";

    //Tangram可能被放在闭包中
    //一些页面级别唯一的属性，需要挂载在window[baidu.guid]上
    window[baidu.guid] = window[baidu.guid] || {};


    /**
     * @namespace baidu.array 操作数组的方法。
     */

    baidu.array = baidu.array || {};


    /**
     * 遍历数组中所有元素
     * @name baidu.array.each
     * @function
     * @grammar baidu.array.each(source, iterator[, thisObject])
     * @param {Array} source 需要遍历的数组
     * @param {Function} iterator 对每个数组元素进行调用的函数，该函数有两个参数，第一个为数组元素，第二个为数组索引值，function (item, index)。
     * @param {Object} [thisObject] 函数调用时的this指针，如果没有此参数，默认是当前遍历的数组
     * @remark
     * each方法不支持对Object的遍历,对Object的遍历使用baidu.object.each 。
     * @shortcut each
     * @meta standard
     *             
     * @returns {Array} 遍历的数组
     */
     
    baidu.each = baidu.array.forEach = baidu.array.each = function (source, iterator, thisObject) {
        var returnValue, item, i, len = source.length;
        
        if ('function' == typeof iterator) {
            for (i = 0; i < len; i++) {
                item = source[i];
                //TODO
                //此处实现和标准不符合，标准中是这样说的：
                //If a thisObject parameter is provided to forEach, it will be used as the this for each invocation of the callback. If it is not provided, or is null, the global object associated with callback is used instead.
                returnValue = iterator.call(thisObject || source, item, i);
        
                if (returnValue === false) {
                    break;
                }
            }
        }
        return source;
    };

    /**
     * @namespace baidu.dom 操作dom的方法。
     */
    baidu.dom = baidu.dom || {};

    /**
     * 从文档中获取指定的DOM元素
     * @name baidu.dom.g
     * @function
     * @grammar baidu.dom.g(id)
     * @param {string|HTMLElement} id 元素的id或DOM元素
     * @shortcut g,T.G
     * @meta standard
     * @see baidu.dom.q
     *             
     * @returns {HTMLElement|null} 获取的元素，查找不到时返回null,如果参数不合法，直接返回参数
     */
    baidu.dom.g = function (id) {
        if ('string' == typeof id || id instanceof String) {
            return document.getElementById(id);
        } else if (id && id.nodeName && (id.nodeType == 1 || id.nodeType == 9)) {
            return id;
        }
        return null;
    };

    // 声明快捷方法
    baidu.g = baidu.G = baidu.dom.g;


    /**
     * 获取目标元素的直接子元素列表
     * @name baidu.dom.children
     * @function
     * @grammar baidu.dom.children(element)
     * @param {HTMLElement|String} element 目标元素或目标元素的id
     * @meta standard
     *             
     * @returns {Array} 目标元素的子元素列表，没有子元素时返回空数组
     */
    baidu.dom.children = function (element) {
        element = baidu.dom.g(element);

        for (var children = [], tmpEl = element.firstChild; tmpEl; tmpEl = tmpEl.nextSibling) {
            if (tmpEl.nodeType == 1) {
                children.push(tmpEl);
            }
        }
        
        return children;    
    };


    /**
     * 移除目标元素的className
     * @name baidu.dom.removeClass
     * @function
     * @grammar baidu.dom.removeClass(element, className)
     * @param {HTMLElement|string} element 目标元素或目标元素的id
     * @param {string} className 要移除的className，允许同时移除多个class，中间使用空白符分隔
     * @remark
     * 使用者应保证提供的className合法性，不应包含不合法字符，className合法字符参考：http://www.w3.org/TR/CSS2/syndata.html。
     * @shortcut removeClass
     * @meta standard
     * @see baidu.dom.addClass
     *             
     * @returns {HTMLElement} 目标元素
     */
    baidu.dom.removeClass = function (element, className) {
        element = baidu.dom.g(element);

        var oldClasses = element.className.split(/\s+/),
            newClasses = className.split(/\s+/),
            lenOld,
            lenDel = newClasses.length,
            j,
            i = 0;
        //考虑到同时删除多个className的应用场景概率较低,故放弃进一步性能优化 
        // by rocy @1.3.4
        for (; i < lenDel; ++i){
            for(j = 0, lenOld = oldClasses.length; j < lenOld; ++j){
                if(oldClasses[j] == newClasses[i]){
                    oldClasses.splice(j, 1);
                    break;
                }
            }
        }
        element.className = oldClasses.join(' ');
        return element;
    };

    // 声明快捷方法
    baidu.removeClass = baidu.dom.removeClass;

    /**
     * @namespace baidu.event 屏蔽浏览器差异性的事件封装。
     * @property target  事件的触发元素
     * @property pageX   鼠标事件的鼠标x坐标
     * @property pageY   鼠标事件的鼠标y坐标
     * @property keyCode  键盘事件的键值
     */
    baidu.event = baidu.event || {};


    /**
     * 事件监听器的存储表
     * @private
     * @meta standard
     */
    baidu.event._listeners = baidu.event._listeners || [];


    /**
     * 为目标元素添加事件监听器
     * @name baidu.event.on
     * @function
     * @grammar baidu.event.on(element, type, listener)
     * @param {HTMLElement|string|window} element 目标元素或目标元素id
     * @param {string} type 事件类型
     * @param {Function} listener 需要添加的监听器
     * @remark
     * 
    1. 不支持跨浏览器的鼠标滚轮事件监听器添加

    2. 改方法不为监听器灌入事件对象，以防止跨iframe事件挂载的事件对象获取失败
        
     * @shortcut on
     * @meta standard
     * @see baidu.event.un
     *             
     * @returns {HTMLElement|window} 目标元素
     */
    baidu.event.on = function (element, type, listener) {
        type = type.replace(/^on/i, '');
        element = baidu.dom.g(element);

        var realListener = function (ev) {
                // 1. 这里不支持EventArgument,  原因是跨frame的事件挂载
                // 2. element是为了修正this
                listener.call(element, ev);
            },
            lis = baidu.event._listeners,
            filter = baidu.event._eventFilter,
            afterFilter,
            realType = type;
        type = type.toLowerCase();
        // filter过滤
        if(filter && filter[type]){
            afterFilter = filter[type](element, type, realListener);
            realType = afterFilter.type;
            realListener = afterFilter.listener;
        }
        
        // 事件监听器挂载
        if (element.addEventListener) {
            element.addEventListener(realType, realListener, false);
        } else if (element.attachEvent) {
            element.attachEvent('on' + realType, realListener);
        }
      
        // 将监听器存储到数组中
        lis[lis.length] = [element, type, listener, realListener, realType];
        return element;
    };

    // 声明快捷方法
    baidu.on = baidu.event.on;