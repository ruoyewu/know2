package com.wuruoye.know.util

/**
 * Created at 2019/3/19 19:16 by wuruoye
 * Description:
 */
object ColorUtil {
    fun color2argb(color: Int): FloatArray {
        val a = (color.shr(24) and 0xff)
        val r = (color.shr(16) and 0xff)
        val g = (color.shr( 8) and 0xff)
        val b = (color                  and 0xff)
        return floatArrayOf(a.toFloat(), r.toFloat(), g.toFloat(), b.toFloat())
    }

    fun argb2color(array: FloatArray): Int {
        return argb2color(array[0], array[1], array[2], array[3])
    }

    fun argb2color(a: Float, r: Float, g: Float, b: Float): Int {
        return Math.round(a).shl(24) or Math.round(r).shl(16) or
                Math.round(g).shl(8) or Math.round(b)
    }

    fun color2hex(color: Int): String {
        val builder = StringBuilder("#")
        val array = color2argb(color)
        for (f in array) {
            val s = Integer.toHexString(f.toInt())
            if (s.length == 1) builder.append(0);
            builder.append(s);
        }
        return builder.toString().toUpperCase()
    }

    fun argb2hex(array: FloatArray): String {
        val builder = StringBuilder("#")
        for (f in array) {
            val s = Integer.toHexString(f.toInt())
            if (s.length == 1) builder.append(0);
            builder.append(s);
        }
        return builder.toString().toUpperCase()
    }

    fun argb2hex(a: Float, r: Float, g: Float, b: Float): String {
        return ColorUtil.argb2hex(floatArrayOf(a, r, g, b))
    }

    fun hex2color(hex: String): Int {
        val array = FloatArray(4)
        for (i in 0 until 4) {
            val c = Integer.valueOf(hex.substring(1+i*2, 3+i*2))
            array[i] = c.toFloat()
        }
        return ColorUtil.argb2color(array)
    }
}