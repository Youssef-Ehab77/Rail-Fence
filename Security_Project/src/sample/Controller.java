package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField input_key;
    @FXML
    private TextField output_lbl;
    @FXML
    private TextField input_txt;

    @FXML
    public void encode_btn_pressed() {
        /*
        plain text to encrypt and remove all the spaces since this algorithm doesn't support the spaces (we can just remove the replaceAll
        function, and we can make the algorithm handle the whitespaces)
        Example: MEET ME AFTER THE TOGA PARTY  --> Depth = 2
        Output Not Considering White Space --> MEMATRHTGPRYETEFETEOAAT
        Output Considering White Space --> ME EATRTETG ATETM FE H OAPRY
        so as we see the output will be so different
        */
        String plainText = input_txt.getText().replaceAll("\\s", "");
        String keyValue = input_key.getText(); // get the key value

        //We check if any text field is empty or if the key is bigger than the text, so we alert the user
        if (plainText.equals("") || keyValue.equals("")) {
            displayMessage("Not All data Filled", "", "Please Fill all The input Boxes", Alert.AlertType.ERROR);
        } else if (plainText.length() <= Integer.parseInt(keyValue)) {
            displayMessage("Key Value Is Bigger Than Text", "", "The Encrypted Text Will Be The Same If the Key Is Bigger Than or Equal The Text Length", Alert.AlertType.WARNING);
            encode_text(plainText);
        } else {
            encode_text(plainText);
        }
    }

    @FXML
    public void decode_btn_pressed() {

        String cipherText = input_txt.getText().replaceAll("\\s", "");  //cipher text to decrypt and remove all the spaces
        String keyValue = input_key.getText();

        //We check if any text field is empty or if the key is bigger than the text, so we alert the user
        if (cipherText.equals("") || keyValue.equals("")) {
            displayMessage("Not All data Filled", "", "Please Fill all The input Boxes", Alert.AlertType.ERROR);
        } else if (cipherText.length() <= Integer.parseInt(keyValue)) {
            displayMessage("Key Value Is Bigger Than Text", "", "The Decrypted Text Will Be The Same If the Key Is Bigger Than or Equal The Text Length", Alert.AlertType.WARNING);
            decode_text(cipherText);
        } else {
            decode_text(cipherText);
        }
    }

    public void displayMessage(String title, String header, String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void encode_text(String plainText) {
        int rows = Integer.parseInt(input_key.getText()); //input key value is the number of rows "depth"
            /*
        number of columns will be the length of the word divided by the number of rows, and we use ceil because if the word length is
         10 and number of rows is 3, then 10/3 = 3.333, so we need more than 3 columns so we ceil the value to 4
         */
        int cols = (int) Math.ceil((float) plainText.length() / rows);
        char[][] encoding = new char[rows][cols]; //we make a 2d array with the number of rows and columns we got
        /*
        we use this counter to handle the out of range index error that may occur due to searching for elements
        in the last column that we created by using ceil to put the extra chars from the word
        */
        int counter = 0;
        StringBuilder cipherText = new StringBuilder(); //cipher text is where we put our chars after reading them from the 2d array row by row

        /*
         in this for loop we start filling the matrix column by column and increase the counter by 1 everytime
         to make sure we don't exceed the index range.
         */
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (counter < plainText.length()) {
                    encoding[j][i] = plainText.charAt(counter);
                    counter++;
                }
            }
        }
        // we reset the value of counter to zero again here
        counter = 0;
        //this time we will read the data row by row and put it in the cipherTest string
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (counter < plainText.length() && (encoding[i][j] != 0)) {
                    cipherText.append(encoding[i][j]);
                    counter++;
                }
            }
        }
        //print the cipherText out to the user
        output_lbl.setText(cipherText.toString());
    }

    public void decode_text(String CT) {
        int rows = Integer.parseInt(input_key.getText()); //input key value is the number of rows "depth"
        String cipherText = CT;
        if (CT.length() % rows != 0) {
            cipherText += "-".repeat(rows - (CT.length() % rows)); //we add '-' sign to get the number of columns
        }
        /*
        number of columns will be the length of the word divided by the number of rows, and we use ceil because if the word length is
         10 and number of rows is 3, then 10/3 = 3.333, so we need more than 3 columns so we ceil the value to 4
         */


        int cols = (int) Math.ceil((float) cipherText.length() / rows);
        char[][] encoding = new char[rows][cols]; //we make a 2d array with the number of rows and columns we got
        /*
        we use this counter to handle the out of range index error that may occur due to searching for elements
        in the last column that we created by using ceil to put the extra chars from the word
        */
        int counter = 0;
        StringBuilder plainText = new StringBuilder(); //cipher text is where we put our chars after reading them from the 2d array row by row

        /*
         the only difference in decryption is we will fill the 2d array row by row instead of column by column
         */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (counter < cipherText.length()) {
                    encoding[i][j] = cipherText.charAt(counter);
                    counter++;
                } else break;
            }
        }
        // we reset the value of counter to zero again here
        counter = 0;
        //this time we will read the data column by column, and we will ignore '-' sign in word that we added earlier
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (counter < cipherText.length() && (encoding[j][i] != '-')) {
                    plainText.append(encoding[j][i]);
                    counter++;
                }
            }
        }
        //print the plain text out to the user
        output_lbl.setText(plainText.toString());
    }
}

