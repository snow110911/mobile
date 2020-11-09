package com.example.project.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.example.project.R;
public class EmotionData {
    public static LinkedHashMap<String, Integer> EMOTION_CLASSIC_MAP;

	static {
        EMOTION_CLASSIC_MAP = new LinkedHashMap<>();
        EMOTION_CLASSIC_MAP.put("[微笑]", R.drawable.expression_1);
        EMOTION_CLASSIC_MAP.put("[撇嘴]", R.drawable.expression_2);
        EMOTION_CLASSIC_MAP.put("[色]", R.drawable.expression_3);
        EMOTION_CLASSIC_MAP.put("[发呆]", R.drawable.expression_4);
        EMOTION_CLASSIC_MAP.put("[得意]", R.drawable.expression_5);
        EMOTION_CLASSIC_MAP.put("[流泪]", R.drawable.expression_6);
        EMOTION_CLASSIC_MAP.put("[害羞]", R.drawable.expression_7);
        EMOTION_CLASSIC_MAP.put("[闭嘴]", R.drawable.expression_8);
        EMOTION_CLASSIC_MAP.put("[睡]", R.drawable.expression_9);

        EMOTION_CLASSIC_MAP.put("[大哭]", R.drawable.expression_10);
        EMOTION_CLASSIC_MAP.put("[尴尬]", R.drawable.expression_11);
        EMOTION_CLASSIC_MAP.put("[发怒]", R.drawable.expression_12);
        EMOTION_CLASSIC_MAP.put("[调皮]", R.drawable.expression_13);
        EMOTION_CLASSIC_MAP.put("[呲牙]", R.drawable.expression_14);
        EMOTION_CLASSIC_MAP.put("[惊讶]", R.drawable.expression_15);
        EMOTION_CLASSIC_MAP.put("[难过]", R.drawable.expression_16);
        // empty 17
        EMOTION_CLASSIC_MAP.put("[囧]", R.drawable.expression_18);
        EMOTION_CLASSIC_MAP.put("[抓狂]", R.drawable.expression_19);

        EMOTION_CLASSIC_MAP.put("[吐]", R.drawable.expression_20);
        EMOTION_CLASSIC_MAP.put("[偷笑]", R.drawable.expression_21);
        EMOTION_CLASSIC_MAP.put("[愉快]", R.drawable.expression_22);
        EMOTION_CLASSIC_MAP.put("[白眼]", R.drawable.expression_23);
        EMOTION_CLASSIC_MAP.put("[傲慢]", R.drawable.expression_24);
        // empty 25
        EMOTION_CLASSIC_MAP.put("[困]", R.drawable.expression_26);
        EMOTION_CLASSIC_MAP.put("[惊恐]", R.drawable.expression_27);
        EMOTION_CLASSIC_MAP.put("[流汗]", R.drawable.expression_28);
        EMOTION_CLASSIC_MAP.put("[憨笑]", R.drawable.expression_29);

        EMOTION_CLASSIC_MAP.put("[悠闲]", R.drawable.expression_30);
        EMOTION_CLASSIC_MAP.put("[奋斗]", R.drawable.expression_31);
        EMOTION_CLASSIC_MAP.put("[咒骂]", R.drawable.expression_32);
        EMOTION_CLASSIC_MAP.put("[疑问]", R.drawable.expression_33);
        EMOTION_CLASSIC_MAP.put("[嘘]", R.drawable.expression_34);
        EMOTION_CLASSIC_MAP.put("[晕]", R.drawable.expression_35);
        // empty 36
        EMOTION_CLASSIC_MAP.put("[衰]", R.drawable.expression_37);
        EMOTION_CLASSIC_MAP.put("[骷髅]", R.drawable.expression_38);
        EMOTION_CLASSIC_MAP.put("[敲打]", R.drawable.expression_39);

        EMOTION_CLASSIC_MAP.put("[再见]", R.drawable.expression_40);
        EMOTION_CLASSIC_MAP.put("[擦汗]", R.drawable.expression_41);
        EMOTION_CLASSIC_MAP.put("[抠鼻]", R.drawable.expression_42);
        EMOTION_CLASSIC_MAP.put("[鼓掌]", R.drawable.expression_43);
        // empty 44
        EMOTION_CLASSIC_MAP.put("[坏笑]", R.drawable.expression_45);
        EMOTION_CLASSIC_MAP.put("[左哼哼]", R.drawable.expression_46);
        EMOTION_CLASSIC_MAP.put("[右哼哼]", R.drawable.expression_47);
        EMOTION_CLASSIC_MAP.put("[哈欠]", R.drawable.expression_48);
        EMOTION_CLASSIC_MAP.put("[鄙视]", R.drawable.expression_49);

        EMOTION_CLASSIC_MAP.put("[委屈]", R.drawable.expression_50);
        EMOTION_CLASSIC_MAP.put("[快哭了]", R.drawable.expression_51);
        EMOTION_CLASSIC_MAP.put("[阴险]", R.drawable.expression_52);
        EMOTION_CLASSIC_MAP.put("[亲亲]", R.drawable.expression_53);
        // empty 54
        EMOTION_CLASSIC_MAP.put("[可怜]", R.drawable.expression_55);
        EMOTION_CLASSIC_MAP.put("[菜刀]", R.drawable.expression_56);
        EMOTION_CLASSIC_MAP.put("[西瓜]", R.drawable.expression_57);
        EMOTION_CLASSIC_MAP.put("[啤酒]", R.drawable.expression_58);
        // empty 59

        // empty 60
        EMOTION_CLASSIC_MAP.put("[咖啡]", R.drawable.expression_61);
        // empty 62
        EMOTION_CLASSIC_MAP.put("[猪头]", R.drawable.expression_63);
        EMOTION_CLASSIC_MAP.put("[玫瑰]", R.drawable.expression_64);
        EMOTION_CLASSIC_MAP.put("[凋谢]", R.drawable.expression_65);
        EMOTION_CLASSIC_MAP.put("[嘴唇]", R.drawable.expression_66);
        EMOTION_CLASSIC_MAP.put("[爱心]", R.drawable.expression_67);
        EMOTION_CLASSIC_MAP.put("[心碎]", R.drawable.expression_68);
        EMOTION_CLASSIC_MAP.put("[蛋糕]", R.drawable.expression_69);

        // empty 70
        EMOTION_CLASSIC_MAP.put("[炸弹]", R.drawable.expression_71);
        // empty 72- 74
        EMOTION_CLASSIC_MAP.put("[便便]", R.drawable.expression_75);
        EMOTION_CLASSIC_MAP.put("[月亮]", R.drawable.expression_76);
        EMOTION_CLASSIC_MAP.put("[太阳]", R.drawable.expression_77);
        // empty 78
        EMOTION_CLASSIC_MAP.put("[拥抱]", R.drawable.expression_79);

        EMOTION_CLASSIC_MAP.put("[强]", R.drawable.expression_80);
        EMOTION_CLASSIC_MAP.put("[弱]", R.drawable.expression_81);
        EMOTION_CLASSIC_MAP.put("[握手]", R.drawable.expression_82);
        EMOTION_CLASSIC_MAP.put("[胜利]", R.drawable.expression_83);
        EMOTION_CLASSIC_MAP.put("[抱拳]", R.drawable.expression_84);
        EMOTION_CLASSIC_MAP.put("[勾引]", R.drawable.expression_85);
        EMOTION_CLASSIC_MAP.put("[拳头]", R.drawable.expression_86);
        // empty 87 - 89

        EMOTION_CLASSIC_MAP.put("[OK]", R.drawable.expression_90);
        // empty 91 92
        EMOTION_CLASSIC_MAP.put("[跳跳]", R.drawable.expression_93);
        EMOTION_CLASSIC_MAP.put("[发抖]", R.drawable.expression_94);
        EMOTION_CLASSIC_MAP.put("[怄火]", R.drawable.expression_95);
        EMOTION_CLASSIC_MAP.put("[转圈]", R.drawable.expression_96);

        // reference http://www.oicqzone.com/tool/emoji/

        EMOTION_CLASSIC_MAP.put(emojiString(0x1F604), R.drawable.expression_97);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F637), R.drawable.expression_98);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F602), R.drawable.expression_99);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F61D), R.drawable.expression_101);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F633), R.drawable.expression_102);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F631), R.drawable.expression_103);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F614), R.drawable.expression_104);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F612), R.drawable.expression_105);

        EMOTION_CLASSIC_MAP.put("[嘿哈]", R.drawable.expression_107);
        EMOTION_CLASSIC_MAP.put("[捂脸]", R.drawable.expression_108);
        EMOTION_CLASSIC_MAP.put("[奸笑]", R.drawable.expression_106);
        EMOTION_CLASSIC_MAP.put("[机智]", R.drawable.expression_109);
        EMOTION_CLASSIC_MAP.put("[皱眉]", R.drawable.expression_119);
        EMOTION_CLASSIC_MAP.put("[耶]", R.drawable.expression_113);

        EMOTION_CLASSIC_MAP.put(emojiString(0x1F47B), R.drawable.expression_114);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F64F), R.drawable.expression_115);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F4AA), R.drawable.expression_116);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F389), R.drawable.expression_117);
        EMOTION_CLASSIC_MAP.put(emojiString(0x1F381), R.drawable.expression_118);

        EMOTION_CLASSIC_MAP.put("[红包]", R.drawable.expression_111);
	}

	private static String emojiString(int code) {
        return new String(Character.toChars(code));
    }

	public static int size() {
        return EMOTION_CLASSIC_MAP.size();
    }

	public static int getImgByName(String imgName) {
		Integer integer = EMOTION_CLASSIC_MAP.get(imgName);
		return integer == null ? -1 : integer;
	}

    public static List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();

        Iterator<Map.Entry<String, Integer>> entries = EMOTION_CLASSIC_MAP.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            notes.add(new Note(entry.getKey(), entry.getValue()));
        }

        return notes;
    }
}
