package com.bordozer.jphoto.admin.services.services;

import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskResult;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskToPerform;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class UpgradeMonitor {

    private UpgradeTaskToPerform currentTask;
    private UpgradeState upgradeState = UpgradeState.STOPPED;

    private List<UpgradeTaskToPerform> upgradeTasksToPerform;

    private int currentTaskProgress;
    private int currentTaskTotal;

    private final Map<String, List<String>> taskMessageMap = newLinkedHashMap();

    public UpgradeTaskToPerform getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(final UpgradeTaskToPerform currentTask) {
        this.currentTask = currentTask;
    }

    public UpgradeState getUpgradeState() {
        return upgradeState;
    }

    public void setUpgradeState(final UpgradeState upgradeState) {
        this.upgradeState = upgradeState;
    }

    public List<UpgradeTaskToPerform> getUpgradeTasksToPerform() {
        return upgradeTasksToPerform;
    }

    public void setUpgradeTasksToPerform(final List<UpgradeTaskToPerform> upgradeTasksToPerform) {
        this.upgradeTasksToPerform = upgradeTasksToPerform;
    }

    public int getCurrentTaskProgress() {
        return currentTaskProgress;
    }

    public void setCurrentTaskProgress(final int currentTaskProgress) {
        this.currentTaskProgress = currentTaskProgress;
    }

    public int getCurrentTaskTotal() {
        return currentTaskTotal;
    }

    public void setCurrentTaskTotal(final int currentTaskTotal) {
        this.currentTaskTotal = currentTaskTotal;
    }

    public void increment() {
        currentTaskProgress++;
    }

    public int getTotalUpgradeTasks() {
        return upgradeTasksToPerform.size();
    }

    public int getDoneUpgradeTasks() {
        return getTaskCount(UpgradeTaskResult.SUCCESSFUL);
    }

    public int getErrorUpgradeTasks() {
        return getTaskCount(UpgradeTaskResult.ERROR);
    }

    private int getTaskCount(final UpgradeTaskResult taskResult) {
        int haveDone = 0;

        for (final UpgradeTaskToPerform upgradeTaskToPerform : upgradeTasksToPerform) {
            if (upgradeTaskToPerform.getUpgradeTaskResult() == taskResult) {
                haveDone++;
            }
        }
        return haveDone;
    }

    public Map<String, List<String>> getTaskMessageMap() {
        return taskMessageMap;
    }

    public void startTaskMessage(final UpgradeTaskToPerform upgradeTaskToPerform, final String message) {
        final String upgradeTaskName = upgradeTaskToPerform.getUpgradeTaskName();

        assertMessageListExists(upgradeTaskName);

        final List<String> messages = taskMessageMap.get(upgradeTaskName);
        messages.add(String.format("<b>%s</b>", message));
    }

    public void endTaskMessage(final UpgradeTaskToPerform upgradeTaskToPerform, final String message) {
        final String upgradeTaskName = upgradeTaskToPerform.getUpgradeTaskName();

        assertMessageListExists(upgradeTaskName);

        final List<String> messages = taskMessageMap.get(upgradeTaskName);
        messages.add(String.format("<b>%s</b>", message));
    }

    public void addTaskMessage(final UpgradeTaskToPerform upgradeTaskToPerform, final String message) {
        final String upgradeTaskName = upgradeTaskToPerform.getUpgradeTaskName();

        assertMessageListExists(upgradeTaskName);

        final List<String> messages = taskMessageMap.get(upgradeTaskName);
        messages.add(message);
    }

    private void assertMessageListExists(final String upgradeTaskName) {
        if (taskMessageMap.get(upgradeTaskName) == null) {
            final List<String> messages = newArrayList();
            taskMessageMap.put(upgradeTaskName, messages);
        }
    }
}
