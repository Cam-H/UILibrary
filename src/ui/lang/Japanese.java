package ui.lang;

public class Japanese implements Language {
	
	private static final String[][] conversions = {
			{"tsu", "つ"},
			{"ka", "か"},
			{"ki", "き"},
			{"ku", "く"},
			{"ke", "け"},
			{"ko", "こ"},
			{"sa", "さ"},
			{"shi", "し"},
			{"su", "す"},
			{"se", "せ"},
			{"so", "そ"},
			{"ta", "た"},
			{"chi", "ち"},
			{"te", "て"},
			{"to", "と"},
			{"na", "な"},
			{"ni", "に"},
			{"nu", "ぬ"},
			{"ne", "ね"},
			{"no", "の"},
			{"ha", "は"},
			{"hi", "ひ"},
			{"fu", "ふ"},
			{"he", "へ"},
			{"ho", "ほ"},
			{"ma", "ま"},
			{"mi", "み"},
			{"mu", "む"},
			{"me", "め"},
			{"mo", "も"},
			{"xya", "ゃ"},
			{"xyu", "ゅ"},
			{"xyo", "ょ"},
			{"ya", "や"},
			{"yu", "ゆ"},
			{"yo", "よ"},
			{"ra", "ら"},
			{"ri", "り"},
			{"ru", "る"},
			{"re", "れ"},
			{"ro", "ろ"},
			{"wa", "わ"},
			{"wo", "を"},
			{"nn", "ん"},
			{"ga", "が"},
			{"gi", "ぎ"},
			{"gu", "ぐ"},
			{"ge", "げ"},
			{"go", "ご"},
			{"za", "ざ"},
			{"ji", "じ"},
			{"zu", "ず"},
			{"ze", "ぜ"},
			{"zo", "ぞ"},
			{"da", "だ"},
			{"ji", "じ"},//TODO
			{"zu", "ず"},
			{"de", "で"},
			{"do", "ど"},
			{"ba", "ば"},
			{"bi", "び"},
			{"bu", "ぶ"},
			{"be", "べ"},
			{"bo", "ぼ"},
			{"pa", "ぱ"},
			{"pi", "ぴ"},
			{"pu", "ぷ"},
			{"pe", "ぺ"},
			{"po", "ぽ"},
			{"xx", "っ"},
			{"a", "あ"},
			{"i", "い"},
			{"u", "う"},
			{"e", "え"},
			{"o", "お"}
	};
	
	@Override
	public String convert(String input) {

		for(int i = 0; i < conversions.length; i++) {
			int j = 0;

			while((j = input.indexOf(conversions[i][0])) != -1) {
				input = input.substring(0, j) + conversions[i][1] + input.substring(j + conversions[i][0].length());
			}
		}
		
		return input;
	}

}
