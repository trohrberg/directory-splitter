package de.tr82.directory.splitter.core;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;

public class AppGui extends JFrame {

    private JTextField txtSourceDir;
    private JTextField txtTargetDir;
    private JTextField txtBucketNamePrefix;
    private JTextField txtFirstBucketIndex;
    private JTextField txtFirstBucketSpaceLeft;
    private JComboBox cmbFirstBucketSpaceLeftUnit;
    private JTextField txtBucketSizeMax;
    private JComboBox cmbBucketSizeMaxUnit;
    private JCheckBox chkDryRun;
    private JTextArea logArea;
    private JButton btnRun;
    private JButton btnClose;

    public AppGui() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        configureFrame();
        createContent();
        registerListeners();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AppGui frame = new AppGui();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void configureFrame() throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException {
        setTitle("Directory Splitter");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 500);
    }

    private void createContent() {
        final JPanel contentPane = createContentPane();
        createHeader(contentPane);
        txtSourceDir = createSourceDirControls(contentPane);
        txtTargetDir = createDestDirControls(contentPane);
        txtBucketNamePrefix = createBucketNamePrefixControls(contentPane);
        txtFirstBucketIndex = createFirstBucketIndexControls(contentPane);
        txtFirstBucketSpaceLeft = createFirstBucketSpaceLeftControls(contentPane);
        cmbFirstBucketSpaceLeftUnit = createFirstBucketSpaceLeftUnitCombo(contentPane);
        txtBucketSizeMax = createBucketSizeMaxControls(contentPane);
        cmbBucketSizeMaxUnit = createBucketSizeMaxUnitCombo(contentPane);
        chkDryRun = createDryRunControls(contentPane);
        createProgressBar(contentPane);
        btnRun = createRunButton(contentPane);
        btnClose = createCloseButton(contentPane);
        logArea = createLogArea(contentPane);
    }

    private void registerListeners() {
        btnRun.addActionListener(new ExecutionActionListener());
        btnClose.addActionListener((ActionEvent e) ->
                AppGui.this.dispatchEvent(new WindowEvent(AppGui.this, WindowEvent.WINDOW_CLOSING)));
    }

    private JPanel createContentPane() {
        final JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        contentPane.setLayout(gbl_contentPane);
        return contentPane;
    }

    private void createHeader(JPanel contentPane) {
        final JLabel lblHeader = new JLabel("Fill in the details and hit 'Run ...'");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblHeader.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_lblHeader = new GridBagConstraints();
        gbc_lblHeader.gridwidth = 4;
        gbc_lblHeader.insets = new Insets(0, 0, 5, 0);
        gbc_lblHeader.gridx = 0;
        gbc_lblHeader.gridy = 0;
        contentPane.add(lblHeader, gbc_lblHeader);
    }

    private JTextField createSourceDirControls(JPanel contentPane) {
        JLabel lblSourceDir = new JLabel("Source directory:");
        GridBagConstraints gbc_lblSourceDir = new GridBagConstraints();
        gbc_lblSourceDir.anchor = GridBagConstraints.EAST;
        gbc_lblSourceDir.insets = new Insets(0, 0, 5, 5);
        gbc_lblSourceDir.gridx = 0;
        gbc_lblSourceDir.gridy = 1;
        contentPane.add(lblSourceDir, gbc_lblSourceDir);

        final JTextField txtSourceDir = new JTextField();
        txtSourceDir.setEditable(false);
        GridBagConstraints gbc_txtSourceDir = new GridBagConstraints();
        gbc_txtSourceDir.gridwidth = 2;
        gbc_txtSourceDir.insets = new Insets(0, 0, 5, 5);
        gbc_txtSourceDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtSourceDir.gridx = 1;
        gbc_txtSourceDir.gridy = 1;
        contentPane.add(txtSourceDir, gbc_txtSourceDir);
        txtSourceDir.setColumns(10);

        final JButton btnChooseSourceDir = new JButton("Choose ...");
        GridBagConstraints gbc_btnChooseSourceDir = new GridBagConstraints();
        gbc_btnChooseSourceDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnChooseSourceDir.insets = new Insets(0, 0, 5, 0);
        gbc_btnChooseSourceDir.gridx = 3;
        gbc_btnChooseSourceDir.gridy = 1;
        contentPane.add(btnChooseSourceDir, gbc_btnChooseSourceDir);
        btnChooseSourceDir.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(AppGui.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                txtSourceDir.setText(fileChooser.getSelectedFile().toString());
            }
        });

        return txtSourceDir;
    }

    private JTextField createDestDirControls(JPanel contentPane) {
        JLabel lblTargetDir = new JLabel("Target directory:");
        GridBagConstraints gbc_lblTargetDir = new GridBagConstraints();
        gbc_lblTargetDir.anchor = GridBagConstraints.EAST;
        gbc_lblTargetDir.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetDir.gridx = 0;
        gbc_lblTargetDir.gridy = 2;
        contentPane.add(lblTargetDir, gbc_lblTargetDir);

        final JTextField txtTargetDir = new JTextField();
        GridBagConstraints gbc_txtTargetDir = new GridBagConstraints();
        gbc_txtTargetDir.gridwidth = 2;
        gbc_txtTargetDir.insets = new Insets(0, 0, 5, 5);
        gbc_txtTargetDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtTargetDir.gridx = 1;
        gbc_txtTargetDir.gridy = 2;
        contentPane.add(txtTargetDir, gbc_txtTargetDir);
        txtTargetDir.setColumns(10);

        final JButton btnChooseTargetDir = new JButton("Choose ...");
        GridBagConstraints gbc_btnChooseTargetDir = new GridBagConstraints();
        gbc_btnChooseTargetDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnChooseTargetDir.insets = new Insets(0, 0, 5, 0);
        gbc_btnChooseTargetDir.gridx = 3;
        gbc_btnChooseTargetDir.gridy = 2;
        contentPane.add(btnChooseTargetDir, gbc_btnChooseTargetDir);
        btnChooseTargetDir.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(AppGui.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                txtTargetDir.setText(fileChooser.getSelectedFile().toString());
            }
        });
        return txtTargetDir;
    }

    private JTextField createBucketNamePrefixControls(JPanel contentPane) {
        final JLabel lblBucketNamePrefix = new JLabel("Bucket name prefix:");
        GridBagConstraints gbc_lblBucketNamePrefix = new GridBagConstraints();
        gbc_lblBucketNamePrefix.anchor = GridBagConstraints.EAST;
        gbc_lblBucketNamePrefix.insets = new Insets(0, 0, 5, 5);
        gbc_lblBucketNamePrefix.gridx = 0;
        gbc_lblBucketNamePrefix.gridy = 3;
        contentPane.add(lblBucketNamePrefix, gbc_lblBucketNamePrefix);

        final JTextField txtBucketNamePrefix = new JTextField();
        txtBucketNamePrefix.setText("RAWBLU_");
        GridBagConstraints gbc_txtBucketNamePrefix = new GridBagConstraints();
        gbc_txtBucketNamePrefix.gridwidth = 2;
        gbc_txtBucketNamePrefix.insets = new Insets(0, 0, 5, 5);
        gbc_txtBucketNamePrefix.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtBucketNamePrefix.gridx = 1;
        gbc_txtBucketNamePrefix.gridy = 3;
        contentPane.add(txtBucketNamePrefix, gbc_txtBucketNamePrefix);
        txtBucketNamePrefix.setColumns(10);
        return txtBucketNamePrefix;
    }

    private JTextField createFirstBucketIndexControls(JPanel contentPane) {
        final JLabel lblFirstBucketIndex = new JLabel("First Bucket index:");
        GridBagConstraints gbc_lblBucketNamePrefix = new GridBagConstraints();
        gbc_lblBucketNamePrefix.anchor = GridBagConstraints.EAST;
        gbc_lblBucketNamePrefix.insets = new Insets(0, 0, 5, 5);
        gbc_lblBucketNamePrefix.gridx = 0;
        gbc_lblBucketNamePrefix.gridy = 4;
        contentPane.add(lblFirstBucketIndex, gbc_lblBucketNamePrefix);

        final JTextField txtFirstBucketIndex = new JTextField();
        txtFirstBucketIndex.setText("1");
        GridBagConstraints gbc_txtBucketNamePrefix = new GridBagConstraints();
        gbc_txtBucketNamePrefix.gridwidth = 2;
        gbc_txtBucketNamePrefix.insets = new Insets(0, 0, 5, 5);
        gbc_txtBucketNamePrefix.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtBucketNamePrefix.gridx = 1;
        gbc_txtBucketNamePrefix.gridy = 4;
        contentPane.add(txtFirstBucketIndex, gbc_txtBucketNamePrefix);
        txtFirstBucketIndex.setColumns(10);
        return txtFirstBucketIndex;
    }

    private JTextField createFirstBucketSpaceLeftControls(JPanel contentPane) {
        JLabel lblFirstBucketSize = new JLabel("First bucket space left:");
        GridBagConstraints gbc_lblFirstBucketSize = new GridBagConstraints();
        gbc_lblFirstBucketSize.anchor = GridBagConstraints.EAST;
        gbc_lblFirstBucketSize.insets = new Insets(0, 0, 5, 5);
        gbc_lblFirstBucketSize.gridx = 0;
        gbc_lblFirstBucketSize.gridy = 5;
        contentPane.add(lblFirstBucketSize, gbc_lblFirstBucketSize);

        JTextField txtFirstBucketSize = new JTextField();
        txtFirstBucketSize.setText("23");
        GridBagConstraints gbc_txtFirstBucketSize = new GridBagConstraints();
        gbc_txtFirstBucketSize.gridwidth = 2;
        gbc_txtFirstBucketSize.insets = new Insets(0, 0, 5, 5);
        gbc_txtFirstBucketSize.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtFirstBucketSize.gridx = 1;
        gbc_txtFirstBucketSize.gridy = 5;
        contentPane.add(txtFirstBucketSize, gbc_txtFirstBucketSize);
        txtFirstBucketSize.setColumns(10);
        return txtFirstBucketSize;
    }

    private JComboBox createFirstBucketSpaceLeftUnitCombo(JPanel contentPane) {
        final JComboBox<String> cmbFirstBucketSizeUnit = new JComboBox<>();
        cmbFirstBucketSizeUnit
                .setModel(new DefaultComboBoxModel<>(new String[]{"Bytes", "KBytes", "MBytes", "GBytes"}));
        cmbFirstBucketSizeUnit.setSelectedIndex(3);
        GridBagConstraints gbc_cmbFirstBucketSizeUnit = new GridBagConstraints();
        gbc_cmbFirstBucketSizeUnit.insets = new Insets(0, 0, 5, 0);
        gbc_cmbFirstBucketSizeUnit.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbFirstBucketSizeUnit.gridx = 3;
        gbc_cmbFirstBucketSizeUnit.gridy = 5;
        contentPane.add(cmbFirstBucketSizeUnit, gbc_cmbFirstBucketSizeUnit);
        return cmbFirstBucketSizeUnit;
    }

    private JTextField createBucketSizeMaxControls(JPanel contentPane) {
        final JLabel lblMaxBucketSize = new JLabel("Max bucket size:");
        GridBagConstraints gbc_lblMaxBucketSize = new GridBagConstraints();
        gbc_lblMaxBucketSize.anchor = GridBagConstraints.EAST;
        gbc_lblMaxBucketSize.insets = new Insets(0, 0, 5, 5);
        gbc_lblMaxBucketSize.gridx = 0;
        gbc_lblMaxBucketSize.gridy = 6;
        contentPane.add(lblMaxBucketSize, gbc_lblMaxBucketSize);

        final JTextField txtMaxBucketSize = new JTextField();
        txtMaxBucketSize.setText("23");
        GridBagConstraints gbc_txtMaxBucketSize = new GridBagConstraints();
        gbc_txtMaxBucketSize.gridwidth = 2;
        gbc_txtMaxBucketSize.insets = new Insets(0, 0, 5, 5);
        gbc_txtMaxBucketSize.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtMaxBucketSize.gridx = 1;
        gbc_txtMaxBucketSize.gridy = 6;
        contentPane.add(txtMaxBucketSize, gbc_txtMaxBucketSize);
        txtMaxBucketSize.setColumns(10);
        return txtMaxBucketSize;
    }

    private JComboBox createBucketSizeMaxUnitCombo(JPanel contentPane) {
        final JComboBox<String> cmbMaxBucketSizeUnit = new JComboBox<>();
        cmbMaxBucketSizeUnit.setModel(new DefaultComboBoxModel<>(new String[]{"Bytes", "KBytes", "MBytes", "GBytes"}));
        cmbMaxBucketSizeUnit.setSelectedIndex(3);
        GridBagConstraints gbc_cmbMaxBucketSizeUnit = new GridBagConstraints();
        gbc_cmbMaxBucketSizeUnit.insets = new Insets(0, 0, 5, 0);
        gbc_cmbMaxBucketSizeUnit.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbMaxBucketSizeUnit.gridx = 3;
        gbc_cmbMaxBucketSizeUnit.gridy = 6;
        contentPane.add(cmbMaxBucketSizeUnit, gbc_cmbMaxBucketSizeUnit);
        return cmbMaxBucketSizeUnit;
    }

    private JCheckBox createDryRunControls(JPanel contentPane) {
        final JLabel lblDryRun = new JLabel("Dry run:");
        GridBagConstraints gbc_lblDryRun = new GridBagConstraints();
        gbc_lblDryRun.insets = new Insets(0, 0, 5, 5);
        gbc_lblDryRun.gridx = 0;
        gbc_lblDryRun.gridy = 7;
        contentPane.add(lblDryRun, gbc_lblDryRun);

        final JCheckBox chkDryRun = new JCheckBox("");
        chkDryRun.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_chkDryRun = new GridBagConstraints();
        gbc_chkDryRun.anchor = GridBagConstraints.WEST;
        gbc_chkDryRun.insets = new Insets(0, 0, 5, 5);
        gbc_chkDryRun.gridx = 1;
        gbc_chkDryRun.gridy = 7;
        contentPane.add(chkDryRun, gbc_chkDryRun);
        return chkDryRun;
    }

    private void createProgressBar(JPanel contentPane) {
        final JProgressBar progressBar = new JProgressBar();
        GridBagConstraints gbc_progressBar = new GridBagConstraints();
        gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_progressBar.insets = new Insets(0, 0, 5, 5);
        gbc_progressBar.gridwidth = 2;
        gbc_progressBar.gridx = 0;
        gbc_progressBar.gridy = 8;
        contentPane.add(progressBar, gbc_progressBar);
    }

    private JButton createRunButton(JPanel contentPane) {
        final JButton btnRun = new JButton("Run");
        GridBagConstraints gbc_btnRun = new GridBagConstraints();
        gbc_btnRun.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnRun.insets = new Insets(0, 0, 5, 5);
        gbc_btnRun.gridx = 2;
        gbc_btnRun.gridy = 8;
        contentPane.add(btnRun, gbc_btnRun);
        return btnRun;
    }

    private JButton createCloseButton(JPanel contentPane) {
        final JButton btnClose = new JButton("Close");
        GridBagConstraints gbc_btnClose = new GridBagConstraints();
        gbc_btnClose.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnClose.insets = new Insets(0, 0, 5, 0);
        gbc_btnClose.gridx = 3;
        gbc_btnClose.gridy = 8;
        contentPane.add(btnClose, gbc_btnClose);
        return btnClose;
    }

    private JTextArea createLogArea(JPanel contentPane) {
        final JTextArea textArea = new JTextArea();

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        GridBagConstraints gbc_textArea = new GridBagConstraints();
        gbc_textArea.gridwidth = 4;
        gbc_textArea.fill = GridBagConstraints.BOTH;
        gbc_textArea.gridx = 0;
        gbc_textArea.gridy = 9;
        contentPane.add(scroll, gbc_textArea);

        return textArea;
    }

    private class ExecutionActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            logArea.setText(null);

            final Path sourceBasePath = new File(txtSourceDir.getText()).toPath();
            final Path targetBasePath = new File(txtTargetDir.getText()).toPath();
            long firstBucketSpaceLeft = calculateBucketSize(txtFirstBucketSpaceLeft.getText(), cmbFirstBucketSpaceLeftUnit.getSelectedItem());
            long bucketSizeMax = calculateBucketSize(txtBucketSizeMax.getText(), cmbBucketSizeMaxUnit.getSelectedItem());
            int firstBucketIndex = Integer.valueOf(txtFirstBucketIndex.getText());

            DirectorySplitter directorySplitter = new DirectorySplitter(sourceBasePath, targetBasePath, txtBucketNamePrefix.getText(),
                    firstBucketIndex, firstBucketSpaceLeft, bucketSizeMax, chkDryRun.isSelected());
            directorySplitter.setLogger((String s) -> logArea.append(s + "\n"));

            try {
                directorySplitter.run();
            } catch (Exception e) {
                logArea.setText("ERROR: Operation failed!\n");
                logArea.append(e.getMessage());
            }
        }

        private long calculateBucketSize(String bucketSize, Object bucketSizeUnit) {
            long result = Long.valueOf(bucketSize);
            if ("KBytes".equals(bucketSizeUnit)) {
                result *= 1024;
            }
            if ("MBytes".equals(bucketSizeUnit)) {
                result *= 1024 * 1024;
            }
            if ("GBytes".equals(bucketSizeUnit)) {
                result *= 1024 * 1024 * 1024;
            }

            return result;
        }
    }
}
