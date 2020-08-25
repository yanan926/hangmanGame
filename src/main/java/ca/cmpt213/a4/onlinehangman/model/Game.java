package ca.cmpt213.a4.onlinehangman.model;

/**
 * A class for holding Game information: status (String), answer and current answer (char array),
 * userAnswer(char), id(long), currentWrongNumber and currentGuessNumber(int)
 *
 * @ authors: Yanan Liu, Student ID: 301368378, Email: yla568@sfu.ca
 */

public class Game {
    private String status;
    private char[] answer, currentAnswer;
    private char userAnswer;
    private long id;
    private int currentWrongNumber, currentGuessNumber;
    private final int MAXWRONG = 7;

    public int getCurrentGuessNumber() {
        return currentGuessNumber;
    }

    public Game(String randomWord) {
        this.status = "Active";
        this.currentWrongNumber = 0;
        this.currentGuessNumber = 0;
        this.answer = randomWord.toCharArray();
        this.currentAnswer = new char[answer.length];
        for (int i = 0; i < answer.length; i++) {
            this.currentAnswer[i] = '_';
        }
    }


    public void addCurrentGuessNumber() {
        this.currentGuessNumber++;
    }

    public void setUserAnswer(char input) {
        this.userAnswer = input;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus() {
        boolean isWon = true;
        for (int i = 0; i < answer.length; i++) {
            if (currentAnswer[i] != answer[i]) {
                isWon = false;
                break;
            }
        }
        if (isWon) {
            this.status = "Won";
        }
        if (this.currentWrongNumber == MAXWRONG) {
            this.status = "Lose";
        }

    }

    public char[] getCurrentAnswer() {
        return this.currentAnswer;
    }

    public void setCurrentAnswer() {
        for (int i = 0; i < answer.length; i++) {
            if (answer[i] == this.userAnswer) {
                currentAnswer[i] = userAnswer;
            }
        }
    }

    public void judgeUserAnswer() {
        boolean inputIsCorrect = false;
        for (int i = 0; i < answer.length; i++) {
            if (answer[i] == this.userAnswer) {
                inputIsCorrect = true;
                break;
            }
        }
        if (!inputIsCorrect) this.currentWrongNumber++;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCurrentWrongNumber() {
        return this.currentWrongNumber;
    }

    public String getAnswer() {
        return new String(this.answer);
    }
}
