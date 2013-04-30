var TrimPath; (function() {
    if (TrimPath == null) {
        TrimPath = new Object()

    }
    if (TrimPath.evalEx == null) {
        TrimPath.evalEx = function(src) {
            return eval(src)

        }

    }
    var UNDEFINED;
    if (Array.prototype.pop == null) {
        Array.prototype.pop = function() {
            if (this.length === 0) {
                return UNDEFINED

            }
            return this[--this.length]

        }

    }
    if (Array.prototype.push == null) {
        Array.prototype.push = function() {
            for (var i = 0; i < arguments.length; ++i) {
                this[this.length] = arguments[i]

            }
            return this.length

        }

    }
    TrimPath.parseTemplate = function(tmplContent, optTmplName, optEtc) {
        if (optEtc == null) {
            optEtc = TrimPath.parseTemplate_etc

        }
        var funcSrc = parse(tmplContent, optTmplName, optEtc);
        var func = TrimPath.evalEx(funcSrc, optTmplName, 1);
        if (func != null) {
            return new optEtc.Template(optTmplName, tmplContent, funcSrc, func, optEtc)

        }
        return null

    };
    try {
        String.prototype.process = function(context, optFlags) {
            var template = TrimPath.parseTemplate(this, null);
            if (template != null) {
                return template.process(context, optFlags)

            }
            return this

        }

    } catch(e) {}
    TrimPath.parseTemplate_etc = {};
    TrimPath.parseTemplate_etc.statementTag = "forelse|for|if|elseif|else|var|macro";
    TrimPath.parseTemplate_etc.statementDef = {
        "if": {
            delta: 1,
            prefix: "if (",
            suffix: ") {",
            paramMin: 1

        },
        "else": {
            delta: 0,
            prefix: "} else {"

        },
        elseif: {
            delta: 0,
            prefix: "} else if (",
            suffix: ") {",
            paramDefault: "true"

        },
        "/if": {
            delta: -1,
            prefix: "}"

        },
        "for": {
            delta: 1,
            paramMin: 3,
            prefixFunc: function(stmtParts, state, tmplName, etc) {
                if (stmtParts[2] != "in") {
                    throw new etc.ParseError(tmplName, state.line, "bad for loop statement: " + stmtParts.join(" "))

                }
                var iterVar = stmtParts[1];
                var listVar = "__LIST__" + iterVar;
                return ["var ", listVar, " = ", stmtParts[3], ";", "var __LENGTH_STACK__;", "if (typeof(__LENGTH_STACK__) == 'undefined' || !__LENGTH_STACK__.length) __LENGTH_STACK__ = new Array();", "__LENGTH_STACK__[__LENGTH_STACK__.length] = 0;", "if ((", listVar, ") != null) { ", "var ", iterVar, "_ct = 0;", "for (var ", iterVar, "_index in ", listVar, ") { ", iterVar, "_ct++;", "if (typeof(", listVar, "[", iterVar, "_index]) == 'function') {continue;}", "__LENGTH_STACK__[__LENGTH_STACK__.length - 1]++;", "var ", iterVar, " = ", listVar, "[", iterVar, "_index];"].join("")

            }

        },
        forelse: {
            delta: 0,
            prefix: "} } if (__LENGTH_STACK__[__LENGTH_STACK__.length - 1] == 0) { if (",
            suffix: ") {",
            paramDefault: "true"

        },
        "/for": {
            delta: -1,
            prefix: "} }; delete __LENGTH_STACK__[__LENGTH_STACK__.length - 1];"

        },
        "var": {
            delta: 0,
            prefix: "var ",
            suffix: ";"

        },
        macro: {
            delta: 1,
            prefixFunc: function(stmtParts, state, tmplName, etc) {
                var macroName = stmtParts[1].split("(")[0];
                return ["var ", macroName, " = function", stmtParts.slice(1).join(" ").substring(macroName.length), "{ var _OUT_arr = []; var _OUT = { write: function(m) { if (m) _OUT_arr.push(m); } }; "].join("")

            }

        },
        "/macro": {
            delta: -1,
            prefix: " return _OUT_arr.join(''); };"

        }

    };
    TrimPath.parseTemplate_etc.modifierDef = {
        eat: function(v) {
            return ""

        },
        escape: function(s) {
            return String(s).replace(/&/g, "&").replace(/</g, "<").replace(/>/g, ">")

        },
        capitalize: function(s) {
            return String(s).toUpperCase()

        },
        "default": function(s, d) {
            return s != null ? s: d

        }

    };
    TrimPath.parseTemplate_etc.modifierDef.h = TrimPath.parseTemplate_etc.modifierDef.escape;
    TrimPath.parseTemplate_etc.Template = function(tmplName, tmplContent, funcSrc, func, etc) {
        this.process = function(context, flags) {
            if (context == null) {
                context = {}

            }
            if (context._MODIFIERS == null) {
                context._MODIFIERS = {}

            }
            if (context.defined == null) {
                context.defined = function(str) {
                    return (context[str] != undefined)

                }

            }
            for (var k in etc.modifierDef) {
                if (context._MODIFIERS[k] == null) {
                    context._MODIFIERS[k] = etc.modifierDef[k]

                }

            }
            if (flags == null) {
                flags = {}

            }
            var resultArr = [];
            var resultOut = {
                write: function(m) {
                    resultArr.push(m)

                }

            };
            try {
                func(resultOut, context, flags)

            } catch(e) {
                if (flags.throwExceptions == true) {
                    throw e

                }
                var result = new String(resultArr.join("") + "[ERROR: " + e.toString() + (e.message ? "; " + e.message: "") + "]");
                result.exception = e;
                return result

            }
            return resultArr.join("")

        };
        this.name = tmplName;
        this.source = tmplContent;
        this.sourceFunc = funcSrc;
        this.toString = function() {
            return "TrimPath.Template [" + tmplName + "]"

        }

    };
    TrimPath.parseTemplate_etc.ParseError = function(name, line, message) {
        this.name = name;
        this.line = line;
        this.message = message

    };
    TrimPath.parseTemplate_etc.ParseError.prototype.toString = function() {
        return ("TrimPath template ParseError in " + this.name + ": line " + this.line + ", " + this.message)

    };
    var parse = function(body, tmplName, etc) {
        body = cleanWhiteSpace(body);
        var funcText = ["var TrimPath_Template_TEMP = function(_OUT, _CONTEXT, _FLAGS) { with (_CONTEXT) {"];
        var state = {
            stack: [],
            line: 1

        };
        var endStmtPrev = -1;
        while (endStmtPrev + 1 < body.length) {
            var begStmt = endStmtPrev;
            begStmt = body.indexOf("{", begStmt + 1);
            while (begStmt >= 0) {
                var endStmt = body.indexOf("}", begStmt + 1);
                var stmt = body.substring(begStmt, endStmt);
                var blockrx = stmt.match(/^\{(cdata|minify|eval)/);
                if (blockrx) {
                    var blockType = blockrx[1];
                    var blockMarkerBeg = begStmt + blockType.length + 1;
                    var blockMarkerEnd = body.indexOf("}", blockMarkerBeg);
                    if (blockMarkerEnd >= 0) {
                        var blockMarker;
                        if (blockMarkerEnd - blockMarkerBeg <= 0) {
                            blockMarker = "{/" + blockType + "}"

                        } else {
                            blockMarker = body.substring(blockMarkerBeg + 1, blockMarkerEnd)

                        }
                        var blockEnd = body.indexOf(blockMarker, blockMarkerEnd + 1);
                        if (blockEnd >= 0) {
                            emitSectionText(body.substring(endStmtPrev + 1, begStmt), funcText);
                            var blockText = body.substring(blockMarkerEnd + 1, blockEnd);
                            if (blockType == "cdata") {
                                emitText(blockText, funcText)

                            } else {
                                if (blockType == "minify") {
                                    emitText(scrubWhiteSpace(blockText), funcText)

                                } else {
                                    if (blockType == "eval") {
                                        if (blockText != null && blockText.length > 0) {
                                            funcText.push("_OUT.write( (function() { " + blockText + " })() );")

                                        }

                                    }

                                }

                            }
                            begStmt = endStmtPrev = blockEnd + blockMarker.length - 1

                        }

                    }

                } else {
                    if (body.charAt(begStmt - 1) != "$" && body.charAt(begStmt - 1) != "\\") {
                        var offset = (body.charAt(begStmt + 1) == "/" ? 2: 1);
                        if (body.substring(begStmt + offset, begStmt + 10 + offset).search(TrimPath.parseTemplate_etc.statementTag) == 0) {
                            break

                        }

                    }

                }
                begStmt = body.indexOf("{", begStmt + 1)

            }
            if (begStmt < 0) {
                break

            }
            var endStmt = body.indexOf("}", begStmt + 1);
            if (endStmt < 0) {
                break

            }
            emitSectionText(body.substring(endStmtPrev + 1, begStmt), funcText);
            emitStatement(body.substring(begStmt, endStmt + 1), state, funcText, tmplName, etc);
            endStmtPrev = endStmt

        }
        emitSectionText(body.substring(endStmtPrev + 1), funcText);
        if (state.stack.length != 0) {
            throw new etc.ParseError(tmplName, state.line, "unclosed, unmatched statement(s): " + state.stack.join(","))

        }
        funcText.push("}}; TrimPath_Template_TEMP");
        return funcText.join("")

    };
    var emitStatement = function(stmtStr, state, funcText, tmplName, etc) {
        var parts = stmtStr.slice(1, -1).split(" ");
        var stmt = etc.statementDef[parts[0]];
        if (stmt == null) {
            emitSectionText(stmtStr, funcText);
            return

        }
        if (stmt.delta < 0) {
            if (state.stack.length <= 0) {
                throw new etc.ParseError(tmplName, state.line, "close tag does not match any previous statement: " + stmtStr)

            }
            state.stack.pop()

        }
        if (stmt.delta > 0) {
            state.stack.push(stmtStr)

        }
        if (stmt.paramMin != null && stmt.paramMin >= parts.length) {
            throw new etc.ParseError(tmplName, state.line, "statement needs more parameters: " + stmtStr)

        }
        if (stmt.prefixFunc != null) {
            funcText.push(stmt.prefixFunc(parts, state, tmplName, etc))

        } else {
            funcText.push(stmt.prefix)

        }
        if (stmt.suffix != null) {
            if (parts.length <= 1) {
                if (stmt.paramDefault != null) {
                    funcText.push(stmt.paramDefault)

                }

            } else {
                for (var i = 1; i < parts.length; i++) {
                    if (i > 1) {
                        funcText.push(" ")

                    }
                    funcText.push(parts[i])

                }

            }
            funcText.push(stmt.suffix)

        }

    };
    var emitSectionText = function(text, funcText) {
        if (text.length <= 0) {
            return

        }
        var nlPrefix = 0;
        var nlSuffix = text.length - 1;
        while (nlPrefix < text.length && (text.charAt(nlPrefix) == "\n")) {
            nlPrefix++

        }
        while (nlSuffix >= 0 && (text.charAt(nlSuffix) == " " || text.charAt(nlSuffix) == "\t")) {
            nlSuffix--

        }
        if (nlSuffix < nlPrefix) {
            nlSuffix = nlPrefix

        }
        if (nlPrefix > 0) {
            funcText.push('if (_FLAGS.keepWhitespace == true) _OUT.write("');
            var s = text.substring(0, nlPrefix).replace("\n", "\\n");
            if (s.charAt(s.length - 1) == "\n") {
                s = s.substring(0, s.length - 1)

            }
            funcText.push(s);
            funcText.push('");')

        }
        var lines = text.substring(nlPrefix, nlSuffix + 1).split("\n");
        for (var i = 0; i < lines.length; i++) {
            emitSectionTextLine(lines[i], funcText);
            if (i < lines.length - 1) {
                funcText.push('_OUT.write("\\n");\n')

            }

        }
        if (nlSuffix + 1 < text.length) {
            funcText.push('if (_FLAGS.keepWhitespace == true) _OUT.write("');
            var s = text.substring(nlSuffix + 1).replace("\n", "\\n");
            if (s.charAt(s.length - 1) == "\n") {
                s = s.substring(0, s.length - 1)

            }
            funcText.push(s);
            funcText.push('");')

        }

    };
    var emitSectionTextLine = function(line, funcText) {
        var endMarkPrev = "}";
        var endExprPrev = -1;
        while (endExprPrev + endMarkPrev.length < line.length) {
            var begMark = "${",
            endMark = "}";
            var begExpr = line.indexOf(begMark, endExprPrev + endMarkPrev.length);
            if (begExpr < 0) {
                break

            }
            if (line.charAt(begExpr + 2) == "%") {
                begMark = "${%";
                endMark = "%}"

            }
            var endExpr = line.indexOf(endMark, begExpr + begMark.length);
            if (endExpr < 0) {
                break

            }
            emitText(line.substring(endExprPrev + endMarkPrev.length, begExpr), funcText);
            var exprArr = line.substring(begExpr + begMark.length, endExpr).replace(/\|\|/g, "#@@#").split("|");
            for (var k in exprArr) {
                if (exprArr[k].replace) {
                    exprArr[k] = exprArr[k].replace(/#@@#/g, "||")

                }

            }
            funcText.push("_OUT.write(");
            emitExpression(exprArr, exprArr.length - 1, funcText);
            funcText.push(");");
            endExprPrev = endExpr;
            endMarkPrev = endMark

        }
        emitText(line.substring(endExprPrev + endMarkPrev.length), funcText)

    };
    var emitText = function(text, funcText) {
        if (text == null || text.length <= 0) {
            return

        }
        text = text.replace(/\\/g, "\\\\");
        text = text.replace(/\n/g, "\\n");
        text = text.replace(/"/g, '\\"');
        funcText.push('_OUT.write("');
        funcText.push(text);
        funcText.push('");')

    };
    var emitExpression = function(exprArr, index, funcText) {
        var expr = exprArr[index];
        if (index <= 0) {
            funcText.push(expr);
            return

        }
        var parts = expr.split(":");
        funcText.push('_MODIFIERS["');
        funcText.push(parts[0]);
        funcText.push('"](');
        emitExpression(exprArr, index - 1, funcText);
        if (parts.length > 1) {
            funcText.push(",");
            funcText.push(parts[1])

        }
        funcText.push(")")

    };
    var cleanWhiteSpace = function(result) {
        result = result.replace(/\t/g, "    ");
        result = result.replace(/\r\n/g, "\n");
        result = result.replace(/\r/g, "\n");
        result = result.replace(/^(\s*\S*(\s+\S+)*)\s*$/, "$1");
        return result

    };
    var scrubWhiteSpace = function(result) {
        result = result.replace(/^\s+/g, "");
        result = result.replace(/\s+$/g, "");
        result = result.replace(/\s+/g, " ");
        result = result.replace(/^(\s*\S*(\s+\S+)*)\s*$/, "$1");
        return result

    };
    TrimPath.parseDOMTemplate = function(elementId, optDocument, optEtc) {
        if (optDocument == null) {
            optDocument = document

        }
        var element = optDocument.getElementById(elementId);
        var content = element.value;
        if (content == null) {
            content = element.innerHTML

        }
        content = content.replace(/</g, "<").replace(/>/g, ">");
        return TrimPath.parseTemplate(content, elementId, optEtc)

    };
    TrimPath.processDOMTemplate = function(elementId, context, optFlags, optDocument, optEtc) {
        return TrimPath.parseDOMTemplate(elementId, optDocument, optEtc).process(context, optFlags)

    }

})();

(function(a) {
    a.fn.Jdropdown = function(d, e) {
        if (!this.length) {
            return

        }
        if (typeof d == "function") {
            e = d;
            d = {}

        }
        var c = a.extend({
            event: "mouseover",
            current: "hover",
            delay: 0

        },
        d || {});
        var b = (c.event == "mouseover") ? "mouseout": "mouseleave";
        a.each(this, 
        function() {
            var h = null,
            g = null,
            f = false;
            a(this).bind(c.event, 
            function() {
                if (f) {
                    clearTimeout(g)

                } else {
                    var j = a(this);
                    h = setTimeout(function() {
                        j.addClass(c.current);
                        f = true;
                        if (e) {
                            e(j)

                        }

                    },
                    c.delay)

                }

            }).bind(b, 
            function() {
                if (f) {
                    var j = a(this);
                    g = setTimeout(function() {
                        j.removeClass(c.current);
                        f = false

                    },
                    c.delay)

                } else {
                    clearTimeout(h)

                }

            })

        })

    }

})(jQuery); 
(function(a) {
    a.extend(a.browser, {
        client: function() {
            return {
                width: document.documentElement.clientWidth,
                height: document.documentElement.clientHeight,
                bodyWidth: document.body.clientWidth,
                bodyHeight: document.body.clientHeight

            }

        },
        scroll: function() {
            return {
                width: document.documentElement.scrollWidth,
                height: document.documentElement.scrollHeight,
                bodyWidth: document.body.scrollWidth,
                bodyHeight: document.body.scrollHeight,
                left: document.documentElement.scrollLeft + document.body.scrollLeft,
                top: document.documentElement.scrollTop + document.body.scrollTop

            }

        },
        screen: function() {
            return {
                width: window.screen.width,
                height: window.screen.height

            }

        },
        isIE6: a.browser.msie && a.browser.version == 6,
        isMinW: function(b) {
            return Math.min(a.browser.client().bodyWidth, a.browser.client().width) <= b

        },
        isMinH: function(b) {
            return a.browser.client().height <= b

        }

    })

})(jQuery); 
(function(a) {
    a.fn.jdPosition = function(f) {
        var e = a.extend({
            mode: null

        },
        f || {});
        switch (e.mode) {
            default:
        case "center":
            var c = a(this).outerWidth(),
            g = a(this).outerHeight();
            var b = a.browser.isMinW(c),
            d = a.browser.isMinH(g);
            a(this).css({
                left: a.browser.scroll().left + Math.max((a.browser.client().width - c) / 2, 0) + "px",
                top: (!a.browser.isIE6) ? (d ? a.browser.scroll().top: (a.browser.scroll().top + Math.max((a.browser.client().height - g) / 2, 0) + "px")) : ((a.browser.scroll().top <= a.browser.client().bodyHeight - g) ? (a.browser.scroll().top + Math.max((a.browser.client().height - g) / 2, 0) + "px") : (a.browser.client().height - g) / 2 + "px")

            });
            break;
            case "auto":
            break;
            case "fixed":
            break

        }

    }

})(jQuery); 
(function(a) {
    a.fn.jdThickBox = function(f, k) {
        if (typeof f == "function") {
            k = f;
            f = {}

        }
        var o = a.extend({
            type: "text",
            source: null,
            width: null,
            height: null,
            title: null,
            _frame: "",
            _div: "",
            _box: "",
            _con: "",
            _loading: "thickloading",
            close: false,
            _close: "",
            _fastClose: false,
            _close_val: "\u00d7",
            _titleOn: true,
            _title: "",
            _autoReposi: false,
            _countdown: false

        },
        f || {});
        var e = (typeof this != "function") ? a(this) : null;
        var c;
        var m = function() {
            clearInterval(c);
            a(".thickframe").add(".thickdiv").hide();
            a(".thickbox").empty().remove();
            if (o._autoReposi) {
                a(window).unbind("resize.jdThickBox").unbind("scroll.jdThickBox")

            }

        };
        if (o.close) {
            m();
            return false

        }
        var d = function(p) {
            if (p != "") {
                return p.match(/\w+/)

            } else {
                return ""

            }

        };
        var n = function(p) {
            if (a(".thickframe").length == 0 || a(".thickdiv").length == 0) {
                a("<iframe class='thickframe' id='" + d(o._frame) + "' marginwidth='0' marginheight='0' frameborder='0' scrolling='no'></iframe>").appendTo(a(document.body));
                a("<div class='thickdiv' id='" + d(o._div) + "'></div>").appendTo(a(document.body))

            } else {
                a(".thickframe").add(".thickdiv").show()

            }
            a("<div class='thickbox' id='" + d(o._box) + "'></div>").appendTo(a(document.body));
            if (o._titleOn) {
                h(p)

            }
            a("<div class='thickcon' id='" + d(o._con) + "' style='width:" + o.width + "px;height:" + o.height + "px;'></div>").appendTo(a(".thickbox"));
            if (o._countdown) {
                b()

            }
            a(".thickcon").addClass(o._loading);
            g();
            j();
            l(p);
            if (o._autoReposi) {
                a(window).bind("resize.jdThickBox", g).bind("scroll.jdThickBox", g)

            }
            if (o._fastClose) {
                a(document.body).bind("click.jdThickBox", 
                function(r) {
                    r = r ? r: window.event;
                    var q = r.srcElement ? r.srcElement: r.target;
                    if (q.className == "thickdiv") {
                        a(this).unbind("click.jdThickBox");
                        m()

                    }

                })

            }

        };
        var b = function() {
            var p = o._countdown;
            a("<div class='thickcountdown' style='width:" + o.width + "'><span id='jd-countdown'>" + p + "</span>\u79d2\u540e\u81ea\u52a8\u5173\u95ed</div>").appendTo(a(".thickbox"));
            c = setInterval(function() {
                p--;
                a("#jd-countdown").html(p);
                if (p == 0) {
                    p = o._countdown;
                    m()

                }

            },
            1000)

        };
        var h = function(p) {
            o.title = (o.title == null && p) ? p.attr("title") : o.title;
            a("<div class='thicktitle' id='" + d(o._title) + "' style='width:" + o.width + "'><span>" + o.title + "</span></div>").appendTo(a(".thickbox"))

        };
        var j = function() {
            if (o._close != null) {
                a("<a href='#' class='thickclose' id='" + d(o._close) + "'>" + o._close_val + "</a>").appendTo(a(".thickbox"));
                a(".thickclose").one("click", 
                function() {
                    m();
                    return false

                })

            }

        };
        var l = function(p) {
            o.source = (o.source == null) ? p.attr("href") : o.source;
            switch (o.type) {
                default:
            case "text":
                a(".thickcon").html(o.source);
                a(".thickcon").removeClass(o._loading);
                if (k) {
                    k()

                }
                break;
                case "html":
                a(o.source).clone().appendTo(a(".thickcon")).show();
                a(".thickcon").removeClass(o._loading);
                if (k) {
                    k()

                }
                break;
                case "image":
                o._index = (o._index == null) ? e.index(p) : o._index;
                a(".thickcon").append("<img src='" + o.source + "' width='" + o.width + "' height='" + o.height + "'>");
                o.source = null;
                a(".thickcon").removeClass(o._loading);
                if (k) {
                    k()

                }
                break;
                case "ajax":
            case "json":
                if (k) {
                    k(o.source, a(".thickcon"), 
                    function() {
                        a(".thickcon").removeClass(o._loading)

                    })

                }
                break;
                case "iframe":
                a("<iframe src='" + o.source + "' marginwidth='0' marginheight='0' frameborder='0' scrolling='no' style='width:" + o.width + "px;height:" + o.height + "px;border:0;'></iframe>").appendTo(a(".thickcon"));
                a(".thickcon").removeClass(o._loading);
                if (k) {
                    k()

                }
                break

            }

        };
        var g = function() {
            var q = a(".thickcon").outerWidth(),
            t = (o._titleOn ? a(".thicktitle").outerHeight() : 0) + a(".thickcon").outerHeight();
            a(".thickbox").css({
                width: q + "px",
                height: t + "px"

            });
            a(".thickbox").jdPosition({
                mode: "center"

            });
            if (a.browser.isIE6) {
                var s = a(".thickbox").outerWidth(),
                u = a(".thickbox").outerHeight();
                var p = a.browser.isMinW(s),
                r = a.browser.isMinH(u);
                a(".thickframe").add(".thickdiv").css({
                    width: p ? s: "100%",
                    height: Math.max(a.browser.client().height, a.browser.client().bodyHeight) + "px"

                })

            }

        };
        if (e != null) {
            e.click(function() {
                n(a(this));
                return false

            })

        } else {
            n()

        }

    };
    a.jdThickBox = a.fn.jdThickBox

})(jQuery);
function jdThickBoxclose() {
    $(".thickclose").trigger("click")

} 
(function(a) {
    a.fn.Jtab = function(d, h) {
        if (!this.length) {
            return

        }
        if (typeof d == "function") {
            h = d;
            d = {}

        }
        var b = a.extend({
            type: "static",
            auto: false,
            event: "mouseover",
            currClass: "curr",
            source: "data-tag",
            hookKey: "data-widget",
            hookItemVal: "tab-item",
            hookContentVal: "tab-content",
            stay: 5000,
            delay: 100,
            threshold: null,
            mainTimer: null,
            subTimer: null,
            index: 0,
            compatible: false

        },
        d || {});
        var f = a(this).find("*[" + b.hookKey + "=" + b.hookItemVal + "]"),
        e = a(this).find("*[" + b.hookKey + "=" + b.hookContentVal + "]"),
        k = b.source.toLowerCase().match(/http:\/\/|\d|\.aspx|\.ascx|\.asp|\.php|\.html\.htm|.shtml|.js/g);
        if (f.length != e.length) {
            return false

        }
        var j = function(m, l) {
            b.subTimer = setTimeout(function() {
                f.eq(b.index).removeClass(b.currClass);
                if (b.compatible) {
                    e.eq(b.index).hide()

                }
                if (l) {
                    b.index++;
                    if (b.index == f.length) {
                        b.index = 0

                    }

                } else {
                    b.index = m

                }
                b.type = (f.eq(b.index).attr(b.source) != null) ? "dynamic": "static";
                c()

            },
            b.delay)

        };
        var g = function() {
            b.mainTimer = setInterval(function() {
                j(b.index, true)

            },
            b.stay)

        };
        var c = function() {
            f.eq(b.index).addClass(b.currClass);
            if (b.compatible) {
                e.eq(b.index).show()

            }
            switch (b.type) {
                default:
            case "static":
                var l = "";
                break;
                case "dynamic":
                var l = (!k) ? f.eq(b.index).attr(b.source) : b.source;
                f.eq(b.index).removeAttr(b.source);
                break

            }
            if (h) {
                h(l, e.eq(b.index), b.index)

            }

        };
        f.each(function(l) {
            a(this).bind(b.event, 
            function() {
                clearTimeout(b.subTimer);
                clearInterval(b.mainTimer);
                j(l, false)

            }).bind("mouseleave", 
            function() {
                if (b.auto) {
                    g()

                } else {
                    return

                }

            })

        });
        if (b.type == "dynamic") {
            j(b.index, false)

        }
        if (b.auto) {
            g()

        }

    }

})(jQuery);
(function(a) {
    a.fn.Jlazyload = function(j, n) {
        if (!this.length) {
            return

        }
        var f = a.extend({
            type: null,
            offsetParent: null,
            source: "data-lazyload",
            placeholderImage: "",
            placeholderClass: "loading-style2",
            threshold: 200

        },
        j || {}),
        k = this,
        g,
        m,
        l = function(r) {
            var u = r.scrollLeft,
            t = r.scrollTop,
            s = r.offsetWidth,
            q = r.offsetHeight;
            while (r.offsetParent) {
                u += r.offsetLeft;
                t += r.offsetTop;
                r = r.offsetParent

            }
            return {
                left: u,
                top: t,
                width: s,
                height: q

            }

        },
        e = function() {
            var v = document.documentElement,
            r = document.body,
            u = window.pageXOffset ? window.pageXOffset: (v.scrollLeft || r.scrollLeft),
            t = window.pageYOffset ? window.pageYOffset: (v.scrollTop || r.scrollTop),
            s = v.clientWidth,
            q = v.clientHeight;
            return {
                left: u,
                top: t,
                width: s,
                height: q

            }

        },
        d = function(w, v) {
            var y,
            x,
            s,
            r,
            q,
            u,
            z = f.threshold ? parseInt(f.threshold) : 0;
            y = w.left + w.width / 2;
            x = v.left + v.width / 2;
            s = w.top + w.height / 2;
            r = v.top + v.height / 2;
            q = (w.width + v.width) / 2;
            u = (w.height + v.height) / 2;
            return Math.abs(y - x) < (q + z) && Math.abs(s - r) < (u + z)

        },
        b = function(q, s, r) {
            if (f.placeholderImage && f.placeholderClass) {
                r.attr("src", f.placeholderImage).addClass(f.placeholderClass)

            }
            if (q) {
                r.attr("src", s).removeAttr(f.source);
                if (n) {
                    n(s, r)

                }

            }

        },
        c = function(q, t, r) {
            if (q) {
                var s = a("#" + t);
                s.html(r.val()).removeAttr(f.source);
                r.remove();
                if (n) {
                    n(t, r)

                }

            }

        },
        p = function(q, s, r) {
            if (q) {
                r.removeAttr(f.source);
                if (n) {
                    n(s, r)

                }

            }

        },
        o = function() {
            m = e(),
            k = k.filter(function() {
                return a(this).attr(f.source)

            });
            a.each(k, 
            function() {
                var t = a(this).attr(f.source);
                if (!t) {
                    return

                }
                var s = (!f.offsetParent) ? m: l(a(f.offsetParent).get(0)),
                r = l(this),
                q = d(s, r);
                switch (f.type) {
                    case "image":
                    b(q, t, a(this));
                    break;
                    case "textarea":
                    c(q, t, a(this));
                    break;
                    case "module":
                    p(q, t, a(this));
                    break;
                    default:
                    break

                }

            })

        },
        h = function() {
            if (k.length > 0) {
                clearTimeout(g);
                g = setTimeout(function() {
                    o()

                },
                10)

            }

        };
        o();
        if (!f.offsetParent) {
            a(window).bind("scroll", 
            function() {
                h()

            }).bind("reset", 
            function() {
                h()

            })

        } else {
            a(f.offsetParent).bind("scroll", 
            function() {
                h()

            })

        }

    }

})(jQuery);
function mlazyload(d) {
    var c = {
        defObj: null,
        defHeight: 0,
        fn: null

    };
    c = $.extend(c, d || {});
    var b = c.defHeight,
    f = (typeof c.defObj == "object") ? c.defObj: $(c.defObj);
    if (f.length < 1) {
        return

    }
    var a = function() {
        var g = document,
        h = (navigator.userAgent.toLowerCase().match(/iPad/i) == "ipad") ? window.pageYOffset: Math.max(g.documentElement.scrollTop, g.body.scrollTop);
        return g.documentElement.clientHeight + h - c.defHeight

    };
    var e = function() {
        if (f.offset().top <= a() && !f.attr("load")) {
            f.attr("load", "true");
            if (c.fn) {
                c.fn()

            }

        }

    };
    e();
    $(window).bind("scroll", 
    function() {
        e()

    })

}
var jdRecent = {
	    element: $("#recent ul"),
	    jsurl: "http://www.360buy.com/lishiset.aspx?callback=jdRecent.setData&id=",
	    cookiename: "_recent",
	    list: null,
	    url: location.href,
	    init: function() {
	        var a = this.url.match(/\/(\d{6}).html/);
	        var b = (a != null && a[0].indexOf("html") != -1) ? a[1] : "";
	        if (!this.list || this.list == null || this.list == "") {
	            if (b == "") {
	                return this.getData(0)

	            } else {
	                this.list = b

	            }

	        } else {
	            if (b == "" || this.list.indexOf(b) != -1) {
	                this.list = this.list

	            } else {
	                if (this.list.split(".").length >= 10) {
	                    this.list = this.list.replace(/.\d+$/, "")

	                }
	                this.list = b + "." + this.list

	            }

	        }
	        $.cookie(this.cookiename, this.list, {
	            expires: 7,
	            path: "/",
	            domain: "360buy.com",
	            secure: false

	        });
	        this.getData(this.list)

	    },
	    clear: function() {
	        $.cookie(this.cookiename, "", {
	            expires: 7,
	            path: "/",
	            domain: "360buy.com",
	            secure: false

	        })

	    },
	    getData: function(a) {
	        if (a == 0) {
	            this.element.html("<li><div class='norecode'>\u6682\u65e0\u8bb0\u5f55!</div></li>");
	            return

	        }
	        var b = a.split(".");
	        for (i in b) {
	            if (i == 0) {
	                this.element.empty()

	            }
	            $.getJSONP(this.jsurl + b[i], this.setData)

	        }

	    },
	    setData: function(a) {
	        this.element.append("<li><div class='p-img'><a href='" + a.url + "'><img src='" + a.img + "' /></a></div><div class='p-name'><a href='" + a.url + "'>" + decodeURIComponent(a.name) + "</a></div></li>")

	    }

	};
	$("#clearRec").click(function() {
	    jdRecent.clear();
	    jdRecent.getData(0)

	});
	mlazyload({
	    defObj: "#recent",
	    defHeight: 50,
	    fn: function() {
	        if (jdRecent.element.length == 1) {
	            jdRecent.init()

	        }

	    }

	});
var jdModelCallCenter = {
	    settings: {
	        clstag1: 0,
	        clstag2: 0

	    },
	    tbClose: function() {
	        if ($(".thickbox").length != 0) {
	            jdThickBoxclose()

	        }

	    },
	    login: function(p) {
	        this.tbClose();
	        var c = this;
	        var b = navigator.userAgent.toLowerCase(),
	        a = (b.match(/ucweb/i) == "ucweb" || b.match(/rv:1.2.3.4/i) == "rv:1.2.3.4");
	        if (a) {
	            location.href = "login.action?returnurl=" + escape(location.href);
	            return

	        }
	        setTimeout(function() {
	            $.jdThickBox({
	                type: "iframe",
	                title: "您尚未登录",
	                source: "index!logininit.action?pid="+p,
	                width: 450,
	                height: 360,
	                _title: "thicktitler",
	                _close: "thickcloser",
	                _con: "thickconr"

	            })

	        },
	        20)

	    },
	    regist: function() {
	        this.tbClose();
	        var a = this;
	        setTimeout(function() {
	            $.jdThickBox({
	                type: "iframe",
	                title: "\u60a8\u5c1a\u672a\u767b\u5f55",
	                source: "http://passport.360buy.com/new/registPersonalFrame.aspx?clstag1=" + a.settings.clstag1 + "&clstag2=" + a.settings.clstag2 + "&r=" + Math.random(),
	                width: 450,
	                height: 500,
	                _title: "thicktitler",
	                _close: "thickcloser",
	                _con: "thickconr"

	            })

	        },
	        20)

	    },
	    init: function() {
	        var a = this;
	        $.ajax({
	            url: "http://passport.360buy.com/new/helloService.ashx?m=ls",
	            dataType: "jsonp",
	            success: function(b) {
	                a.tbClose();
	                if (b && b.info) {
	                    $("#loginbar").html(b.info)

	                }
	                a.settings.fn()

	            }

	        })

	    }
};
$.extend(jdModelCallCenter, {
    autoLocation: function(a) {
        var b = this;
        $.login({
            modal: true,
            complete: function(c) {
                if (c != null) {
                    window.location = a

                }

            }

        })

    }

});
$.extend(jdModelCallCenter, {
    doAttention: function(a) {
        var b = "user/attention!save.action";
        $.login({
            modal: true,
            param:a,
            complete: function(c) {
                if (c != null) {
                    if (a > 0) {
                        var g = 510,
                        d = 120,
                        f = false;
                        $.jdThickBox({
                            type: "iframe",
                            source: "user/attention!save.action?pid=" + a + "&t=" + Math.random(),
                            width: g,
                            height: d,
                            title: "提示",
                            _box: "attboxr",
                            _con: "attconr",
                            _countdown: f

                        })
                    }
                }
            }

        })

    }

});
$(".btn-coll").bind("click", 
		function() {
		    var b = $(this);
		    var a = parseInt(b.attr("id").replace("coll", ""));
		    $.extend(jdModelCallCenter.settings, {
		        clstag1: "login|keycount|5|3",
		        clstag2: "login|keycount|5|4",
		        id: a,
		        fn: function() {
		            jdModelCallCenter.doAttention(this.id)

		        }

		    });
		    jdModelCallCenter.settings.fn()
});
$(".btn-login").bind("click", 
		function() {
		 	$.login({
		         modal: true,
		         param:0,
		         complete: function(c) {
		            
		         }
		 	});
});
$.login = function(a) {
    a = $.extend({
        loginService: "http://passport.360buy.com/loginservice.aspx?callback=?",
        loginMethod: "Login",
        loginUrl: "https://passport.360buy.com/new/login.aspx",
        returnUrl: location.href,
        automatic: true,
        complete: null,
        modal: false

    },
    a || {});
    if (a.loginService != "" && a.loginMethod != "") {
    	var islogin = false;
    	$.post("index!isLogin.excsec",{},function(data) {
			islogin = data;
			if (islogin=='false') {
				if (a.modal) {
                    jdModelCallCenter.login(a.param);
                } else {
                    location.href = a.loginUrl + "?returnurl=" + escape(a.returnUrl)
                }
			}else{
				a.complete("success");
			}
    	});
    }

};

(function() {
	pageConfig.FN_ImgError(document);
    $("img[data-lazyload]").Jlazyload({
        type: "image",
        placeholderClass: "err-product"

    });
    $("#shortcut .menu").Jdropdown({
        delay: 50

    });
    $("#navitems li").Jdropdown();
	$("#categorys").Jdropdown({delay: 200});
	$("#_JD_ALLSORT").find(".item").Jdropdown({delay: 200});
	$("#hot").Jtab({
        delay: 300,
        source: "data-boole"

    },
    function(c, b, d) {
        if (d == 4) {
            if (!c) {
                return;

            }
            pageConfig.FN_GuessYou(c, b);

        } else {
            b.find("img").Jlazyload({
                type: "image",
                source: "data-src"

            },
            function() {
                pageConfig.FN_ImgError(b.get(0));

            });

        }

    });
	$("#hot2").Jtab({
        delay: 300,
        source: "data-boole"

    },
    function(c, b, d) {
        if (d == 4) {
            if (!c) {
                return;

            }
            pageConfig.FN_GuessYou(c, b);

        } else {
            b.find("img").Jlazyload({
                type: "image",
                source: "data-src"

            },
            function() {
                pageConfig.FN_ImgError(b.get(0));

            });

        }

    });
	$("#my360buy dl").Jdropdown({
        delay: 100
    },
    function(a) {
        if (a.attr("load")) {
            return

        }
        var islogin;
        $.post(ctx+"/index!isLogin.excsec",{},function(data) {
			islogin = data;
			var d = a.find("dd").eq(0),
            c = "",
            b;
            if (islogin=='false') {
                c += UC.TPL_UnRegist;
                c += UC.TPL_UList;

            } else {
                c += UC.TPL_Regist;
                c += UC.TPL_UList;

            }
            d.html(c);
            a.attr("load", "1");
            setTimeout(function() {
                a.removeAttr("load")

            },
            10000);
		});
        /*$.login({
            automatic: false,
            complete: function(e) {
                if (!e) {
                    return

                }
                var d = a.find("dd").eq(0),
                c = "",
                b;
                if (!e.IsAuthenticated) {
                    c += UC.TPL_UnRegist;
                    c += UC.TPL_UList;
                    //b = readCookie(UC.DATA_Cookie);


                } else {
                   // c += UC.TPL_Regist.process(e);
                    c += UC.TPL_OList.placeholder;
                    c += UC.TPL_UList

                }
                d.html(c);
                a.attr("load", "1");
                setTimeout(function() {
                    a.removeAttr("load")

                },
                60000);
                
                //UC.FN_InitOList()

            }

        })*/

    });


    document.onkeyup = function(c) {
        var a = document.activeElement.tagName.toLowerCase();
        if (a == "input" || a == "textarea") {
            return

        }
        var c = c ? c: window.event,
        b = c.keyCode || c.which;
        switch (b) {
            case 68:
            if (!window.pageConfig.clientViewTop) {
                window.pageConfig.clientViewTop = 0

            }
            window.pageConfig.clientViewTop += document.documentElement.clientHeight;
            window.scrollTo(0, pageConfig.clientViewTop);
            break;
            case 83:
            window.scrollTo(0, 0);
            window.pageConfig.clientViewTop = 0;
            document.getElementById("key").focus();
            break;
            case 84:
            window.scrollTo(0, 0);
            window.pageConfig.clientViewTop = 0;
            break;
            default:
            break

        }

    }

})();


var UC = {
    DATA_Cookie: "_recent",
    TPL_UnRegist: '<div class="prompt"><span class="fl">您好,请登录</span><span class="fr"><a href="javascript:login()" class="btn-login" clstag="homepage|keycount|home2012|04a">登录</a></span></div>',
    TPL_Regist: '<div class="prompt"><span class="fr"><a href="'+ctx+'/user/index.action">进入会员中心&nbsp;&gt;</a></span></div>',
    TPL_OList: {
        placeholder: '<div id="jduc-orderlist"></div>',
        fragment: '<div class="orderlist">				<div class="smt">					<h4>\u6700\u65b0\u8ba2\u5355\u72b6\u6001\uff1a</h4>					<div class="extra"><a href="http://jd2008.360buy.com/JdHome/OrderList.aspx" target="_blank">\u67e5\u770b\u6240\u6709\u8ba2\u5355&nbsp;&gt;</a></div>				</div>				<div class="smc">					<ul>						{for item in orderList}						<li class="fore${parseInt(item_index)+1}">							<div class="p-img fl">								{for image in item.OrderDetail}									{if image_index<2}										<a href="http://www.360buy.com/product/${image.ProductId}.html" target="_blank"><img src="${pageConfig.FN_GetImageDomain(image.ProductId)}n5/${image.ImgUrl}" width="50" height="50" alt="${image.ProductName}" /></a>									{/if}								{/for}								{if item.OrderDetail.length>2}									<a href="${item.passKeyUrl}" target="_blank" class="more">\u66f4\u591a</a>								{/if}							</div>							<div class="p-detail fr">								\u8ba2\u5355\u53f7\uff1a${item.OrderId}<br />								\u72b6\u3000\u6001\uff1a<span>${UC.FN_SetState(item.OrderState)}</span><br />								\u3000\u3000\u3000\u3000<a href="${item.passKeyUrl}">\u67e5\u770b\u8be6\u60c5</a>							</div>						</li>						{/for}					</ul>				</div>			</div>'

    },
    TPL_UList: '<div class="uclist"><ul class="fore1 fl"><li><a target="_blank" clstag="homepage|keycount|home2012|04b" href="'+ctx+'/user/enquiry.action">询价回复<span id="num-unfinishedorder"></span></a></li><li><a target="_blank" clstag="homepage|keycount|home2012|04c" href="'+ctx+'/user/attention.action">我的关注<span id="num-consultation"></span></a></li></ul><ul class="fore2 fl"><li><a target="_blank" clstag="homepage|keycount|home2012|04d" href="'+ctx+'/user/post.action">我的评价<span id="num-reduction"></span></a></li><li><a target="_blank" clstag="homepage|keycount|home2012|04e" href="'+ctx+'/user/user!input.action">个人信息<span id="num-ticket"></span></a></li></ul></div>',
    TPL_VList: {
        placeholder: '<div class="viewlist" clstag="homepage|keycount|home2012|04j">				<div class="smt">					<h4>\u6700\u8fd1\u6d4f\u89c8\u7684\u5546\u54c1\uff1a</h4>					<div style="float:right;padding-right:9px;"><a style="border:0;color:#005EA7" href="http://my.360buy.com/history/list.html" target="_blank">\u67e5\u770b\u6d4f\u89c8\u5386\u53f2&nbsp;&gt;</a></div>				</div>				<div class="smc" id="jduc-viewlist">					<div class="loading-style1"><b></b>\u52a0\u8f7d\u4e2d\uff0c\u8bf7\u7a0d\u5019...</div>					<ul class="lh hide"></ul>				</div>			</div>',
        fragment: '<li><a href="${url}" target="_blank" title="${decodeURIComponent(name)}"><img src="${img}" width="50" height="50" alt="${decodeURIComponent(name)}" /></a></li>'

    },
    FN_SetState: function(a) {
        var a = a;
        if (a.length > 4) {
            a = "<span title=" + a + ">" + a.substr(0, 4) + "...</span>"

        }
        return a

    },
    FN_InitVList: function(a) {
        var b = a.split(".");
        $.each(b, 
        function(c) {
            if (c >= 5) {
                return

            }
            //$.getJSONP("http://www.360buy.com/lishiset.aspx?callback=UC.FN_ShowVList&id=" + b[c], UC.FN_ShowVList)

        })

    },
    FN_ShowVList: function(b) {
        var c = $("#jduc-viewlist").find(".loading-style1");
        if (c.length > 0) {
            c.hide()

        }
        var a = this.TPL_VList.fragment.process(b);
        $("#jduc-viewlist").find("ul").eq(0).append(a).show()

    },
    FN_setWords: function(b) {
        var c = '<font style="color:{0}">({1})</font>',
        a = "";
        if (b == 0) {
            a = "#ccc"

        } else {
            a = "#c00"

        }
        return pageConfig.FN_StringFormat(c, a, b)

    },
    FN_InitOList: function() {
        $.ajax({
            url: "http://minijd.360buy.com/getOrderList",
            dataType: "jsonp",
            success: function(b) {
                if (b && b.error == 0 && b.orderList) {
                    var a = UC.TPL_OList.fragment.process(b);
                    $("#jduc-orderlist").html(a)

                }

            }

        });
        $.ajax({
            url: "http://minijd.360buy.com/getHomeCount",
            dataType: "jsonp",
            success: function(a) {
                if (a && a.error == 0) {
                    $("#num-unfinishedorder").html(UC.FN_setWords(a.orderCount))

                }

            }

        });
        $.ajax({
            url: "http://comm.360buy.com/index.php?mod=Consultation&action=havingReplyCount",
            dataType: "jsonp",
            success: function(a) {
                if (a) {
                    $("#num-consultation").html(UC.FN_setWords(a.cnt))

                }

            }

        });
        $.ajax({
            url: "http://t.360buy.com/follow/followProductCount.action",
            data: {
                method: "GetCount"

            },
            dataType: "jsonp",
            success: function(a) {
                if (a) {
                    $("#num-reduction").html(UC.FN_setWords(a.priceReduction))

                }

            }

        });
        $.ajax({
            url: "http://coupon.360buy.com/service.ashx",
            data: {
                m: "getcouponcount"

            },
            dataType: "jsonp",
            success: function(a) {
                if (a) {
                    $("#num-ticket").html(UC.FN_setWords(a.CouponCount))

                }

            }

        })

    }

};
pageConfig.FN_InitSidebar = function() {
    if (!$("#toppanel").length) {
        $(document.body).prepend('<div class="w ld" id="toppanel"></div>')

    }
    $("#toppanel").append('<div id="sidepanel" class="hide"></div>');
    var a = $("#sidepanel");
    this.scroll = function() {
        var b = this;
        $(window).bind("scroll", 
        function() {
            var c = document.body.scrollTop || document.documentElement.scrollTop;
            if (c == 0) {
                a.hide()

            } else {
                a.show()

            }

        });
        b.initCss();
        $(window).bind("resize", 
        function() {
            b.initCss()

        })

    };
    this.initCss = function() {
        var b,
        c = pageConfig.compatible ? 1210: 990;
        if (screen.width >= 1210) {
            if ($.browser.msie && $.browser.version <= 6) {
                b = {
                    right: "-26px"

                }

            } else {
                b = {
                    right: (document.documentElement.clientWidth - c) / 2 - 26 + "px"

                }

            }
            a.css(b)

        }

    };
    this.addCss = function(b) {
        a.css(b)

    };
    this.addItem = function(b) {
        a.append(b)

    };
    this.setTop = function() {
        this.addItem("<a href='#' class='gotop' title='\u4f7f\u7528\u5feb\u6377\u952eT\u4e5f\u53ef\u8fd4\u56de\u9876\u90e8\u54e6\uff01'><b></b>\u8fd4\u56de\u9876\u90e8</a>")

    }

};
var $o = (function(g) {
    var e = {};
    e.replace = function(r, s) {
        return r.replace(/#{(.*?)}/g, 
        function() {
            var t = arguments[1];
            if ("undefined" != typeof(s[t])) {
                return s[t]

            } else {
                return arguments[0]

            }

        })

    };
    String.prototype.trim = function() {
        return this.replace(/^\s*(.*?)\s*$/, "$1")

    };
    String.prototype.isEmpty = function() {
        if (0 == this.length) {
            return true

        } else {
            return false

        }

    };
    var m = '<a id="d_#{id}" href="javascript:void(0);" title="#{title}" onclick="$o.clickItem(this)" cid="#{cid}" cLevel="#{cLevel}" ev_val="#{ev_val}" onmouseover="$o.mouseOverEvItem(event, this);" onmouseout="$o.mouseOutEvItem(event, this);">#{ev_name}</a>';
    var c = '<a href="javascript:void(0);" title="#{title}" onclick="$o.clickItem(this)" cid="#{cid}" cLevel="#{cLevel}" onmouseover="$o.mouseOverEvItem(event, this)" onmouseout="$o.mouseOutEvItem(event, this);">\u66F4\u591A&gt;&gt;</a>';
    var f = '<div id="d_#{id}" class="item3" cid="#{cid}" cLevel="#{cLevel}" onmouseover="$o.mouseOverItem(this)" onmouseout="$o.mouseOutItem(this)"><span>\u6309#{es_name}\uff1a</span>#{evs}</div>';
    var h = '<li id="d_#{id}" title="#{title}" onclick="$o.clickItem(this)" onmouseover="$o.mouseOverItem(this)" onmouseout="$o.mouseOutItem(this)"><div>#{keyword}</div></li>';
    var b = '<div id="d_#{id}" class="#{className}" title="#{title}" cid="#{cid}" cLevel="#{cLevel}" onclick="$o.clickItem(this)" onmouseover="$o.mouseOverItem(this)" onmouseout="$o.mouseOutItem(this)">\u5728<strong>#{cname}</strong>\u5206\u7c7b\u4e2d\u641c\u7d22</div>';
    var k = '<li class="fore1"><div id="d_#{id}" class="fore1" title="#{title}" onclick="$o.clickItem(this)" onmouseover="$o.mouseOverItem(this)" onmouseout="$o.mouseOutItem(this)">#{keyword}</div>#{categorys}</li>';
    var n = "dd.search.360buy.com";
    var p = "http://" + n + "/?key=#{keyword}&callback=$o.onloadItems";
    var o = "#FFDFC6";
    var d = "#FFF";
    var l = $("#key");
    var q = $("#shelper");
    function a() {
        this.length = 0;
        this.index = -1;
        this.HIndex = -1;
        this.needCreatedItem = false

    }
    a.prototype.init = function() {
        this.length = 0;
        this.index = -1;
        this.HIndex = -1;
        this.needCreatedItem = false

    };
    a.prototype.hideTip = function() {
        this.init();
        q.html("").hide()

    };
    a.prototype.clickItem = function(u) {
        var t = u.getAttribute("cid");
        if (null != t && "" != t) {
            search.cid = t

        } else {
            search.cid = null

        }
        var r = u.getAttribute("cLevel");
        if (null != r && "" != r) {
            search.cLevel = r

        } else {
            search.cLevel = null

        }
        var v = u.getAttribute("ev_val");
        if (null != v && !v.trim().isEmpty()) {
            search.ev_val = v

        } else {
            search.ev_val = null

        }
        var s = u.getAttribute("title");
        if (null != s && !s.trim().isEmpty()) {
            l.val(s)

        }
        search("key")

    };
    a.prototype.mouseOverItem = function(s) {
        if (null == s) {
            return

        }
        s.style.backgroundColor = o;
        if ( - 1 != this.index && null != s.id && !s.id.trim().isEmpty()) {
            var t = s.id.split("_");
            if (2 == t.length) {
                var r = parseInt(t[1], 10);
                if (r != this.index) {
                    $("#d_" + this.index).css("background-color", d);
                    this.index = r

                }
                if ( - 1 != this.HIndex) {
                    $("#d_" + this.index + "_" + this.HIndex).css("text-decoration", "none");
                    this.HIndex = -1

                }

            }

        }
        this.needCreatedItem = true

    };
    a.prototype.mouseOutItem = function(r) {
        if (null != r) {
            r.style.backgroundColor = d

        }
        this.needCreatedItem = false

    };
    a.prototype.mouseOverEvItem = function(t, u) {
        if ("function" == typeof(t.stopPropagation)) {
            t.stopPropagation()

        } else {
            t.cancelBubble = true

        }
        var r = parseInt(u.parentNode.id.split("_")[1]);
        this.index = r;
        var s = parseInt(u.id.split("_")[2], 10);
        this.HIndex = s;
        u.parentNode.style.backgroundColor = o;
        u.style.textDecoration = "underline";
        this.needCreatedItem = true

    };
    a.prototype.mouseOutEvItem = function(r, s) {
        if ("function" == typeof(r.stopPropagation)) {
            r.stopPropagation()

        } else {
            r.cancelBubble = true

        }
        s.parentNode.style.backgroundColor = d;
        s.style.textDecoration = "none"

    };
    a.prototype.moveUp = function() {
        var t = this;
        if (t.length > 0) {
            t.index -= 1;
            if (t.index < 0) {
                t.index = t.length - 1

            }
            var s = t.index;
            var r = (s == t.length - 1 ? 0: s + 1);
            t.selectItemNode(s, r)

        }

    };
    a.prototype.moveDown = function() {
        var s = this;
        if (s.length > 0) {
            s.index = ($o.index + 1) % $o.length;
            var r = s.index;
            var t = (0 === r ? $o.length - 1: r - 1);
            s.selectItemNode(r, t)

        }

    };
    a.prototype.selectItemNode = function(t, A) {
        var u = this;
        var z = $("#d_" + t);
        var w = $("#d_" + A);
        if (z.length > 0 && w.length > 0) {
            z.css("background-color", o);
            if (t != A) {
                w.css("background-color", d)

            }
            var y = z.attr("title");
            if (null != y && !y.trim().isEmpty()) {
                l.val(y)

            }
            var v = z.attr("cid");
            if (null != v && "" != v) {
                search.cid = v

            } else {
                search.cid = null

            }
            var x = z.attr("cLevel");
            if (null != x && "" != x) {
                search.cLevel = x

            } else {
                search.cLevel = null

            }
            search.ev_val = null;
            if ( - 1 != u.HIndex) {
                var s = $("#d_" + A + "_" + u.HIndex);
                if (s.length > 0) {
                    s.css("text-decoration", "none");
                    u.HIndex = -1

                }

            }
            var r = z.attr("class");
            if ( - 1 == u.HIndex && r == "item3") {
                u.moveRight()

            }

        }

    };
    a.prototype.moveLeft = function(r) {
        var y = this;
        var C = $("#d_" + y.index);
        var t = C.attr("class");
        if (null != t && "item3" == t) {
            var A = C.attr("ev_amount");
            var z = parseInt(A, 10);
            y.HIndex -= 1;
            if (y.HIndex < 0) {
                y.HIndex = z - 1

            }
            var w = y.HIndex;
            var B = "#d_" + y.index + "_" + y.HIndex;
            var v = $(B);
            var s = (w == z - 1 ? 0: w + 1);
            var x = "#d_" + y.index + "_" + s;
            var u = $(x);
            y.selectHorizontalItemNode(v, u)

        }

    };
    a.prototype.moveRight = function(r) {
        var x = this;
        var A = $("#d_" + x.index);
        var t = A.attr("class");
        if (null != t && "item3" == t) {
            var z = A.attr("ev_amount");
            var y = parseInt(z, 10);
            x.HIndex = (x.HIndex + 1) % y;
            var w = x.HIndex;
            var s = (0 == w ? y - 1: w - 1);
            var v = $("#d_" + x.index + "_" + w);
            var u = $("#d_" + x.index + "_" + s);
            x.selectHorizontalItemNode(v, u)

        }

    };
    a.prototype.selectHorizontalItemNode = function(s, t) {
        s.css("text-decoration", "underline");
        if (s.attr("id") != t.attr("id")) {
            t.css("text-decoration", "none")

        }
        var r = s.attr("ev_val");
        if (null != r && !r.trim().isEmpty()) {
            search.ev_val = r

        } else {
            search.ev_val = null

        }

    };
    a.prototype.keyup = function(u) {
        var t = this;
        var s = u;
        if (null != window.event && "object" == typeof(window.event)) {
            s = window.event

        }
        if (0 == l.length) {
            l = $("#key")

        }
        if (0 == q.length) {
            q = $("tie")

        }
        var r = l.val().trim();
        if ("" == r) {
            q.html("").hide();
            return

        }
        var w = s.keyCode;
        switch (w) {
            case 37:
            t.moveLeft(s);
            break;
            case 38:
            t.moveUp(s);
            break;
            case 39:
            t.moveRight(s);
            break;
            case 40:
            t.moveDown();
            break;
            case 27:
            t.hideTip();
            break;
            default:
            r = encodeURIComponent(r);
            var v = e.replace(p, {
                keyword: r

            });
            $.ajax({
                url: v,
                dataType: "jsonp",
                scriptCharset: "utf-8",
                jsonp: "jsonp_callback"

            });
            break

        }

    };
    a.prototype.onloadItems = function(ap) {
        var ax = l.val().trim();
        if ("" == ax) {
            q.html("").hide();
            return

        }
        var F = ap.length;
        if (0 == F) {
            this.hideTip();
            return

        }
        var ah = this;
        ah.init();
        var Y = "";
        var ad = "";
        var G = "";
        var U = 0;
        if ("undefined" != typeof(window.pageConfig) && "undefined" != typeof(window.pageConfig.searchType)) {
            U = window.pageConfig.searchType

        }
        var W = /^[1-8]4$/;
        var M = false;
        var X = false;
        var s = 0;
        var I = 0;
        for (var aw = 0, av = 0, O = F; aw < O; aw++) {
            var w = ap[aw];
            var z = l.val().trim();
            var P = new RegExp("^(" + z + ")");
            var Q = w.keyword.trim();
            var ag = Q.search(P);
            var S = Q;
            if (ag > -1) {
                var aA = Q.replace(P, "");
                S = z + "<strong>" + aA + "</strong>"

            }
            if ("string" == typeof(w.cid) && !w.cid.trim().isEmpty()) {
                if ("" == ad) {
                    ad = e.replace(k, {
                        id: av,
                        title: w.keyword,
                        keyword: S

                    });
                    av += 1

                }
                if ("string" == typeof(w.cname) && w.cname.trim().isEmpty()) {
                    F -= 1;
                    continue

                }
                var H = w.level;
                if (null == H || "undefined" == typeof(H)) {
                    F -= 1;
                    continue

                }
                if (0 == U) {
                    X = true;
                    if ("string" == typeof(H) && W.test(H)) {
                        F -= 1;
                        continue

                    }

                } else {
                    if (5 == U) {
                        if ("string" == typeof(H) && !(/^[5-8]2$/.test(H))) {
                            F -= 1;
                            continue

                        }

                    } else {
                        if (1 == U || 2 == U || 3 == U || 4 == U) {
                            F -= 1;
                            continue

                        }

                    }

                }
                var al = "";
                if ("3" == H && null != w.expand && (w.expand instanceof Array) && w.expand.length > 0) {
                    M = true

                } else {
                    M = false

                }
                var E = "";
                if (M) {
                    E = "item2"

                } else {
                    E = "item1"

                }
                var ak = e.replace(b, {
                    id: av,
                    title: w.keyword,
                    cid: w.cid,
                    cLevel: w.level,
                    className: E,
                    cname: w.cname

                });
                var T = "";
                if (M) {
                    var J = w.expand;
                    s = w.expand.length;
                    loopEs: for (var au = 0; au < s; au++) {
                        var aq = w.expand[au];
                        var aa = aq.esname;
                        if (null == aa || "undefined" == typeof(aa) || "" == aa) {
                            continue loopEs

                        }
                        var ae = aq.esid;
                        if (null == ae || "undefined" == typeof(ae) || "" == ae) {
                            continue loopEs

                        }
                        av += 1;
                        if (0 == I) {
                            I = av

                        }
                        var A = aq.evs;
                        var ar = 0;
                        var v = false;
                        if (null != A && (A instanceof Array) && A.length > 0) {
                            v = true;
                            ar = A.length

                        } else {
                            v = false

                        }
                        var V = e.replace(f, {
                            id: av,
                            cid: w.cid,
                            cLevel: w.level,
                            es_name: aa

                        });
                        if (v) {
                            sEvTmp = "";
                            loopEv: for (var an = 0; an < ar; an++) {
                                var N = A[an];
                                var am = N.evname;
                                var L = N.evid;
                                if (null == am || "" == am) {
                                    continue loopEv

                                }
                                if (null == L || "" == L) {
                                    continue loopEv

                                }
                                var D = encodeURIComponent(aa) + "_" + encodeURIComponent(am);
                                if ("\u4EF7\u683C" == aa) {
                                    var az = "";
                                    if (am.search(/(\d*).*\u4E0B/) > -1) {
                                        az = "0-" + RegExp.$1

                                    } else {
                                        if (am.search(/(\d*).*\u4E0A/) > -1) {
                                            az = RegExp.$1 + "gt"

                                        } else {
                                            az = am.replace(/\uff0d/g, "-")

                                        }

                                    }
                                    D = "exprice_" + az

                                }
                                if ("\u54C1\u724C" == aa) {
                                    D = "exbrand_" + encodeURIComponent(am)

                                }
                                var ao = av + "_" + an;
                                sEvTmp += e.replace(m, {
                                    id: ao,
                                    title: w.keyword,
                                    ev_name: am,
                                    ev_val: D,
                                    cid: w.cid,
                                    cLevel: w.level

                                })

                            }
                            sEvTmp += e.replace(c, {
                                title: w.keyword,
                                cid: w.cid,
                                cLevel: w.level

                            });
                            V = e.replace(V, {
                                evs: sEvTmp

                            })

                        }
                        T += V

                    }

                }
                ak += T;
                G += ak

            } else {
                var ak = e.replace(h, {
                    id: av,
                    title: w.keyword,
                    keyword: S

                });
                Y += ak

            }
            av += 1

        }
        if ("" != ad) {
            ad = e.replace(ad, {
                categorys: G

            });
            Y = ad + Y;
            ah.length = F + 1

        } else {
            ah.length = F

        }
        ah.length += s;
        if ("" != Y) {
            Y += '<li class="close" onclick="$o.hideTip()">\u5173\u95ed</li>';
            q.html(Y).show();
            var ai = $("ul#shelper .item3");
            var ab = ai.length;
            if (ab <= 3) {
                for (var aw = 0; aw < ab; aw++) {
                    var R = ai[aw];
                    var ac = $(R);
                    var B = ac.children("a");
                    var aj = ac.offset().top;
                    var Z = ac.height();
                    var C = aj + Z / 2;
                    var K = B.length - 1;
                    var y = null;
                    var x = null;
                    evLoop: for (var av = K; av >= 0; av--) {
                        var u = B[av];
                        if (null == y) {
                            y = u.parentNode

                        }
                        var af = $(u);
                        if (av == K) {
                            x = af

                        }
                        var ay = af.offset().top;
                        if (av < K && ay > C) {
                            var aB = u;
                            y.removeChild(aB)

                        } else {
                            continue evLoop

                        }

                    }
                    if (null != x && null != y) {
                        var r = y.children.length;
                        if (x.offset().top > C && r >= 2) {
                            y.removeChild(y.children[r - 2]);
                            r -= 1

                        }
                        var at = "d_" + (I + aw) + "_" + (r - 2);
                        y.lastChild.setAttribute("id", at);
                        y.setAttribute("ev_amount", r - 1)

                    }

                }

            }

        } else {
            q.html("").hide()

        }

    };
    var j = new a();
    l.keyup(function(r) {
        j.keyup(r)

    });
    l.blur(function() {
        if (!j.needCreatedItem) {
            j.hideTip()

        }

    });
    return j

})();
pageConfig.FN_InitSidebar = function() {
    if (!$("#toppanel").length) {
        $(document.body).prepend('<div class="w ld" id="toppanel"></div>')

    }
    $("#toppanel").append('<div id="sidepanel" class="hide"></div>');
    var a = $("#sidepanel");
    this.scroll = function() {
        var b = this;
        $(window).bind("scroll", 
        function() {
            var c = document.body.scrollTop || document.documentElement.scrollTop;
            if (c == 0) {
                a.hide()

            } else {
                a.show()

            }

        });
        b.initCss();
        $(window).bind("resize", 
        function() {
            b.initCss()

        })

    };
    this.initCss = function() {
        var b,
        c = pageConfig.compatible ? 1210: 990;
        if (screen.width >= 1210) {
            if ($.browser.msie && $.browser.version <= 6) {
                b = {
                    right: "-26px"

                }

            } else {
                b = {
                    right: (document.documentElement.clientWidth - c) / 2 - 26 + "px"

                }

            }
            a.css(b)

        }

    };
    this.addCss = function(b) {
        a.css(b)

    };
    this.addItem = function(b) {
        a.append(b)

    };
    this.setTop = function() {
        this.addItem("<a href='#' class='gotop' title='\u4f7f\u7528\u5feb\u6377\u952eT\u4e5f\u53ef\u8fd4\u56de\u9876\u90e8\u54e6\uff01'><b></b>\u8fd4\u56de\u9876\u90e8</a>")

    }

};

