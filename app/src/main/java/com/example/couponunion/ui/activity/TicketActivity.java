package com.example.couponunion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.couponunion.R;
import com.example.couponunion.base.BaseActivity;
import com.example.couponunion.model.domain.TicketResult;
import com.example.couponunion.presenter.ITicketPresenter;
import com.example.couponunion.utils.LogUtils;
import com.example.couponunion.utils.PresenterManager;
import com.example.couponunion.utils.ToastUtils;
import com.example.couponunion.utils.UrlUtils;
import com.example.couponunion.view.ITicketPageCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPageCallback {

    private ITicketPresenter mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();

    private boolean mHasTaobaoApp = false;

    private String mTicketCode = null;

    @BindView(R.id.ticket_back_btn)
    ImageView backBtn;

    @BindView(R.id.ticket_copy_or_open_btn)
    TextView receiveBtn;

    @BindView(R.id.ticket_cover)
    ImageView ticketCoverIv;

    @BindView(R.id.ticket_code)
    EditText ticketCodeEdt;


    @Override
    protected void initPresenter() {
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }

        //检查淘宝是否在设备上已经安装
        String taobaoPackageName = "com.taobao.taobao";
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(taobaoPackageName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobaoApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtils.d(this, "mHasTaobaoApp == > " + mHasTaobaoApp);
        }

        receiveBtn.setText(mHasTaobaoApp ? "打开淘宝领券" : "复制淘口令");

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        //领券按钮
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先将淘口令复制到剪贴板，再根据是否有淘宝来执行不同的逻辑
                //获取剪贴板管理器
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ticket_code", mTicketCode);
                //将淘口令复制到剪贴板
                cm.setPrimaryClip(clipData);
                //当设备有淘宝App时打开淘宝
                if (mHasTaobaoApp) {
                    Intent taobaoIntent = new Intent();
                    ComponentName componentName = new ComponentName("com.taobao.taobao" , "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                } else {
                    ToastUtils.showToast("复制成功！分享口令或打开淘宝");
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    /**
     * @param cover  商品图片链接
     * @param result 商品口令结果
     */
    @Override
    public void onTicketLoaded(String cover, TicketResult result) {

        if (ticketCoverIv != null && !TextUtils.isEmpty(cover)) {
            String targetCover = UrlUtils.getCoverPath(cover);
            Glide.with(this).load(targetCover).into(ticketCoverIv);
        }
        if (ticketCodeEdt != null && result.getData().getTbk_tpwd_create_response() != null) {
            mTicketCode = result.getData().getTbk_tpwd_create_response().getData().getModel();
            ticketCodeEdt.setText(mTicketCode);
        }


    }


    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        //释放资源
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }
}
