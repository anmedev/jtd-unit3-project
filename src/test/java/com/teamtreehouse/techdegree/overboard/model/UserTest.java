package com.teamtreehouse.techdegree.overboard.model;

//import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
//import org.junit.Rule;
import org.junit.Test;
//import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

//@Rule
//public ExpectedException thrown = ExpectedException.none();

public class UserTest {
    private Board board;
    private User questioner;
    private User answerer;
    private Question question;
    private Answer answer;

    @Before
    public void setUp() {
        board = new Board("Unit Testing");
        questioner = board.createUser("Anelle");
        answerer = board.createUser("Sally");
        question = questioner.askQuestion("What is a unit?");
        answer = answerer.answerQuestion(question,"A unit is a portion of code that does exactly one task, so it's the smallest testable part of an application.");
    }

    @Test
    public void questionerReputationIncreaseBy5PointsWhenQuestionIsUpvoted() {
        // Upvote the question
        answerer.upVote(question);

        // Assert the questioner's reputation increases by 5 points
        assertEquals(5, questioner.getReputation());
    }

    @Test
    public void answererReputationIncreaseBy10PointsWhenAnswerIsUpvoted() {
        // Upvote the answer
        questioner.upVote(answer);

        // Assert the answerer's reputation increases by 10 points
        assertEquals(10, answerer.getReputation());
    }

    @Test
    public void answererReputationIncreaseBy15PointsForAcceptedAnswer() {
        questioner.acceptAnswer(answer);

        // Assert the answerer's reputation increases by 15 points
        assertEquals(15, answerer.getReputation());
    }

    @Test(expected = VotingException.class)
    public void questionerUpvotesOwnQuestion(){
        questioner.upVote(question);
    }

    @Test(expected = VotingException.class)
    public void questionerDownvotesOwnQuestion() {
        questioner.downVote(question);
    }

    @Test(expected = VotingException.class)
    public void answererUpvotesOwnAnswer() {
        answerer.upVote(answer);
    }

    @Test(expected = VotingException.class)
    public void answererDownvotesOwnAnswer() {
        answerer.downVote(answer);
    }

//    @Test
//    public void questionerCanOnlyAcceptAnswer() throws Exception {
//        thrown.expect(AnswerAcceptanceException.class);
//        answerer.acceptAnswer(answer);
//        questioner.acceptAnswer(answer);
//
//    }

}