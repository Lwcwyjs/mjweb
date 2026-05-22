package com.util;

public class ConverUtil {
    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return
     */
    public String convertlwzt(String lwzt) {
        String srt = "";
        if(lwzt!=null) {
            if (lwzt.equals("0")) {
                srt = "未联网";
            } else if (lwzt.equals("1")) {
                srt = "已联网";
            }
        }
        return srt;
    }
    public  String ConvertCpys(String cpys)
    {
        String srt="5";
        if(cpys!=null) {
            if (cpys.equals("蓝色")) {
                srt = "0";
            } else if (cpys.equals("黄色")) {
                srt = "1";
            } else if (cpys.equals("白色")) {
                srt = "2";
            } else if (cpys.equals("黑色")) {
                srt = "3";
            } else if (cpys.equals("绿色")) {
                srt = "4";
            } else if (cpys.equals("黄绿色")) {
                srt = "6";
            } else {
                srt = "5";
            }
        }
        return srt;
    }
    public String convertjclx(String jclx) {
        String srt = "";
        if(jclx!=null) {
            if (jclx.equals("1")) {
                srt = "进";
            } else if (jclx.equals("2")) {
                srt = "出";
            }
        }
        return srt;
    }
    public String convertbgzt(String bgzt) {
        String srt = "";
        if(bgzt!=null) {
            if (bgzt.equals("1")) {
                srt = "手动抬杆";
            } else if (bgzt.equals("2")) {
                srt = "自动抬杆";
            }
        }
        return srt;
    }
    public String  convertrlzl(String rlzl) throws Exception {
        String rlzlid="Z";
        if(rlzl!=null) {
            if (rlzl.equals("汽油")) {
                rlzlid = "A";
            } else if (rlzl.equals("柴油")) {
                rlzlid = "B";
            } else if (rlzl.indexOf("电")>=0||rlzl.indexOf("新能源")>=0) {
                rlzlid = "C";
            } else if (rlzl.equals("混合油")) {
                rlzlid = "D";
            } else if (rlzl.equals("天然气")) {
                rlzlid = "E";
            } else{
                rlzlid = "Z";
            }
        }
        return rlzlid;
    }
    public String  convertrlzlex(String rlzl) throws Exception {
        String rlzlid="其他";
        if(rlzl!=null) {
            if (rlzl.equals("A")) {
                rlzlid = "汽油";
            } else if (rlzl.equals("B")) {
                rlzlid = "柴油";
            } else if (rlzl.equals("C")) {
                rlzlid = "电";
            } else if (rlzl.equals("D")) {
                rlzlid = "混合油";
            } else if (rlzl.equals("E")) {
                rlzlid = "天然气";
            } else if (rlzl.equals("Z")) {
                rlzlid = "其他";
            }
        }
        return rlzlid;
    }
    public String pfsztopfhz(String pfbz) {
        String srt = "";
        if(pfbz!=null) {
            if (pfbz.equals("0")) {
                srt = "国0";
            } else if (pfbz.equals("1")) {
                srt = "国Ⅰ";
            } else if (pfbz.equals("2")) {
                srt = "国Ⅱ";
            } else if (pfbz.equals("3")) {
                srt = "国Ⅲ";
            } else if (pfbz.equals("4")) {
                srt = "国Ⅳ";
            } else if (pfbz.equals("5")) {
                srt = "国Ⅴ";
            } else if (pfbz.equals("6")) {
                srt = "国Ⅵ";
            } else if (pfbz.equals("D")) {
                srt = "电动";
            } else if (pfbz.equals("X")) {
                srt = "未知";
            }
            else{
                srt=pfbz;
            }
        }
        return srt;
    }
    public String pfsztopfjd(String pfbz) {
        String srt = "";
        if(pfbz!=null) {
            if (pfbz.equals("0")) {
                srt = "国0";
            } else if (pfbz.equals("1")) {
                srt = "国Ⅰ";
            } else if (pfbz.equals("2")) {
                srt = "国Ⅱ";
            } else if (pfbz.equals("3")) {
                srt = "国Ⅲ";
            } else if (pfbz.equals("4")) {
                srt = "国Ⅳ";
            } else if (pfbz.equals("5")) {
                srt = "国Ⅴ";
            } else if (pfbz.equals("6")) {
                srt = "国Ⅵ";
            } else if (pfbz.equals("D")) {
                srt = "新能源";
            } else if (pfbz.equals("X")) {
                srt = "未知";
            }
            else{
                srt=pfbz;
            }
        }
        return srt;
    }
    public String pfhztopfsz(String pfbz)
    {
        String srt = pfbz;
        if (pfbz.equals("国0")||pfbz.contains("0"))
        {
            srt = "0";
        }
        else if (pfbz.equals("国Ⅰ")||pfbz.contains("1"))
        {
            srt = "1";
        }
        else if (pfbz.equals("国Ⅱ")||pfbz.contains("2"))
        {
            srt = "2";
        }
        else if (pfbz.equals("国Ⅲ")||pfbz.contains("3"))
        {
            srt = "3";
        }
        else if (pfbz.equals("国Ⅳ")||pfbz.contains("4"))
        {
            srt = "4";
        }
        else if (pfbz.equals("国Ⅴ")||pfbz.contains("5"))
        {
            srt = "5";
        }
        else if (pfbz.equals("国Ⅵ")||pfbz.contains("6"))
        {
            srt = "6";
        }
        else if (pfbz.equals("电动"))
        {
            srt = "D";
        }
        else if (pfbz.equals("未知"))
        {
            srt = "X";
        }
        return srt;
    }
}
