module edu.cegepvicto.itineraire {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.cegepvicto.itineraire to javafx.fxml;
    exports edu.cegepvicto.itineraire;
}