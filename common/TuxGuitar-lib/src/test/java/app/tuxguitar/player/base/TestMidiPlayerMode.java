package app.tuxguitar.player.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * 日期：2026-02-06
 * 作业名称：SWE 261P Project: Part 2. Functional Testing and Finite State Machines.
 *
 * 说明：
 * 该测试类用于验证 MidiPlayerMode 的有限状态机行为，
 * 覆盖 Simple 与 Custom 两种模式下的状态不变式与状态转移。
 */
public class TestMidiPlayerMode {

	@Test
	public void testDefaultSimpleResetSetsCurrentPercent() {
		// 初始化并清空状态，默认应为 Simple 模式，且当前节奏等于 simplePercent
		MidiPlayerMode mode = new MidiPlayerMode();
		mode.clear();
		mode.reset();

		assertEquals(MidiPlayerMode.TYPE_SIMPLE, mode.getType());
		assertEquals(MidiPlayerMode.DEFAULT_TEMPO_PERCENT, mode.getSimplePercent());
		assertEquals(MidiPlayerMode.DEFAULT_TEMPO_PERCENT, mode.getCurrentPercent());
	}

	@Test
	public void testCustomResetStartsFromCustomFrom() {
		// 切换到 Custom 模式后，reset 应将 currentPercent 设置为 customFrom
		MidiPlayerMode mode = new MidiPlayerMode();
		mode.setType(MidiPlayerMode.TYPE_CUSTOM);
		mode.setCustomPercentFrom(80);
		mode.setCustomPercentTo(120);
		mode.setCustomPercentIncrement(5);
		mode.reset();

		assertEquals(80, mode.getCurrentPercent());
	}

	@Test
	public void testCustomNotifyLoopIncrementsAndCaps() {
		// Custom 模式下 notifyLoop 递增，且不超过 customTo（封顶）
		MidiPlayerMode mode = new MidiPlayerMode();
		mode.setType(MidiPlayerMode.TYPE_CUSTOM);
		mode.setCustomPercentFrom(80);
		mode.setCustomPercentTo(95);
		mode.setCustomPercentIncrement(10);
		mode.reset();

		mode.notifyLoop();
		assertEquals(90, mode.getCurrentPercent());

		mode.notifyLoop();
		assertEquals(95, mode.getCurrentPercent());

		mode.notifyLoop();
		assertEquals(95, mode.getCurrentPercent());
	}

	@Test
	public void testSimpleNotifyLoopResetsToSimplePercent() {
		// Simple 模式下 notifyLoop 会强制回到 simplePercent（自环）
		MidiPlayerMode mode = new MidiPlayerMode();
		mode.setType(MidiPlayerMode.TYPE_SIMPLE);
		mode.setSimplePercent(110);
		mode.setCurrentPercent(70);
		mode.notifyLoop();

		assertEquals(110, mode.getCurrentPercent());
	}
}
