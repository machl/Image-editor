/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.ProgressMonitor;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import zapoctakjavagrafika.tasks.DitherImageTask;
import zapoctakjavagrafika.tasks.EdgeDetectImageTask;
import zapoctakjavagrafika.tasks.GaussImageTask;
import zapoctakjavagrafika.tasks.GrayScaleImageTask;
import zapoctakjavagrafika.tasks.ImageTask;
import zapoctakjavagrafika.tasks.RotateImageTask;

/**
 * Třída hlavního okna aplikace.
 * 
 * @author Lukáš Machalík
 */
public class AppWindow extends JFrame implements PropertyChangeListener {

    // GUI
    private final ImagePanel imagePanel = new ImagePanel();
    private final JLabel imageNameLabel = new JLabel();
    private final JLabel imageSizeLabel = new JLabel();
    private final JButton openButton = new JButton();
    private final JButton saveButton = new JButton();
    private final JButton originalButton = new JButton();
    private final JButton grayscaleButton = new JButton();
    private final JButton rotateButton = new JButton();
    private final JButton edgedetectButton = new JButton();
    private final JButton gaussButton = new JButton();
    private final JButton ditheringButton = new JButton();
    private ProgressMonitor progressMonitor;
    
    // Other
    private BufferedImage original, image;
    private File openedFile;
    private static final JFileChooser fc = new JFileChooser();
    static int defaultGaussKernelSize = 5;
    static int defaultDitherNumOfShades = 256;
    private ImageTask task;

    /**
     * Vytvoří nové hlavní okno aplikace.
     */
    public AppWindow() {
        createAndShowGUI();

        // Nastaví souborový filtr pro JFileChooser dialogy
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Images (*.jpg, *.png, *.gif, *.bmp)", "jpg", "jpeg", "png", "gif", "bmp");
        fc.resetChoosableFileFilters();
        fc.setFileFilter(filter);
    }

    /**
     * Inicializace grafického rozhraní.
     */
    private void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Init side panel
        imageNameLabel.setText("<html><b>Opened file:</b><br>none</html>");
        imageSizeLabel.setText("<html><b>Image resolution:</b><br>not opened file</html>");

