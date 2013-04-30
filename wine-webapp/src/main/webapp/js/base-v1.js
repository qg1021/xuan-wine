window.pageConfig = window.pageConfig || {};
pageConfig.wideVersion = (function() {
    return (screen.width >= 1210);


})();
if (pageConfig.wideVersion && pageConfig.compatible) {
    //document.getElementsByTagName("body")[0].className = "root61";
	


}
pageConfig.FN_GetUrl = function(a, b) {
    if (typeof a == "string") {
        return a;


    } else {
        return pageConfig.FN_GetDomain(a) + b + ".html";


    }


};
pageConfig.FN_StringFormat = function() {
    var e = arguments[0],
    f = arguments.length;
    if (f > 0) {
        for (var d = 0; d < f; d++) {
            e = e.replace(new RegExp("\\{" + d + "\\}", "g"), arguments[d + 1]);


        }


    }
    return e;


};
pageConfig.FN_GetDomain = function(b, c) {
    var a = "http://{0}.360buy.com/{1}";
    switch (b) {
        case 1:
        a = this.FN_StringFormat(a, "www", "product/");
        break;
        case 2:
        a = this.FN_StringFormat(a, "book", "");
        break;
        case 3:
        a = this.FN_StringFormat(a, "mvd", "");
        break;
        default:
        break;


    }
    return a;


};
pageConfig.FN_GetImageDomain = function(d) {
    var c,
    d = String(d);
    switch (d.match(/(\d)$/)[1] % 5) {
        case 0:
        c = 10;
        break;
        case 1:
        c = 11;
        break;
        case 2:
        c = 12;
        break;
        case 3:
        c = 13;
        break;
        case 4:
        c = 14;
        break;
        default:
        c = 10;


    }
    return "http://img{0}.360buyimg.com/".replace("{0}", c);


};
pageConfig.FN_ImgError = function(b) {
    var c = b.getElementsByTagName("img");
    for (var a = 0; a < c.length; a++) {
        c[a].onerror = function() {
            var d = "",
            e = this.getAttribute("data-img");
            if (!e) {
                return;


            }
            switch (e) {
                case "1":
                d = "err-product";
                break;
                case "2":
                d = "err-poster";
                break;
                case "3":
                d = "err-price";
                break;
                default:
                return;


            }
            this.src = "images/blank.gif";
            this.className = d;


        };


    }


};
pageConfig.FN_SetPromotion = function(b) {
    if (b == 0) {
        return;


    }
    var e = "����,���,�׷�,���,����,ֱ��,��Ʒ,����,����,����",
    d = e.split(",")[parseInt(b) - 1],
    c = "<b class='pi{0}'>{1}</b>";
    switch (d.length) {
        case 1:
        c = c.replace("{0}", " pix1 pif1");
        break;
        case 2:
        c = c.replace("{0}", " pix1");
        break;
        case 4:
        c = c.replace("{0}", " pix1 pif4");
        break;


    }
    return c.replace("{1}", d);


};
pageConfig.FN_GetRandomData = function(c) {
    var b = 0,
    f = 0,
    a,
    e = [];
    for (var d = 0; d < c.length; d++) {
        a = c[d].weight ? parseInt(c[d].weight) : 1;
        e[d] = [];
        e[d].push(b);
        b += a;
        e[d].push(b);


    }
    f = Math.ceil(b * Math.random());
    for (var d = 0; d < e.length; d++) {
        if (f > e[d][0] && f <= e[d][1]) {
            return c[d];


        }


    }


};
pageConfig.FN_GetCompatibleData = function(b) {
    var a = (screen.width < 1210);
    if (a) {
        b.width = b.widthB;
        b.height = b.heightB;
        b.src = b.srcB;


    }
    return b;


};
pageConfig.FN_InitSlider = function(c, g) {
    var b = function(j, i) {
        return j.group - i.group;


    };
    g.sort(b);
    var h = g[0].data,
    f = [],
    e = (h.length == 3) ? "style2": "style1",
    a;
    f.push('<div class="slide-itemswrap"><ul class="slide-items"><li class="');
    f.push(e);
    f.push('" data-tag="');
    f.push(g[0].aid);
    f.push('">');
    for (var d = 0; d < h.length; d++) {
        a = this.FN_GetCompatibleData(h[d]);
        f.push('<div class="fore');
        f.push(d + 1);
        f.push('" width="');
        f.push(a.width);
        f.push('" height="');
        f.push(a.height);
        f.push('"><a target="_blank" href="');
        f.push(a.href);
        f.push('" title="');
        f.push(a.alt);
        f.push('" clstag="homepage|keycount|home2012|091');
        f.push(d + 1);
        f.push('"><img src="');
        if (d == 0) {
            f.push(a.src);


        } else {
            f.push('http://misc.360buyimg.com/lib/img/e/blank.gif" style="background:url(');
            f.push(a.src);
            f.push(") no-repeat center 0;");


        }
        f.push('" width="');
        f.push(a.width);
        f.push('" height="');
        f.push(a.height);
        f.push('" /></a></div>');


    }
    f.push('</li></ul></div><div class="slide-controls"><span class="curr">1</span></div>');
    document.getElementById(c).innerHTML = f.join("");


};
function login() {
    location.href = "login.action?returnUrl=" + escape(location.href).replace(/\//g, "%2F");
    return false;


}
function regist() {
    location.href = "register.action?returnUrl=" + escape(location.href);
    return false;


}
function createCookie(c, d, f, e) {
    var e = (e) ? e: "/";
    if (f) {
        var b = new Date();
        b.setTime(b.getTime() + (f * 24 * 60 * 60 * 1000));
        var a = "; expires=" + b.toGMTString();


    } else {
        var a = "";


    }
    document.cookie = c + "=" + d + a + "; path=" + e;


}
function readCookie(b) {
    var e = b + "=";
    var a = document.cookie.split(";");
    for (var d = 0; d < a.length; d++) {
        var f = a[d];
        while (f.charAt(0) == " ") {
            f = f.substring(1, f.length);


        }
        if (f.indexOf(e) == 0) {
            return f.substring(e.length, f.length);


        }


    }
    return null;


}

var CookieUtil = {};
CookieUtil.setASCIICookie = function(e, f, b, c, a, d) {
    if ("string" == typeof(e) && "string" == typeof(f)) {
        f = escape(f);
        CookieUtil.setCookie(e, f, b, c, a, d);


    }


};
CookieUtil.setUnicodeCookie = function(e, f, b, c, a, d) {
    if ("string" == typeof(e) && "string" == typeof(f)) {
        f = encodeURIComponent(f);
        CookieUtil.setCookie(e, f, b, c, a, d);


    }


};
CookieUtil.setCookie = function(f, g, b, c, a, e) {
    if ("string" == typeof(f) && "string" == typeof(g)) {
        var d = f + "=" + g;
        if (b) {
            d += "; expires=" + b.toGMTString();


        }
        if (c) {
            d += "; path=" + c;


        }
        if (a) {
            d += "; domain=" + a;


        }
        if (e) {
            d += "; secure";


        }
        document.cookie = d;


    }


};
CookieUtil.getUnicodeCookie = function(b) {
    var d = getCookie(b);
    if ("string" == typeof(b)) {
        var c = getCookie(b);
        if (null == c) {
            return null;


        } else {
            return decodeURIComponent(c);


        }


    } else {
        var a = document.cookie;
        return a;


    }


};
CookieUtil.getASCIICookie = function(b) {
    var d = getCookie(b);
    if ("string" == typeof(b)) {
        var c = getCookie(b);
        if (null == c) {
            return null;


        } else {
            return unescape(c);


        }


    } else {
        var a = document.cookie;
        return a;


    }


};
CookieUtil.getCookie = function(d) {
    var a = document.cookie;
    if ("string" == typeof(d)) {
        var c = "(?:; )?" + d + "=([^;]*);?";
        var b = new RegExp(c);
        if (b.test(a)) {
            return RegExp["$1"];


        } else {
            return null;


        }


    } else {
        return a;


    }


};
CookieUtil.deleteCookie = function(c, b, a) {
    CookieUtil.setCookie(c, "", new Date(0), b, a);


};
var searchlog = (function() {
    var f = "http://sstat.360buy.com/scslog?args=";
    var e = "{keyword}^#psort#^#page#^#cid#^" + encodeURIComponent(document.referrer);
    var d = "2";
    var c = "";
    var a = "";
    var b = function b() {
        var j = "";
        var h = "";
        var l = "";
        var m = "0";
        if (arguments.length > 0) {
            if (arguments[0] == 0) {
                j = f + d + "^" + e + "^^^58^^" + a + "^" + c;


            } else {
                if (arguments[0] == 1) {
                    if (d != 10) {
                        j = f + "1^" + e + "^";


                    } else {
                        j = f + "11^" + e + "^";


                    }
                    for (var k = 1; k < arguments.length; k++) {
                        j += encodeURI(arguments[k]) + "^";


                    }
                    if (arguments.length > 3 && arguments[3] == "51") {
                        h = arguments[1];


                    }
                    if (arguments.length > 3 && arguments[3] == "55") {
                        l = arguments[1];


                    }
                    if (arguments.length > 3 && arguments[3] == "56") {
                        m = arguments[1];


                    }
                    for (var k = 0, g = 5 - arguments.length; k < g; k++) {
                        j += "^";


                    }
                    j += a + "^" + c;


                }


            }


        }
        j = j.replace("#cid#", h);
        j = j.replace("#psort#", l);
        j = j.replace("#page#", m);
        $.getScript(j);


    };
    return b;


})();
function addToFavorite() {
    var d = "http://www.machine1688.com/";
    var c = "东方农机商城-最全面的农业机械产品网络选购咨询平台";
    if (document.all) {
        window.external.AddFavorite(d, c);


    } else {
        if (window.sidebar) {
            window.sidebar.addPanel(c, d, "");


        } else {
            alert("对不起，您的浏览器不支持此操作!\n请您使用菜单栏或Ctrl+D收藏本站。");


        }


    }
}

//设为首页
function setHomepage(pageURL) {
	if (document.all) {
		document.body.style.behavior='url(#default#homepage)';
		document.body.setHomePage(pageURL);
	}
	else if (window.sidebar) {
		if(window.netscape) {
			try {
				netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
			}
			catch (e) {
				alert( "该操作被浏览器拒绝，如果想启用该功能，请在地址栏内输入 about:config,然后将项signed.applets.codebase_principal_support 值该为true" );
			}
		}
		var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components. interfaces.nsIPrefBranch);
		prefs.setCharPref('browser.startup.homepage',pageURL);
	}
}
function login() {
    location.href = "login.action?returnurl=" + escape(location.href).replace(/\//g, "%2F");
    return false;

}

