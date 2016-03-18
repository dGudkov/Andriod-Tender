package com.nicecode.android.tender.fragments;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.nicecode.android.tender.ApplicationWrapper;
import com.nicecode.android.tender.R;
import com.nicecode.android.tender.dto.Response;
import com.nicecode.android.tender.dto.User;
import com.nicecode.android.tender.library.async.AsyncTask;
import com.nicecode.android.tender.library.utils.LayoutUtils;
import com.nicecode.android.tender.library.utils.WeakReference;
import com.nicecode.android.tender.library.widget.EmptyTextWatcher;
import com.nicecode.android.tender.library.widget.TextWatcher;
import com.nicecode.android.tender.utils.Utils;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public class Fragment_User_Login extends BaseFragment {

    private View mFadeLayout;
    private View mUserNameLayout, mPasswordLayout;
    private View mUserNameInnerLayout, mPasswordInnerLayout;
    private EditText mUserNameText, mPasswordText;
    private ImageView mUserNameImage, mPasswordImage;
    private EmptyTextWatcher mUserNameTextWatcher, mPasswordWatcher;

    private LoginUserTask mLoginUserTask;
    private TextView mMoreText;
    private View mForgotTextLayout;
    private TextView mSugnUptext, mGladSeeYou;

    public Fragment_User_Login() {
        super();
        this.mLayoutId = R.layout.fragment_user_login_layout;
    }

    @SuppressWarnings("all")
    @Override
    protected void initView(View rootView) {

        this.mToolBar.findViewById(R.id.activity_userlogin_status_bar_back_layout).setVisibility(View.VISIBLE);
        this.mToolBar.findViewById(R.id.activity_userlogin_status_bar_back_layout).setOnClickListener(this);
        this.mToolBar.findViewById(R.id.activity_userlogin_status_bar_back).setVisibility(View.VISIBLE);
        this.mToolBar.findViewById(R.id.activity_userlogin_status_bar_back).setOnClickListener(this);

        this.mToolBar.findViewById(R.id.activity_userlogin_status_bar_more_layout).setVisibility(View.VISIBLE);
        this.mToolBar.findViewById(R.id.activity_userlogin_status_bar_more_layout).setOnClickListener(this);
        this.mMoreText = (TextView) this.mToolBar.findViewById(R.id.activity_userlogin_status_bar_more);
        this.mMoreText.setVisibility(View.VISIBLE);
        this.mMoreText.setOnClickListener(this);

        int width = this.mApplication.getMetrics().widthPixels;

        this.mFadeLayout = rootView.findViewById(R.id.fragment_login_fade_layout);
        this.mFadeLayout.setVisibility(View.INVISIBLE);

        this.mUserNameLayout = rootView.findViewById(R.id.fragment_login_user_layout);
        this.mUserNameInnerLayout = this.mUserNameLayout.findViewById(R.id.widget_clientinput_layout);
        this.mUserNameText = (EditText) this.mUserNameLayout.findViewById(R.id.widget_clientinput_text);
        this.mUserNameImage = (ImageView) this.mUserNameLayout.findViewById(R.id.widget_clientinput_image);

        this.mSugnUptext = (TextView) rootView.findViewById(R.id.fragment_login_sign_up_text);
        this.mGladSeeYou = (TextView) rootView.findViewById(R.id.fragment_login_glad_see_you_text);

        this.mPasswordLayout = rootView.findViewById(R.id.fragment_login_password_layout);
        this.mPasswordInnerLayout = this.mPasswordLayout.findViewById(R.id.widget_clientinput_layout);
        this.mPasswordText = (EditText) this.mPasswordLayout.findViewById(R.id.widget_clientinput_text);
        this.mPasswordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mPasswordImage = (ImageView) this.mPasswordLayout.findViewById(R.id.widget_clientinput_image);

        this.mUserNameTextWatcher = new EmptyTextWatcher(this.mUserNameText, new TextWatcher.EditTextWatcherChanged() {
            @Override
            public void onEditTextWatcherChanged() {
                Fragment_User_Login fragment = Fragment_User_Login.this;
                if (fragment.mUserNameTextWatcher.isValid()) {
                    fragment.mUserNameImage.setVisibility(View.GONE);
                } else {
                    fragment.mUserNameImage.setVisibility(View.VISIBLE);
                }
                fragment.validateContinue();
            }
        });

        this.mPasswordWatcher = new EmptyTextWatcher(this.mPasswordText, new TextWatcher.EditTextWatcherChanged() {
            @Override
            public void onEditTextWatcherChanged() {
                Fragment_User_Login fragment = Fragment_User_Login.this;
                if (fragment.mPasswordWatcher.isValid()) {
                    fragment.mPasswordImage.setVisibility(View.GONE);
                } else {
                    fragment.mPasswordImage.setVisibility(View.VISIBLE);
                }
                fragment.validateContinue();
            }
        });

        this.initLogoLayout();
        this.initButtonLayout(rootView);

        ViewTreeObserver vto = this.mRootView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    Fragment_User_Login.this.mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    Fragment_User_Login.this.mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                Fragment_User_Login fragment = Fragment_User_Login.this;

                fragment.initImageSize(fragment.mUserNameInnerLayout, fragment.mUserNameImage, R.drawable.user);
                fragment.initImageSize(fragment.mPasswordInnerLayout, fragment.mPasswordImage, R.drawable.password);
            }
        });
    }

    private void initLogoLayout() {
        this.mGladSeeYou.setTextSize(this.mApplication.FRAGMENT_USER_LOGIN_GLAD_SEE_YOU_TEXT_SIZE);
        this.mSugnUptext.setTextSize(this.mApplication.FRAGMENT_USER_LOGIN_SIGN_UP_TEXT_SIZE);
    }

    @SuppressWarnings("all")
    private void initButtonLayout(View rootView) {
        View layout = rootView.findViewById(R.id.fragment_login_data_layout);
        LinearLayout.LayoutParams lLp = (LinearLayout.LayoutParams) layout.getLayoutParams();
        lLp.leftMargin = this.mApplication.STATUS_BAR_LEFT_MARGIN;
        lLp.rightMargin = this.mApplication.STATUS_BAR_RIGHT_MARGIN;

        this.initInputLayout(
                this.mUserNameLayout,
                this.mUserNameText,
                R.string.str_fragment_user_login_user_name_text_hint
        );

        this.initInputLayout(
                this.mPasswordLayout,
                this.mPasswordText,
                R.string.str_fragment_user_login_usePassword_text_hint
        );

        this.mForgotTextLayout = rootView.findViewById(R.id.fragment_login_forgot_text_layout);
        lLp = (LinearLayout.LayoutParams) this.mForgotTextLayout.getLayoutParams();
        lLp.topMargin = lLp.bottomMargin = this.mApplication.STATUS_BAR_LEFT_MARGIN;

        TextView textView = (TextView) rootView.findViewById(R.id.fragment_login_forgot_text);
        textView.setTextSize(this.mApplication.FRAGMENT_USER_LOGIN_FORGOT_TEXT_SIZE);
        this.mForgotTextLayout.setOnClickListener(this);
    }

    @SuppressWarnings("all")
    private void initInputLayout(View layout, EditText text, int hintId) {
        layout.setPadding(
                0,
                this.mApplication.STATUS_BAR_LEFT_MARGIN / 2,
                0,
                this.mApplication.STATUS_BAR_RIGHT_MARGIN / 2
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layout.setPaddingRelative(
                    0,
                    this.mApplication.STATUS_BAR_LEFT_MARGIN / 2,
                    0,
                    this.mApplication.STATUS_BAR_RIGHT_MARGIN / 2
            );
        }

        text.setHint(hintId);
        text.setTextSize(this.mApplication.FRAGMENT_USER_LOGIN_EDIT_TEXT_SIZE);
    }

    private void initImageSize(View layout, ImageView image, int imageId) {
        int height = layout.getHeight();

        RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) image.getLayoutParams();
        rLp.width = rLp.height = (int) (height * this.mApplication.FRAGMENT_USER_LOGIN_IMAGES_SIZE_RATIO);
        rLp.leftMargin = this.mApplication.STATUS_BAR_LEFT_MARGIN;

        if (imageId > 0) {
            LayoutUtils.loadImage(image, imageId, imageId);
        }
    }

    private void updateDetailLayoutParams(
            boolean clean,
            EditText editText,
            TextWatcher textWatcher,
            final View text_layout,
            ImageView image,
            View.OnClickListener listener) {

        if (!clean) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    int color;
                    if (hasFocus) {
                        color = ContextCompat.getColor(
                                Fragment_User_Login.this.getContext(),
                                R.color.clr_widget_clientinput_layout_text_border_selected);
                    } else {
                        color = ContextCompat.getColor(
                                Fragment_User_Login.this.getContext(),
                                R.color.clr_widget_clientinput_layout_text_border);
                    }
                    text_layout.setBackgroundColor(color);
                    Fragment_User_Login.this.setImeVisibility(v, hasFocus);
                }
            });
        } else {
            editText.setOnFocusChangeListener(null);
        }

        if (textWatcher != null) {
            if (clean) {
                editText.removeTextChangedListener(textWatcher);
            } else {
                editText.addTextChangedListener(textWatcher);
            }
        }
        image.setOnClickListener(listener);
    }

    @Override
    public void onResume() {

        super.onResume();

        updateDetailLayoutParams(
                false,
                this.mUserNameText,
                this.mUserNameTextWatcher,
                this.mUserNameInnerLayout,
                this.mUserNameImage, this
        );

        updateDetailLayoutParams(
                false,
                this.mPasswordText,
                this.mPasswordWatcher,
                this.mPasswordInnerLayout,
                this.mPasswordImage, this
        );

        this.mUserNameText.setText("79889501003@yandex.ru");
        this.mPasswordText.setText("pass");
    }

    @Override
    public void onPause() {
        if (this.mLoginUserTask != null) {
            this.mLoginUserTask.interrupt(true);
        }

        updateDetailLayoutParams(
                true,
                this.mUserNameText,
                this.mUserNameTextWatcher,
                this.mUserNameInnerLayout,
                this.mUserNameImage, null
        );

        updateDetailLayoutParams(
                true,
                this.mPasswordText,
                this.mPasswordWatcher,
                this.mPasswordInnerLayout,
                this.mPasswordImage, this
        );

        super.onPause();
    }

    @Override
    public void onDestroy() {
        this.mUserNameLayout = null;
        this.mPasswordLayout = null;
        this.mUserNameText = null;
        this.mPasswordText = null;
        this.mUserNameImage = null;
        this.mPasswordImage = null;
        this.mUserNameInnerLayout = null;
        this.mPasswordInnerLayout = null;
        this.mFadeLayout = null;
        this.mLoginUserTask = null;
        this.mForgotTextLayout = null;
        this.mSugnUptext = null;
        this.mGladSeeYou = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_userlogin_status_bar_more_layout:
            case R.id.activity_userlogin_status_bar_more:
                if (this.mLoginUserTask != null) {
                    this.mLoginUserTask.interrupt(true);
                }
                this.mLoginUserTask = new LoginUserTask(
                        this,
                        this.mUserNameText.getText().toString(),
                        this.mPasswordText.getText().toString()
                );
                this.mLoginUserTask.execute();
                break;
            case R.id.fragment_login_forgot_text_layout:
                Snackbar.make(v, "This functionality not implemented now.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.activity_userlogin_status_bar_back_layout:
            case R.id.activity_userlogin_status_bar_back:
                if (!this.getActivity().isFinishing()) {
                    this.getActivity().finish();
                }
                break;
            default:
                super.onClick(v);
        }
    }


    private void validateContinue() {
        boolean valid =
                (this.mUserNameTextWatcher.isValid()) &&
                        (this.mPasswordWatcher.isValid());
        this.mMoreText.setEnabled(valid);
    }

    @Override
    protected void onToolBarClick(View view) {
        switch (view.getId()) {
            case R.id.activity_userlogin_status_bar_back_layout:
            case R.id.activity_userlogin_status_bar_back:
                if (!this.getActivity().isFinishing()) {
                    this.getActivity().finish();
                }
                break;
            default:
                super.onToolBarClick(view);
        }
    }

    @Override
    protected boolean isToolBarComponent(View v) {
        return ((v.getId() == R.id.activity_userlogin_status_bar_back_layout) ||
                (v.getId() == R.id.activity_userlogin_status_bar_back) ||
                (v.getId() == R.id.activity_userlogin_status_bar_more_layout) ||
                (v.getId() == R.id.activity_userlogin_status_bar_more));
    }

    private class LoginUserTask extends AsyncTask<Void, Void, Response> {

        private WeakReference<Fragment_User_Login> mFragment;
        private String mUserName, mUserPass;

        public LoginUserTask(Fragment_User_Login fragment, String userName, String userPass) {
            this.mFragment = new WeakReference<>(fragment);
            this.mUserName = userName;
            this.mUserPass = userPass;
        }

        @Override
        protected void onPreExecute() {
            Fragment_User_Login fragment = this.mFragment.get();
            if (fragment != null) {
                View view = fragment.mFadeLayout;
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
                view = fragment.mMoreText;
                if (view != null) {
                    view.setEnabled(false);
                }
                view = fragment.mUserNameText;
                if (view != null) {
                    view.setEnabled(false);
                }
                view = fragment.mPasswordText;
                if (view != null) {
                    view.setEnabled(false);
                }
                view = fragment.mForgotTextLayout;
                if (view != null) {
                    view.setEnabled(false);
                }
            }
        }

        @Override
        protected Response doInBackground(Void... params) {
            Response response = null;
            Fragment_User_Login fragment = this.mFragment.get();

            if (fragment != null) {
                try {
                    this.stopIfCancelled();
                    response = Utils.LoginUser(this.mUserName, this.mUserPass);
                    response.validate();
                } catch (Exception e) {
                    Log.e("Fragment_Registration", "Error: " + e.getMessage());
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            Fragment_User_Login fragment = this.mFragment.get();
            try {
                if (fragment != null) {
                    ApplicationWrapper application = fragment.mApplication;
                    if (response != null) {
                        if (response.getErrorMessage() == null) {
                            if (response.getData() instanceof LinkedTreeMap) {
                                LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.getData();
                                try {
                                    if (application != null) {
                                        application.getPreferences().setUser(new User((LinkedTreeMap<String, Object>) map.get("user")));
                                        application.getPreferences().setToken((String) map.get("token"));
//                            Intent intent= new Intent(fragment.getActivity(), Activity_GetPhoto.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            if (!fragment.getActivity().isFinishing()) {
//                                fragment.getActivity().finish();
//                            }
                                    }
                                } catch (ClassCastException e) {
                                    Snackbar.make(fragment.mRootView, "Unknonw ClassCast error.", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                            } else {
                                Snackbar.make(fragment.mRootView, "Unknonw data error.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }
                        } else {
                            Snackbar.make(fragment.mRootView, response.getErrorMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {
                        Snackbar.make(fragment.mRootView, "Unknown error", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            } finally {
                super.onPostExecute(response);
            }
        }

        @Override
        protected void finalizeTask() {
            if (this.mFragment != null) {
                Fragment_User_Login fragment = this.mFragment.get();
                if (fragment != null) {

                    View view = fragment.mUserNameText;
                    if (view != null) {
                        view.setEnabled(true);
                    }
                    view = fragment.mPasswordText;
                    if (view != null) {
                        view.setEnabled(true);
                    }
                    view = fragment.mForgotTextLayout;
                    if (view != null) {
                        view.setEnabled(true);
                    }
                    view = fragment.mMoreText;
                    if (view != null) {
                        view.setEnabled(true);
                    }
                    view = fragment.mFadeLayout;
                    if (view != null) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    fragment.mLoginUserTask = null;
                    this.mFragment = null;
                }
            }
        }
    }

}
