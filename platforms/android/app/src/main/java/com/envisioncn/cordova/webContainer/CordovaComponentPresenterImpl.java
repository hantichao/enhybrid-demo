package com.envisioncn.cordova.webContainer;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.envisioncn.mobile.hybrid.EnvConstants;
import com.envisioncn.mobile.hybrid.router.Router;
import com.envision.demo.R;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.envisioncn.mobile.hybrid.EnvConstants.ROUTER_KEY_LEFT_ICON;
import static com.envisioncn.mobile.hybrid.EnvConstants.ROUTER_KEY_RIGHT_ICON;
import static com.envisioncn.mobile.hybrid.EnvConstants.ROUTER_KEY_SHOW_NAVI;
import static com.envisioncn.mobile.hybrid.EnvConstants.ROUTER_KEY_TITLE;
import static com.envisioncn.mobile.hybrid.EnvConstants.ROUTER_KEY_URL;

/**
 * CordovaWebView 逻辑处理
 */
public class CordovaComponentPresenterImpl implements CordovaComponentPresenter {
    private static final String TAG = CordovaComponentPresenterImpl.class.getSimpleName();

    private boolean isInitialAppear = true;

    private CordovaComponentView mView = null;

    public CordovaComponentPresenterImpl(CordovaComponentView v) {
        this.mView = v;
    }

    protected String launchUrl = null;
    protected Bundle extraBundle = null;
    private String pageName = null;

    /**
     * 初始化行为
     */
    @Override
    public void onCreated(Bundle bundle) {
        extraBundle = bundle;
        // 设置ActionBar, 只做一次，在onResume中不会再次设置，避免荣耀等手机上重复设置时出现ActionBar闪烁问题
        initApperance();
        // 解析url
        initLaunchUrl(bundle);
        // 加载页面
        loadWebContent();
    }

