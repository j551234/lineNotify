package io.jenkins.plugins;

import java.io.IOException;
import java.net.URLEncoder;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import jenkins.tasks.SimpleBuildStep;

public class LineNotifyNotifier extends Notifier implements SimpleBuildStep {

	private String token;

	private String remark;

	@DataBoundConstructor
	public LineNotifyNotifier(String token) {
		this.token = token;
	}

	@DataBoundSetter
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getToken() {
		return token;
	}

	public String getRemark() {
		return remark;
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		LineNotify l = new LineNotify();
		StringBuffer message = new StringBuffer();
		message.append(LineNotify.wrap + LineNotify.wrap);

		message.append(
				URLEncoder.encode("專案名稱：" + ("".equals(run.getParent().getDisplayName()) ? run.getParent().getName()
						: run.getParent().getDisplayName()), "UTF-8"));

		message.append(LineNotify.wrap + LineNotify.wrap);

		message.append(URLEncoder.encode("狀態：" + (run.getResult() == null ? "" : run.getResult().toString()), "UTF-8"));

		message.append(LineNotify.wrap + LineNotify.wrap);

		message.append(URLEncoder.encode("建置歷程：" + run.getNumber(), "UTF-8"));

		message.append(LineNotify.wrap + LineNotify.wrap);

		long diff = System.currentTimeMillis() - run.getStartTimeInMillis();

		long minutes = diff / (1000 * 60);
		long seconds = (diff / 1000) - (minutes * 60);

		message.append(URLEncoder.encode("建置時間：" + minutes + "分" + seconds + "秒", "UTF-8"));

		message.append(LineNotify.wrap + LineNotify.wrap);

		message.append(URLEncoder.encode("備註：" + remark, "UTF-8"));

		l.sendMessage(token, message.toString(), true);
	}

	@Extension
	public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		@Override
		public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {

			return true;
		}

		@Override
		public String getDisplayName() {
			return "LINE Notify";
		}
	}

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

}