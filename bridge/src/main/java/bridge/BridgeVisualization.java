package bridge;

import bridge.ui.Car;
import bridge.ui.TrafficLight;
import de.prob2.ui.visualisation.fx.Visualisation;
import de.prob2.ui.visualisation.fx.listener.FormulaListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * @author Christoph Heinzen
 * @version 0.1.0
 * @since 02.10.17
 */
public class BridgeVisualization extends Visualisation{

    private static final int INITIAL_WIDTH = 765;
    private static final int INITIAL_HEIGHT = 555;
    private static final double STROKE_WIDTH = 1.5;

    private Group root;

    private Car[] toIslandCars = new Car[3];
    private Car[] fromIslandCars = new Car[3];
    private Car[] islandCars = new Car[3];
    private Car[] mainlandCars = new Car[3];
    private TrafficLight mainlandTrafficLight;
    private TrafficLight islandTrafficLight;
    private Text textN;
    private Text textA;
    private Text textB;
    private Text textC;
    private Car ilInSensorCar;
    private Car ilOutSensorCar;
    private Car mlInSensorCar;
    private Car mlOutSensorCar;

    @Override
    protected String getName() {
        return "Bridge Visualization";
    }

    @Override
    protected String[] getMachines() {
        return new String[0];
    }

    @Override
    protected Node initialize() {

        AnchorPane pane = new AnchorPane();
        pane.setMinWidth(100);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null ,null)));

        root = new Group();

        pane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
               scaleVisualization(newValue.doubleValue());
            }
        });

        Rectangle sea = new Rectangle(0, 0, 765, 555);
        sea.setFill(Color.DARKBLUE);

        Rectangle bridge = new Rectangle(190, 197.5, 410,60);
        bridge.setFill(Color.WHITE);
        bridge.setStroke(Color.BLACK);
        bridge.setStrokeWidth(STROKE_WIDTH);

        SVGPath mainland = new SVGPath();
        mainland.setContent("M 765,0 L 700,0 A 250 319 0 0 0 700,555 L 765,555");
        mainland.setStroke(Color.BLACK);
        mainland.setStrokeWidth(STROKE_WIDTH);
        mainland.setFill(Color.WHITE);

        Ellipse island = new Ellipse(100, 227.5, 100, 126);
        island.setFill(Color.WHITE);
        island.setStroke(Color.BLACK);
        island.setStrokeWidth(STROKE_WIDTH);

        mainlandTrafficLight = new TrafficLight(540, 107.5);
        mainlandTrafficLight.setOpacity((model.getValue("ml_tl") != null ? 1 : 0));
        islandTrafficLight = new TrafficLight(200, 267.5);
        islandTrafficLight.setOpacity((model.getValue("il_tl") != null ? 1 : 0));

        textN = new Text(20,40, "n = 0");
        textN.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        textN.setFill(Color.WHITE);
        textA = new Text(200,187.5, "a = 0");
        textA.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        textA.setFill(Color.WHITE);
        textA.setOpacity((model.getValue("a") != null ? 1 : 0));
        textB = new Text(65,135, "b = 0");
        textB.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        textB.setOpacity((model.getValue("b") != null ? 1 : 0));
        textC = new Text(495,285.5, "c = 0");
        textC.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        textC.setFill(Color.WHITE);
        textC.setOpacity((model.getValue("c") != null ? 1 : 0));

        root.getChildren().addAll(sea, bridge, island, mainland, mainlandTrafficLight, islandTrafficLight, textA, textB, textC, textN);

        if (model.getValue("il_in_sr") != null) {
            Rectangle mlInSensor = new Rectangle(550, 217.5, 20, 20);
            mlInSensor.setFill(Color.BLACK);
            Rectangle mlOutSensor = new Rectangle(585, 217.5, 20, 20);
            mlOutSensor.setFill(Color.BLACK);
            Rectangle ilInSensor = new Rectangle(207.5, 217.5, 20, 20);
            ilInSensor.setFill(Color.BLACK);
            Rectangle ilOutSensor = new Rectangle(172.5, 217.5, 20, 20);
            ilOutSensor.setFill(Color.BLACK);
            root.getChildren().addAll(mlInSensor, mlOutSensor, ilInSensor, ilOutSensor);

            ilInSensorCar = new Car(207.5,207.5);
            ilInSensorCar.setOpacity(0);
            ilOutSensorCar = new Car(110, 207.5);
            ilOutSensorCar.setScaleX(-1 * ilOutSensorCar.getScaleX());
            ilOutSensorCar.setOpacity(0);
            mlInSensorCar = new Car(490,207.5);
            mlInSensorCar.setScaleX(-1 * mlInSensorCar.getScaleX());
            mlInSensorCar.setOpacity(0);
            mlOutSensorCar = new Car(580, 207.5);
            mlOutSensorCar.setOpacity(0);
            root.getChildren().addAll(ilInSensorCar, ilOutSensorCar, mlInSensorCar, mlOutSensorCar);

        }

        for (int i = 0; i < 3; i++) {
            toIslandCars[i] = new Car(292.5 + (i * 85), 207.5);
            toIslandCars[i].setOpacity(0);
            fromIslandCars[i] = new Car(405 - (i * 85), 207.5);
            fromIslandCars[i].setScaleX(-1 * fromIslandCars[i].getScaleX());
            fromIslandCars[i].setOpacity(0);
            islandCars[i] = new Car(20, 162.5 + (i * 45));
            islandCars[i].setOpacity(0);
            mainlandCars[i] = new Car(670, 162.5 + (i * 45));
        }

        root.getChildren().addAll(toIslandCars);
        root.getChildren().addAll(fromIslandCars);
        root.getChildren().addAll(islandCars);
        root.getChildren().addAll(mainlandCars);

        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        pane.getChildren().add(root);

        return pane;
    }

    @Override
    protected void stop() {}

    @Override
    protected void registerFormulaListener() {
        registerFormulaListener(new FormulaListener("n", "b", "il_out_sr", "ml_out_sr") {
            @Override
            public void variablesChanged(Object[] newValues) throws Exception {
                Boolean mlOutSensor = translateToBool(newValues[3]);
                mlOutSensor = (mlOutSensor != null && mlOutSensor);
                for (int i = 0; i < 3; i++) {
                    islandCars[i].setOpacity(0);
                    mainlandCars[i].setOpacity(0);
                }
                Boolean ilOutSensor = translateToBool(newValues[2]);
                Integer n = translateToInt(newValues[0]);
                textN.setText("n = " + n);
                for (int i = 0; i < (3 - n - (mlOutSensor ? 1 : 0)); i++) {
                    mainlandCars[i].setOpacity(1);
                }
                mlOutSensorCar.setOpacity((mlOutSensor ? 1 : 0));
                Integer b = translateToInt(newValues[1]);
                if (b != null) {
                    textB.setOpacity(1);
                    textB.setText("b = " + b);
                    if (ilOutSensor != null && ilOutSensor) {
                        for (int i = 0; i < b-1; i++) {
                            islandCars[i].setOpacity(1);
                        }
                        ilOutSensorCar.setOpacity(1);
                    } else {
                        for (int i = 0; i < b; i++) {
                            islandCars[i].setOpacity(1);
                        }
                        ilOutSensorCar.setOpacity(0);
                    }
                } else {
                    for (int i = 0; i < n; i++) {
                        islandCars[i].setOpacity(1);
                    }
                }
            }
        });

        registerFormulaListener(new FormulaListener("a", "il_in_sr") {
            @Override
            public void variablesChanged(Object[] newValues) throws Exception {
                for (Car toIslandCar : toIslandCars) {
                    toIslandCar.setOpacity(0);
                }
                Integer a = translateToInt(newValues[0]);
                Boolean sensor = translateToBool(newValues[1]);
                if (a != null) {
                    textA.setOpacity(1);
                    textA.setText("a = " + a);
                    if (sensor != null && sensor) {
                        for (int i = 0; i < a - 1; i++) {
                            toIslandCars[i].setOpacity(1);
                        }
                        ilInSensorCar.setOpacity(1);
                    } else {
                        for (int i = 0; i < a; i++) {
                            toIslandCars[i].setOpacity(1);
                        }
                        ilInSensorCar.setOpacity(0);
                    }
                }
            }
        });

        registerFormulaListener(new FormulaListener("c", "ml_in_sr") {
            @Override
            public void variablesChanged(Object[] newValues) throws Exception {
                for (Car fromIslandCar : fromIslandCars) {
                    fromIslandCar.setOpacity(0);
                }
                Boolean sensor = translateToBool(newValues[1]);
                Integer c = translateToInt(newValues[0]);
                if (c != null) {
                    textC.setOpacity(1);
                    textC.setText("c = " + c);
                    if (sensor != null && sensor) {
                        for (int i = 0; i < c - 1; i++) {
                            fromIslandCars[i].setOpacity(1);
                        }
                        mlInSensorCar.setOpacity(1);
                    } else {
                        for (int i = 0; i < c; i++) {
                            fromIslandCars[i].setOpacity(1);
                        }
                        mlInSensorCar.setOpacity(0);
                    }
                }
            }
        });

        registerFormulaListener(new FormulaListener("ml_tl") {
            @Override
            public void variablesChanged(Object[] newValues) throws Exception {
                if (newValues[0] != null) {
                    mainlandTrafficLight.setOpacity(1);
                    mainlandTrafficLight.setGreen(newValues[0].toString().equals("green"));
                }
            }
        });

        registerFormulaListener(new FormulaListener("il_tl") {
            @Override
            public void variablesChanged(Object[] newValues) throws Exception {
                if (newValues[0] != null) {
                    islandTrafficLight.setOpacity(1);
                    islandTrafficLight.setGreen(newValues[0].toString().equals("green"));
                }
            }
        });

    }

    @Override
    protected void registerEventListener() {}

    private void scaleVisualization(double width) {
        double scale = width / INITIAL_WIDTH;
        root.setScaleX(scale);
        root.setScaleY(scale);

        double translateX = ((INITIAL_WIDTH * scale) - INITIAL_WIDTH) / 2.0;
        double translateY = ((INITIAL_HEIGHT * scale) - INITIAL_HEIGHT) / 2.0;
        root.setTranslateX(translateX);
        root.setTranslateY(translateY);
    }
}
