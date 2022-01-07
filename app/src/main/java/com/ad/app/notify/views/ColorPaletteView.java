package com.ad.app.notify.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.ad.app.notify.R;
import com.ad.app.notify.utils.Utils;
import com.google.android.material.card.MaterialCardView;

public class ColorPaletteView extends RelativeLayout {

    Context context;
    AttributeSet attrs;
    int styleAttr;

    MaterialCardView view;

    public ColorPaletteView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ColorPaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public ColorPaletteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {

        inflate(context, R.layout.layout_color_palette, this);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ColorPaletteView,
                styleAttr, 0);

        view = findViewById(R.id.view);

        setAttrsToView(arr);
        arr.recycle();
    }

    public void setAttrsToView(TypedArray arr) {

        view.setStrokeColor(Color.parseColor("#A0A0A0"));
        view.setStrokeWidth(
                arr.getBoolean(R.styleable.ColorPaletteView_enabled, false) ?
                        10 : 0);

        view.setCardBackgroundColor(arr.getColor(R.styleable.ColorPaletteView_color,
                Utils.getAttrColor(context, R.attr.colorIcon)));
    }

    public void setEnabled(boolean isEnabled) {
        view.setStrokeColor(Color.parseColor("#707070"));
        view.setStrokeWidth(isEnabled ? 10 : 0);
    }

    public void setColor(int i) {
        this.view.setCardBackgroundColor(i);
    }
}