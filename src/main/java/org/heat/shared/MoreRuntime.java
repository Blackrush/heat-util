package org.heat.shared;

import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MoreRuntime {
	private static final Object tasks__GUARD = new Object();
	private static final Stack<Runnable> tasks = new Stack<>();
	private static final AtomicBoolean shuttingDown = new AtomicBoolean(false);

	public static void onShutdown(Runnable task) {
		if (shuttingDown.get()) return;

		synchronized (tasks__GUARD) {
			tasks.push(task);
		}
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				MoreRuntime.shuttingDown.set(true);

				synchronized (tasks__GUARD) {
					while (!tasks.isEmpty()) {
						tasks.pop().run();
					}
				}
			}
		});
	}

    public static Optional<String> sysprop(String key) {
        return Optional.ofNullable(System.getProperty(key));
    }
}
