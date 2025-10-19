package metodos;

import Interfaces.NuevaFactura;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GeneradorFacturaPDF {

    public static String generarFacturaPDF(String nombreCliente, String direccion, String rfc, String telefono, String correo) {
        Document documento = new Document();
        String rutaPDF = "";
        try {
            String ruta = "facturas/";
            new File(ruta).mkdirs();
            String nombreArchivo = "Factura_" + System.currentTimeMillis() + ".pdf";
            rutaPDF = ruta + nombreArchivo;

            PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(rutaPDF));
            documento.open();

            // Definir colores
            BaseColor colorPrimario = new BaseColor(70, 130, 180); // Azul
            BaseColor colorSecundario = new BaseColor(240, 248, 255); // Azul claro
            BaseColor colorTexto = new BaseColor(51, 51, 51); // Gris oscuro

            // Definir fuentes
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, colorPrimario);
            Font subtituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, colorPrimario);
            Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 11, colorTexto);
            Font textoBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, colorTexto);
            Font tablaHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font tablaCeldaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, colorTexto);

            // =============== ENCABEZADO CON LOGO ===============
            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{3f, 2f});

            // Logo y nombre de la empresa (lado izquierdo)
            PdfPCell celdaLogo = new PdfPCell();
            celdaLogo.setBorder(Rectangle.NO_BORDER);
            celdaLogo.setPaddingBottom(20);

            // Aquí puedes agregar tu logo si tienes la imagen
            Image logo = Image.getInstance("src/icons/Logo.png");
            logo.scaleToFit(80, 80);
            celdaLogo.addElement(logo);
            Paragraph logoTexto = new Paragraph("FarmaCode", tituloFont);
            logoTexto.setAlignment(Element.ALIGN_LEFT);
            celdaLogo.addElement(logoTexto);

            Paragraph infoEmpresa = new Paragraph();
            infoEmpresa.add(new Chunk("Sistema de Gestión Farmacéutica\n", textoFont));
            infoEmpresa.add(new Chunk("RFC: FARC123456ABC\n", textoFont));
            infoEmpresa.add(new Chunk("Tel: (664) 123-4567\n", textoFont));
            infoEmpresa.add(new Chunk("info@farmacode.com", textoFont));
            infoEmpresa.setAlignment(Element.ALIGN_LEFT);
            celdaLogo.addElement(infoEmpresa);

            // Información de la factura (lado derecho)
            PdfPCell celdaFactura = new PdfPCell();
            celdaFactura.setBorder(Rectangle.NO_BORDER);
            celdaFactura.setPaddingBottom(20);
            celdaFactura.setHorizontalAlignment(Element.ALIGN_RIGHT);

            Paragraph facturaInfo = new Paragraph();
            facturaInfo.add(new Chunk("FACTURA\n", subtituloFont));
            facturaInfo.add(new Chunk("No. FACT-" + String.format("%06d", System.currentTimeMillis() % 1000000) + "\n", textoBoldFont));
            facturaInfo.add(new Chunk("Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "\n", textoFont));
            facturaInfo.add(new Chunk("Hora: " + new SimpleDateFormat("HH:mm:ss").format(new Date()), textoFont));
            facturaInfo.setAlignment(Element.ALIGN_RIGHT);
            celdaFactura.addElement(facturaInfo);

            encabezado.addCell(celdaLogo);
            encabezado.addCell(celdaFactura);
            documento.add(encabezado);

            // =============== LÍNEA SEPARADORA ===============
            Paragraph linea = new Paragraph(" ");
            linea.setSpacingAfter(10);
            documento.add(linea);

            // Línea horizontal
            PdfPTable lineaHorizontal = new PdfPTable(1);
            lineaHorizontal.setWidthPercentage(100);
            PdfPCell celdaLinea = new PdfPCell();
            celdaLinea.setFixedHeight(2);
            celdaLinea.setBackgroundColor(colorPrimario);
            celdaLinea.setBorder(Rectangle.NO_BORDER);
            lineaHorizontal.addCell(celdaLinea);
            documento.add(lineaHorizontal);

            // =============== INFORMACIÓN DEL CLIENTE ===============
            Paragraph espaciado = new Paragraph(" ");
            espaciado.setSpacingAfter(15);
            documento.add(espaciado);

            Paragraph tituloCliente = new Paragraph("DATOS DEL CLIENTE", subtituloFont);
            tituloCliente.setSpacingAfter(10);
            documento.add(tituloCliente);

            PdfPTable tablaCliente = new PdfPTable(2);
            tablaCliente.setWidthPercentage(100);
            tablaCliente.setWidths(new float[]{1f, 3f});

            // Estilo para las celdas del cliente
            PdfPCell celdaLabel, celdaValor;

            String[][] datosCliente = {
                {"NOMBRE:", nombreCliente},
                {"RFC:", rfc},
                {"DIRECCIÓN:", direccion},
                {"TELÉFONO:", telefono},
                {"CORREO:", correo}
            };

            for (String[] dato : datosCliente) {
                celdaLabel = new PdfPCell(new Phrase(dato[0], textoBoldFont));
                celdaLabel.setBorder(Rectangle.NO_BORDER);
                celdaLabel.setPadding(5);
                celdaLabel.setBackgroundColor(colorSecundario);

                celdaValor = new PdfPCell(new Phrase(dato[1], textoFont));
                celdaValor.setBorder(Rectangle.NO_BORDER);
                celdaValor.setPadding(5);

                tablaCliente.addCell(celdaLabel);
                tablaCliente.addCell(celdaValor);
            }

            documento.add(tablaCliente);

            // =============== PRODUCTOS ===============
            espaciado.setSpacingAfter(20);
            documento.add(espaciado);

            Paragraph tituloProductos = new Paragraph("DETALLE DE PRODUCTOS", subtituloFont);
            tituloProductos.setSpacingAfter(10);
            documento.add(tituloProductos);

            PdfPTable tablaProductos = new PdfPTable(4);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.setWidths(new float[]{3f, 1f, 1.5f, 1.5f});

            // Encabezados de la tabla
            String[] encabezados = {"PRODUCTO", "CANT.", "PRECIO UNIT.", "TOTAL"};
            for (String encabezadi : encabezados) {
                PdfPCell celda = new PdfPCell(new Phrase(encabezadi, tablaHeaderFont));
                celda.setBackgroundColor(colorPrimario);
                celda.setPadding(8);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaProductos.addCell(celda);
            }

            // Datos de los productos
            DefaultTableModel model = (DefaultTableModel) NuevaFactura.TblVenta.getModel();
            double total = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                String prod = model.getValueAt(i, 0).toString();
                String cant = model.getValueAt(i, 1).toString();
                String precio = "$" + model.getValueAt(i, 2).toString();
                String subtotal = "$" + model.getValueAt(i, 3).toString();
                total += Double.parseDouble(model.getValueAt(i, 3).toString());

                // Alternar colores de fila
                BaseColor colorFila = (i % 2 == 0) ? BaseColor.WHITE : colorSecundario;

                PdfPCell[] celdas = {
                    new PdfPCell(new Phrase(prod, tablaCeldaFont)),
                    new PdfPCell(new Phrase(cant, tablaCeldaFont)),
                    new PdfPCell(new Phrase(precio, tablaCeldaFont)),
                    new PdfPCell(new Phrase(subtotal, tablaCeldaFont))
                };

                for (int j = 0; j < celdas.length; j++) {
                    celdas[j].setBackgroundColor(colorFila);
                    celdas[j].setPadding(6);
                    celdas[j].setBorder(Rectangle.BOTTOM);
                    if (j > 0) {
                        celdas[j].setHorizontalAlignment(Element.ALIGN_CENTER);
                    }
                    tablaProductos.addCell(celdas[j]);
                }
            }

            documento.add(tablaProductos);

            // =============== TOTAL ===============
            espaciado.setSpacingAfter(15);
            documento.add(espaciado);

            PdfPTable tablaTotal = new PdfPTable(2);
            tablaTotal.setWidthPercentage(100);
            tablaTotal.setWidths(new float[]{3f, 1f});

            // Celda vacía
            PdfPCell celdaVacia = new PdfPCell();
            celdaVacia.setBorder(Rectangle.NO_BORDER);

            // Celda del total
            PdfPCell celdaTotal = new PdfPCell();
            celdaTotal.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            celdaTotal.setBorderWidth(2);
            celdaTotal.setBorderColor(colorPrimario);
            celdaTotal.setBackgroundColor(colorSecundario);
            celdaTotal.setPadding(10);

            Paragraph totalTexto = new Paragraph();
            totalTexto.add(new Chunk("TOTAL A PAGAR:\n", textoBoldFont));
            totalTexto.add(new Chunk("$" + String.format("%.2f", total*1.16),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, colorPrimario)));
            totalTexto.setAlignment(Element.ALIGN_CENTER);
            celdaTotal.addElement(totalTexto);

            tablaTotal.addCell(celdaVacia);
            tablaTotal.addCell(celdaTotal);
            documento.add(tablaTotal);

            // =============== PIE DE PÁGINA ===============
            espaciado.setSpacingAfter(30);
            documento.add(espaciado);

            Paragraph piePagina = new Paragraph();
            piePagina.add(new Chunk("¡Gracias por su preferencia!\n", subtituloFont));
            piePagina.add(new Chunk("FarmaCode - Su farmacia de confianza\n", textoFont));
            piePagina.add(new Chunk("www.farmacode.com", textoFont));
            piePagina.setAlignment(Element.ALIGN_CENTER);
            documento.add(piePagina);

            documento.close();

            JOptionPane.showMessageDialog(null, "Factura generada correctamente: " + nombreArchivo);
            return rutaPDF;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void enviarPorWhatsApp(String telefono, String rutaPDF) {
        try {
            String mensaje = "Factura generada. Archivo: " + rutaPDF;
            String numeroFormateado = telefono.replaceAll("[^\\d]", "");
            String url = "https://wa.me/" + numeroFormateado + "?text=" + URLEncoder.encode(mensaje, "UTF-8");
            Desktop.getDesktop().browse(new URI(url));
            JOptionPane.showMessageDialog(null, "Se abrió WhatsApp para enviar la factura.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enviarPorCorreo(String destinatario, String rutaPDF) {
        final String remitente = "ajasas282005@gmail.com";
        final String clave = "gmzp vown ytrs froj"; // usa contraseña de aplicación si es Gmail

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Factura generada");

            MimeBodyPart texto = new MimeBodyPart();
            texto.setText("Adjunto se encuentra la factura generada.");

            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.attachFile(new File(rutaPDF));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);

            message.setContent(multipart);

            Transport.send(message);
            JOptionPane.showMessageDialog(null, "Correo enviado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
