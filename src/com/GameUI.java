package com;

import com.actors.Character;
import com.enums.TeamColor;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

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
public final class GameUI {

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
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.WINDOW_MODAL);
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

        final ButtonType buttonTypeGobelin = new ButtonType("Gobelin 1pt");
        final ButtonType buttonTypeDwarf = new ButtonType("Nain 2pts");
        final ButtonType buttonTypeElf = new ButtonType("Elfe 3 pts");
        final ButtonType buttonTypeDwarfChief = new ButtonType("Chef Nain 4pts");
        final ButtonType buttonTypeElfChief = new ButtonType("Chef Elfe 5pts");
        final ButtonType buttonTypeSkip = new ButtonType("Passer");

        alert.getButtonTypes().setAll(buttonTypeDwarf, buttonTypeElf, buttonTypeDwarfChief, buttonTypeElfChief, buttonTypeGobelin, buttonTypeSkip);

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
        } else if(result.get() == buttonTypeGobelin) {
            buyOption = 5;
        } else {
            buyOption = 0;
        }

        return buyOption;
    }

    /**
     * Affiche une boite de dialogue demandant le nombre de case désirées [5, 10, 15]
     * @return le nombre choisi par l'utilisateur, 5 par défaut.
     */
    public int chooseNumberOfCaseDialog() {
        List<String> choices = new ArrayList<>();
        choices.add("5");
        choices.add("8");
        choices.add("12");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("5", choices);
        dialog.setTitle("Choix du plateau");
        dialog.setHeaderText("");
        dialog.setContentText("Choisissez le nombre de cases pour votre plateau :");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        int choice = 5;
        if (result.isPresent()){
            choice = Integer.parseInt(result.get());
        }

        final GridPane grid = (GridPane)scene.lookup("#game_grid");
        if(grid != null)
        {
            grid.setGridLinesVisible(true);
            for (int i = 0; i < choice; i++)
            {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / choice);
                grid.addColumn(choice);
                grid.getColumnConstraints().add(colConst);
            }
        }

        return choice;
    }

    /**
     * Affiche le contenu de l'ensemble des cases du plateau
     * @param caseNumber numero de la case
     * @param blue toutes les unités de l'équipe BLUE
     * @param red toutes les unités de l'équipe RED
     */
    public void displayCase(int caseNumber, final ArrayList<Character> blue, final ArrayList<Character> red) {
        final GridPane grid = (GridPane)scene.lookup("#game_grid");
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
        final Color labelColor = color == BLUE ? Color.BLUE : Color.RED;

        final Label label = new Label(character.toString());
        label.setWrapText(false);
        label.setTextFill(labelColor);
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(Double.MAX_VALUE);
        unitsGrid.add(label, 0, index);
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
