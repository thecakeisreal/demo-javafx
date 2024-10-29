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

/**
 * Contrôleur pour la fenêtre de création d'un itinéraire de
 * voyage.
 *
 * Le contrôleur valide la saisie des données puis permet d'acheter
 * les billets sélectionnés.
 */
public class ItineraireController {

    /**
     * Boîte de choix du pays de départ.
     */
    @FXML
    private ComboBox<String> choixPaysDepart;

    /**
     * Étiquette pour afficher l'erreur dans le choix d'un pays
     * de départ.
     */
    @FXML
    private Label erreurPaysDepart;

    /**
     * Champ pour sélectionner une ville de départ.
     */
    @FXML
    private TextField champVilleDepart;

    /**
     * Étiquette pour afficher l'erreur dans le choix d'une ville
     * de départ.
     */
    @FXML
    private Label erreurVilleDepart;

    /**
     * Boîte de choix du pays d'arrivée.
     */
    @FXML
    private ComboBox<String> choixPaysArrive;

    /**
     * Étiquette pour afficher l'erreur dans le champ de pays
     * d'arrivé.
     */
    @FXML
    private Label erreurPaysArrive;

    /**
     * Champ pour saisir une ville d'arrivée.
     */
    @FXML
    private TextField champVilleArrivee;

    /**
     * Étiquette pour afficher l'erreur dans le champ d'une ville
     * d'arrivé.
     */
    @FXML
    private Label erreurVilleArrivee;

    /**
     * Contrôle pour sélectionner la date du déplacement.
     */
    @FXML
    private DatePicker choixDateDeplacement;

    /**
     * Étiquette pour afficher l'erreur dans la sélection d'une
     * date de déplacement.
     */
    @FXML
    private Label erreurDateDeplacement;

    /**
     * Groupement des boutons pour les moyens de transport afin d'en
     * sélectionner qu'un seul.
     */
    @FXML
    private ToggleGroup groupeMoyenTransport;

    /**
     * Bouton pour la sélection d'un autobus.
     */
    @FXML
    private RadioButton selectionAutobus;

    /**
     * Bouton pour la sélection d'un train.
     */
    @FXML
    private RadioButton selectionTrain;

    /**
     * Bouton pour la sélection d'un navire.
     */
    @FXML
    private RadioButton selectionNavire;

    /**
     * Bouton pour la sélection d'un avion.
     */
    @FXML
    private RadioButton selectionAvion;

    /**
     * Étiquette pour afficher une erreur dans le choix
     * d'un moyen de transport.
     */
    @FXML
    private Label erreurChoixMoyen;

    /**
     * Permet de sélectionner un supplément de bagage dans le
     * transport.
     */
    @FXML
    private CheckBox selectionSupplementBagage;

    /**
     * Permet de sélectionner une option en première classe
     * dans le transport.
     */
    @FXML
    private CheckBox selectionPremiereClasse;

    /**
     * Permet de sélectionner un billet à dates flexibles dans le
     * transport.
     */
    @FXML
    private CheckBox selectionDateFlexible;

    /**
     * Sélectionne le nombre de personnes pour lesquelles acheter
     * un billet.
     */
    @FXML
    private Spinner<Integer> selectionNombrePersonnes;

    /**
     * Étiquette qui affiche le total à payer par personne.
     */
    @FXML
    private Label etiquetteTotalPersonne;

    /**
     * Étiquette qui affiche le total à payer pour tous les billets.
     */
    @FXML Label etiquetteTotalGroupe;

    /**
     * Conserve le coût du moyen de transport actif
     */
    private IntegerProperty coutTransport;

    /**
     * Conserve le cout de tous les extra du déplacement
     */
    private IntegerProperty coutExtra;

    /**
     * Méthode appelée à l'initialisation de la vue.
     */
    @FXML
    public void initialize()
    {
        initialiserProprietesCout();
        populerChoixPays();
        configurerValidationFormulaire();
        configurerReactiviteCalculTotal();
    }

    /**
     * Fait la liaison entre les champs qui ont un impact sur le coût total
     * du transport et les champs qui affichent le coût du transport.
     */
    private void configurerReactiviteCalculTotal() {
        // Sélection pour les moyens de transport
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

        // Coût pour les suppléments
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
     * Fait la liaison entre les champs de formulaire et les méthodes de validation
     * du formulaire.
     */
    private void configurerValidationFormulaire() {
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
    }

    /**
     * Rempli les boites de sélection de pays avec le nom des pays valides
     */
    private void populerChoixPays() {
        // Peuplement des ComboBox
        ObservableList<String> listePays = FXCollections.observableArrayList("Canada", "États-Unis", "France");
        choixPaysDepart.setItems(listePays);
        choixPaysArrive.setItems(listePays);
    }

    /**
     * Initialise les propriétés pour la gestion du coût du transport
     */
    private void initialiserProprietesCout() {
        // Initialisation des propriétés
        coutTransport = new SimpleIntegerProperty();
        coutTransport.addListener((observable, oldValue, newValue) -> {
            calculerCoutTransport();
        });

        coutExtra = new SimpleIntegerProperty();
        coutExtra.addListener((observable, oldValue, newValue) -> {
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
            valide = !dateDeplacement.isBefore(LocalDate.now());
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

    /**
     * Affecte ou non la classe de style "erreur" à un composante selon s'il est
     * erronné ou non.
     * @param node le noeud auquel affecter le style d'erreur
     * @param valide est-ce que l'élément est valide ou non
     */
    private void gererStyleErreur(Node node, boolean valide) {
        ObservableList<String> styles = node.getStyleClass();

        if(valide) {
            styles.remove("erreur");
        }
        if(!valide && !styles.contains("erreur")) {
            styles.add("erreur");
        }
    }

    /**
     * Réinitialise le contenu du formulaire
     */
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

    /**
     * Annule la saisie, ce qui cause la fermeture de l'application.
     */
    @FXML
    private void annuler() {
        Platform.exit();
    }
}