    @Override
    public void initApperance() {
        String titleKey = extraBundle.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_TITLE) != null ?
                extraBundle.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_TITLE).toString() : "";
        int resId = this.mView.getContext().getResources().getIdentifier(titleKey, "string", this.mView.getContext().getPackageName());
        String title = titleKey;
        if (resId != 0) {
            title = this.mView.getContext().getResources().getString(resId);
        }
        String rightIcon = extraBundle.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_RIGHT_ICON) != null ?
                extraBundle.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_RIGHT_ICON).toString() : "";
        String leftIcon = extraBundle.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_LEFT_ICON) != null ?
                extraBundle.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_LEFT_ICON).toString() : "";
        boolean showNavi = (Boolean) extraBundle.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_SHOW_NAVI);
        Object toolbarObj = mView.getToolBar();
        if (null != toolbarObj && toolbarObj instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) toolbarObj;
            //是否显示导航栏
            if (!showNavi) {
                toolbar.setVisibility(View.GONE);
                return;
            }
            toolbar.setVisibility(View.VISIBLE);
            TextView titleTV = (TextView) toolbar.findViewById(R.id.toolbar_title);
            titleTV.setText(title);

            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.postEventToJs(EnvWebContainerConstants.EVENT_TYPE_NAVI_TOP);
                }
            });

            View leftIconL = toolbar.findViewById(R.id.left_icon_corner);
            EnvIconView leftBtnIV = (EnvIconView) toolbar.findViewById(R.id.left_icon_iv);
            // 是否显示返回按钮
            if (leftIconL != null) {
                leftIconL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView.postEventToJs(EnvWebContainerConstants.EVENT_TYPE_NAVI_LEFT);
                    }
                });
            }
            if (leftIcon != null && leftIcon.length() > 0) {
                if (leftIconL != null) {
                    leftIconL.setVisibility(View.VISIBLE);
                }
                if (null != leftBtnIV) {
                    // 右边按钮显示IconFont
                    if (leftIcon.length() == 1 && leftIcon.charAt(0) > 4096) {
                        leftBtnIV.setText(leftIcon);
                    } else {
                        //右边按钮显示文字
                        leftBtnIV.setText(getStringFromRes(leftIcon));
                    }
                    // 默认点击事件传给WebView
                }
            } else {
                if (leftIconL != null) {
                    leftIconL.setVisibility(View.INVISIBLE);
                }
            }

            View rightIconR = toolbar.findViewById(R.id.right_icon_corner);
            EnvIconView rightBtnIV = (EnvIconView) toolbar.findViewById(R.id.right_icon_iv);
            // 是否显示右边按钮
            if (rightIconR != null) {
                // 默认点击事件传给WebView
                rightIconR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView.postEventToJs(EnvWebContainerConstants.EVENT_TYPE_NAVI_RIGHT);
                    }
                });
            }
            if (rightIcon != null && rightIcon.length() > 0) {
                if (rightIconR != null) {
                    rightIconR.setVisibility(View.VISIBLE);
                }
                if (rightBtnIV != null) {
                    // 右边按钮显示IconFont
                    if (rightIcon.length() == 1 && rightIcon.charAt(0) > 4096) {
                        rightBtnIV.setText(rightIcon);
                    } else {
                        //右边按钮显示文字
                        rightBtnIV.setText(getStringFromRes(rightIcon));
                    }
                }
            } else {
                if (rightIconR != null) {
                    rightIconR.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public String getPageName() {
        if (pageName == null) {
            Log.e(TAG, "@@@ should invoke onCreated before using pageName");
            pageName = "unknown";
        }
        return pageName;
    }

    @Override
    public void loadWebContent() {
        mView.loadWebContent(launchUrl);
    }

    @Override
    public void onPageStarted() {
        Log.d(TAG, "@@@ onPageStarted " + launchUrl);
    }

    @Override
    public void onReceivedError() {
        Log.d(TAG, "@@@ onReceivedError " + launchUrl);
    }

    @Override
    public void onPageFinished() {
        Log.d(TAG, "@@@ onPageFinished " + launchUrl);
    }

    /**
     * 解析当前的url
     *
     * @param extra bundle数据
     */
    private void initLaunchUrl(Bundle extra) {
        String url = extra.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_URL) != null ?
                extra.get(EnvConstants.ROUTER_KEY_PERFIX + ROUTER_KEY_URL).toString() : "";
        pageName = url;
        launchUrl = concatUrl(url, extra);
    }

    /**
     * 根据url通配符和参数构建url
     *
     * @param urlPatten
     * @param argBundle
     * @return
     */
    private String concatUrl(String urlPatten, Bundle argBundle) {
        String finalUrl = null;
        String rootUrl = Router.sharedRouter().getRootUrl();
        if (rootUrl == null) {
            rootUrl = "";
        }
        String remoteServer = argBundle.getString(EnvConstants.ROUTER_KEY_PERFIX + EnvConstants.ROUTER_KEY_REMOTE_SERVER, "");
        String url = argBundle.getString(EnvConstants.ROUTER_KEY_PERFIX + EnvConstants.ROUTER_KEY_OPEN_URL, "");

        String regEx = "^http(s)?://.*";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        Matcher rootUrlMatcher = pattern.matcher(rootUrl);
        boolean rootUrlRs = rootUrlMatcher.matches();

        Matcher remoteServerMatcher = pattern.matcher(remoteServer);
        boolean remoteServerRs = remoteServerMatcher.matches();

        // 配置了rootUrl，且rootUrl指向服务器，rootUrl具有最高优先级
        // 当前设置rootUrl的方法已经变为私有，所以这个判断分支不会进入
        if (rootUrlRs) {
            finalUrl = rootUrl + url;
        } else if (remoteServerRs) {
            // 配置了remoteServer，且remoteServer指向服务器，remoteServer具有高于本地路径的优先级
            finalUrl = remoteServer + url;
        } else {
            // 没有配置rootUrl和配置了remoteServer，先尝试从本地加载资源，如果本地资源不存在则展示错误页
            String tempPath = url.substring(0, url.indexOf("/", 1)) + File.separator +
                    EnvConstants.KEY_BUNDLES_DIR + url.substring(url.indexOf("/", 1));
            String tempUrl = rootUrl + tempPath;
            String tempFilePath = tempUrl.replace("file://", "").replaceAll("#.*", ""); // 去除path中的hash部分

            File file = new File(tempFilePath);
            if (!file.exists()) {
                //  使用config中配置的ErrorUrl，注意不能设置为nil或"about:blank"，否则cordova内部会直接finish页面
            }
            if (finalUrl == null) {
                finalUrl = tempUrl;
            }
        }
        return finalUrl;
    }


    private String getStringFromRes(String key) {
        return mView.getStringFromRes(key);
    }

    /**
     * 点击导航栏上的返回按钮,返回上一个页面
     */
    protected void backToPrevious() {
        mView.backToPrevious();
    }
}

