package com.wan.qd.kysdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wan.qc.sdk.core.dialog.QCLoadingDialog;
import com.wan.qc.sdk.open.LoginResult;
import com.wan.qc.sdk.open.PayInfo;
import com.wan.qc.sdk.open.QCPayCallback;
import com.wan.qc.sdk.open.QCSDKManager;
import com.wan.qc.sdk.open.QCSdkCallback;

/**
 * @]author xk
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private Activity activity;
    private Button btnPay;
    private Button btnLogin;
    private Button btnExit;
    private Button btnLogout;
    private EditText edTMoney;
    private TextView tvStatus;
    private QCLoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        loadingDialog = new QCLoadingDialog(activity);
        initView();
        initData();
    }

    private void initView() {
        btnPay = findViewById(R.id.btnPay);
        edTMoney = findViewById(R.id.money);
        btnExit = findViewById(R.id.btnExit);
        btnLogin = findViewById(R.id.login);
        btnLogout = findViewById(R.id.btnLogout);
        tvStatus = findViewById(R.id.tvStatue);
        btnPay.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        setState(QCSDKManager.getInstance().isLogin());
    }

    private void setState(boolean state) {
        if (state) {
            btnLogin.setVisibility(View.INVISIBLE);
            btnPay.setVisibility(View.VISIBLE);
            edTMoney.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnPay.setVisibility(View.GONE);
            edTMoney.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化 设置回调
     */
    private void initData() {
        QCSDKManager.getInstance().setParams("1001", "b8c37e33defde51cf91e1e03e51657da", true);
        QCSDKManager.getInstance().init(MainActivity.this, new QCSdkCallback() {

            @Override
            public void initSuccess() {
                toast("初始化成功");
            }

            @Override
            public void initFailure(int code, String msg) {
                toast("初始化失败：" + code + "  " + msg);
            }

            @Override
            public void loginSuccess(LoginResult result) {
                loadingDialog.cancel();
                toast("登陆成功：" + result.toString());
                String userId = result.getUserId();
                String appId = result.getAppId();
                String ticket = result.toString();
                tvStatus.setText(result.toString());
                setState(QCSDKManager.getInstance().isLogin());
            }

            @Override
            public void loginFailure(int code, String msg) {
                loadingDialog.cancel();
                toast("登陆失败：" + code + "  " + msg);
            }

            @Override
            public void logoutSuccess() {
                toast("登出成功");
                setState(QCSDKManager.getInstance().isLogin());
            }

            @Override
            public void logoutFailure(int code, String msg) {
                toast("登出失败：" + code + "  " + msg);
            }
        });
    }

    /**
     * 登录
     */
    private void login() {
        loadingDialog.show();
        QCSDKManager.getInstance().login(activity);
    }

    /**
     * 登出
     */
    private void logout() {
        QCSDKManager.getInstance().logout();
    }

    /**
     * 支付
     */
    private void pay() {
        int money = 1;
        String price = ((TextView) findViewById(R.id.money)).getText()
                .toString().trim();

        if (!TextUtils.isEmpty(price)) {
            if (price.length() < 7) {
                money = Integer.parseInt(price);
            } else {
                money = Integer.parseInt(price.substring(0, 6));
            }
        }
        PayInfo order = new PayInfo();
        order.setGoodsName("太白的蒲扇");                           // 物品名称
        order.setGoodsDesc("太白的蒲扇，五金一件");                  // 物品描述
        order.setAmount(money);                                   // 商品价格（单位分）
        order.setOrderId(SystemClock.elapsedRealtime() + "");     // 订单号，请游戏服务器生成并记录，保持唯一性
        order.setNotifyUrl("http://xxx.x.xx.x.notifyUrl");               // 回调地址，支付成功后，订单号，金额及透传参数会通过此地址回调到游戏服务器
        order.setRoleId("r001");                                  // 角色id
        order.setRoleName("农夫三拳");                             // 角色名称
        order.setServerId("s001");                                // 服务器id
        order.setRoleName("无限长空");                              // 服务器名称

        order.addExtraInfo("key1", "透传参数1");                    //透传参数，如有需求，请自定义
        order.addExtraInfo("key2", "透传参数2");                    //透传参数，如有需求，请自定义
        order.addExtraInfo("key3", "透传参数3");                    //透传参数，如有需求，请自定义

        QCSDKManager.getInstance().pay(activity, order, new QCPayCallback() {

            @Override
            public void success(int code, String order) {
                // 支付成功
            }

            @Override
            public void failure(int code, String order, String msg) {
                // 支付失败
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPay:
                pay();
                break;
            case R.id.login:
                login();
                break;
            case R.id.btnLogout:
                logout();
                break;
            case R.id.btnExit:
                System.exit(0);
                break;
        }
    }


    public void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
