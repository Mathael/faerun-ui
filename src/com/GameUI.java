package com;

import com.actors.Character;
import com.enums.TeamColor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.enums.TeamColor.BLUE;
import static com.enums.TeamColor.RED;

/**
 * @author Leboc Philippe.
 * Gestion complete de l'interface de l'utilisateur.
 * ATTENTION : GameUI est un Singleton !
 */
public class GameUI {

    private Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Affiche à l'utilisateur une fenêtre permettant d'acheter des unités.
     * @param color couleur de l'équipe en cours d'action
     * @param resources ressources de l'équipe en cours d'action
     * @return le code (sous forme d'entier) de l'unité à acheter.
     */
    public int chooseBuyOptionsDialog(final TeamColor color, final int resources) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setY(0);
        alert.setX(0);
        alert.setTitle("Achat de troupe");
        alert.setHeaderText("[ " + color.getName() + " ] Vous avez " + resources + " ressources.");
        alert.setContentText("Choisissez l'unité que vous désirez acheter");

        final DialogPane dialogPane = alert.getDialogPane();
        if(color == RED)
            dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #ff3f49; -fx-font-weight: bold");
        else
            dialogPane.lookup(".header-panel").setStyle("-fx-background-color: cornflowerblue; -fx-font-weight: bold");


        final ButtonType buttonTypeDwarf = new ButtonType("Nain 1pts");
        final ButtonType buttonTypeElf = new ButtonType("Elfe 2 pts");
        final ButtonType buttonTypeDwarfChief = new ButtonType("Chef Nain 3pts");
        final ButtonType buttonTypeElfChief = new ButtonType("Chef Elfe 4pts");
        final ButtonType buttonTypeSkip = new ButtonType("Passer");

        alert.getButtonTypes().setAll(buttonTypeDwarf, buttonTypeElf, buttonTypeDwarfChief, buttonTypeElfChief, buttonTypeSkip);

        final Optional<ButtonType> result = alert.showAndWait();

        final int buyOption;
        if (result.get() == buttonTypeDwarf){
            buyOption = 1;
        } else if (result.get() == buttonTypeElf) {
            buyOption = 2;
        } else if (result.get() == buttonTypeDwarfChief) {
            buyOption = 3;
        } else if(result.get() == buttonTypeElfChief) {
            buyOption = 4;
        } else {
            buyOption = 0;
        }

        return buyOption;
    }

    public int chooseNumberOfCaseDialog() {
        List<String> choices = new ArrayList<>();
        choices.add("5");
        choices.add("10");
        choices.add("15");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("5", choices);
        dialog.setTitle("Choix du plateau");
        dialog.setHeaderText("");
        dialog.setContentText("Choisissez le nombre de cases pour votre plateau :");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return Integer.parseInt(result.get());
        }

        return 5;
    }

    /**
     * Affiche
     * @param caseNumber
     * @param blue
     * @param red
     */
    public void displayCase(int caseNumber, final ArrayList<Character> blue, final ArrayList<Character> red) {
        final GridPane grid = (GridPane)scene.lookup("#game_grid_5");
        GridPane unitsGrid = (GridPane)scene.lookup("#unitsGrid_"+caseNumber);
        final int unitAmountOnCase = blue.size() + red.size();

        if(unitsGrid != null) {
            grid.getChildren().remove(grid.lookup("#unitsGrid_"+caseNumber));
        }

        unitsGrid = new GridPane();

        if(grid != null)
        {
            unitsGrid.setId("unitsGrid_"+caseNumber);
            for (int i = 0; i < unitAmountOnCase; i++)
            {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / unitAmountOnCase);
                unitsGrid.getColumnConstraints().add(colConst);
            }

            int labelIndex = 0;

            for (Character character : blue)
            {
                createLabel(unitsGrid, character, labelIndex, BLUE);
                labelIndex++;
            }

            for (Character character : red)
            {
                createLabel(unitsGrid, character, labelIndex, RED);
                labelIndex++;
            }

            grid.add(unitsGrid, caseNumber-1, 0);
        }
    }

    /**
     * Affichage d'une alerte indiquant la fin du jeu et la couleur de l'équipe gagnante
     * @param color couleur de l'équipe gagnante
     */
    public void displayVictory(TeamColor color) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Annonce de victoire");
        alert.setHeaderText("L'équipe " + color.getName() + " à gagné !");
        alert.setContentText("Le sang de l'équipe adverse à repaint le champ de bataille !");

        alert.showAndWait();
    }

    private void createLabel(GridPane unitsGrid, Character character, int index, TeamColor color) {
        Color labelColor = color == BLUE ? Color.BLUE : Color.RED;

        final Label label = new Label(character.toString());
        label.setWrapText(true);
        label.setTextFill(labelColor);
        unitsGrid.add(label, 0, index);
    }

    public void clearMessage() {
        final Label label = (Label) scene.lookup("#system_label");
        if(label != null) label.setText("");
    }

    public void sendMessage(String text) {
        final Label label = (Label) scene.lookup("#system_label");
        if(label != null) label.setText(text);

    }

    public static GameUI getInstance()
    {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder
    {
        protected static final GameUI instance = new GameUI();
    }
}
