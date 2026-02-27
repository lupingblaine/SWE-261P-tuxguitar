package app.tuxguitar.player.base;

// part5：使用 Mockito 验证 MidiTransmitter 的“广播（fan-out）”和“接收器管理”行为。
// 目标：
// 1) 发送事件时，是否正确广播给所有已注册接收器；
// 2) 重复 ID 注册与删除后行为是否符合预期。
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

public class TestMidiTransmitterMocking {

	@Test
	public void testSendNoteOnFanOutToAllRegisteredReceivers() throws MidiPlayerException {
		// 创建被测对象：负责把 MIDI 消息转发给多个接收器。
		MidiTransmitter transmitter = new MidiTransmitter();
		// 创建两个接收器 mock，作为被验证的协作者。
		MidiReceiver receiverA = mock(MidiReceiver.class);
		MidiReceiver receiverB = mock(MidiReceiver.class);

		// 注册两个不同 ID 的接收器。
		transmitter.addReceiver("a", receiverA);
		transmitter.addReceiver("b", receiverB);
		// 发送一个 NoteOn 事件。
		transmitter.sendNoteOn(2, 60, 90, -1, false);

		// 验证广播行为：
		// 两个接收器都应各收到 1 次完全相同参数的调用。
		verify(receiverA, times(1)).sendNoteOn(2, 60, 90, -1, false);
		verify(receiverB, times(1)).sendNoteOn(2, 60, 90, -1, false);
	}

	@Test
	public void testDuplicateIdIsIgnoredAndRemovedReceiverStopsReceiving() throws MidiPlayerException {
		// 创建被测对象。
		MidiTransmitter transmitter = new MidiTransmitter();
		// 准备 3 个接收器 mock：
		// firstReceiver：第一次用 ID=dup 注册的对象
		// duplicateReceiver：第二次同 ID=dup 注册（应被忽略）
		// activeReceiver：另一个独立 ID 的正常接收器
		MidiReceiver firstReceiver = mock(MidiReceiver.class);
		MidiReceiver duplicateReceiver = mock(MidiReceiver.class);
		MidiReceiver activeReceiver = mock(MidiReceiver.class);

		transmitter.addReceiver("dup", firstReceiver);
		// 同 ID 再次注册：按实现应被忽略，不覆盖 firstReceiver。
		transmitter.addReceiver("dup", duplicateReceiver);
		transmitter.addReceiver("active", activeReceiver);

		// 删除 dup 后，理论上 dup 对应接收器不应再收到消息。
		transmitter.removeReceiver("dup");
		// 发送广播事件。
		transmitter.sendAllNotesOff();

		// 验证管理逻辑：
		// dup 路径的两个 mock 都不应收到调用；
		// active 路径应收到 1 次调用。
		verify(firstReceiver, never()).sendAllNotesOff();
		verify(duplicateReceiver, never()).sendAllNotesOff();
		verify(activeReceiver, times(1)).sendAllNotesOff();
	}
}
