package com;

import com.enums.TeamColor;
import com.model.Case;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.model.Battlefield;

import java.io.File;

/**
 * @author LEBOC Philippe.
 */
public class Main extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("sample/sample.fxml"));
        primaryStage.setTitle("Faerun");

        // Creation et affectation de la scene graphique
        final Scene scene = new Scene(root, 1100, 350);
        primaryStage.setScene(scene);
        GameUI.getInstance().setScene(scene);

        // Chargement des images des chateaux et affichage
        final File blueCastleImgFile = new File("data/castle-blue.png");
        final File redCastleImgFile = new File("data/castle-red.png");

        final Image imageBC = new Image(blueCastleImgFile.toURI().toString());
        final Image imageRC = new Image(redCastleImgFile.toURI().toString());

        final ImageView blueCastleImg = (ImageView) scene.lookup("#castle_blue") ;
        blueCastleImg.setImage(imageBC);
        final ImageView redCastleImg = (ImageView) scene.lookup("#castle_red") ;
        redCastleImg.setImage(imageRC);

        // Affichage de la fenetre principale
        primaryStage.show();

        // Récupération du choix du nombre de cases.
        final int casesCount = GameUI.getInstance().chooseNumberOfCaseDialog();

        // Creation du plateau, des chateaux et des cases.
        final Battlefield field = new Battlefield(casesCount);

        boolean end = false;
        while(!end)
        {
            // Placements des troupes construites au tour précédant
            field.spawnCharacters();

            // Affichage du plateau
            for (Case i : field.getCases()) {
                GameUI.getInstance().displayCase(i.getNumber(), i.getBlueCharacters(), i.getRedCharacters());
            }

            // Don d'une ressource à chaque tour
            field.giveTurnRewards();

            // Dépense des ressources
            field.buyPhase();

            // Phase de mouvement
            field.movementPhase();

            // Phase de combat
            field.attackPhase();

            // Vérification des conditions de victoire
            if(!field.getCase(1).getRedCharacters().isEmpty()) {
                for (Case i : field.getCases()) {
                    GameUI.getInstance().displayCase(i.getNumber(), i.getBlueCharacters(), i.getRedCharacters());
                }

                GameUI.getInstance().displayVictory(TeamColor.RED);
                end = true;
            }

            if(!field.getCase(casesCount).getBlueCharacters().isEmpty()) {
                for (Case i : field.getCases()) {
                    GameUI.getInstance().displayCase(i.getNumber(), i.getBlueCharacters(), i.getRedCharacters());
                }

                GameUI.getInstance().displayVictory(TeamColor.BLUE);
                end = true;
            }
        }
    }
}
