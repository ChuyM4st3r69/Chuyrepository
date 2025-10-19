package metodos;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class CorreoUtil {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java CorreoUtil <destinatario> <codigo>");
            return;
        }

        String destinatario = args[0];
        String codigo = args[1];

        boolean enviado = enviarCodigoPorCorreo(destinatario, codigo);
        if (enviado) {
            System.out.println("Código enviado correctamente.");
        } else {
            System.out.println("Error al enviar el código.");
        }
    }

    public static boolean enviarCodigoPorCorreo(String destinatario, String codigo) {
        final String remitente = "ajasas282005@gmail.com"; // Tu correo Gmail
        final String claveApp = "gmzp vown ytrs froj"; // Contraseña de aplicación (de Google)

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, claveApp);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            mensaje.setSubject("Código de verificación");
            mensaje.setText("Tu código de verificación es: " + codigo);

            Transport.send(mensaje);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
