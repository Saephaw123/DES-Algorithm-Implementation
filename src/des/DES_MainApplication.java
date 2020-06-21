package des;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class DES_MainApplication extends JFrame implements ActionListener {
    JTextField tfPlainText, tHexKey;
    JLabel lForImage, lForImage1, lEnterHexKey, lPlainText1;
    JButton btnLoadImage, btnChangeHex, btnChangeHex1, btnEncode, btnSeeImage1, btnDecode, btnChangePlainText;
    JTextArea taHexValue, taHexValue1, taEncodedValue, taDecodedValue;
    JPanel jPanel1;
    JScrollPane jScrollPane1, jScrollPane2, jScrollPane3, jScrollPane4;

    public DES_MainApplication() {

        btnLoadImage = new JButton("Load Image");
        btnLoadImage.setBounds(50, 100, 100, 30);
        add(btnLoadImage);

        lForImage = new JLabel("Original image will be here ...");
        lForImage.setBounds(160, 30, 200, 150);
        add(lForImage);


        btnChangeHex = new JButton("Hex >>");
        btnChangeHex.setBounds(370, 100, 100, 30);
        add(btnChangeHex);

        taHexValue = new JTextArea("Hex Value from image... ");
        taHexValue.setSize(200, 150);
        taHexValue.setLineWrap(true);
        taHexValue.setRows(5);

        jScrollPane1 = new JScrollPane(taHexValue);
        jScrollPane1.setBounds(490, 30, 200, 150);
        add(jScrollPane1);

        lEnterHexKey = new JLabel("Enter 8 plain text Keys >>");
        lEnterHexKey.setBounds(60, 190, 200, 50);
        add(lEnterHexKey);

        tHexKey = new JTextField();
        tHexKey.setBounds(220, 200, 140, 30);
        add(tHexKey);

        btnChangeHex1 = new JButton(("Hex >>"));
        btnChangeHex1.setBounds(370, 200, 100, 30);
        add(btnChangeHex1);

        taHexValue1 = new JTextArea("Hex value from plain text key ...");
        taHexValue1.setSize(200, 50);
        taHexValue1.setLineWrap(true);
        taHexValue1.setRows(5);

        jScrollPane2 = new JScrollPane(taHexValue1);
        jScrollPane2.setBounds(490, 200, 200, 50);
        add(jScrollPane2);

        btnEncode = new JButton("Encode >>");
        btnEncode.setBounds(370, 320, 100, 30);
        add(btnEncode);

        taEncodedValue = new JTextArea("Encoded Value in Hex format ... ");
        taEncodedValue.setSize(200, 150);
        taEncodedValue.setLineWrap(true);

        jScrollPane3 = new JScrollPane(taEncodedValue);
        jScrollPane3.setBounds(490, 270, 200, 150);
        add(jScrollPane3);

        btnDecode = new JButton("Decode >>");
        btnDecode.setBounds(370, 480, 100, 30);
        add(btnDecode);

        btnSeeImage1 = new JButton(("<< Image"));
        btnSeeImage1.setBounds(370, 520, 100, 30);
        add(btnSeeImage1);

        lForImage1 = new JLabel("Decoded Image will be here ...");
        lForImage1.setBounds(160, 440, 200, 150);
        add(lForImage1);

        taDecodedValue = new JTextArea("Decoded Value in Hex format ...");
        taDecodedValue.setRows(5);
        taDecodedValue.setSize(200, 150);
        taDecodedValue.setLineWrap(true);

        jScrollPane4 = new JScrollPane(taDecodedValue);
        jScrollPane4.setBounds(490, 440, 200, 150);
        add(jScrollPane4);


        btnChangeHex.addActionListener(this);
        btnLoadImage.addActionListener(this);
        btnChangeHex1.addActionListener(this);
        btnEncode.addActionListener(this);
        btnDecode.addActionListener(this);
        btnSeeImage1.addActionListener(this);

        setTitle("UI Implementation for DES Algorithm");
        setSize(800, 650);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLoadImage) {
            loadImage();
        } else if (e.getSource() == btnChangeHex) {
            try {
                taHexValue.setText(getHexFromImage(selectedFile));
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else if (e.getSource() == btnChangeHex1) {
            String plainKey = tHexKey.getText().trim();
            if (plainKey.length() == 8) {
                String hexKey = plainTextKeyTo16BitHex(plainKey);
                taHexValue1.setText(hexKey.toUpperCase());
            } else {
                JOptionPane.showMessageDialog(null, "Plain text key must be 8 charaters");
            }

        } else if (e.getSource() == btnEncode) {
            String hexFromImg = taHexValue.getText().trim();
            String hexKey = taHexValue1.getText().trim();
            if (isHex(hexFromImg) && isHex(hexKey)) {
                String encodedValue = Encode(hexFromImg, hexKey);
                taEncodedValue.setText(encodedValue);
            } else {
                JOptionPane.showMessageDialog(null, "No Hex value from image and key");
            }
        } else if (e.getSource() == btnSeeImage1) {
            String decodeValue = taDecodedValue.getText().trim();
            if (isHex(decodeValue)) {
                File imageFile = null;
                try {
                    imageFile = getImageFromHex(decodeValue);
                } catch (IOException | DecoderException ioException) {
                    ioException.printStackTrace();
                }
                String path = imageFile.getAbsolutePath();
                lForImage1.setIcon(ResizeImage(path));
            } else {
                JOptionPane.showMessageDialog(null, "Encoded value must be hexadecimal value");
            }
        } else if (e.getSource() == btnDecode) {
            String encodedValue = taEncodedValue.getText().trim();
            String hexKey = taHexValue1.getText().trim();
            if (isHex(encodedValue) && isHex(hexKey)) {
                String decodedValue = Decode(encodedValue, hexKey);
                taDecodedValue.setText(decodedValue);
            } else {
                JOptionPane.showMessageDialog(null, "Encoded value and Key Must be Hex");
            }
        } else if (e.getSource() == btnChangePlainText) {
            String decodedValue = taDecodedValue.getText().trim();
            if (isHex(decodedValue)) {
                lPlainText1.setText(fromHexString(decodedValue));
            } else {
                JOptionPane.showMessageDialog(null, "Decoded Value must be Hex.");
            }

        }
    }

    File selectedFile;

    private void loadImage() {
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        //filter the files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
        file.addChoosableFileFilter(filter);
        int result = file.showSaveDialog(null);
        //if the user click on save in Jfilechooser
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = file.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            lForImage.setIcon(ResizeImage(path));
        }
        //if the user click on save in Jfilechooser

        else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("No File Select");
        }
    }


    public ImageIcon ResizeImage(String ImagePath) {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(lForImage.getWidth(), lForImage.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }

    private String getHexFromImage(File file) throws IOException {
        BufferedImage bImage = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);

        byte[] imageBytesValue = bos.toByteArray();

        char[] hex = Hex.encodeHex(imageBytesValue);
        String hexValue = new String(hex);
        System.out.println("Original Hex Length>>>>" + hexValue.length());
        hexValue += "0D0A";
        int lenghtOfHexValue = hexValue.length();
        System.out.println("Hex Length After 0D0A>>>>" + lenghtOfHexValue);
        System.out.println("lenghtOfHexValue%6>>>>" + lenghtOfHexValue % 16);
        int requireExtraLength = 16 - (lenghtOfHexValue % 6);
        if (requireExtraLength != 0 && requireExtraLength != 16) {
            for (int i = 0; i < requireExtraLength; i++)
                hexValue += "0";
        }
        System.out.println("Final Hex Length>>>>" + hexValue.length());
        return hexValue.toUpperCase();
    }


    private File getImageFromHex(String hexvalue) throws IOException, DecoderException {
        byte[] encodedImageBytes = Hex.decodeHex(hexvalue.toCharArray());
        ByteArrayInputStream bis = new ByteArrayInputStream(encodedImageBytes);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, "jpg", new File("newImage1.jpg"));
        System.out.println("image created");

        return new File("newImage1.jpg");
    }

    private String plainTextKeyTo16BitHex(String plainTextKey) {
        String textInputKeyInHex = toHexString(plainTextKey.getBytes());
        String key = "";

        //According to the DES Algorithm, make the key to be 16 Hexadecimals
        if (textInputKeyInHex.length() > 16) {
            key += textInputKeyInHex.substring(0, 15);
        } else if (textInputKeyInHex.length() < 16) {
            int require = 16 - textInputKeyInHex.length();
            for (int i = 0; i < require; i++) {
                textInputKeyInHex += '0';
            }
            key = textInputKeyInHex;
        }
        return textInputKeyInHex;
    }

    private boolean isHex(String inputData) {
        return inputData.trim().matches("[0-9A-Fa-f]+");
    }


    public static String toHexString(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ba.length; i++)
            str.append(String.format("%x", ba[i]));
        return str.toString();
    }

    public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

    private String Encode(String inputData, String key) {
        String pl = inputData.trim();
        int x = pl.length();
        String Res = "";

        dES_encode encode = new dES_encode();
        encode.setBig_key(encode.hex_to_bin(key.toUpperCase()));


        double size = 0;
        size = Math.ceil((double) x / (double) 16);
        int ind = 0;
        for (int i = 0; i < size; i++) {
            String h = "";
            for (int j = 0; j < 16; j++) {
                if (ind < x) {
                    if (pl.toCharArray()[ind] != ' ') {
                        h += pl.toCharArray()[ind];
                    } else {
                        j--;

                    }
                    ind++;
                } else {
                    h += "A";
                }

            }
            System.out.println("h >>>" + h);
            encode.setPlain(encode.hex_to_bin(h.toUpperCase()));
            encode.setHex_plain(h.toUpperCase());
            encode.encode();
            Res = (encode.bin_to_hex(encode.getCipher()));
        }
        return Res;
    }

    private String Decode(String inputdata, String key) {
        String ci = inputdata.replace(" ", "");
        int x = ci.length();
        String Res = "";

        des_decode decode = new des_decode();
        decode.setBig_key(decode.hex_to_bin(key));

        double size = 0;
        size = Math.ceil((double) x / (double) 16);
        int ind = 0;
        for (int i = 0; i < size; i++) {
            String h = "";
            for (int j = 0; j < 16; j++) {
                if (ind < x) {
                    if (ci.toCharArray()[ind] != ' ') {
                        h += ci.toCharArray()[ind];
                    } else {
                        j--;

                    }
                    ind++;
                } else {
                    h += "A";
                }

            }
            System.out.println("h " + h);
            decode.setCipher(decode.hex_to_bin(h));
            decode.setHex_cipher(h);
            decode.decode();
            Res = (decode.bin_to_hex(decode.getPlain()));
        }
        return Res;
    }

    public static void main(String args[]) {
        new DES_MainApplication();
    }
}
