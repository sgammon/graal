/*
 * Copyright (c) 2012, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
// Checkstyle: stop
//@formatter:off
package com.oracle.truffle.sl.parser.operations;

// DO NOT MODIFY - generated from SimpleLanguage.g4 using "mx create-sl-parser"

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings("all")
public class SimpleLanguageOperationsLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, WS=19, COMMENT=20, LINE_COMMENT=21, OP_OR=22, OP_AND=23, OP_COMPARE=24, 
		OP_ADD=25, OP_MUL=26, IDENTIFIER=27, STRING_LITERAL=28, NUMERIC_LITERAL=29;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "WS", "COMMENT", "LINE_COMMENT", "OP_OR", "OP_AND", "OP_COMPARE", 
			"OP_ADD", "OP_MUL", "LETTER", "NON_ZERO_DIGIT", "DIGIT", "HEX_DIGIT", 
			"OCT_DIGIT", "BINARY_DIGIT", "TAB", "STRING_CHAR", "IDENTIFIER", "STRING_LITERAL", 
			"NUMERIC_LITERAL"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'function'", "'('", "','", "')'", "'{'", "'}'", "'break'", "';'", 
			"'continue'", "'debugger'", "'while'", "'if'", "'else'", "'return'", 
			"'='", "'.'", "'['", "']'", null, null, null, "'||'", "'&&'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "WS", "COMMENT", "LINE_COMMENT", 
			"OP_OR", "OP_AND", "OP_COMPARE", "OP_ADD", "OP_MUL", "IDENTIFIER", "STRING_LITERAL", 
			"NUMERIC_LITERAL"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SimpleLanguageOperationsLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SimpleLanguageOperations.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\37\u00fa\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22"+
		"\3\22\3\23\3\23\3\24\6\24\u0099\n\24\r\24\16\24\u009a\3\24\3\24\3\25\3"+
		"\25\3\25\3\25\7\25\u00a3\n\25\f\25\16\25\u00a6\13\25\3\25\3\25\3\25\3"+
		"\25\3\25\3\26\3\26\3\26\3\26\7\26\u00b1\n\26\f\26\16\26\u00b4\13\26\3"+
		"\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\5\31\u00c8\n\31\3\32\3\32\3\33\3\33\3\34\5\34\u00cf"+
		"\n\34\3\35\3\35\3\36\3\36\3\37\5\37\u00d6\n\37\3 \3 \3!\3!\3\"\3\"\3#"+
		"\3#\3$\3$\3$\7$\u00e3\n$\f$\16$\u00e6\13$\3%\3%\7%\u00ea\n%\f%\16%\u00ed"+
		"\13%\3%\3%\3&\3&\3&\7&\u00f4\n&\f&\16&\u00f7\13&\5&\u00f9\n&\3\u00a4\2"+
		"\'\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\29\2;\2="+
		"\2?\2A\2C\2E\2G\35I\36K\37\3\2\f\5\2\13\f\16\17\"\"\4\2\f\f\17\17\4\2"+
		"--//\4\2,,\61\61\6\2&&C\\aac|\3\2\63;\3\2\62;\5\2\62;CHch\3\2\629\5\2"+
		"\f\f\17\17$$\2\u00fe\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2"+
		"\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2"+
		"\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2G\3\2\2"+
		"\2\2I\3\2\2\2\2K\3\2\2\2\3M\3\2\2\2\5V\3\2\2\2\7X\3\2\2\2\tZ\3\2\2\2\13"+
		"\\\3\2\2\2\r^\3\2\2\2\17`\3\2\2\2\21f\3\2\2\2\23h\3\2\2\2\25q\3\2\2\2"+
		"\27z\3\2\2\2\31\u0080\3\2\2\2\33\u0083\3\2\2\2\35\u0088\3\2\2\2\37\u008f"+
		"\3\2\2\2!\u0091\3\2\2\2#\u0093\3\2\2\2%\u0095\3\2\2\2\'\u0098\3\2\2\2"+
		")\u009e\3\2\2\2+\u00ac\3\2\2\2-\u00b7\3\2\2\2/\u00ba\3\2\2\2\61\u00c7"+
		"\3\2\2\2\63\u00c9\3\2\2\2\65\u00cb\3\2\2\2\67\u00ce\3\2\2\29\u00d0\3\2"+
		"\2\2;\u00d2\3\2\2\2=\u00d5\3\2\2\2?\u00d7\3\2\2\2A\u00d9\3\2\2\2C\u00db"+
		"\3\2\2\2E\u00dd\3\2\2\2G\u00df\3\2\2\2I\u00e7\3\2\2\2K\u00f8\3\2\2\2M"+
		"N\7h\2\2NO\7w\2\2OP\7p\2\2PQ\7e\2\2QR\7v\2\2RS\7k\2\2ST\7q\2\2TU\7p\2"+
		"\2U\4\3\2\2\2VW\7*\2\2W\6\3\2\2\2XY\7.\2\2Y\b\3\2\2\2Z[\7+\2\2[\n\3\2"+
		"\2\2\\]\7}\2\2]\f\3\2\2\2^_\7\177\2\2_\16\3\2\2\2`a\7d\2\2ab\7t\2\2bc"+
		"\7g\2\2cd\7c\2\2de\7m\2\2e\20\3\2\2\2fg\7=\2\2g\22\3\2\2\2hi\7e\2\2ij"+
		"\7q\2\2jk\7p\2\2kl\7v\2\2lm\7k\2\2mn\7p\2\2no\7w\2\2op\7g\2\2p\24\3\2"+
		"\2\2qr\7f\2\2rs\7g\2\2st\7d\2\2tu\7w\2\2uv\7i\2\2vw\7i\2\2wx\7g\2\2xy"+
		"\7t\2\2y\26\3\2\2\2z{\7y\2\2{|\7j\2\2|}\7k\2\2}~\7n\2\2~\177\7g\2\2\177"+
		"\30\3\2\2\2\u0080\u0081\7k\2\2\u0081\u0082\7h\2\2\u0082\32\3\2\2\2\u0083"+
		"\u0084\7g\2\2\u0084\u0085\7n\2\2\u0085\u0086\7u\2\2\u0086\u0087\7g\2\2"+
		"\u0087\34\3\2\2\2\u0088\u0089\7t\2\2\u0089\u008a\7g\2\2\u008a\u008b\7"+
		"v\2\2\u008b\u008c\7w\2\2\u008c\u008d\7t\2\2\u008d\u008e\7p\2\2\u008e\36"+
		"\3\2\2\2\u008f\u0090\7?\2\2\u0090 \3\2\2\2\u0091\u0092\7\60\2\2\u0092"+
		"\"\3\2\2\2\u0093\u0094\7]\2\2\u0094$\3\2\2\2\u0095\u0096\7_\2\2\u0096"+
		"&\3\2\2\2\u0097\u0099\t\2\2\2\u0098\u0097\3\2\2\2\u0099\u009a\3\2\2\2"+
		"\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d"+
		"\b\24\2\2\u009d(\3\2\2\2\u009e\u009f\7\61\2\2\u009f\u00a0\7,\2\2\u00a0"+
		"\u00a4\3\2\2\2\u00a1\u00a3\13\2\2\2\u00a2\u00a1\3\2\2\2\u00a3\u00a6\3"+
		"\2\2\2\u00a4\u00a5\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a5\u00a7\3\2\2\2\u00a6"+
		"\u00a4\3\2\2\2\u00a7\u00a8\7,\2\2\u00a8\u00a9\7\61\2\2\u00a9\u00aa\3\2"+
		"\2\2\u00aa\u00ab\b\25\2\2\u00ab*\3\2\2\2\u00ac\u00ad\7\61\2\2\u00ad\u00ae"+
		"\7\61\2\2\u00ae\u00b2\3\2\2\2\u00af\u00b1\n\3\2\2\u00b0\u00af\3\2\2\2"+
		"\u00b1\u00b4\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b5"+
		"\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b6\b\26\2\2\u00b6,\3\2\2\2\u00b7"+
		"\u00b8\7~\2\2\u00b8\u00b9\7~\2\2\u00b9.\3\2\2\2\u00ba\u00bb\7(\2\2\u00bb"+
		"\u00bc\7(\2\2\u00bc\60\3\2\2\2\u00bd\u00c8\7>\2\2\u00be\u00bf\7>\2\2\u00bf"+
		"\u00c8\7?\2\2\u00c0\u00c8\7@\2\2\u00c1\u00c2\7@\2\2\u00c2\u00c8\7?\2\2"+
		"\u00c3\u00c4\7?\2\2\u00c4\u00c8\7?\2\2\u00c5\u00c6\7#\2\2\u00c6\u00c8"+
		"\7?\2\2\u00c7\u00bd\3\2\2\2\u00c7\u00be\3\2\2\2\u00c7\u00c0\3\2\2\2\u00c7"+
		"\u00c1\3\2\2\2\u00c7\u00c3\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c8\62\3\2\2"+
		"\2\u00c9\u00ca\t\4\2\2\u00ca\64\3\2\2\2\u00cb\u00cc\t\5\2\2\u00cc\66\3"+
		"\2\2\2\u00cd\u00cf\t\6\2\2\u00ce\u00cd\3\2\2\2\u00cf8\3\2\2\2\u00d0\u00d1"+
		"\t\7\2\2\u00d1:\3\2\2\2\u00d2\u00d3\t\b\2\2\u00d3<\3\2\2\2\u00d4\u00d6"+
		"\t\t\2\2\u00d5\u00d4\3\2\2\2\u00d6>\3\2\2\2\u00d7\u00d8\t\n\2\2\u00d8"+
		"@\3\2\2\2\u00d9\u00da\4\62\63\2\u00daB\3\2\2\2\u00db\u00dc\7\13\2\2\u00dc"+
		"D\3\2\2\2\u00dd\u00de\n\13\2\2\u00deF\3\2\2\2\u00df\u00e4\5\67\34\2\u00e0"+
		"\u00e3\5\67\34\2\u00e1\u00e3\5;\36\2\u00e2\u00e0\3\2\2\2\u00e2\u00e1\3"+
		"\2\2\2\u00e3\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5"+
		"H\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00eb\7$\2\2\u00e8\u00ea\5E#\2\u00e9"+
		"\u00e8\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb\u00e9\3\2\2\2\u00eb\u00ec\3\2"+
		"\2\2\u00ec\u00ee\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ee\u00ef\7$\2\2\u00ef"+
		"J\3\2\2\2\u00f0\u00f9\7\62\2\2\u00f1\u00f5\59\35\2\u00f2\u00f4\5;\36\2"+
		"\u00f3\u00f2\3\2\2\2\u00f4\u00f7\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f6"+
		"\3\2\2\2\u00f6\u00f9\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f8\u00f0\3\2\2\2\u00f8"+
		"\u00f1\3\2\2\2\u00f9L\3\2\2\2\16\2\u009a\u00a4\u00b2\u00c7\u00ce\u00d5"+
		"\u00e2\u00e4\u00eb\u00f5\u00f8\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}