package de.tr82.directory.splitter.core.ui;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MainDialog implements Initializable {

    private Text txtSourceDir;
    private Button btnChooseSourceDir;
    private Text txtTargetDir;
    private Button btnChooseTargetDir;
    private Text txtBucketPrefix;
    private Spinner<Integer> txtFirstBucketIndex;
    private Spinner<Double> txtFirstBucketSpaceLeft;
    private ChoiceBox<String> cmbFirstBucketSpaceLeftUnit;
    private Spinner<Integer> txtBucketMaxSize;
    private ChoiceBox<String> cmbBucketMaxSizeUnit;
    private Button btnRun;
    private Button btnDryRun;
    private TextArea txtLogArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtFirstBucketIndex.
    }
}
