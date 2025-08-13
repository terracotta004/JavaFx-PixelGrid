module com.terracotta004.javafxpixelgrid {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.terracotta004.javafxpixelgrid to javafx.fxml;
    exports com.terracotta004.javafxpixelgrid;
}