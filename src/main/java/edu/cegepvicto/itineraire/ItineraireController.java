package edu.cegepvicto.itineraire;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ItineraireController {

    @FXML
    private ComboBox<String> choixPaysDepart;

    @FXML
    private Label erreurPaysDepart;

    @FXML
    private TextField champVilleDepart;

    @FXML
    private Label erreurVilleDepart;

    @FXML
    private ComboBox<String> choixPaysArrive;

    @FXML
    private Label erreurPaysArrive;

    @FXML
    private TextField champVilleArrivee;

    @FXML
    private Label erreurVilleArrivee;

    @FXML
    private DatePicker choixDateDeplacement;

    @FXML
    private Label erreurDateDeplacement;

    @FXML
    private ToggleGroup groupeMoyenTransport;

    @FXML
    private RadioButton selectionAutobus;

    @FXML
    private RadioButton selectionTrain;

    @FXML
    private RadioButton selectionNavire;

    @FXML
    private RadioButton selectionAvion;

    @FXML
    private Label erreurChoixMoyen;

    @FXML
    private CheckBox selectionSupplementBagage;

    @FXML
    private CheckBox selectionPremiereClasse;

    @FXML
    private CheckBox selectionDateFlexible;

    @FXML
    private Spinner<Integer> selectionNombrePersonnes;

    @FXML
    private Label etiquetteTotalPersonne;

    @FXML Label etiquetteTotalGroupe;

    /**
     * Conserve le coût du moyen de transport actif
     */
    private IntegerProperty coutTransport;

    /**
     * Conserve le cout de tous les extra du déplacement
     */
    private IntegerProperty coutExtra;

    @FXML
    public void initialize()
    {
        // Initialisation des propriétés
        coutTransport = new SimpleIntegerProperty();
        coutTransport.addListener((observable, oldValue, newValue) -> {
            calculerCoutTransport();
        });

        coutExtra = new SimpleIntegerProperty();
        coutExtra.addListener((observable, oldValue, newValue) -> {
            calculerCoutTransport();
        });

        // Peuplement des ComboBox
        ObservableList<String> listePays = FXCollections.observableArrayList("Canada", "États-Unis", "France");
        choixPaysDepart.setItems(listePays);
        choixPaysArrive.setItems(listePays);

        // Formulaire avec validation dynamique
        choixPaysDepart.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                validerPays(choixPaysDepart, erreurPaysDepart);
            }
        });

        champVilleDepart.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                validerVille(champVilleDepart, choixPaysDepart.getSelectionModel().getSelectedItem(), erreurVilleDepart);
            }
        });

        choixPaysArrive.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                validerPays(choixPaysArrive, erreurPaysArrive);
            }
        });

        champVilleArrivee.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                validerVille(champVilleArrivee, choixPaysArrive.getSelectionModel().getSelectedItem(), erreurVilleArrivee);
            }
        });

        choixDateDeplacement.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                validationDateDeplacement();
            }
        });

        // Réaction du formulaire au changement de valeurs du moyen de transport, des options et du nombre de personnes
        selectionAutobus.selectedProperty().addListener((observable, oldValue, newValue ) -> {
            coutTransport.set(50);      // Autobus coute de base 50$ par facteur de distance
        });
        selectionTrain.selectedProperty().addListener((observable, oldValue, newValue ) -> {
            coutTransport.set(75);      // Train coute de base 50$ par facteur de distance
        });
        selectionNavire.selectedProperty().addListener((observable, oldValue, newValue ) -> {
            coutTransport.set(200);     // Navire coute de base 50$ par facteur de distance
        });
        selectionAvion.selectedProperty().addListener((observable, oldValue, newValue ) -> {
            coutTransport.set(500);     // Avion coute de base 50$ par facteur de distance
        });

        selectionSupplementBagage.selectedProperty().addListener((observable, oldValue, newValue ) -> {
            // Le supplément bagage coute 25$
            if(newValue) {
                coutExtra.set(coutExtra.get() + 25);
            }
            else {
                coutExtra.set(coutExtra.get() - 25);
            }
        });

        selectionPremiereClasse.selectedProperty().addListener((observable, oldValue, newValue ) -> {
            // Le supplément bagage coute 250$
            if(newValue) {
                coutExtra.set(coutExtra.get() + 250);
            }
            else {
                coutExtra.set(coutExtra.get() - 250);
            }
        });

        selectionDateFlexible.selectedProperty().addListener((observable, oldValue, newValue ) -> {
            // Le supplément bagage coute 15$
            if(newValue) {
                coutExtra.set(coutExtra.get() + 15);
            }
            else {
                coutExtra.set(coutExtra.get() - 15);
            }
        });

        // Changement du nombre de personnes
        selectionNombrePersonnes.valueProperty().addListener((observable, oldValue, newValue ) -> {
            calculerCoutTransport();
        });
    }


    /**
     * Déclenche la confirmation et l'achat des billets.
     */
    @FXML
    private void acheterBillets() {
        validerFormulaire();
    }

    /**
     * Valide l'entièreté du formulaire.
     * @return si tous les champs du formulaire sont valides ou non.
     */
    private boolean validerFormulaire() {
        boolean formulaireValide = true;

        formulaireValide &= validerPays(choixPaysDepart, erreurPaysDepart);
        formulaireValide &= validerVille(champVilleDepart, choixPaysDepart.getSelectionModel().getSelectedItem(), erreurVilleDepart);
        formulaireValide &= validerPays(choixPaysArrive, erreurPaysArrive);
        formulaireValide &= validerVille(champVilleArrivee, choixPaysArrive.getSelectionModel().getSelectedItem(), erreurVilleArrivee);
        formulaireValide &= validationDateDeplacement();
        formulaireValide &= validationMoyenTransport();

        return formulaireValide;
    }

    /**
     * Valide qu'un pays a été sélectionné dans la liste déroulante fournie en paramètre.
     * @param choixPays la liste déroulante des pays.
     * @param messageErreur le champ qui contient le message d'erreur à afficher.
     * @return si le champ est valide ou non.
     */
    private boolean validerPays(ComboBox<String> choixPays, Label messageErreur) {
        boolean valide = choixPays.getSelectionModel().getSelectedItem() != null;

        // Message d'erreur
        String message = valide ? "" : "Un pays doit être sélectionné.";
        messageErreur.setText(message);

        gererStyleErreur(choixPays, valide);

        return valide;
    }

    /**
     * Valide que la ville entrée appartien bien au pays qui a été sélectionné dans la liste déroulante fournie en paramètre.
     * @param champVille le champ qui contient le nom de la ville.
     * @param pays le nom du pays.
     * @param messageErreur le champ qui contient le message d'erreur à afficher.
     * @return si le champ est valide ou non.
     */
    private boolean validerVille(TextField champVille, String pays, Label messageErreur) {
        String nomVille = champVille.getText();

        // Code de vérification si la ville appartient bien au pays
        // messageErreur.setText("La ville " + nomVille + " n'est pas dans le pays " + pays + ".");

        return true;
    }

    /**
     * Valide que la date de déplacement est antérieure à celle de l'achat.
     * @return si la date de déplacement est valide.
     */
    private boolean validationDateDeplacement() {
        LocalDate dateDeplacement = choixDateDeplacement.getValue();
        System.out.println(dateDeplacement);
        boolean valide = dateDeplacement != null;
        String message;

        if(valide) {
            valide = dateDeplacement.isBefore(LocalDate.now());
            message = valide ? "" : "La date doit être aujourd'hui ou dans le futur.";
        } else {
            message = "Une date pour le déplacement doit être indiquée.";
        }

        erreurDateDeplacement.setText(message);
        return valide;
    }

    /**
     * Valide qu'un moyen de transport est sélectionné et à du sens pour la destination.
     * @return si le moyen de transport est valide.
     */
    private boolean validationMoyenTransport() {
        // Récupère l'item sélectionné
        boolean valide = groupeMoyenTransport.getSelectedToggle() != null;
        String messageErreur = "";

        if(!valide) {
            messageErreur = "Un moyen de transport doit être sélectionné.";
        } else {
            // Récupère le nom du moyen de transport.
            String moyenTransport = ((RadioButton) groupeMoyenTransport.getSelectedToggle()).getText();

            // Valide si le moyen existe entre deux destinations
            // messageErreur = "Impossible de se rendre de A à B par le " + moyenTransport + ".";
            // valide = false;
        }

        erreurChoixMoyen.setText(messageErreur);
        return valide;
    }

    /**
     * Calcule puis affiche les coûts de transport
     */
    private void calculerCoutTransport() {
        int coefficientDistance = 2;        // Calculer par les villes de départ et d'arrivée

        int coutParDeplacement = coutTransport.get() * coefficientDistance + coutExtra.get();
        int coutTotal = coutParDeplacement * selectionNombrePersonnes.getValue();

        etiquetteTotalPersonne.setText(coutParDeplacement + " $");
        etiquetteTotalGroupe.setText(coutTotal + " $");
    }

    private void gererStyleErreur(Node node, boolean valide) {
        ObservableList<String> styles = node.getStyleClass();

        if(valide) {
            styles.remove("erreur");
        }
        if(!valide && !styles.contains("erreur")) {
            styles.add("erreur");
        }
    }

    @FXML
    private void reinitialiser() {
        // Calcul du total
        etiquetteTotalGroupe.setText("0 $");
        etiquetteTotalPersonne.setText("0 $");

        // Message d'erreur
        erreurChoixMoyen.setText("");
        erreurDateDeplacement.setText("");
        erreurPaysArrive.setText("");
        erreurPaysDepart.setText("");
        erreurVilleArrivee.setText("");
        erreurVilleDepart.setText("");

        // Réinitialisation des choix
        choixPaysDepart.getSelectionModel().clearSelection();
        champVilleArrivee.setText("");
        choixPaysArrive.getSelectionModel().clearSelection();
        champVilleDepart.setText("");
        choixDateDeplacement.setValue(LocalDate.now());
        groupeMoyenTransport.getSelectedToggle().setSelected(false);
        selectionSupplementBagage.setSelected(false);
        selectionDateFlexible.setSelected(false);
        selectionPremiereClasse.setSelected(false);
        selectionNombrePersonnes.getValueFactory().setValue(1);
    }

    @FXML
    private void annuler() {
        Platform.exit();
    }
}