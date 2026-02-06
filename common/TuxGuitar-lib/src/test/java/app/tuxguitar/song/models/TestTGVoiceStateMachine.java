package app.tuxguitar.song.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import app.tuxguitar.song.factory.TGFactory;

/**
 * 日期：2026-02-06
 * 作业名称：SWE 261P Project: Part 2. Functional Testing and Finite State Machines.
 *
 * 说明：
 * 该测试类用于验证 TGVoice 的有限状态机行为，
 * 重点覆盖 empty 标志与 notes 列表之间的状态一致性与转移。
 */
public class TestTGVoiceStateMachine {

	@Test
	public void testAddNoteTransitionsToActive() {
		// 初始应为空；addNote 后进入“非空且有音符”的 Active 状态
		TGFactory factory = new TGFactory();
		TGVoice voice = factory.newVoice(0);
		TGNote note = factory.newNote();

		assertTrue(voice.isEmpty());
		assertEquals(0, voice.countNotes());

		voice.addNote(note);

		assertFalse(voice.isEmpty());
		assertEquals(1, voice.countNotes());
		assertFalse(voice.isRestVoice());
	}

	@Test
	public void testRemoveLastNoteLeavesInconsistentState() {
		// removeNote 不会自动把 empty 设回 true，因此会进入“empty=false 但 notes 为空”的不一致状态
		TGFactory factory = new TGFactory();
		TGVoice voice = factory.newVoice(0);
		TGNote note = factory.newNote();

		voice.addNote(note);
		voice.removeNote(note);

		assertFalse(voice.isEmpty());
		assertEquals(0, voice.countNotes());
		assertTrue(voice.isRestVoice());
	}

	@Test
	public void testSetEmptyTrueClearsNotesAndSetsEmpty() {
		// setEmpty(true) 会清空 notes，并把 empty 设为 true，回到 Empty 状态
		TGFactory factory = new TGFactory();
		TGVoice voice = factory.newVoice(0);
		TGNote note = factory.newNote();

		voice.addNote(note);
		voice.setEmpty(true);

		assertTrue(voice.isEmpty());
		assertEquals(0, voice.countNotes());
		assertTrue(voice.isRestVoice());
	}

	@Test
	public void testSetEmptyFalseFromEmptyCreatesInconsistentState() {
		// 空状态下 setEmpty(false) 会进入“empty=false 但 notes 为空”的不一致状态
		TGFactory factory = new TGFactory();
		TGVoice voice = factory.newVoice(0);

		voice.setEmpty(false);

		assertFalse(voice.isEmpty());
		assertEquals(0, voice.countNotes());
		assertTrue(voice.isRestVoice());
	}
}
