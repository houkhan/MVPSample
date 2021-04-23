package commonwidget;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;


/**
 * description:弹窗浮动加载进度条
 * Created by xsf
 * on 2016.07.17:22
 */
public class LoadingDialog {
    /**
     * 加载数据对话框
     */
    private QMUITipDialog qmuiTipDialog;

    private static LoadingDialog loadingDialog;

    private static Context mContext;

    public static LoadingDialog getLoadingDialog(Context context) {
        if (loadingDialog == null) {
            synchronized (LoadingDialog.class) {
                if (loadingDialog == null) {
                    mContext = context;
                    loadingDialog = new LoadingDialog();
                }
            }
        }
        return loadingDialog;
    }

    /**
     * 显示加载中对话框
     *
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public void showDialogForLoading(String msg, boolean cancelable) {
        qmuiTipDialog = new QMUITipDialog.Builder(mContext).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create(cancelable);
        qmuiTipDialog.show();
    }

    /**
     * 显示加载成功对话框
     *
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public void showDialogForLoadingSucess(String msg, boolean cancelable) {
        qmuiTipDialog = new QMUITipDialog.Builder(mContext).setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(msg)
                .create(cancelable);
        qmuiTipDialog.show();
    }

    /**
     * 显示加载失败对话框
     *
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public void showDialogForLoadingFail(String msg, boolean cancelable) {
        qmuiTipDialog = new QMUITipDialog.Builder(mContext).setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(msg)
                .create(cancelable);
        qmuiTipDialog.show();
    }


    /**
     * 关闭加载对话框
     */
    public void cancelDialogForLoading() {
        if (qmuiTipDialog != null) {
            qmuiTipDialog.dismiss();
        }
    }
}
