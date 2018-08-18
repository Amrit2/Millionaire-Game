/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Amrit
 */
public class ReadQuestionsFile {

    File f;
    FileReader fr;
    BufferedReader br;
    Scanner keyboard;
    Lifelines lifeline;
    private int moneyWon = 0;
    String[] options;
    String answer;
    String question;
    Questions quizQuestion;
    LeaderBoard leaderboard;

    public ReadQuestionsFile(String name) throws IOException {
        f = new File("questions.txt");
        fr = new FileReader(f);
        br = new BufferedReader(fr);
        leaderboard = new LeaderBoard();
        answer = "";
        question = "";
        options = new String[4];
        quizQuestion = new Questions();

        keyboard = new Scanner(System.in);
        lifeline = new Lifelines();

        getQuestions(name, quizQuestion, answer, question, options);

    }

    public void setMoneyWon(int money) {
        this.moneyWon = money;
    }

    public int getMoneyWon() {
        return this.moneyWon;
    }

    private void getQuestions(String name, Questions quizQues, String question, String answer, String[] options) throws IOException {
        String userAnswer = "";
        String line;
        int questionNumber = 1;

        int limit = 5;
        int currentLine = 0;
        int count = 0;
//        leaderboard.showLeaderBoard();
        leaderboard.sortedBoard();
        while (questionNumber <= 22 && !userAnswer.equalsIgnoreCase("Q")) {

            while ((line = br.readLine()) != null && currentLine <= limit) {
                if (line.contains("?")) {
                    question = line;
                }
                if (line.contains(":")) {
                    options[count++] = line;
                }
                if ((line.contains("A") || line.contains("B") || line.contains("C") || line.contains("D")) && !line.contains(":")) {
                    answer = line;
                }
                currentLine++;
            }

            limit += 6;
            count = 0;
            quizQues.setQuestion(question);
            quizQues.setAnswer(answer);
            quizQues.setOptions(options);
            System.out.println(quizQues.toString());

            System.out.println("Would you like to use one of the life lines? If so, type yes else please type a letter to submit your answer or Q to quit the game.");
            userAnswer = keyboard.nextLine();

            if (userAnswer.equalsIgnoreCase("Yes")) {

                do {
                    useLifeLine(lifeline, quizQues, options, answer, question);
                    System.out.println("Would you like to use one of the life lines? If so, type yes else please type a LETTER to submit your answer or Q to quit the game.");
                    userAnswer = keyboard.nextLine();

                } while (userAnswer.equalsIgnoreCase("Yes"));

            }
            if (!userAnswer.equalsIgnoreCase("Q")) {
                this.setMoneyWon(checkAnswer(userAnswer, answer, moneyWon));
                questionNumber++;
            }

        }
        leaderboard.addToTheFile(name, this.getMoneyWon());
        br.close();
        fr.close();
    }

    public int checkAnswer(String userAnswer, String answer, int moneyWon) throws FileNotFoundException, IOException {
        if (userAnswer.equalsIgnoreCase(answer)) {
            if (moneyWon == 0) {
                moneyWon = 100;
            } else {
                moneyWon *= 2;
                if (moneyWon == 400 || this.moneyWon == 600) {
                    moneyWon -= 100;
                } else if (moneyWon == 128000) {
                    moneyWon -= 3000;
                }
            }
            System.out.println("Correct Answer! You've reached " + moneyWon + " dollars.\n"); //money
        } else {
            if (moneyWon >= 1000) {
                moneyWon = 1000;
            } else if (this.moneyWon >= 32000) {
                moneyWon = 32000;
            } else {
                moneyWon = 0;
            }
            System.out.println("Wrong Answer :(. The correct answer is " + answer + ". You are on " + moneyWon);
        }

        return moneyWon;
    }

    private void useLifeLine(Lifelines lifeline, Questions quizQues, String[] options, String answer, String question) {
        String chosenLifeLine = "";
        System.out.println("Type the related NUMBER to pick an option "
                + "\n1. 50:50"
                + "\n2. Phone a friend"
                + "\n3. Audience Vote");
        chosenLifeLine = keyboard.nextLine();
        if (!chosenLifeLine.equalsIgnoreCase("Q")) {
            if (chosenLifeLine.equalsIgnoreCase("1")) {
                String[] resultedOption = lifeline.setFiftyFiftyOptions(options, answer, question);
                quizQues.setOptions(resultedOption);
                System.out.println(quizQues.toString());
            }
            if (chosenLifeLine.equalsIgnoreCase("2")) {
                lifeline.setPhoneAFriendOptions(options, answer, question);
                System.out.println(quizQues.toString());
            }
            if (chosenLifeLine.equalsIgnoreCase("3")) {
                quizQues.setOptions(lifeline.setAudienceVoteOptions(options, answer, question));
                System.out.println(quizQues.toString());
            }

        }
    }

}
