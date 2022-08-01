package com.example.carkeyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * Created by xpc.
 * <p>
 * Date: 2022/07/29 9:08
 * <p>
 * Desc:车牌号输入 自定义控件
 */

public class CustomCarNumberKeyboard extends RelativeLayout implements View.OnClickListener, KeyboardAction {

    private EditText[] editTexts;
    private Activity mActivity;
    private View mNumView;
    private View mProvinceView;
    private TextView select_end_province;
    private TextView select_end_num;
    int viewCount = 7;
    int focusPosition = 0;
    private LayoutInflater mInflater;
    String inputContent;//最终所有输入框的内容的拼接字符串

    private static final int[] VIEW_IDS = new int[]{
            R.id.item_code_iv1, R.id.item_code_iv2, R.id.item_code_iv3,
            R.id.item_code_iv4, R.id.item_code_iv5, R.id.item_code_iv6,
            R.id.item_code_iv7, R.id.item_code_iv8
    };

    private static final int[] VIEW_PROVINCE_IDS = new int[]{
            R.id.select_province_11_tv, R.id.select_province_12_tv, R.id.select_province_13_tv,
            R.id.select_province_14_tv, R.id.select_province_15_tv, R.id.select_province_16_tv,
            R.id.select_province_17_tv, R.id.select_province_18_tv, R.id.select_province_19_tv,
            R.id.select_province_110_tv,
            R.id.select_province_21_tv, R.id.select_province_22_tv, R.id.select_province_23_tv,
            R.id.select_province_24_tv, R.id.select_province_25_tv, R.id.select_province_26_tv,
            R.id.select_province_27_tv, R.id.select_province_28_tv, R.id.select_province_29_tv,
            R.id.select_province_210_tv,
            R.id.select_province_31_tv, R.id.select_province_32_tv, R.id.select_province_33_tv,
            R.id.select_province_34_tv, R.id.select_province_35_tv, R.id.select_province_35_tv,
            R.id.select_province_36_tv, R.id.select_province_37_tv, R.id.select_province_38_tv,
            R.id.select_province_41_tv, R.id.select_province_42_tv, R.id.select_province_43_tv,
            R.id.select_province_delete_tv
    };

    private static final int[] VIEW_NUM_IDS = new int[]{
            R.id.select_num_100_tv, R.id.select_num_101_tv, R.id.select_num_102_tv,
            R.id.select_num_103_tv, R.id.select_num_104_tv, R.id.select_num_105_tv,
            R.id.select_num_106_tv, R.id.select_num_107_tv, R.id.select_num_108_tv,
            R.id.select_num_109_tv,
            R.id.select_num_200_tv, R.id.select_num_201_tv, R.id.select_num_202_tv,
            R.id.select_num_203_tv, R.id.select_num_204_tv, R.id.select_num_205_tv,
            R.id.select_num_206_tv, R.id.select_num_207_tv, R.id.select_num_208_tv,
            R.id.select_num_209_tv,
            R.id.select_num_300_tv, R.id.select_num_301_tv, R.id.select_num_302_tv,
            R.id.select_num_303_tv, R.id.select_num_304_tv, R.id.select_num_305_tv,
            R.id.select_num_306_tv, R.id.select_num_307_tv, R.id.select_num_308_tv,
            R.id.select_num_309_tv,
            R.id.select_num_400_tv, R.id.select_num_401_tv, R.id.select_num_402_tv,
            R.id.select_num_403_tv, R.id.select_num_404_tv, R.id.select_num_405_tv,
            R.id.select_num_406_tv,
            R.id.select_num_delete_tv
    };

    public CustomCarNumberKeyboard(Context context) {
        this(context, null);
    }

    public CustomCarNumberKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCarNumberKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        editTexts = new EditText[8];
        View.inflate(context, R.layout.layout_license_plate_frame2, this);
        int textsLength = VIEW_IDS.length;
        for (int i = 0; i < textsLength; i++) {
            //textview放进数组中，方便修改操作
            editTexts[i] = (EditText) findViewById(VIEW_IDS[i]);
        }
//        editTexts[0].setBackgroundResource(R.drawable.car_number_bg_blue);//第一个输入框默认设置点中效果
        editTexts[0].setFocusable(true);
        editTexts[0].setFocusableInTouchMode(true);
        editTexts[0].requestFocus();
        hideKeyboard(editTexts[0]);
    }

    private @NonNull
    InputListener inputListener;

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    /**
     * 输入完成监听回调接口
     */
    public interface InputListener {

        /**
         * @param content 当输入完成时的全部内容
         */
        void inputComplete(String content);

        /**
         * 删除操作
         */
//        void deleteContent();

    }

    /**
     * 每次使用时 必须要设置viewCount的值
     * 7是燃油车 8是新能源车
     * @param viewCount
     */
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
        editTexts = new EditText[8];
        for (int i = 0; i < 8; i++) {
            //textview放进数组中，方便修改操作
            editTexts[i] = (EditText) findViewById(VIEW_IDS[i]);
        }
        if (viewCount==7){
            editTexts[7].setVisibility(View.GONE);
        }else {
            editTexts[7].setVisibility(View.VISIBLE);
        }
        lastIndex = viewCount - 1;
        setlistener(viewCount);
    }

    int lastIndex;

    /**
     * 设置edittext的内容
     */
    public void setViewContent(String content) {
        int length = content.length();
        char[] contents = content.toCharArray();
        setViewCount(length);
        for (int i = 0; i < length; i++) {
            editTexts[i].setText(String.valueOf(contents[i]));
        }
        editTexts[length - 1].setFocusable(true);
        editTexts[length - 1].setFocusableInTouchMode(true);
        editTexts[length - 1].requestFocus();
        String lastEdittextStr = editTexts[length - 1].getText().toString();
        editTexts[length - 1].setSelection(lastEdittextStr.length());
    }

    public String getInputContent() {
        return inputContent;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setlistener(int counts) {
        for (int i = 0; i < counts; i++) {
            int finalI1 = i;
            editTextHideKeyboart(mActivity, editTexts[finalI1]);
            if (finalI1 == 0) {
                //第一位
                editTexts[i].setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            hideKeyboard(editTexts[0]);
                            mProvinceView.setVisibility(VISIBLE);
                            mNumView.setVisibility(GONE);
                            focusPosition = 0;
                            String content = editTexts[0].getText().toString();
                            editTexts[0].setSelection(content.length());
                        }
                    }
                });
                editTexts[finalI1].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String content = s.toString();
                        hideKeyboard(editTexts[0]);
