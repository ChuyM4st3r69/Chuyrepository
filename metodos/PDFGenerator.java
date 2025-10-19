package metodos;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {

    private static final DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");

    /**
     * Método mejorado para generar tickets PDF con diseño profesional
     */
    public static void generarFacturaPDFSeguro(
            String rutaPDF,
            String[] productos,
            int[] cantidades,
            double[] preciosUnitarios,
            double[] subtotales,
            String nombreVendedor,
            String puestoVendedor,
            double subtotalGeneral,
            double iva,
            double totalGeneral,
            int numeroFactura,
            int porcentajeIva) {

        try {
            // Crear documento con tamaño A4
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(rutaPDF));
            document.open();

            // =============== DEFINIR COLORES Y FUENTES ===============
            BaseColor colorPrimario = new BaseColor(70, 130, 180); // Azul corporativo
            BaseColor colorSecundario = new BaseColor(240, 248, 255); // Azul claro
            BaseColor colorTexto = new BaseColor(51, 51, 51); // Gris oscuro
            BaseColor colorVerde = new BaseColor(46, 125, 50); // Verde para totales

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, colorPrimario);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, colorPrimario);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 11, colorTexto);
            Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, colorTexto);
            Font fontContacto = FontFactory.getFont(FontFactory.HELVETICA, 10, colorTexto);
            Font fontTablaHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font fontTabla = FontFactory.getFont(FontFactory.HELVETICA, 10, colorTexto);
            Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, colorVerde);
            Font fontPequena = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);

            // =============== ENCABEZADO CON LOGO ===============
            try {
                String logoPath = "src/icons/Logo.png";
                Image logo = Image.getInstance(logoPath);
                logo.scaleToFit(120, 120);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } catch (Exception e) {
                // Si no se encuentra el logo, usar texto estilizado
                Paragraph logoTexto = new Paragraph("💊", FontFactory.getFont(FontFactory.HELVETICA, 40, colorPrimario));
                logoTexto.setAlignment(Element.ALIGN_CENTER);
                logoTexto.setSpacingAfter(10);
                document.add(logoTexto);
            }

            // Título principal
            Paragraph titulo = new Paragraph("FarmaCode", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(5);
            document.add(titulo);

            // Subtítulo del ticket
            Paragraph subtitulo = new Paragraph("TICKET DE VENTA", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(8);
            document.add(subtitulo);

            // Información de contacto
            Paragraph contacto = new Paragraph("📞 Teléfono: 669 932 8266", fontContacto);
            contacto.setAlignment(Element.ALIGN_CENTER);
            contacto.setSpacingAfter(15);
            document.add(contacto);

            // =============== LÍNEA SEPARADORA ===============
            PdfPTable lineaSeparadora = new PdfPTable(1);
            lineaSeparadora.setWidthPercentage(100);
            PdfPCell celdaLinea = new PdfPCell();
            celdaLinea.setFixedHeight(2);
            celdaLinea.setBackgroundColor(colorPrimario);
            celdaLinea.setBorder(Rectangle.NO_BORDER);
            lineaSeparadora.addCell(celdaLinea);
            document.add(lineaSeparadora);

            // Espaciado
            document.add(new Paragraph(" ", fontNormal));

            // =============== INFORMACIÓN DEL TICKET ===============
            String fechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    .format(LocalDateTime.now());

            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setWidths(new float[]{1.2f, 2f});

            String[][] infoTicket = {
                {"TICKET No:", String.format("TKT-%06d", numeroFactura)},
                {"FECHA:", fechaHora.split(" ")[0]},
                {"HORA:", fechaHora.split(" ")[1]},
                {"VENDEDOR:", nombreVendedor},
                {"PUESTO:", puestoVendedor}
            };

            for (String[] info : infoTicket) {
                // Celda etiqueta
                PdfPCell celdaEtiqueta = new PdfPCell(new Phrase(info[0], fontBold));
                celdaEtiqueta.setBorder(Rectangle.NO_BORDER);
                celdaEtiqueta.setPadding(4);
                celdaEtiqueta.setBackgroundColor(colorSecundario);

                // Celda valor
                PdfPCell celdaValor = new PdfPCell(new Phrase(info[1], fontNormal));
                celdaValor.setBorder(Rectangle.NO_BORDER);
                celdaValor.setPadding(4);

                tablaInfo.addCell(celdaEtiqueta);
                tablaInfo.addCell(celdaValor);
            }

            document.add(tablaInfo);

            // =============== ESPACIADO ===============
            Paragraph espacio = new Paragraph(" ");
            espacio.setSpacingAfter(15);
            document.add(espacio);

            // =============== TÍTULO DE PRODUCTOS ===============
            Paragraph tituloProductos = new Paragraph("DETALLE DE PRODUCTOS", fontSubtitulo);
            tituloProductos.setSpacingAfter(10);
            document.add(tituloProductos);

            // =============== TABLA DE PRODUCTOS ===============
            PdfPTable tablaProductos = new PdfPTable(4);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.setSpacingBefore(5);
            tablaProductos.setSpacingAfter(15);

            // Anchos de columnas optimizados
            float[] anchos = {3.5f, 1f, 1.5f, 1.5f};
            tablaProductos.setWidths(anchos);

            // Encabezados de la tabla con estilo
            String[] encabezados = {"PRODUCTO", "CANT.", "PRECIO UNIT.", "SUBTOTAL"};
            for (String encabezado : encabezados) {
                PdfPCell celda = new PdfPCell(new Phrase(encabezado, fontTablaHeader));
                celda.setBackgroundColor(colorPrimario);
                celda.setPadding(8);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(Rectangle.NO_BORDER);
                tablaProductos.addCell(celda);
            }

            // Datos de productos con filas alternadas
            for (int i = 0; i < productos.length; i++) {
                BaseColor colorFila = (i % 2 == 0) ? BaseColor.WHITE : colorSecundario;

                // Producto
                PdfPCell celdaProducto = new PdfPCell(new Phrase(productos[i], fontTabla));
                celdaProducto.setBackgroundColor(colorFila);
                celdaProducto.setPadding(6);
                celdaProducto.setBorder(Rectangle.BOTTOM);
                celdaProducto.setBorderColor(BaseColor.LIGHT_GRAY);

                // Cantidad
                PdfPCell celdaCantidad = new PdfPCell(new Phrase(String.valueOf(cantidades[i]), fontTabla));
                celdaCantidad.setBackgroundColor(colorFila);
                celdaCantidad.setPadding(6);
                celdaCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaCantidad.setBorder(Rectangle.BOTTOM);
                celdaCantidad.setBorderColor(BaseColor.LIGHT_GRAY);

                // Precio unitario
                PdfPCell celdaPrecio = new PdfPCell(new Phrase(formatoMoneda.format(preciosUnitarios[i]), fontTabla));
                celdaPrecio.setBackgroundColor(colorFila);
                celdaPrecio.setPadding(6);
                celdaPrecio.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaPrecio.setBorder(Rectangle.BOTTOM);
                celdaPrecio.setBorderColor(BaseColor.LIGHT_GRAY);

                // Subtotal
                PdfPCell celdaSubtotal = new PdfPCell(new Phrase(formatoMoneda.format(subtotales[i]), fontTabla));
                celdaSubtotal.setBackgroundColor(colorFila);
                celdaSubtotal.setPadding(6);
                celdaSubtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaSubtotal.setBorder(Rectangle.BOTTOM);
                celdaSubtotal.setBorderColor(BaseColor.LIGHT_GRAY);

                tablaProductos.addCell(celdaProducto);
                tablaProductos.addCell(celdaCantidad);
                tablaProductos.addCell(celdaPrecio);
                tablaProductos.addCell(celdaSubtotal);
            }

            document.add(tablaProductos);

            // =============== RESUMEN DE TOTALES ===============
            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(100);
            tablaTotales.setWidths(new float[]{3f, 1f});

            // Celda vacía para alineación
            PdfPCell celdaVacia = new PdfPCell();
            celdaVacia.setBorder(Rectangle.NO_BORDER);

            // Contenedor de totales
            PdfPCell celdaTotales = new PdfPCell();
            celdaTotales.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            celdaTotales.setBorderWidth(1);
            celdaTotales.setBorderColor(colorPrimario);
            celdaTotales.setBackgroundColor(colorSecundario);
            celdaTotales.setPadding(10);

            // Crear contenido de totales
            Paragraph contenidoTotales = new Paragraph();

            Chunk subtotalChunk = new Chunk("Subtotal: ", fontBold);
            Chunk subtotalValor = new Chunk(formatoMoneda.format(subtotalGeneral) + "\n", fontNormal);

            Chunk ivaChunk = new Chunk("IVA (" + porcentajeIva + "%): ", fontBold);
            Chunk ivaValor = new Chunk(formatoMoneda.format(iva) + "\n", fontNormal);

            Chunk totalChunk = new Chunk("TOTAL: ", fontBold);
            Chunk totalValor = new Chunk(formatoMoneda.format(totalGeneral), fontTotal);

            contenidoTotales.add(subtotalChunk);
            contenidoTotales.add(subtotalValor);
            contenidoTotales.add(ivaChunk);
            contenidoTotales.add(ivaValor);
            contenidoTotales.add(new Chunk("________________\n", fontNormal));
            contenidoTotales.add(totalChunk);
            contenidoTotales.add(totalValor);
            contenidoTotales.setAlignment(Element.ALIGN_RIGHT);

            celdaTotales.addElement(contenidoTotales);

            tablaTotales.addCell(celdaVacia);
            tablaTotales.addCell(celdaTotales);
            document.add(tablaTotales);

            // =============== PIE DE PÁGINA ===============
            espacio.setSpacingAfter(25);
            document.add(espacio);

            // Mensaje de agradecimiento
            Paragraph agradecimiento = new Paragraph("¡Gracias por su compra!", fontSubtitulo);
            agradecimiento.setAlignment(Element.ALIGN_CENTER);
            agradecimiento.setSpacingAfter(8);
            document.add(agradecimiento);

            // =============== INFORMACIÓN DE FACTURACIÓN ===============
            // Marco compacto para la información de facturación
            PdfPTable tablaFacturacion = new PdfPTable(1);
            tablaFacturacion.setWidthPercentage(75);
            tablaFacturacion.setSpacingBefore(8);
            tablaFacturacion.setSpacingAfter(10);

            PdfPCell celdaFacturacion = new PdfPCell();
            celdaFacturacion.setBorder(Rectangle.BOX);
            celdaFacturacion.setBorderColor(new BaseColor(255, 152, 0)); // Color naranja para destacar
            celdaFacturacion.setBorderWidth(1f);
            celdaFacturacion.setBackgroundColor(new BaseColor(255, 248, 225)); // Fondo amarillo claro
            celdaFacturacion.setPadding(8);

            Paragraph infoFacturacion = new Paragraph();
            infoFacturacion.add(new Chunk("⚠️ FACTURACIÓN: ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, new BaseColor(255, 87, 34))));
            infoFacturacion.add(new Chunk("24 HORAS ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, new BaseColor(255, 87, 34))));
            infoFacturacion.add(new Chunk("para solicitar factura fiscal\n",
                    FontFactory.getFont(FontFactory.HELVETICA, 8, colorTexto)));
            infoFacturacion.add(new Chunk("📧 facturacion@farmacode.com | 📞 669 932 8266",
                    FontFactory.getFont(FontFactory.HELVETICA, 8, colorTexto)));
            infoFacturacion.setAlignment(Element.ALIGN_CENTER);

            celdaFacturacion.addElement(infoFacturacion);
            tablaFacturacion.addCell(celdaFacturacion);
            document.add(tablaFacturacion);

            // Información adicional
            Paragraph infoComprobante = new Paragraph("Este documento es su comprobante de compra", fontPequena);
            infoComprobante.setAlignment(Element.ALIGN_CENTER);
            infoComprobante.setSpacingAfter(5);
            document.add(infoComprobante);

            // Información corporativa
            Paragraph infoCorporativa = new Paragraph("FarmaCode - Su farmacia de confianza", fontContacto);
            infoCorporativa.setAlignment(Element.ALIGN_CENTER);
            document.add(infoCorporativa);

            document.close();
            System.out.println("Ticket PDF generado exitosamente: " + rutaPDF);

        } catch (Exception e) {
            System.err.println("Error al generar PDF:");
            e.printStackTrace();
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Método helper para agregar celdas de encabezado (mantenido para
     * compatibilidad)
     */
    private static void agregarCeldaEncabezado(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setBackgroundColor(new BaseColor(70, 130, 180));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(8);
        celda.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celda);
    }

    /**
     * Método helper para agregar celdas de texto (mantenido para
     * compatibilidad)
     */
    private static void agregarCeldaTexto(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setPadding(6);
        celda.setBorder(Rectangle.BOTTOM);
        celda.setBorderColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(celda);
    }

    /**
     * Método original mantenido para compatibilidad (DEPRECATED)
     *
     * @deprecated Usar generarFacturaPDFSeguro en su lugar
     */
    @Deprecated
    public static void generarFacturaPDF(
            String rutaPDF,
            javax.swing.JTable tabla,
            String nombreVendedor,
            String puestoVendedor,
            double subtotal,
            double iva,
            double total,
            int numeroFactura,
            int porcentajeIva) {

        System.out.println(" ADVERTENCIA: Usando método deprecated generarFacturaPDF");

        // Convertir datos de la tabla a arrays de manera segura
        int numFilas = tabla.getRowCount();
        String[] productos = new String[numFilas];
        int[] cantidades = new int[numFilas];
        double[] preciosUnitarios = new double[numFilas];
        double[] subtotales = new double[numFilas];

        for (int i = 0; i < numFilas; i++) {
            // Producto
            productos[i] = tabla.getValueAt(i, 0) != null ? tabla.getValueAt(i, 0).toString() : "Producto sin nombre";

            // Cantidad (manejo seguro)
            Object cantidadObj = tabla.getValueAt(i, 1);
            if (cantidadObj instanceof Integer) {
                cantidades[i] = (Integer) cantidadObj;
            } else if (cantidadObj instanceof String) {
                try {
                    cantidades[i] = Integer.parseInt(cantidadObj.toString().trim());
                } catch (NumberFormatException e) {
                    cantidades[i] = 1;
                    System.err.println("Cantidad inválida en fila " + i + ", usando 1 por defecto");
                }
            } else {
                cantidades[i] = 1;
            }

            // Precio unitario (manejo seguro)
            Object precioObj = tabla.getValueAt(i, 2);
            if (precioObj != null) {
                String precioStr = precioObj.toString().replace("$", "").replace(",", "").trim();
                try {
                    preciosUnitarios[i] = Double.parseDouble(precioStr);
                } catch (NumberFormatException e) {
                    preciosUnitarios[i] = 0.0;
                    System.err.println("Precio inválido en fila " + i + ", usando 0.0 por defecto");
                }
            } else {
                preciosUnitarios[i] = 0.0;
            }

            // Subtotal (manejo seguro)
            Object subtotalObj = tabla.getValueAt(i, 3);
            if (subtotalObj != null) {
                String subtotalStr = subtotalObj.toString().replace("$", "").replace(",", "").trim();
                try {
                    subtotales[i] = Double.parseDouble(subtotalStr);
                } catch (NumberFormatException e) {
                    subtotales[i] = cantidades[i] * preciosUnitarios[i];
                    System.err.println("Subtotal inválido en fila " + i + ", calculando automáticamente");
                }
            } else {
                subtotales[i] = cantidades[i] * preciosUnitarios[i];
            }
        }

        // Llamar al método mejorado
        generarFacturaPDFSeguro(
                rutaPDF,
                productos,
                cantidades,
                preciosUnitarios,
                subtotales,
                nombreVendedor,
                puestoVendedor,
                subtotal,
                iva,
                total,
                numeroFactura,
                porcentajeIva
        );
    }
}
