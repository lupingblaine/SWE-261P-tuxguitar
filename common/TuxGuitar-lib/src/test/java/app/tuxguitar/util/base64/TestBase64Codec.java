package app.tuxguitar.util.base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class TestBase64Codec {

	/**
	 * 日期：2026-02-06
	 * 作业名称：SWE 261P Project: Part 3. White Box Testing and Coverage.
	 *
	 * 说明：
	 * 该测试类针对 Base64Encoder / Base64Decoder 的核心路径进行白盒覆盖提升，
	 * 包括：不同长度输入的编码（0/1/2/3/多字节）、填充规则、解码容错（忽略非 Base64 字符），
	 * 以及二进制字节数组的 encode→decode 往返一致性。
	 */

	private static byte[] bytes(String s) {
		return s.getBytes(StandardCharsets.US_ASCII);
	}

	private static String str(byte[] b) {
		return new String(b, StandardCharsets.US_ASCII);
	}

	@Test
	public void testEncodeKnownVectors() {
		// 已知向量：验证编码结果与标准 Base64 文本一致（覆盖 padding 规则）
		assertEquals("", str(Base64Encoder.encode(bytes(""))));
		assertEquals("Zg==", str(Base64Encoder.encode(bytes("f"))));
		assertEquals("Zm8=", str(Base64Encoder.encode(bytes("fo"))));
		assertEquals("Zm9v", str(Base64Encoder.encode(bytes("foo"))));
		assertEquals("Zm9vYmFy", str(Base64Encoder.encode(bytes("foobar"))));
	}

	@Test
	public void testDecodeKnownVectors() {
		// 已知向量：验证解码正确还原原文（覆盖无/单/双 padding 情况）
		assertEquals("", str(Base64Decoder.decode(bytes(""))));
		assertEquals("f", str(Base64Decoder.decode(bytes("Zg=="))));
		assertEquals("fo", str(Base64Decoder.decode(bytes("Zm8="))));
		assertEquals("foo", str(Base64Decoder.decode(bytes("Zm9v"))));
		assertEquals("foobar", str(Base64Decoder.decode(bytes("Zm9vYmFy"))));
	}

	@Test
	public void testDecodeIgnoresNonBase64Characters() {
		// 容错：解码时应忽略非 Base64 字符（如换行）
		String withNewline = "Zm9v\nYmFy"; // "foobar" split by newline
		assertEquals("foobar", str(Base64Decoder.decode(bytes(withNewline))));
	}

	@Test
	public void testRoundTripBinaryBytes() {
		// 二进制往返：包含 0、127、-1 等边界字节，确保 encode→decode 后不丢失
		byte[] original = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 127, -1 };
		byte[] encoded = Base64Encoder.encode(original);
		byte[] decoded = Base64Decoder.decode(encoded);
		assertArrayEquals(original, decoded);
	}
}
