package com.lee.timer.three;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TaskContext {

	private static final int CTL_NONE = 0;  //ctl none
	private static final int CTL_SETUP = 1;  //task context setup

	private AtomicInteger ctl = new AtomicInteger(CTL_NONE);

	private ScheduledThreadPoolExecutor executor;

	private boolean ctlPlus(int ctl) {
		return ctlNil(this.ctl.getAndAccumulate(ctl, (now, c) -> now | c), ctl);
	}

	private boolean ctlNil(int ctl) {
		return ctlNil(this.ctl.get(), ctl);
	}

	private boolean ctlNil(int now, int ctl) {
		return (now & ctl) == 0;
	}

	private void defaultSetup() {
		this.setup(Math.min(1, Runtime.getRuntime().availableProcessors() / 2));
	}

	public void setup(int nthreads) {
		if (ctlPlus(CTL_SETUP)) {
			executor = new ScheduledThreadPoolExecutor(nthreads);
		} else if (executor.getCorePoolSize() < nthreads) {
			executor.setCorePoolSize(nthreads);
		}
	}

	public void schedule(Task task) {
		if (ctlNil(CTL_SETUP)) {
			this.defaultSetup();
		}
		if (task.period == -1) {//once task
			executor.schedule(task, task.delay, task.unit);
		} else {
			executor.scheduleWithFixedDelay(task, task.delay, task.period, task.unit);
		}
	}

	@PreDestroy
	public void shutdown() {
		if (executor != null)
			executor.shutdownNow();
	}

}
