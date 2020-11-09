package com.example.project.utils;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpannableMaker {
    private static Pattern sPatternEmotion =
            Pattern.compile("\\[([\u4e00-\u9fa5\\w])+\\]|[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]");

    public static Spannable buildEmotionSpannable(Context context, String text, int textSize) {

        Matcher matcherEmotion = sPatternEmotion.matcher(text);
        SpannableString spannableString = new SpannableString(text);

        while (matcherEmotion.find()) {
            String key = matcherEmotion.group();
            int imgRes = EmotionData.getImgByName(key);
            if (imgRes != -1) {
                int start = matcherEmotion.start();
                ImageSpan span = createImageSpanByRes(imgRes, context, textSize);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    private static ImageSpan createImageSpanByRes(int imgRes, Context context, int textSize) {
        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
        ImageSpan span = null;
        int size = textSize * 13 / 10;
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
        span = new ImageSpan(context, scaleBitmap);
        return span;
    }
}