        openButton.setText("Open file");
        openButton.setEnabled(true);
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save file");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        originalButton.setText("Original");
        originalButton.setEnabled(false);
        originalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                originalButtonActionPerformed(evt);
            }
        });

        grayscaleButton.setText("Grayscale");
        grayscaleButton.setEnabled(false);
        grayscaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grayscaleButtonActionPerformed(evt);
            }
        });

        rotateButton.setText("Rotate");
        rotateButton.setEnabled(false);
        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateButtonActionPerformed(evt);
            }
        });

        edgedetectButton.setText("Edge detect");
        edgedetectButton.setEnabled(false);
        edgedetectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgedetectButtonActionPerformed(evt);
            }
        });

        gaussButton.setText("Gaussian blur");
        gaussButton.setEnabled(false);
        gaussButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gaussButtonActionPerformed(evt);
            }
        });

        ditheringButton.setText("Dithering");
        ditheringButton.setEnabled(false);
        ditheringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ditheringButtonActionPerformed(evt);
            }
        });

        // Create side panel
        JPanel sidePanel = new JPanel();
        sidePanel.setBorder(new EmptyBorder(5, 5, 5, 5) );
        sidePanel.setLayout(new GridLayout(0, 1));
        sidePanel.add(imageNameLabel);
        sidePanel.add(imageSizeLabel);
        sidePanel.add(openButton);
        sidePanel.add(saveButton);
        sidePanel.add(originalButton);
        //sidePanel.add(new JSeparator(JSeparator.HORIZONTAL));
        sidePanel.add(new JLabel("<html><br><b>Filters:</b></html>"));
        sidePanel.add(grayscaleButton);
        sidePanel.add(rotateButton);
        sidePanel.add(edgedetectButton);
        sidePanel.add(gaussButton);
        sidePanel.add(ditheringButton);


        // Fill content pane
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidePanel, BorderLayout.LINE_END);
        JScrollPane imageScrollPane = new JScrollPane(imagePanel);
        getContentPane().add(imageScrollPane, BorderLayout.CENTER);

        // Fits window in the middle and sets default size as 2/3 of screen resolution.
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setPreferredSize(new Dimension((int)resolution.getWidth()/3*2, (int)resolution.getHeight()/3*2));
        pack();
        int x = (int) ((resolution.getWidth() - getWidth()) / 2);
        int y = (int) ((resolution.getHeight() - getHeight()) / 2);
        setLocation(x, y);
        
        setVisible(true);
    }

    /**
     * Zpřístupní tlačítka pro úpravu obrázku (po úspěšném otevření souboru).
     */
    private void enableButtonsAfterOpeningFile() {
        saveButton.setEnabled(true);
        grayscaleButton.setEnabled(true);
        edgedetectButton.setEnabled(true);
        gaussButton.setEnabled(true);
        ditheringButton.setEnabled(true);
        rotateButton.setEnabled(true);
    }

    /**
     * Zakáže používání tlačítek (před vykonáváním filtru).
     */
    private void disableAllButtons() {
        openButton.setEnabled(false);
        saveButton.setEnabled(false);
        grayscaleButton.setEnabled(false);
        edgedetectButton.setEnabled(false);
        gaussButton.setEnabled(false);
        ditheringButton.setEnabled(false);
        rotateButton.setEnabled(false);
        originalButton.setEnabled(false);
    }

    /**
     * Zpřístupní tlačítka (po vykonání filtru).
     */
    private void enableAllButtons() {
        openButton.setEnabled(true);
        saveButton.setEnabled(true);
        grayscaleButton.setEnabled(true);
        edgedetectButton.setEnabled(true);
        gaussButton.setEnabled(true);
        ditheringButton.setEnabled(true);
        rotateButton.setEnabled(true);
        originalButton.setEnabled(true);
    }
    
    /**
     * Obsloužení události pro změnu progressu SwingWorkeru.
     * Více viz. {@link java.beans.PropertyChangeListener PropertyChangeListener}
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            progressMonitor.setProgress(progress);
            String message = String.format("Completed %d%%.\n", progress);
            progressMonitor.setNote(message);
            if (progressMonitor.isCanceled()) {
                task.cancel(false);
            }
        }
    }

    /**
     * Zobrazí obrázek předaný parametrem v okně.
     * @param image obrázek pro zobrazení
     */
    private void setImage(BufferedImage image) {
        this.image = image;
        imagePanel.setImage(image);
        imagePanel.repaint();
        imageSizeLabel.setText("<html><b>Image resolution:</b><br>"+image.getWidth() + "x" + image.getHeight()+"</html>");
        setTitle(openedFile.getName() + " (" + image.getWidth() + "x" + image.getHeight() + ")");
    }
    
    /**
     * Vykonání akce (pro tlačítko) Open.
     * @param evt příslušný ActionEvent
     */
    private void openButtonActionPerformed(ActionEvent evt) {
        fc.setDialogTitle("Open");
        int success = fc.showOpenDialog(this);

        if (success == JFileChooser.APPROVE_OPTION) {
            openedFile = fc.getSelectedFile();
            try {
                original = ImageIO.read(openedFile);
                
                String name = openedFile.getName();
                if (name.length() > 15) {
                    name = name.substring(0, 15) + "...";
                }
                imageNameLabel.setText("<html><b>Opened file:</b><br>"+name+"</html>");
                //imageNameLabel.setToolTipText("Opened file: " + openedFile.getName());
                
                //image = original;
                setImage(original);
                enableButtonsAfterOpeningFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Can't open file!\n" + e.getLocalizedMessage(),
                        "Can't open file!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Vykonání akce (pro tlačítko) Save.
     * @param evt příslušný ActionEvent
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        fc.setSelectedFile(openedFile);
        fc.setDialogTitle("Save as...");
        File file = null;
        boolean savingSuccess = true;
        boolean askForFile = true;
        while (askForFile) {
            int success = fc.showSaveDialog(this);
            if (success == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                String path = file.getPath();
                if (!path.endsWith(".jpg") && !path.endsWith(".jpeg") && !path.endsWith(".png") && !path.endsWith(".gif") && !path.endsWith(".bmp")) {
                    file = new File(path + ".png");
                }
                if (file.exists()) {
                    int success2 = JOptionPane.showOptionDialog(
                            this,
                            "The file already exists. Do you want to overwrite it?",
                            "File already exists!",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            null);
                    if (success2 == JOptionPane.YES_OPTION) {
                        askForFile = false;
                    }
                } else {
                    askForFile = false;
                }
            } else {
                askForFile = false;
                savingSuccess = false;
            }
        }

        if (savingSuccess) {
            openedFile = file;
            try {
                String suffix = openedFile.getName().substring(openedFile.getName().lastIndexOf('.') + 1);
                ImageIO.write(image, suffix, openedFile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Can't save a file!\n" + e.getLocalizedMessage(),
                        "Can't save a file!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Vykonání akce (pro tlačítko) Original.
     * @param evt příslušný ActionEvent
     */
    private void originalButtonActionPerformed(java.awt.event.ActionEvent evt) {
        originalButton.setEnabled(false);

        setImage(original);
    }

    /**
     * Vykonání akce (pro tlačítko) Grayscale.
     * @param evt příslušný ActionEvent
     */
    private void grayscaleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        disableAllButtons();
        progressMonitor = new ProgressMonitor(this, ((JButton) evt.getSource()).getName(), "", 0, 100);
        progressMonitor.setProgress(0);
        task = new GrayScaleImageTask(image) {
            @Override
            public void done() {
                setImage(result);
                enableAllButtons();
            }
        };
        task.addPropertyChangeListener(this);
        task.execute();
    }

    /**
     * Vykonání akce (pro tlačítko) Rotate.
     * @param evt příslušný ActionEvent
     */
    private void rotateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton rotate90leftButton = new JRadioButton();
        JRadioButton rotate90rightButton = new JRadioButton();
        JRadioButton rotate180Button = new JRadioButton();
        JRadioButton flipHorButton = new JRadioButton();
        JRadioButton flipVerButton = new JRadioButton();
        buttonGroup.add(rotate90leftButton);
        rotate90leftButton.setText("Rotate 90° left");
        buttonGroup.add(rotate90rightButton);
        rotate90rightButton.setText("Rotate 90° right");
        buttonGroup.add(rotate180Button);
        rotate180Button.setText("Rotate 180°");
        buttonGroup.add(flipHorButton);
        flipHorButton.setText("Flip horizontally");
        buttonGroup.add(flipVerButton);
        flipVerButton.setText("Flip vertically");

        int option = JOptionPane.showOptionDialog(
                null,
                new Object[]{rotate90leftButton, rotate90rightButton, rotate180Button, flipHorButton, flipVerButton},
                "Type of rotate/flip",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (option == JOptionPane.OK_OPTION) {
            int rotateMode = 0;
            if (rotate90leftButton.isSelected()) {
                rotateMode = RotateImageTask.ROTATE_90_LEFT;
            } else if (rotate90rightButton.isSelected()) {
                rotateMode = RotateImageTask.ROTATE_90_RIGHT;
            } else if (rotate180Button.isSelected()) {
                rotateMode = RotateImageTask.ROTATE_180;
            } else if (flipHorButton.isSelected()) {
                rotateMode = RotateImageTask.FLIP_HORIZONTALLY;
            } else if (flipVerButton.isSelected()) {
                rotateMode = RotateImageTask.FLIP_VERTICALLY;
            }

            disableAllButtons();
            progressMonitor = new ProgressMonitor(this, ((JButton) evt.getSource()).getName(), "", 0, 100);
            progressMonitor.setProgress(0);
            task = new RotateImageTask(image, rotateMode) {
                @Override
                public void done() {
                    setImage(result);
                    enableAllButtons();
                }
            };
            task.addPropertyChangeListener(this);
            task.execute();
        }
    }

    /**
     * Vykonání akce (pro tlačítko) Edge Detect.
     * @param evt příslušný ActionEvent
     */
    private void edgedetectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        disableAllButtons();
        progressMonitor = new ProgressMonitor(this, ((JButton) evt.getSource()).getName(), "", 0, 100);
        progressMonitor.setProgress(0);
        task = new EdgeDetectImageTask(image) {
            @Override
            public void done() {
                setImage(result);
                enableAllButtons();
            }
        };
        task.addPropertyChangeListener(this);
        task.execute();
    }

    /**
     * Vykonání akce (pro tlačítko) Gauusian blur.
     * @param evt příslušný ActionEvent
     */
    private void gaussButtonActionPerformed(ActionEvent evt) {
        SpinnerNumberModel sModel = new SpinnerNumberModel(defaultGaussKernelSize, 3, 30, 1);
        JSpinner spinner = new JSpinner(sModel);
        int option = JOptionPane.showOptionDialog(
                null,
                new Object[]{"The level of Gaussian blur (kernel size): ", spinner},
                "The parameters of Gaussian blur",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (option == JOptionPane.OK_OPTION) {
            defaultGaussKernelSize = (Integer) spinner.getValue();

            disableAllButtons();
            progressMonitor = new ProgressMonitor(this, ((JButton) evt.getSource()).getName(), "", 0, 100);
            progressMonitor.setProgress(0);
            task = new GaussImageTask(image, defaultGaussKernelSize) {
                @Override
                public void done() {
                    setImage(result);
                    enableAllButtons();
                }
            };
            task.addPropertyChangeListener(this);
            task.execute();
        }
    }

    /**
     * Vykonání akce (pro tlačítko) Dithering.
     * @param evt příslušný ActionEvent
     */
    private void ditheringButtonActionPerformed(java.awt.event.ActionEvent evt) {
        SpinnerNumberModel sModel = new SpinnerNumberModel(defaultDitherNumOfShades, 2, 256, 1);
        JSpinner spinner = new JSpinner(sModel);
        int option = JOptionPane.showOptionDialog(
                null,
                new Object[]{"Number of gray levels (2-256): ", spinner},
                "Dithering parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (option == JOptionPane.OK_OPTION) {
            defaultDitherNumOfShades = (Integer) spinner.getValue();

            disableAllButtons();
            progressMonitor = new ProgressMonitor(this, ((JButton) evt.getSource()).getName(), "", 0, 100);
            progressMonitor.setProgress(0);
            task = new DitherImageTask(image, defaultDitherNumOfShades) {
                @Override
                public void done() {
                    setImage(result);
                    enableAllButtons();
                }
            };
            task.addPropertyChangeListener(this);
            task.execute();
        }
    }
}
