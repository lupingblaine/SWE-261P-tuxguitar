package app.tuxguitar.thread;

// part5：TGThreadManager 的 Stubbing 测试（与 3.4 的目标类不同）。
// 目的：验证 TGThreadManager 会把 start 调用转发给“注入的 handler”，
// 而不是依赖真实线程处理实现。
// 使用桩（stub）的目的，是把测试变成“可控、可验证、只测当前逻辑”。

// 在你这个 TGThreadManager 场景里，具体目的有 3 个：

// 替代真实线程行为
// 不启动真实线程，避免并发和时序干扰，让测试稳定。

// 聚焦验证转发逻辑
// 你要测的是 TGThreadManager 是否把 start(...) 调给 handler，不是测线程执行本身。

// 可观察调用细节
// 通过桩记录调用次数和优先级，能精确断言：

// 被调用了几次
// NORMAL/HIGH 各几次
// 一句话：
// 桩让你“只测管理器是否正确转发”，而不被真实线程系统拖复杂。
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import app.tuxguitar.util.TGContext;

public class TestTGThreadManagerStubbing {

	@Test
	public void testManagerUsesStubbedThreadHandlerInsteadOfRealOne() {
		// 创建上下文并获取 TGThreadManager 单例实例。
		TGContext context = new TGContext();
		TGThreadManager manager = TGThreadManager.getInstance(context);
		// 准备一个桩对象（Stub）：不真正开线程，只记录调用次数和优先级分布。
		StubThreadHandler handlerStub = new StubThreadHandler();
		// 注入桩对象，替换真实线程处理器。
		manager.setThreadHandler(handlerStub);

		// 触发默认优先级（NORMAL）start 调用。
		manager.start(new Runnable() {
			public void run() {
				// not needed in this stubbing test
			}
		});
		// 触发高优先级（HIGH）start 调用。
		manager.start(TGThreadPriority.HIGH, new Runnable() {
			public void run() {
				// not needed in this stubbing test
			}
		});

		// 断言：
		// 1) start 总调用次数为 2；
		// 2) NORMAL 路径调用 1 次；
		// 3) HIGH 路径调用 1 次。
		// 这些结果说明 TGThreadManager 已经在使用我们注入的 stub。
		assertEquals(2, handlerStub.startCalls);
		assertEquals(1, handlerStub.normalCalls);
		assertEquals(1, handlerStub.highCalls);
	}

	// 最小桩实现：
	// 仅记录 TGThreadManager 传来的调用，不执行真实线程逻辑，保证测试可控且稳定。
	private static class StubThreadHandler implements TGThreadHandler {
		// 记录 start 总调用次数。
		private int startCalls;
		// 记录 NORMAL 优先级调用次数。
		private int normalCalls;
		// 记录 HIGH 优先级调用次数。
		private int highCalls;

		public void start(TGThreadPriority priority, Runnable runnable) {
			// 被 TGThreadManager 调用时，记录调用信息。
			this.startCalls++;
			if (priority == TGThreadPriority.HIGH) {
				this.highCalls++;
			} else {
				this.normalCalls++;
			}
		}

		public void loop(TGThreadLoop loop) {
			// 本测试不验证 loop 行为，留空即可。
			// not needed in this stubbing test
		}

		public void yield() {
			// 本测试不验证 yield 行为，留空即可。
			// not needed in this stubbing test
		}

		public void dispose() {
			// 本测试不验证 dispose 行为，留空即可。
			// not needed in this stubbing test
		}

		public Object getThreadId() {
			// 返回当前线程 ID（满足接口要求；本测试不依赖该值）。
			return Thread.currentThread().getId();
		}
	}
}
