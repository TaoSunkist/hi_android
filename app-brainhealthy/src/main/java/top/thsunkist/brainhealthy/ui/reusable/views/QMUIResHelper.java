/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.thsunkist.brainhealthy.ui.reusable.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import top.thsunkist.brainhealthy.R;
import top.thsunkist.brainhealthy.ui.reusable.views.QMUIDrawableHelper;
import top.thsunkist.brainhealthy.ui.reusable.views.QMUIViewHelper;
import top.thsunkist.brainhealthy.utilities.QMUIDisplayHelper;

/**
 * @author cginechen
 * @date 2016-09-22
 */
public class QMUIResHelper {
    private static TypedValue sTmpValue;

    public static float getAttrFloatValue(Context context, int attr) {
        return getAttrFloatValue(context.getTheme(), attr);
    }

    public static float getAttrFloatValue(Resources.Theme theme, int attr) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return 0;
        }
        return sTmpValue.getFloat();
    }

    public static int getAttrColor(Context context, int attrRes) {
        return getAttrColor(context.getTheme(), attrRes);
    }

    public static int getAttrColor(Resources.Theme theme, int attr) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return 0;
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrColor(theme, sTmpValue.data);
        }
        return sTmpValue.data;
    }

    @Nullable
    public static ColorStateList getAttrColorStateList(Context context, int attrRes) {
        return getAttrColorStateList(context, context.getTheme(), attrRes);
    }

    @Nullable
    public static ColorStateList getAttrColorStateList(Context context, Resources.Theme theme, int attr) {
        if (attr == 0) {
            return null;
        }
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return null;
        }
        if (sTmpValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && sTmpValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return ColorStateList.valueOf(sTmpValue.data);
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrColorStateList(context, theme, sTmpValue.data);
        }
        if (sTmpValue.resourceId == 0) {
            return null;
        }
        return ContextCompat.getColorStateList(context, sTmpValue.resourceId);
    }

    @Nullable
    public static Drawable getAttrDrawable(Context context, int attr) {
        return getAttrDrawable(context, context.getTheme(), attr);
    }

    @Nullable
    public static Drawable getAttrDrawable(Context context, Resources.Theme theme, int attr) {
        if (attr == 0) {
            return null;
        }
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return null;
        }
        if (sTmpValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && sTmpValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return new ColorDrawable(sTmpValue.data);
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrDrawable(context, theme, sTmpValue.data);
        }

        if (sTmpValue.resourceId != 0) {
            return QMUIDrawableHelper.getVectorDrawable(context, sTmpValue.resourceId);
        }
        return null;
    }

    @Nullable
    public static Drawable getAttrDrawable(Context context, TypedArray typedArray, int index) {
        TypedValue value = typedArray.peekValue(index);
        if (value != null) {
            if (value.type != TypedValue.TYPE_ATTRIBUTE && value.resourceId != 0) {
                return QMUIDrawableHelper.getVectorDrawable(context, value.resourceId);
            }
        }
        return null;
    }

    public static int getAttrDimen(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!context.getTheme().resolveAttribute(attrRes, sTmpValue, true)) {
            return 0;
        }
        return TypedValue.complexToDimensionPixelSize(sTmpValue.data, QMUIDisplayHelper.getDisplayMetrics(context));
    }

    @Nullable
    public static String getAttrString(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!context.getTheme().resolveAttribute(attrRes, sTmpValue, true)) {
            return null;
        }
        CharSequence str = sTmpValue.string;
        return str == null ? null : str.toString();
    }

    public static int getAttrInt(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        context.getTheme().resolveAttribute(attrRes, sTmpValue, true);
        return sTmpValue.data;
    }
}
