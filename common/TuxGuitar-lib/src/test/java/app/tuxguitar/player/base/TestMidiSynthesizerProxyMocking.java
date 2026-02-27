package app.tuxguitar.player.base;

// part5：使用 Mockito 验证 MidiSynthesizerProxy 的“委托转发行为”。
// 目标：确认 proxy 是否把调用正确转发给内部 MidiSynthesizer，
// 以及在未配置 synth 时是否按默认逻辑返回。
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class TestMidiSynthesizerProxyMocking {

	@Test
	public void testDelegatesCallsWhenSynthesizerIsSet() throws MidiPlayerException {
		// 创建被测对象（代理类）。
		MidiSynthesizerProxy proxy = new MidiSynthesizerProxy();
		// 创建两个 mock：
		// 1) synth：被代理转发的目标
		// 2) channel：openChannel 的返回对象
		MidiSynthesizer synth = mock(MidiSynthesizer.class);
		MidiChannel channel = mock(MidiChannel.class);
		// 把 mock synth 注入 proxy。
		proxy.setMidiSynthesizer(synth);

		// 预设 mock 行为（when...thenReturn）：
		// 指定当调用这些方法时应该返回什么。
		when(synth.openChannel(3)).thenReturn(channel);
		when(synth.isChannelOpen(channel)).thenReturn(true);
		when(synth.isBusy()).thenReturn(true);

		// 调用 proxy 方法并断言返回值：
		// 如果 proxy 委托正确，这里应得到预设返回值。
		assertEquals(channel, proxy.openChannel(3));
		proxy.closeChannel(channel);
		assertTrue(proxy.isChannelOpen(channel));
		assertTrue(proxy.isBusy());

		// 关键行为验证（verify）：
		// 确认 proxy 确实调用了 synth 的对应方法与参数。
		verify(synth).openChannel(3);
		verify(synth).closeChannel(channel);
		verify(synth).isChannelOpen(channel);
		verify(synth).isBusy();
	}

	@Test
	public void testReturnsDefaultsWhenSynthesizerIsNotSet() throws MidiPlayerException {
		// 仅创建 proxy，不注入 synth，模拟“未配置依赖”场景。
		MidiSynthesizerProxy proxy = new MidiSynthesizerProxy();
		// 这里创建 channel 只是用于传参，预期不会发生任何交互。
		MidiChannel channel = mock(MidiChannel.class);

		// 断言默认行为：
		// openChannel 返回 null；isChannelOpen/isBusy 返回 false；closeChannel 不抛错。
		assertNull(proxy.openChannel(1));
		proxy.closeChannel(channel);
		assertFalse(proxy.isChannelOpen(channel));
		assertFalse(proxy.isBusy());
		// 验证 channel 这个 mock 没有被调用，说明 proxy 在无 synth 时没有做多余动作。
		verifyNoInteractions(channel);
	}
}
