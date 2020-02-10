package frc.robot;
/**
 * To be fair, you have to have a very high IQ to understand Rick and Morty. The humour is extremely subtle, and without a solid grasp of theoretical physics most of the jokes will go over a typical viewer's head. There's also Rick's nihilistic outlook, which is deftly woven into his characterisation- his personal philosophy draws heavily from Narodnaya Volya literature, for instance. The fans understand this stuff; they have the intellectual capacity to truly appreciate the depths of these jokes, to realise that they're not just funny- they say something deep about LIFE. As a consequence people who dislike Rick & Morty truly ARE idiots- of course they wouldn't appreciate, for instance, the humour in Rick's existential catchphrase "Wubba Lubba Dub Dub," which itself is a cryptic reference to Turgenev's Russian epic Fathers and Sons. I'm smirking right now just imagining one of those addlepated simpletons scratching their heads in confusion as Dan Harmon's genius wit unfolds itself on their television screens. What fools.. how I pity them. 😂
 * And yes, by the way, i DO have a Rick & Morty tattoo. And no, you cannot see it. It's for the ladies' eyes only- and even then they have to demonstrate that they're within 5 IQ points of my own (preferably lower) beforehand. Nothin personnel kid 😎
 */
public class OverEngineering extends Thread {
    private RGB RGB;

    public OverEngineering(RGB RGB) {
        this.RGB = RGB;
    }
    private char[] english = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
    'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
    'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
    ',', '.', '?', ' ' };

    private String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", 
    ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
    "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
    "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
            "-----", "--..--", ".-.-.-", "..--..", " " };

    private String message;
    public void run() {
        try { // the compiler whines about sleep
            String[] morseWords = stringToMorseCode(message);
            for (int i = 0; i < morseWords.length; i++) { // for each letter in message
                char[] brokenMessage = morseWords[i].toCharArray(); // break up each letter into dots and dashes
                for (int j = 0; j < brokenMessage.length; j++) { // for each dot and dash in the message
                    // be yellow for the appropriate time then be off for the appropriate time
                    if (brokenMessage[i] == '.') {
                        RGB.setYellow();
                        sleep(200);
                    }
                    else if (brokenMessage[i] == '-') {
                        RGB.setYellow();
                        sleep(600);
                    }
                    else if (brokenMessage[i] == ' ') {
                        RGB.setOff();
                        sleep(1200);
                    }
                    RGB.setOff();
                    sleep(200);
                }
            }
        } catch (Exception error) {}; // Its rgb. Does it matter if it crashes?
    }

    public String[] stringToMorseCode(String message) {

        //english to morse code
        char[] chars = message.toCharArray();
        String[] morseCode = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < english.length; j++) {
                if (english[j] == chars[i]) {
                    morseCode[i] = morse[i];
                }
            }
        }
        return morseCode;

    }
    public void setMessage(String message) {
        this.message = message;
    }
}