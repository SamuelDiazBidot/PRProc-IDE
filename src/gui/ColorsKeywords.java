package gui;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ColorsKeywords{
		private DefaultStyledDocument document;
		private int findLastNonWordChar (String text, int index) {
			while (--index >= 0) {
				if (String.valueOf(text.charAt(index)).matches("\\W")) {
					break;
				}
			}
			return index;
		}

		private int findFirstNonWordChar (String text, int index) {
			while (index < text.length()) {
				if (String.valueOf(text.charAt(index)).matches("\\W")) {
					break;
				}
				index++;
			}
			return index;
		}

		public ColorsKeywords () { 
			final StyleContext context = StyleContext.getDefaultStyleContext();
			final AttributeSet attribute = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
			final AttributeSet attrBlack = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
//			final AttributeSet attrBrown = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, new Color(4,190,15));
			final AttributeSet attrBrown = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, new Color(120,79,40));

				this.document = new DefaultStyledDocument() {
				public void insertString (int offset, String string, AttributeSet a) throws BadLocationException {
					super.insertString(offset, string, a);

					String text = getText(0, getLength());
					int before = findLastNonWordChar(text, offset);
					if (before < 0) before = 0;
					int after = findFirstNonWordChar(text, offset + string.length());
					int wordL = before;
					int wordR = before;

					while (wordR <= after) {
						if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
							if (text.substring(wordL, wordR).matches("(\\W)*(LOAD|LOADIM|POP|STORE|PUSH|LOADRIND|STORERIND|ADD|SUB|ADDIM|SUBIM|AND|OR|XOR|NOT|NEG|SHIFTR|SHIFTL|ROTAR|ROTAL|JMPRIND|JMPADDR|JCONDRIN|JCONDADDR|LOOP|GRT|GRTEQ|EQ|NEQ|NOP|CALL|RETURN)"))
								setCharacterAttributes(wordL, wordR - wordL, attribute, false);
							else if (text.substring(wordL, wordR).matches("(\\W)*(org|db|const)"))
								setCharacterAttributes(wordL, wordR - wordL, attrBrown, false);
							else
								setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
							wordL = wordR;
						}
						wordR++;
					}
				}	

				public void remove (int offs, int len) throws BadLocationException {
					super.remove(offs, len);

					String text = getText(0, getLength());
					int before = findLastNonWordChar(text, offs);
					if (before < 0) before = 0;
					int after = findFirstNonWordChar(text, offs);

					if (text.substring(before, after).matches("(\\W)*(LOAD|LOADIM|POP|STORE|PUSH|LOADRIND|STORERIND|ADD|SUB|ADDIM|SUBIM|AND|OR|XOR|NOT|NEG|SHIFTR|SHIFTL|ROTAR|ROTAL|JMPRIND|JMPADDR|JCONDRIN|JCONDADDR|LOOP|GRT|GRTEQ|EQ|NEQ|NOP|CALL|RETURN)")) {
						setCharacterAttributes(before, after - before, attribute, false);
					}else if (text.substring(before, after).matches("(\\W)*(org|db|const)"))
						setCharacterAttributes(before, after - before, attrBrown, false);
					else {
						setCharacterAttributes(before, after - before, attrBlack, false);
					}
				}
			};
			//pane.setStyledDocument(document);
		}
		
		public DefaultStyledDocument getDocument() {
			return document;
		}
	}	