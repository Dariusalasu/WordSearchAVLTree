import javafx.application.Application;
import javafx.stage.Stage;
import AVLTree.*;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class WordSearch extends Application {
    private static final AVLTree<String> mTreeByNumber = new AVLTree<String>();
    private static final AVLTree<String> mTreeByWord = new AVLTree<String>();

    public static void main(String[] args) {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<Integer> future = ex.submit(new InsertWords());

        try {
            System.out.println("Word list is loading. . .");
            Scanner scanner = new Scanner(System.in);

            Integer mInt = future.get();
            ex.shutdown();

            if(mInt > 0) { System.out.println("Word list is ready\n"); }
            System.out.println("Enter a number between 1-500 to find the 1-500th most common word." +
                    "\nOr, enter a word to see if it is on the top 500 most commonly used words." +
                    "\nEnter # to exit");

            String input = scanner.nextLine();
            while(!input.equals("#")) {
                String regex = "-?[0-9]*";
                Node<String> mNode;
                if(Pattern.matches(regex, input)) {
                    if((mNode = mTreeByNumber.findKey(input)) != null) {
                        Data mData = mNode.getData();
                        printResults(true, input, mData.getWord(), mData.getFrequency());
                    } else {
                        System.out.println("The " + input + "th most commonly used word is not available");
                    }
                } else {
                    if((mNode = mTreeByWord.findKey(input)) != null) {
                        Data mData = mNode.getData();
                        printResults(false, mData.getWord(), input, mData.getFrequency());
                    } else {
                        System.out.println("The word \'" + input + "\' is not on the top 500 most commonly used list.");
                    }
                }

                System.out.println("\nEnter another number or word or # to close");
                input = scanner.nextLine();
            }
            System.out.println("Thank you for using the AVL Word Search!");

        }catch(Exception e) {
            e.printStackTrace();
        }

//        System.exit(0);
//        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public static void printResults(boolean isNumber, String number, String word, int frequency) {
        if(number.equals("1"))
            number+="st";
        else if(number.equals("2"))
            number+="nd";
        else if(number.equals("3"))
            number+="rd";
        else
            number+="th";

        if(isNumber)
            System.out.println("The " + number + " most commonly used word on the English Web in 2013 was \'" + word+"\'");
        else
            System.out.println("\'"+word + "\' was the " + number + " most commonly used word on the English Web in 2013.");

        System.out.println("It was used " + frequency + " times in the samples.");
    }

    static class InsertWords implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            try {
                File file = new File(WordSearch.class.getClassLoader().getResource("common_words.txt").toURI());
                Scanner sc = new Scanner(file);
                while(sc.hasNext()) {
                    String mLine = sc.nextLine();
                    if(!mLine.contains("//")) {
                        String[] mInfo = mLine.split(" ");
                        if (mInfo.length == 3) {
                            // Insert into tree sorted by numbers
                            Data mData1 = new Data(mInfo[1], Integer.parseInt(mInfo[2]));
                            mTreeByNumber.insert(mInfo[0].toLowerCase(), mData1);

                            // Insert into tree sorted by words
                            Data mData2 = new Data(mInfo[0], Integer.parseInt(mInfo[2]));
                            mTreeByWord.insert(mInfo[1].toLowerCase(), mData2);
                        } else {
                            System.out.println("Error (invalid): " + mLine);
                        }
                    }
                }
            }catch(Exception e) {
                System.out.println("Error reading in file(s)");
                e.printStackTrace();
                return 0; // Error
            }
            return 1; // On Success
        }
    }
}
