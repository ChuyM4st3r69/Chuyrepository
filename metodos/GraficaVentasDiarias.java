/*package metodos;

import farmacia.Farmacia;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.NumberAxis;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GraficaVentasDiarias {

    public static JPanel generarGraficaVentasLinealCompleta() {
        // 1. Crear dataset con los datos de ventas
        XYSeriesCollection dataset = obtenerDatosVentasDiariasLineal();

        // 2. Crear el gráfico lineal CON TÍTULO
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Ventas por Hora - " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                "Hora del día",
                "Monto ($)",
                dataset,
                PlotOrientation.VERTICAL,
                true, // leyenda
                true, // tooltips
                false // URLs
        );

        // 3. Personalización avanzada del gráfico
        personalizarGrafica(chart);

        // 4. Crear el panel del gráfico
        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }

    private static void personalizarGrafica(JFreeChart chart) {
        // Obtener el área de dibujo (plot)
        XYPlot plot = chart.getXYPlot();

        // A. Personalizar el renderizador (líneas y puntos)
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // 1. Configurar la línea
        renderer.setSeriesPaint(0, new Color(59, 89, 182));
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(0, true);

        // 2. Configurar los puntos
        renderer.setSeriesShape(0, new Ellipse2D.Double(-4, -4, 8, 8));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setSeriesOutlinePaint(0, new Color(59, 89, 182));

        plot.setRenderer(renderer);

        // B. Personalizar el fondo
        plot.setBackgroundPaint(new Color(240, 240, 240));
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // C. Personalizar los ejes
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setRange(0, 24);
        domainAxis.setTickMarkPaint(Color.DARK_GRAY);
        domainAxis.setAxisLinePaint(new Color(150, 150, 150));

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setTickMarkPaint(Color.DARK_GRAY);
        rangeAxis.setAxisLinePaint(new Color(150, 150, 150));

        // D. Personalizar el título (SOLO SI EXISTE)
        if(chart.getTitle() != null) {
            chart.getTitle().setPaint(new Color(70, 70, 70));
            chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        }

        // E. Personalizar leyenda (SOLO SI EXISTE)
        if(chart.getLegend() != null) {
            chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));
        }
    }

    private static XYSeriesCollection obtenerDatosVentasDiariasLineal() {
        XYSeries series = new XYSeries("Ventas");

        try (Connection conn = Farmacia.ConectarBD()) {
            String sql = "SELECT HOUR(f.fecha) as hora, "
                    + "ROUND(SUM(df.cantidad * df.precio_unitario), 2) as total "
                    + "FROM facturas f "
                    + "JOIN detalle_facturas df ON f.idfacturas = df.facturas_idfacturas "
                    + "WHERE DATE(f.fecha) = CURDATE() "
                    + "GROUP BY HOUR(f.fecha) "
                    + "ORDER BY hora";

            try (PreparedStatement stmt = conn.prepareStatement(sql); 
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int hora = rs.getInt("hora");
                    double total = rs.getDouble("total");
                    series.add(hora, total);
                }

                if (series.getItemCount() == 0) {
                    series.add(12, 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al obtener datos de ventas:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    public static void actualizarGraficaEnLabel(JLabel label) {
        // Crear gráfica con título para el JLabel también
        JFreeChart chart = crearGraficaParaLabel();
        BufferedImage image = chart.createBufferedImage(
                label.getWidth(),
                label.getHeight(),
                BufferedImage.TYPE_INT_RGB,
                null
        );
        label.setIcon(new ImageIcon(image));
        label.revalidate();
        label.repaint();
    }

    private static JFreeChart crearGraficaParaLabel() {
        XYSeriesCollection dataset = obtenerDatosVentasDiariasLineal();
        // Ahora con título para evitar el error
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Ventas por Hora", // Título para el JLabel
                null, // Sin etiqueta eje X
                null, // Sin etiqueta eje Y
                dataset,
                PlotOrientation.VERTICAL,
                true, // leyenda
                true, // tooltips
                false // URLs
        );
        personalizarGrafica(chart);
        return chart;
    }
}*/