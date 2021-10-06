package com.github.tianjing.tgtools.security.xss.util;


import tgtools.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 田径
 * @date 2019-10-17 14:55
 * @desc
 **/
public class XssHelper {

    public static final List<Pattern> XSS_REGEX_PATTERNS = getPatterns();

    /**
     * @param value
     * @return
     */
    public static boolean matchXssContent(String value) {
        if (StringUtil.isNotBlank(value)) {
            Matcher matcher = null;
            for (Pattern pattern : XSS_REGEX_PATTERNS) {
                matcher = pattern.matcher(value);
                // 匹配
                if (matcher.find()) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        if (StringUtil.isNotEmpty(matcher.group(i))) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    /**
     * 正则规则字符串
     *
     * @return
     */
    public static List<Object[]> getXssPatternList() {
        List<Object[]> ret = new ArrayList<Object[]>();
        ret.add(new Object[]{"<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"(javascript:|vbscript:|view-source:)*", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        return ret;
    }

    /**
     * 正则对象
     *
     * @return
     */
    public static List<Pattern> getPatterns() {
        List<Pattern> vList = new ArrayList<Pattern>();
        String vRegex = null;
        Integer vFlag = null;
        int arrLength = 0;
        List<Object[]> vXssPatternList = getXssPatternList();
        for (Object[] vArr : vXssPatternList) {
            arrLength = vArr.length;
            for (int i = 0; i < arrLength; i++) {
                vRegex = (String) vArr[0];
                vFlag = (Integer) vArr[1];
                vList.add(Pattern.compile(vRegex, vFlag));
            }
        }
        return vList;
    }
}
