package fr.icefeather.sip.sender;

import fr.icefeather.sip.sender.utils.SipSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    private JTextField toHostTextField;
    private JTextField toServiceTextField;
    private JTextField fromUserTextField;
    private JTextField fromPortTextField;
    private JEditorPane messageEditorPane;
    private JTextField toPortTextField;
    private JButton sendButton;
    private JPanel appPanel;

    public App() {
        // ELEMENTS
        final AppForm formulaire = new AppForm();

        final AppTextField toHostFormField = new AppTextField(toHostTextField, "127.0.0.1", null, true);
        formulaire.formFields.add(toHostFormField);
        final AppTextField toPortFormField = new AppTextField(toPortTextField, "5060", "5060", true);
        formulaire.formFields.add(toPortFormField);
        final AppTextField toServiceFormField = new AppTextField(toServiceTextField, "service", null, true);
        formulaire.formFields.add(toServiceFormField);
        final AppTextField fromUserFormField = new AppTextField(fromUserTextField, "user", null, true);
        formulaire.formFields.add(fromUserFormField);
        final AppTextField fromPortFormField = new AppTextField(fromPortTextField, "5060", "5060", true);
        formulaire.formFields.add(fromPortFormField);
        final AppTextField messageFormField = new AppTextField(messageEditorPane, "Message", null, false);
        formulaire.formFields.add(messageFormField);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (formulaire.valider()) {
                    try {
                        SipSender.send(toHostFormField.getText(), Integer.parseInt(toPortFormField.getText()), toServiceFormField.getText(),
                                fromUserFormField.getText(), Integer.parseInt(fromPortFormField.getText()), messageFormField.getText());
                    } catch (IOException exception) {
                        afficherErreur("Erreur lors de l'envoi du message SIP", exception);
                    }
                }
            }
        });
    }

    public void afficherErreur(String titre, Exception e) {
        JOptionPane d = new JOptionPane();
        d.showMessageDialog(appPanel.getParent(), e.getMessage(), titre, JOptionPane.ERROR_MESSAGE);
    }


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Windows".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
        JFrame frame = new JFrame("sip-sender");
        final App application = new App();
//        frame.setIconImage(ImageIO.read(application.getClass().getResource("/icons/inbox.png")));
        final JPanel panel = application.appPanel;

        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }



    class AppTextField implements AppFormField {

        public JTextComponent textComponent;
        public Boolean isPlaceholder = false;
        public String placeholder;
        public String defaultText;
        public Boolean notnull;

        public AppTextField(JTextComponent textComponent, String placeholder, String defaultText, Boolean notnull) {
            this.textComponent = textComponent;
            this.placeholder = placeholder;
            this.defaultText = defaultText;
            this.notnull = notnull;
            if (defaultText == null || defaultText.isEmpty()){
                showPlaceholder();
            } else {
                showDefaultText();
            }
            listeners();
        }

        public String getText(){
            if (!isPlaceholder){
                return textComponent.getText();
            }
            return null;
        }

        private void showDefaultText() {
            isPlaceholder = false;
            textComponent.setForeground(Color.BLACK);
            textComponent.setFont(new Font(textComponent.getFont().getFamily(), Font.PLAIN, textComponent.getFont().getSize()));
            textComponent.setText(defaultText);
        }

        private void showPlaceholder() {
            isPlaceholder = true;
            textComponent.setText(placeholder);
            textComponent.setForeground(Color.GRAY);
            textComponent.setFont(new Font(textComponent.getFont().getFamily(), Font.ITALIC, textComponent.getFont().getSize()));
        }

        private void listeners(){
            textComponent.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if(isPlaceholder) {
                        showDefaultText();
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(textComponent.getText().isEmpty()){
                        showPlaceholder();
                    }
                }
            });
        }

        @Override
        public Boolean valider() {
            if (notnull) {
                if (getText() == null || getText().isEmpty()){
                    textComponent.setForeground(Color.RED);
                    return false;
                }
            }
            return true;
        }

    }


    interface AppFormField {
        Boolean valider();
    }


    class AppForm {

        public List<AppFormField> formFields = new ArrayList<>();

        public Boolean valider(){
            boolean valide = true;
            for (AppFormField formField : formFields){
                if(!formField.valider()){
                    valide = false;
                }
            }
            return valide;
        }

    }

}
