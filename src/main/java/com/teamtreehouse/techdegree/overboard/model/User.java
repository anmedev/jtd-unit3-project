package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class User {
    private final Board board; // Reference to the board this user belongs to
    private String name;       // Name of the user

    // Constructor to initialize a User object with a board and a username
    protected User(Board board, String userName) {
        this.board = board;    // Assign the provided board
        name = userName;       // Assign the provided username
    }

    // Creates a new question with the provided text and adds it to the board
    public Question askQuestion(String text) {
        Question question = new Question(this, text); // Create a new Question authored by this user
        board.addQuestion(question);                 // Add the question to the board
        return question;                             // Return the created question
    }

    // Creates a new answer to the given question and adds it to the board
    public Answer answerQuestion(Question question, String text) {
        Answer answer = new Answer(question, this, text); // Create a new Answer authored by this user
        board.addAnswer(answer);                         // Add the answer to the board
        return answer;                                   // Return the created answer
    }

    // Marks an answer as accepted, if this user is the author of the question
    public void acceptAnswer(Answer answer) {
        User questioner = answer.getQuestion().getAuthor(); // Get the author of the question
        if (!questioner.equals(this)) { // Ensure the current user is the author of the question
            // If not, throw an exception with an appropriate message
            String message = String.format("Only %s can accept this answer as it is their question",
                    questioner.getName());
            throw new AnswerAcceptanceException(message);
        }
        answer.setAccepted(true); // Mark the answer as accepted
    }

    // Upvotes a post if the post's author is not this user
    public boolean upVote(Post post) {
        if (post.getAuthor().equals(this)) { // Prevent self-voting
            throw new VotingException("You cannot vote for yourself!");
        }
        return post.addUpVoter(this); // Add this user as an up-voter to the post
    }

    // Downvotes a post if the post's author is not this user
    public boolean downVote(Post post) {
        if (post.getAuthor().equals(this)) { // Prevent self-voting
            throw new VotingException("You cannot vote for yourself!");
        }
        return post.addDownVoter(this); // Add this user as a down-voter to the post
    }

    // Calculates the reputation of the user based on their posts
    public int getReputation() {
        int reputation = 0;
        // Add 5 points for each upvote on questions authored by this user
        for (Question question : getQuestions()) {
            reputation += (question.getUpVotes() * 5);
        }
        // Add 10 points for each upvote on answers, subtract 1 point for each downvote,
        // and add 15 points for accepted answers
        for (Answer answer : getAnswers()) {
            reputation += (answer.getUpVotes() * 10);
            reputation -= answer.getDownVotes();
            if (answer.isAccepted()) {
                reputation += 15;
            }
        }
        return reputation; // Return the calculated reputation
    }

    // Retrieves a list of all questions authored by this user
    public List<Question> getQuestions() {
        return board.getQuestions().stream() // Stream all questions from the board
                .filter(question -> question.getAuthor().equals(this)) // Keep only those authored by this user
                .collect(collectingAndThen(toList(), Collections::unmodifiableList)); // Return as an unmodifiable list
    }

    // Retrieves a list of all answers authored by this user
    public List<Answer> getAnswers() {
        return board.getAnswers().stream() // Stream all answers from the board
                .filter(answer -> answer.getAuthor().equals(this)) // Keep only those authored by this user
                .collect(collectingAndThen(toList(), Collections::unmodifiableList)); // Return as an unmodifiable list
    }

    // Returns the name of the user
    public String getName() {
        return name;
    }
}
