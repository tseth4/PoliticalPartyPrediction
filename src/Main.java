import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.BufferedReader;
//import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        Survey survey = new Survey();
        survey.conductSurvey();
    }
}

class Question {
    String text;
    List<String> options;

    Question(String text, List<String> options) {
        this.text = text;
        this.options = options;
    }
}

class PoliticalParty {
    String name;
    int score;

    PoliticalParty(String name) {
        this.name = name;
        this.score = 0;
    }
}

class Survey {
    private List<Question> questions;
    private List<PoliticalParty> parties;
    private Map<String, Map<String, Map<String, Integer>>> answerWeights;

    public Survey() {
        initializeQuestions();
        initializeParties();
        initiateCSVFiles();
        initializeWeights();
    }

    /**
     * Populate questions
     */
    private void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("What should be the government's role in healthcare?",
                Arrays.asList("Implement universal healthcare", "Maintain private healthcare with regulations",
                        "Fully privatize healthcare", "Keep current mixed system")));
        questions.add(new Question("How should the government approach gun control?",
                Arrays.asList("Implement strict gun control", "Enforce existing laws rigorously",
                        "Loosen gun ownership restrictions", "Maintain current gun laws")));
        questions.add(new Question("What's your stance on environmental regulations?",
                Arrays.asList("Increase regulations to combat climate change", "Balance environmental protection with economic growth",
                        "Reduce regulations to promote business growth", "Climate change is not a significant concern")));
        questions.add(new Question("Which political party do you most closely affiliate with?",
                Arrays.asList("Democrat", "Republican", "Independent", "Libertarian")));
    }

    /**
     * Populate parties
     */
    private void initializeParties() {
        this.parties = Arrays.asList(
                new PoliticalParty("Independent"),
                new PoliticalParty("Democrat"),
                new PoliticalParty("Libertarian"),
                new PoliticalParty("Republican")
        );
    }


    /**
     * Populate weights
     */
    private void initializeWeights() {
        answerWeights = new HashMap<>();
        // Healthcare question
        Map<String, Map<String, Integer>> healthcareWeights = new HashMap<>();
        healthcareWeights.put("Implement universal healthcare", Map.of("Democrat", 2, "Republican", 0, "Independent", 0, "Libertarian", 0));
        healthcareWeights.put("Maintain private healthcare with regulations", Map.of("Democrat", 2, "Republican", 0, "Independent", 1, "Libertarian", 2));
        healthcareWeights.put("Fully privatize healthcare", Map.of("Democrat", 0, "Republican", 2, "Independent", 0, "Libertarian", 1));
        healthcareWeights.put("Keep current mixed system", Map.of("Democrat", 0, "Republican", 1, "Independent", 1, "Libertarian", 0));
        answerWeights.put("What should be the government's role in healthcare?", healthcareWeights);
        // Gun control question
        Map<String, Map<String, Integer>> gunControlWeights = new HashMap<>();
        gunControlWeights.put("Implement strict gun control", Map.of("Democrat", 2, "Republican", 0, "Independent", 0, "Libertarian", 0));
        gunControlWeights.put("Enforce existing laws rigorously", Map.of("Democrat", 2, "Republican", 0, "Independent", 1, "Libertarian", 1));
        gunControlWeights.put("Loosen gun ownership restrictions", Map.of("Democrat", 0, "Republican", 2, "Independent", 0, "Libertarian", 1));
        gunControlWeights.put("Maintain current gun laws", Map.of("Democrat", 0, "Republican", 0, "Independent", 1, "Libertarian", 0));
        answerWeights.put("How should the government approach gun control?", gunControlWeights);
        // Environmental regulations question
        Map<String, Map<String, Integer>> environmentWeights = new HashMap<>();
        environmentWeights.put("Increase regulations to combat climate change", Map.of("Democrat", 2, "Republican", 0, "Independent", 0, "Libertarian", 0));
        environmentWeights.put("Balance environmental protection with economic growth", Map.of("Democrat", 0, "Republican", 0, "Independent", 2, "Libertarian", 0));
        environmentWeights.put("Reduce regulations to promote business growth", Map.of("Democrat", 0, "Republican", 2, "Independent", 0, "Libertarian", 1));
        environmentWeights.put("Climate change is not a significant concern", Map.of("Democrat", 0, "Republican", 2, "Independent", 0, "Libertarian", 0));
        answerWeights.put("What's your stance on environmental regulations?", environmentWeights);
    }

    public void initiateCSVFiles() {
        for (PoliticalParty party : parties) {
            createEmptyCSVFile(party.name + ".csv");
        }
    }


    public void createEmptyCSVFile(String filename){
        try (FileWriter writer = new FileWriter(filename)) {
            System.out.println("Empty CSV file created successfully: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeResponsesToFile(String partyInput){
        String csvFile = partyInput + ".csv";
        System.out.println("Writing to " + csvFile);

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write header
            writer.append("Question,Answer,Party,Weight\n");
            // Write data
            for (Map.Entry<String, Map<String, Map<String, Integer>>> questionEntry : answerWeights.entrySet()) {
                String question = questionEntry.getKey();
                for (Map.Entry<String, Map<String, Integer>> answerEntry : questionEntry.getValue().entrySet()) {
                    String answer = answerEntry.getKey();
                    for (Map.Entry<String, Integer> partyEntry : answerEntry.getValue().entrySet()) {
                        String party = partyEntry.getKey();
                        Integer weight = partyEntry.getValue();
                        writer.append(question)
                                .append(",")
                                .append(answer)
                                .append(",")
                                .append(party)
                                .append(",")
                                .append(weight.toString())
                                .append("\n");
                    }
                }
            }

            System.out.println("CSV file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Main method to initiate the survey
     */
    public void conductSurvey() {
        Scanner scanner = new Scanner(System.in);
        String userParty = null;
        for (int i = 0; i < questions.size(); i++) {
            // get question
            Question question = questions.get(i);
            // print out question
            System.out.println(question.text);
            // print out question options
            for (int j = 0; j < question.options.size(); j++) {
                System.out.println((j + 1) + ". " + question.options.get(j));
            }
            // get user input choice index
            int choice = -1;
            while (choice < 0 || choice >= question.options.size()) {
                System.out.print("Please enter a valid choice: ");
                choice = scanner.nextInt() - 1;
                if (choice < 0 || choice >= question.options.size()) {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
            // if we are at the last question
            if (i == questions.size() - 1) {
                // This is the party affiliation question
                userParty = question.options.get(choice);
            // if not at the last question
            } else {
                // call updateScores with the question and the choice
                updateScores(question.text, question.options.get(choice));


            }
        }

        System.out.println("Survey completed. User's stated party: " + userParty);
        System.out.println("Final prediction: " + predictParty());

        String prediction = predictParty();
        if (!prediction.equals("Uncertain, please try again")) {
            writeResponsesToFile(prediction);
        }
    }

    /**
     *
     * @param question
     * @param answer {answer input}
     */
    private void updateScores(String question, String answer) {
        // get the weights for each answer for the current question
        // remember answerWeights holds the data for the weights for each answer
        Map<String, Map<String, Integer>> questionWeights = answerWeights.get(question);
        if (questionWeights != null) {
            // get the weights for the answer that was inputed by the user store in answerWeights
            Map<String, Integer> answerWeights = questionWeights.get(answer);
//            System.out.println("Answer Weights: " + answerWeights);
            //
            if (answerWeights != null) {
                // loop through political parties
                for (PoliticalParty party : parties) {
                    // get the weight inputs of each party from the answered user input
                    Integer weight = answerWeights.get(party.name);
                    if (weight != null) {
                        // then add those weights to the current party
                        party.score += weight;
                    }
                }
            }
        }
    }

    /**
     * Make the prediction
     * @return
     */
    private String predictParty() {
        PoliticalParty highestScoring = null;
        int highestScore = Integer.MIN_VALUE;
        boolean tie = false;

        for (PoliticalParty party : parties) {
            if (party.score > highestScore) {
                highestScoring = party;
                highestScore = party.score;
                tie = false;
            } else if (party.score == highestScore) {
                tie = true;
            }
        }

        if (tie || highestScoring == null) {
            return "Uncertain";
        }
        return highestScoring.name;
    }
}

