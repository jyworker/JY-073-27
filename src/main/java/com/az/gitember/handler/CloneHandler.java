package com.az.gitember.handler;

import com.az.gitember.data.RemoteRepoParameters;
import com.az.gitember.service.Context;
import com.az.gitember.ui.StatusBar;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.TransportException;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class CloneHandler extends AbstractAsyncHandler<Void> {

    private static final Logger log = Logger.getLogger(CloneHandler.class.getName());

    private final RemoteRepoParameters params;

    public CloneHandler(Component parent, StatusBar statusBar, RemoteRepoParameters params) {
        super(parent, statusBar);
        this.params = params;
    }

    @Override
    protected String getOperationName() {
        return "Clone";
    }

    @Override
    protected Void doInBackground() throws Exception {
        Context.getGitRepoService().cloneRepository(params, progressMonitor);
        Context.init(params.getDestinationFolder());
        return null;
    }

    @Override
    protected void onSuccess(Void result) {
        statusBar.setStatus("Clone completed successfully");
        String url = params.getUrl();
        if (url != null && (url.startsWith("https:") || url.startsWith("http:"))) {
            Context.getCurrentProject().ifPresent(project -> {
                project.setUserName(params.getUserName());
                project.setUserPwd(params.getUserPwd());
                project.setAccessToken(params.getAccessToken());
                Context.saveSettings();
            });
        }
    }

    @Override
    protected void onError(Exception e) {
        String localized = localizeCloneError(e);
        if (localized != null) {
            log.severe("Clone failed: " + e.getMessage());
            statusBar.clearProgress();
            statusBar.setStatus("克隆失败");
            JOptionPane.showMessageDialog(parent,
                    localized,
                    "克隆失败", JOptionPane.ERROR_MESSAGE);
            return;
        }
        super.onError(e);
    }

    private String localizeCloneError(Exception e) {
        Throwable root = e;
        while (root.getCause() != null) {
            root = root.getCause();
        }

        if (e instanceof JGitInternalException || root instanceof JGitInternalException) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("already exists") && msg.contains("not an empty directory")) {
                return "目标目录已存在且非空，请选择一个空目录或删除已有目录后重试。";
            }
            if (msg.contains("already exists")) {
                return "目标路径已存在，请选择其他目录。";
            }
            return "克隆时发生内部错误：" + msg;
        }

        if (e instanceof TransportException || root instanceof TransportException) {
            return localizeTransportError(e.getMessage());
        }

        if (e instanceof InvalidRemoteException || root instanceof InvalidRemoteException) {
            return "无效的远程仓库地址，请检查 URL 格式是否正确。";
        }

        if (e.getMessage() != null) {
            String msg = e.getMessage().toLowerCase();
            if (msg.contains("already exists") && msg.contains("not an empty directory")) {
                return "目标目录已存在且非空，请选择一个空目录或删除已有目录后重试。";
            }
            if (msg.contains("already exists")) {
                return "目标路径已存在，请选择其他目录。";
            }
        }

        return null;
    }

    private String localizeTransportError(String msg) {
        if (msg == null) {
            return "网络传输错误，无法完成克隆操作。";
        }
        String lower = msg.toLowerCase();
        if (lower.contains("not authorized") || lower.contains("authentication")
                || lower.contains("401") || lower.contains("403")
                || lower.contains("auth fail")) {
            return "认证失败，请检查用户名、密码或访问令牌是否正确。";
        }
        if (lower.contains("connection refused") || lower.contains("connect timed out")
                || lower.contains("connection timed out") || lower.contains("connect failed")) {
            return "无法连接远程仓库，请检查网络连接或仓库地址是否正确。";
        }
        if (lower.contains("not found") || lower.contains("does not exist")
                || lower.contains("repository not found")) {
            return "远程仓库不存在，请检查仓库地址是否正确。";
        }
        if (lower.contains("hostkey") || lower.contains("host key")) {
            return "SSH 主机密钥验证失败，请确认远程主机的密钥是否可信。";
        }
        if (lower.contains("unknown host")) {
            return "无法解析远程主机名，请检查仓库地址和网络连接。";
        }
        return "网络传输错误：" + msg;
    }
}