//                        editTexts[0].setText(content);
                        if (content.length() > 0) {
                            editTexts[1].setFocusable(true);
                            editTexts[1].setFocusableInTouchMode(true);
                            editTexts[1].requestFocus();
                        }
                    }
                });
            } else {
                editTexts[finalI1].setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            hideKeyboard(editTexts[finalI1]);
                            mProvinceView.setVisibility(GONE);
                            mNumView.setVisibility(VISIBLE);
                            focusPosition = finalI1;
                            String content = editTexts[finalI1].getText().toString();
                            editTexts[finalI1].setSelection(content.length());
                        }
                    }
                });
                editTexts[finalI1].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        hideKeyboard(editTexts[finalI1]);
                        String content = s.toString();
//                            editTexts[finalI].setText(content);
                        if (content.length() > 0) {
                            if (finalI1 < lastIndex) {
                                editTexts[finalI1 + 1].setFocusable(true);
                                editTexts[finalI1 + 1].setFocusableInTouchMode(true);
                                editTexts[finalI1 + 1].requestFocus();
                            }
                        } else {
//                                editTexts[finalI].setFocusable(true);
                        }
                    }
                });
            }
        }
    }

    /**
     * 设置框内字体颜色
     */
    public int onSetTextColor(int resId) {
        return resId;
    }

    public void setKeyboardContainerLayout(RelativeLayout layout) {
        mInflater = LayoutInflater.from(mActivity);
        mProvinceView = mInflater.inflate(R.layout.layout_keyboard_province, null);
        LayoutParams rlParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mProvinceView.setLayoutParams(rlParams);
        mNumView = mInflater.inflate(R.layout.layout_keyboard_num, null);
        mNumView.setLayoutParams(rlParams);
        int provinceLength = VIEW_PROVINCE_IDS.length;
        View view;
        for (int i = 0; i < provinceLength; i++) {
            view = mProvinceView.findViewById(VIEW_PROVINCE_IDS[i]);
            view.setOnClickListener(this);
        }
        int numLength = VIEW_NUM_IDS.length;
        for (int i = 0; i < numLength; i++) {
            view = mNumView.findViewById(VIEW_NUM_IDS[i]);
            view.setOnClickListener(this);
        }
        layout.addView(mProvinceView);
        layout.addView(mNumView);
        select_end_province = mProvinceView.findViewById(R.id.select_end_province);
        select_end_num = mNumView.findViewById(R.id.select_end_num);
        mNumView.setVisibility(GONE);
        select_end_province.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProvinceView.setVisibility(View.GONE);
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < viewCount; i++) {
                    stringBuffer.append(editTexts[i].getText().toString().trim());
                }
                inputContent = stringBuffer.toString();
                inputListener.inputComplete(inputContent);
            }
        });
        select_end_num.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumView.setVisibility(View.GONE);
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < viewCount; i++) {
                    stringBuffer.append(editTexts[i].getText().toString().trim());
                }
                inputContent = stringBuffer.toString();
                inputListener.inputComplete(inputContent);
            }
        });
    }


    /**
     * 键盘的点击事件
     */
    @Override
    public void onClick(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setSelected(true);
            String text = tv.getText().toString();
            if (view.getId() == R.id.select_province_delete_tv ||
                    view.getId() == R.id.select_num_delete_tv) {
                String content = editTexts[focusPosition].getText().toString();
                if (content.length() > 0) {
                    editTexts[focusPosition].setText(text);
                } else {
                    if (focusPosition > 0) {
                        editTexts[focusPosition - 1].setFocusable(true);
                        editTexts[focusPosition - 1].setFocusableInTouchMode(true);
                        editTexts[focusPosition - 1].requestFocus();
                    } else {
                        //这里应该不用再次进行下面的操作，因为本来就是已经获取焦点了，不用再次获取焦点
//                        editTexts[focusPosition].setFocusable(true);
                    }
                }
            } else {
                editTexts[focusPosition].setText(text);
            }
        }
    }
}
