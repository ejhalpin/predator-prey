/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life;


import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 *
 * @author Eugio
 */
public class Life extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        control = primaryStage;
        control.setOnCloseRequest((new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent we) {
                we.consume();
                Platform.exit();
            }
        }));
        
        initComponents();
        control.setTitle("PredPrey Simulation");
        control.setScene(advScene);
        control.setX(X);
        control.setY(Y);
        //advScene.getStylesheets().add(stylesheetURL);
        
        control.show();
    }
    private void validate(){
        x = Integer.parseInt(xField.getText());
        y = Integer.parseInt(yField.getText());
        flora = Double.parseDouble(floraMultiplier.getEditor().getText());
        t = Integer.parseInt(tField.getText());
        npred = Integer.parseInt(predField.getText());
        nprey = Integer.parseInt(preyField.getText());
        predHealth = Double.parseDouble(healthField.getText());
        preyHealth = Double.parseDouble(preyHealthField.getText());
        hunt = Double.parseDouble(huntField.getText());
        predDecay =Double.parseDouble(predDecayField.getText());
        preyDecay = Double.parseDouble(preyDecayField.getText());
        breed = Double.parseDouble(breedField.getText());
        predMaxAge = Integer.parseInt(predMaxAgeField.getText());
        preyMaxAge = Integer.parseInt(preyMaxAgeField.getText());
        maxHealth = Double.parseDouble(maxHealthField.getText());
        area = x*y;
        population = npred+nprey;
       
    }
    private void initComponents(){
        //stylesheetURL =getClass().getResource("resources/style.css").toString();
        
        mainPane = new BorderPane();
        advPane = new GridPane();
        advPane.setPadding(new Insets(0,5,0,5));
        advPane.setVgap(5);
        advPane.setHgap(5);
        
        x = 50;
        y = 50;
        flora = 20d;
        t = 1000;
        
        predHealth = 10.0;
        preyHealth = 10.0;
        maxHealth = 15.0;
        hunt = 0.20;
        predDecay = 2.0;
        preyDecay = 4.0;
        breed = 0.5;
        predMaxAge = 10;
        preyMaxAge = 10;
        area = x*y;
        npred = (int)Math.round(area/4);
        nprey = (int)Math.round(area/4);
        population = npred+nprey;
        
        //<editor-fold defaultstate="collapsed" desc="environemnt properties layout">
        VBox eBox = new VBox();
        eBox.setAlignment(Pos.CENTER);
        eBox.getChildren().add(new Label("Environment"));
        Separator s = new Separator();
        s.setPrefSize(105.0, 10.0);
        eBox.getChildren().add(s);
        
        
        xSlider = new Slider();
        xSlider.setOrientation(Orientation.VERTICAL);
        xSlider.setPrefHeight(228);
        xSlider.setMin(0d);
        xSlider.setMax(100d);
        xSlider.setValue(50d);
        xSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                x = (int)Math.round(xSlider.getValue());
                area = x*y;
                String val = Integer.toString(x);
                xField.setText(val);
                predSlider.setMax(Double.valueOf(area));
                preySlider.setMax(Double.valueOf(area));
                predSlider.setValue(Double.valueOf(area/4));
                preySlider.setValue(Double.valueOf(area/4));
            }
        });
        
        xField = new TextField("50");
        xField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                int val = 0;
                try{
                    val = Integer.parseInt(xField.getText());
                }
                catch(NumberFormatException nfe){
                    xField.setText(Integer.toString(x));
                    return;
                }
                xSlider.setValue(Double.valueOf(val));
                x = val;
            }
        });
        xField.setPadding(new Insets(5,1,5,1));
        xField.setAlignment(Pos.CENTER);
        xField.setPrefSize(50.0,25.0);
        Label xlabel = new Label("cols");
        HBox box0 = new HBox();
        box0.setPrefHeight(23.0);
        box0.setAlignment(Pos.CENTER);
        box0.getChildren().add(xlabel);
        VBox xBox = new VBox();
        xBox.setAlignment(Pos.CENTER);
        xBox.getChildren().add(box0);
        xBox.getChildren().add(xSlider);
        xBox.getChildren().add(xField);
        xBox.setPadding(new Insets(0,5,0,0));
        
        
        ySlider = new Slider();
        ySlider.setOrientation(Orientation.VERTICAL);
        ySlider.setPrefHeight(228);
        ySlider.setMin(0d);
        ySlider.setMax(100d);
        ySlider.setValue(50d);
        ySlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                y = (int)Math.round(ySlider.getValue());
                area = x*y;
                String val = Integer.toString(y);
                yField.setText(val);
                predSlider.setMax(Double.valueOf(area));
                preySlider.setMax(Double.valueOf(area));
                predSlider.setValue(Double.valueOf(area/4));
                preySlider.setValue(Double.valueOf(area/4));
            }
            
        });
        
        yField = new TextField("50");
        yField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                int val = 0;
                try{
                    val = Integer.parseInt(yField.getText());
                }
                catch(NumberFormatException nfe){
                    yField.setText(Integer.toString(y));
                    return;
                }
                ySlider.setValue(Double.valueOf(val));
                y = val;
            }
        });
        yField.setPadding(new Insets(5,1,5,1));
        yField.setAlignment(Pos.CENTER);
        yField.setPrefSize(50.0, 25.0);
        Label ylabel = new Label("rows");
        HBox box1 = new HBox();
        box1.setAlignment(Pos.CENTER);
        box1.setPrefHeight(23.0);
        box1.getChildren().add(ylabel);
        VBox yBox = new VBox();
        yBox.setAlignment(Pos.CENTER);
        yBox.getChildren().add(box1);
        yBox.getChildren().add(ySlider);
        yBox.getChildren().add(yField);
        
        
        
        VBox box2 = new VBox();
        box2.setAlignment(Pos.CENTER);
        Separator s0 = new Separator();
        s0.setPrefSize(105.0, 20.0);
        Label rlabel = new Label("Flora Multiplier");
        box2.getChildren().add(s0);
        box2.getChildren().add(rlabel);
        floraMultiplier = new Spinner(new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 50.0, 20.0, 0.1));
        floraMultiplier.setEditable(true);
        floraMultiplier.getEditor().setPadding(new Insets(5,1,5,1));
        floraMultiplier.getEditor().setAlignment(Pos.CENTER);
        floraMultiplier.getEditor().setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                double val = 0d;
                try{
                    val = Double.parseDouble(floraMultiplier.getEditor().getText());
                }
                catch(NumberFormatException nfe){
                    floraMultiplier.getValueFactory().setValue(flora);
                    return;
                }
                if(val<0){
                    floraMultiplier.getValueFactory().setValue(0d);
                    return;
                }
                if(val > 50){
                    
                    floraMultiplier.getValueFactory().setValue(50d);
                    return;
                }
                floraMultiplier.getValueFactory().setValue(val);
                
            }
        });
        floraMultiplier.setPrefWidth(105.0);
        box2.getChildren().add(floraMultiplier);
        Separator s1 = new Separator();
        s1.setPrefSize(105.0, 20.0);
        box2.getChildren().add(s1);
        HBox tBox = new HBox();
        tBox.setAlignment(Pos.CENTER);
        Label tlabel = new Label("Time:  ");
        tField = new TextField("1000");
        tField.setPadding(new Insets(5,1,5,1));
        tField.setAlignment(Pos.CENTER);
        tField.setPrefSize(50.0, 25.0);
        tField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                int val = 0;
                try{
                    val = Integer.parseInt(tField.getText());
                }
                catch(NumberFormatException nfe){
                    tField.setText(Integer.toString(t));
                    return;
                }
                t = val;
            }
        });
        tBox.getChildren().add(tlabel);
        tBox.getChildren().add(tField);
        box2.getChildren().add(tBox);
       
        
        
        
        HBox gBox = new HBox();
        gBox.setAlignment(Pos.CENTER);
        gBox.getChildren().add(xBox);
        gBox.getChildren().add(yBox);
        
        col0 = new VBox();
        col0.setAlignment(Pos.CENTER);
        col0.setPadding(new Insets(10,0,0,0));
        col0.getChildren().add(eBox);
        col0.getChildren().add(gBox);
        col0.getChildren().add(box2);
        
        
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="predator properties layout">
        col1 = new VBox();
        col1.setAlignment(Pos.CENTER);
        
        col1.setPadding(new Insets(10,0,0,0));
        
        VBox pbox0 = new VBox();
        pbox0.setAlignment(Pos.CENTER);
        pbox0.getChildren().add(new Label("Predator Properties"));
        Separator ps0 = new Separator();
        ps0.setPrefSize(425.0,10.0);
        pbox0.getChildren().add(ps0);
        col1.getChildren().add(pbox0);
        HBox pbox1 = new HBox();
        pbox1.setAlignment(Pos.CENTER);
        HBox lb0 = new HBox();
        lb0.setAlignment(Pos.CENTER_RIGHT);
        lb0.setPrefWidth(125);
        lb0.getChildren().add(new Label("Initial Population: "));
        pbox1.getChildren().add(lb0);
        predSlider = new Slider();
        predSlider.setMax(2500d);
        predSlider.setMin(0d);
        predSlider.setValue(Double.valueOf(area/4));
        predSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                npred = (int)Math.round(predSlider.getValue());
                population = npred+nprey;
                String val = Integer.toString(npred);
                predField.setText(val);
                if(population>area){
                    preySlider.setValue(Double.valueOf(area-npred));
                }
                //TO-DO: balance this value with the value of preySlider
            }
        });
        predSlider.setPrefWidth(250);
        predField = new TextField(Integer.toString((int)Math.round(area/4)));
        predField.setPadding(new Insets(5,1,5,1));
        predField.setPrefWidth(50);
        predField.setAlignment(Pos.CENTER);
        predField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                int val = 0;
                try{
                    val = Integer.parseInt(predField.getText());
                }
                catch(Exception e){
                    predField.setText(Integer.toString(npred));
                    System.out.println("Number Format Exception - predField");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return;
                }
                npred = val;
                predSlider.setValue(val);
            }
        });
        pbox1.getChildren().add(predSlider);
        pbox1.getChildren().add(predField);
        pbox1.setPadding(new Insets(0,0,5,0));
        col1.getChildren().add(pbox1);
        HBox pbox2 = new HBox();
        pbox2.setAlignment(Pos.CENTER);
        HBox lb1 = new HBox();
        lb1.setAlignment(Pos.CENTER_RIGHT);
        lb1.setPrefWidth(125);
        lb1.getChildren().add(new Label("Initial Health: "));
        pbox2.getChildren().add(lb1);
        healthSlider = new Slider();
        healthSlider.setMax(50.0);
        healthSlider.setValue(10.0);
        healthSlider.setPrefWidth(250d);
        healthSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                String val = df.format(healthSlider.getValue());
                healthField.setText(val);
                predHealth = Double.parseDouble(val);
                //TO-DO: balance this value with the value of preySlider
            }
        });
        healthField = new TextField("10.0");
        healthField.setAlignment(Pos.CENTER);
        healthField.setPadding(new Insets(5,1,5,1));
        healthField.setPrefWidth(50);
        healthField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                Double val = 0d;
                try{
                    String d = df.format(Double.parseDouble(healthField.getText()));
                    val = Double.parseDouble(d);
                }
                catch(NumberFormatException nfe){
                    healthField.setText(Double.toString(predHealth));
                    
                    System.out.println("Number Format Exception - healthField");
                    return;
                }
                predHealth = val;
                healthSlider.setValue(val);
            }
        });
        pbox2.getChildren().add(healthSlider);
        pbox2.getChildren().add(healthField);
        pbox2.setPadding(new Insets(0,0,5,0));
        
        col1.getChildren().add(pbox2);
        
        HBox pbox3 = new HBox();
        pbox3.setAlignment(Pos.CENTER);
        HBox lb2 = new HBox();
        lb2.setPrefWidth(125);
        lb2.setAlignment(Pos.CENTER_RIGHT);
        lb2.getChildren().add(new Label("Hunt Proficiency: "));
        pbox3.getChildren().add(lb2);
        huntSlider = new Slider();
        huntSlider.setMax(1.0);
        huntSlider.setValue(0.2);
        huntSlider.setPrefWidth(250d);
        huntSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                String val = pf.format(huntSlider.getValue());
                huntField.setText(val);
                hunt = Double.parseDouble(val);
                //TO-DO: balance this value with the value of preySlider
            }
        });
        huntField = new TextField("0.20");
        huntField.setAlignment(Pos.CENTER);
        huntField.setPadding(new Insets(5,1,5,1));
        huntField.setPrefWidth(50);
        huntField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                Double val = 0d;
                try{
                    String d = pf.format(Double.parseDouble(huntField.getText()));
                    val = Double.parseDouble(d);
                }
                catch(NumberFormatException nfe){
                    huntField.setText(Double.toString(hunt));
                    
                    System.out.println("Number Format Exception - huntField");
                    return;
                }
                hunt = val;
                huntSlider.setValue(val);
            }
        });
        pbox3.getChildren().add(huntSlider);
        pbox3.getChildren().add(huntField);
        pbox3.setPadding(new Insets(0,0,5,0));
        
        HBox pbox4 = new HBox();
        pbox4.setAlignment(Pos.CENTER);
        HBox lb3 = new HBox();
        lb3.setAlignment(Pos.CENTER_RIGHT);
        lb3.setPrefWidth(125);
        lb3.getChildren().add(new Label("Health Decay Rate: "));
        pbox4.getChildren().add(lb3);
        predDecaySlider = new Slider();
        predDecaySlider.setMax(10.0);
        predDecaySlider.setValue(2.0);
        predDecaySlider.setPrefWidth(250d);
        predDecaySlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                String val = df.format(predDecaySlider.getValue());
                predDecayField.setText(val);
                predDecay = Double.parseDouble(val);
                //TO-DO: balance this value with the value of preySlider
            }
        });
        predDecayField = new TextField("2.0");
        predDecayField.setAlignment(Pos.CENTER);
        predDecayField.setPadding(new Insets(5,1,5,1));
        predDecayField.setPrefWidth(50);
        predDecayField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                Double val = 0d;
                try{
                    String d = df.format(Double.parseDouble(predDecayField.getText()));
                    val = Double.parseDouble(d);
                }
                catch(NumberFormatException nfe){
                    predDecayField.setText(Double.toString(predDecay));
                    System.out.println("Number Format Exception - predDecayField");
                    return;
                }
                predDecay = val;
                predDecaySlider.setValue(val);
            }
        });
        pbox4.getChildren().add(predDecaySlider);
        pbox4.getChildren().add(predDecayField);
        pbox4.setPadding(new Insets(0,0,5,0));
        col1.getChildren().add(pbox4);
        
        HBox pbox5 = new HBox();
        pbox5.setAlignment(Pos.CENTER);
        HBox lb4 = new HBox();
        lb4.setAlignment(Pos.CENTER_RIGHT);
        lb4.setPrefWidth(125);
        lb4.getChildren().add(new Label("Breed Success: "));
        pbox5.getChildren().add(lb4);
        breedSlider = new Slider();
        breedSlider.setMax(1.0);
        breedSlider.setValue(0.5);
        breedSlider.setPrefWidth(250d);
        breedSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                String val = pf.format(breedSlider.getValue());
                breedField.setText(val);
                breed = Double.parseDouble(val);
                //TO-DO: balance this value with the value of preySlider
            }
        });
        breedField = new TextField("0.50");
        breedField.setAlignment(Pos.CENTER);
        breedField.setPadding(new Insets(5,1,5,1));
        breedField.setPrefWidth(50);
        breedField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                Double val = 0d;
                try{
                    String d = pf.format(Double.parseDouble(breedField.getText()));
                    val = Double.parseDouble(d);
                }
                catch(NumberFormatException nfe){
                    breedField.setText(Double.toString(breed));
                    
                    System.out.println("Number Format Exception - breedField");
                    return;
                }
                breed = val;
                breedSlider.setValue(val);
            }
        });
        pbox5.getChildren().add(breedSlider);
        pbox5.getChildren().add(breedField);
        pbox5.setPadding(new Insets(0,0,5,0));
        
        
        HBox pbox6 = new HBox();
        pbox6.setAlignment(Pos.CENTER);
        HBox lb5 = new HBox();
        lb5.setAlignment(Pos.CENTER_RIGHT);
        lb5.setPrefWidth(125);
        lb5.getChildren().add(new Label("Maximum Age: "));
        pbox6.getChildren().add(lb5);
        predMaxAgeSlider = new Slider();
        predMaxAgeSlider.setMax(100.0);
        predMaxAgeSlider.setValue(10.0);
        predMaxAgeSlider.setPrefWidth(250d);
        predMaxAgeSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                int val = (int)Math.round(predMaxAgeSlider.getValue());
                predMaxAgeField.setText(Integer.toString(val));
                predMaxAge = val;
                //TO-DO: balance this value with the value of preySlider
            }
        });
        predMaxAgeField = new TextField("10");
        predMaxAgeField.setAlignment(Pos.CENTER);
        predMaxAgeField.setPadding(new Insets(5,1,5,1));
        predMaxAgeField.setPrefWidth(50);
        predMaxAgeField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                int val = 0;
                try{
                    
                    val = Integer.parseInt(predMaxAgeField.getText());
                }
                catch(NumberFormatException nfe){
                    predMaxAgeField.setText(Integer.toString(predMaxAge));
                    
                    System.out.println("Number Format Exception - predMaxAgeField");
                    return;
                }
                predMaxAge = val;
                predMaxAgeSlider.setValue(Double.valueOf(val));
            }
        });
        pbox6.getChildren().add(predMaxAgeSlider);
        pbox6.getChildren().add(predMaxAgeField);
        pbox6.setPadding(new Insets(0,0,5,0));
        
        col1.getChildren().add(pbox6);
        
        col1.getChildren().add(pbox3);
        col1.getChildren().add(pbox5);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="prey properties layout">
        VBox ybox0 = new VBox();
        ybox0.setAlignment(Pos.CENTER);
        Separator ys = new Separator();
        ys.setPrefSize(425.0, 10.0);
        ybox0.getChildren().add(ys);
        ybox0.getChildren().add(new Label("Prey Properties"));
        Separator ys0 = new Separator();
        ys0.setPrefSize(425.0,10.0);
        ybox0.getChildren().add(ys0);
        col1.getChildren().add(ybox0);
        HBox ybox1 = new HBox();
        ybox1.setAlignment(Pos.CENTER);
        HBox lb6 = new HBox();
        lb6.setAlignment(Pos.CENTER_RIGHT);
        lb6.setPrefWidth(125);
        lb6.getChildren().add(new Label("Initial Population: "));
        ybox1.getChildren().add(lb6);
        preySlider = new Slider();
        preySlider.setMax(2500d);
        preySlider.setMin(0d);
        preySlider.setValue(Double.valueOf(area/4));
        preySlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                nprey = (int)Math.round(preySlider.getValue());
                population = npred+nprey;
                String val = Integer.toString(nprey);
                preyField.setText(val);
                if(population>area){
                    predSlider.setValue(Double.valueOf(area-nprey));
                }
            }
        });
        preySlider.setPrefWidth(250);
        preyField = new TextField(Integer.toString((int)Math.round(area/4)));
        preyField.setPadding(new Insets(5,1,5,1));
        preyField.setPrefWidth(50);
        preyField.setAlignment(Pos.CENTER);
        preyField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                int val = 0;
                try{
                    Integer.parseInt(preyField.getText());
                }
                catch(NumberFormatException nfe){
                    preyField.setText(Integer.toString(nprey));
                    
                    System.out.println("Number Format Exception - preyField");
                    return;
                }
                //TO-DO: check limits
                nprey = val;
                preySlider.setValue(Double.valueOf(val));
            }
        });
        ybox1.getChildren().add(preySlider);
        ybox1.getChildren().add(preyField);
        ybox1.setPadding(new Insets(0,0,5,0));
        col1.getChildren().add(ybox1);
        
        HBox ybox2 = new HBox();
        ybox2.setAlignment(Pos.CENTER);
        HBox lb7 = new HBox();
        lb7.setAlignment(Pos.CENTER_RIGHT);
        lb7.setPrefWidth(125);
        lb7.getChildren().add(new Label("Maximum Health: "));
        ybox2.getChildren().add(lb7);
        maxHealthSlider = new Slider();
        maxHealthSlider.setMax(50.0);
        maxHealthSlider.setValue(15.0);
        maxHealthSlider.setPrefWidth(250d);
        maxHealthSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                String val = df.format(maxHealthSlider.getValue());
                maxHealthField.setText(val);
                maxHealth = Double.parseDouble(val);
                //TO-DO: balance this value with the value of preySlider
            }
        });
        maxHealthField = new TextField("15.0");
        maxHealthField.setAlignment(Pos.CENTER);
        maxHealthField.setPadding(new Insets(5,1,5,1));
        maxHealthField.setPrefWidth(50);
        maxHealthField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                Double val = 0d;
                try{
                    String d = df.format(Double.parseDouble(maxHealthField.getText()));
                    val = Double.parseDouble(d);
                }
                catch(NumberFormatException nfe){
                    maxHealthField.setText(Double.toString(maxHealth));
                    System.out.println("Number Format Exception - maxHealthField");
                    return;
                }
                maxHealth = val;
                maxHealthSlider.setValue(val);
            }
        });
        ybox2.getChildren().add(maxHealthSlider);
        ybox2.getChildren().add(maxHealthField);
        ybox2.setPadding(new Insets(0,0,5,0));
        
        HBox ybox3 = new HBox();
        ybox3.setAlignment(Pos.CENTER);
        HBox lb8 = new HBox();
        lb8.setAlignment(Pos.CENTER_RIGHT);
        lb8.setPrefWidth(125);
        lb8.getChildren().add(new Label("Initial Health: "));
        ybox3.getChildren().add(lb8);
        preyHealthSlider = new Slider();
        preyHealthSlider.setMax(50.0);
        preyHealthSlider.setValue(10.0);
        preyHealthSlider.setPrefWidth(250d);
        preyHealthSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                String val = df.format(preyHealthSlider.getValue());
                preyHealthField.setText(val);
                preyHealth = Double.parseDouble(val);
                //TO-DO: balance this value with the value of preySlider
            }
        });
        preyHealthField = new TextField("10.0");
        preyHealthField.setAlignment(Pos.CENTER);
        preyHealthField.setPadding(new Insets(5,1,5,1));
        preyHealthField.setPrefWidth(50);
        preyHealthField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                Double val = 0d;
                try{
                    String d = df.format(Double.parseDouble(preyHealthField.getText()));
                    val = Double.parseDouble(d);
                }
                catch(NumberFormatException nfe){
                    preyHealthField.setText(Double.toString(preyHealth));
                    System.out.println("Number Format Exception - preyHealthField");
                    return;
                }
                preyHealth = val;
                preyHealthSlider.setValue(val);
            }
        });
        ybox3.getChildren().add(preyHealthSlider);
        ybox3.getChildren().add(preyHealthField);
        ybox3.setPadding(new Insets(0,0,5,0));
        
        col1.getChildren().add(ybox3);
        col1.getChildren().add(ybox2);
        
        HBox ybox4 = new HBox();
        ybox4.setAlignment(Pos.CENTER);
        HBox lb9 = new HBox();
        lb9.setAlignment(Pos.CENTER_RIGHT);
        lb9.setPrefWidth(125);
        lb9.getChildren().add(new Label("Health Decay Rate: "));
        ybox4.getChildren().add(lb9);
        preyDecaySlider = new Slider();
        preyDecaySlider.setMax(10.0);
        preyDecaySlider.setValue(2.0);
        preyDecaySlider.setPrefWidth(250d);
        preyDecaySlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                String val = df.format(preyDecaySlider.getValue());
                preyDecayField.setText(val);
                preyDecay = Double.parseDouble(val);
                //TO-DO: balance this value with the value of preySlider
            }
        });
        preyDecayField = new TextField("4.0");
        preyDecayField.setAlignment(Pos.CENTER);
        preyDecayField.setPadding(new Insets(5,1,5,1));
        preyDecayField.setPrefWidth(50);
        preyDecayField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                Double val = 0d;
                try{
                    String d = df.format(Double.parseDouble(preyDecayField.getText()));
                    val = Double.parseDouble(d);
                }
                catch(NumberFormatException nfe){
                    preyDecayField.setText(Double.toString(preyDecay));
                    System.out.println("Number Format Exception - preyDecayField");
                    return;
                }
                preyDecay = val;
                preyDecaySlider.setValue(val);
            }
        });
        ybox4.getChildren().add(preyDecaySlider);
        ybox4.getChildren().add(preyDecayField);
        ybox4.setPadding(new Insets(0,0,5,0));
        col1.getChildren().add(ybox4);
        
        HBox ybox5 = new HBox();
        ybox5.setAlignment(Pos.CENTER);
        HBox lb10 = new HBox();
        lb10.setAlignment(Pos.CENTER_RIGHT);
        lb10.setPrefWidth(125);
        lb10.getChildren().add(new Label("Maximum Age: "));
        ybox5.getChildren().add(lb10);
        preyMaxAgeSlider = new Slider();
        preyMaxAgeSlider.setMax(100.0);
        preyMaxAgeSlider.setValue(10.0);
        preyMaxAgeSlider.setPrefWidth(250d);
        preyMaxAgeSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                int val = (int)Math.round(preyMaxAgeSlider.getValue());
                preyMaxAgeField.setText(Integer.toString(val));
                preyMaxAge = val;
                //TO-DO: balance this value with the value of preySlider
            }
        });
        preyMaxAgeField = new TextField("10");
        preyMaxAgeField.setAlignment(Pos.CENTER);
        preyMaxAgeField.setPadding(new Insets(5,1,5,1));
        preyMaxAgeField.setPrefWidth(50);
        preyMaxAgeField.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                int val = 0;
                try{
                    
                    val = Integer.parseInt(preyMaxAgeField.getText());
                }
                catch(NumberFormatException nfe){
                    preyMaxAgeField.setText(Integer.toString(preyMaxAge));
                    System.out.println("Number Format Exception - preyMaxAgeField");
                    return;
                }
                preyMaxAge = val;
                preyMaxAgeSlider.setValue(Double.valueOf(val));
            }
        });
        ybox5.getChildren().add(preyMaxAgeSlider);
        ybox5.getChildren().add(preyMaxAgeField);
        ybox5.setPadding(new Insets(0,0,5,0));
        
        col1.getChildren().add(ybox5);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="compile button layout">
        VBox cBox = new VBox();
        cBox.setAlignment(Pos.CENTER);
        cBox.setPadding(new Insets(0,0,10,0));
        Separator sp = new Separator();
        sp.setPrefSize(540.0, 10.0);
        cBox.getChildren().add(sp);
        compile = new Button("compile");
        compile.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                compile();
            }
        });
        cBox.getChildren().add(compile);
        //</editor-fold>
        
        
        VBox bar = new VBox();
        HBox spacer = new HBox();
        spacer.setPrefHeight(25);
        bar.getChildren().add(spacer);
        bar.setAlignment(Pos.BOTTOM_CENTER);
        Separator v0 = new Separator(Orientation.VERTICAL);
        v0.setPrefSize(10, 390);
        bar.getChildren().add(v0);
        advPane.add(col0,0,0);
        advPane.add(bar,1,0);
        advPane.add(col1, 2, 0);
        advPane.add(cBox, 0, 1, 3, 1);
        advPane.setAlignment(Pos.CENTER);
        advPane.setPrefSize(620,675);
        mainPane.setCenter(advPane);
        aboutMI = new MenuItem("about the simulation");
        aboutMI.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                mainPane.setCenter(aboutPane);
            }
        });
        resetMI = new MenuItem("reset the simulation");
        resetMI.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                resetSim();
            }
        });
        Menu menu = new Menu("Simulation");
        menu.getItems().add(aboutMI);
        menu.getItems().add(resetMI);
        simMenu = new MenuBar(menu);
        mainPane.setTop(simMenu);
        
        GridPane right = new GridPane();
        right.setPadding(new Insets(5,5,5,5));
        //Image animationIcon = new Image(getClass().getResourceAsStream("resources/animation_icon.png"));
        configButton = new Button("Config");
        //configButton.setGraphic(new ImageView(animationIcon));
        configButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                mainPane.setCenter(advPane);
            }
        });
        
        lineGraphButton = new Button("graph");
        //Image graphIcon = new Image(getClass().getResourceAsStream("resources/line_graph_icon.png"));
        //test1.setGraphic(new ImageView(graphIcon));
        lineGraphButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                mainPane.setCenter(lineChart);
            }
        });
        
        aniButton = new Button("animation");
        aniButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                mainPane.setCenter(aniPane);
            }
        });
        right.setAlignment(Pos.CENTER);
        right.setVgap(10);
        right.add(configButton,0,0);
        right.add(lineGraphButton,0,1);
        right.add(aniButton,0,2);
        
        
        
        
        
        
        VBox cons = new VBox();
        HBox lab = new HBox();
        lab.setPadding(new Insets(5,5,5,5));
        lab.getChildren().add(new Label("notes"));
        lab.setAlignment(Pos.CENTER);
        cons.getChildren().add(lab);
        TextArea console = new TextArea();
        console.setText("console...");
        cons.setPadding(new Insets(5,5,5,5));
        console.setPrefSize(250,650);
        
        cons.getChildren().add(console);
        mainPane.setLeft(cons);
        mainPane.setRight(right);
        
        advScene = new Scene(mainPane);
        
        //<editor-fold defaultstate="collapsed" desc="compile layout">
        compileStage = new Stage();
        compilePane = new GridPane();
        compilePane.setPadding(new Insets(10,10,10,10));
        compilePane.setHgap(10);
        compilePane.setVgap(10);
        
        HBox barBox = new HBox();
        barBox.setAlignment(Pos.CENTER);
        barBox.setPrefSize(300, 150);
        compileBar = new ProgressBar();
        barBox.getChildren().add(compileBar);
        HBox cBox1 = new HBox();
        cBox1.setAlignment(Pos.CENTER);
        cBox1.setPrefWidth(150);
        compileCancel = new Button("cancel");
        compileCancel.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                stop();
                
                advPane.setOpacity(1);
                compileStage.close();
                control.requestFocus();
                
            }
        });
        cBox1.getChildren().add(compileCancel);
        HBox pBox = new HBox();
        pBox.setAlignment(Pos.CENTER);
        compilePause = new Button("pause");
        compilePause.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                if(!buttonState){
                    pause();
                   
                    compilePause.setText("resume");
                    buttonState = true;
                    return;
                }
                if(buttonState){
                    resume();
                    
                    compilePause.setText("pause");
                    buttonState = false;
                    
                }
            }
        });
        pBox.getChildren().add(compilePause);
        
        
        compilePane.add(barBox,0,0,2,1);
        compilePane.add(cBox1,0,1);
        compilePane.add(pBox,1,1);
        compileScene = new Scene(compilePane);
        //compileScene.getStylesheets().add(stylesheetURL);
        compileStage.setTitle("compiling life...");
        compileStage.setScene(compileScene);
        compileStage.setOnCloseRequest((new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent we) {
                we.consume();
                stop();
                
                advPane.setOpacity(1);
                compileStage.close();
                control.requestFocus();
            }
        }));
        
        
       
        
        
        
        //</editor-fold>
        
        evolutions = new ArrayList<>(t);
        
        //<editor-fold defaultstate="collapsed" desc="data visualization setup">
        
        gtypes = FXCollections.observableArrayList(
            "line graph","scatter plot","bar chart");
        dtypes = FXCollections.observableArrayList(
            "predator population","prey population","pred avg health","prey avg health");
        
       
        //</editor-fold>
        
        //make the about pane
        StringBuilder sb = new StringBuilder();
        aboutURL = getClass().getResourceAsStream("resources/about.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(aboutURL));
        String l;
        try{
            while((l=in.readLine())!=null){
                sb.append(l).append(nl);
            }
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        aboutArea = new TextArea(sb.toString());
        aboutArea.setPrefSize(620, 675);
        aboutArea.setWrapText(true);
        aboutPane = new GridPane();
        aboutPane.setAlignment(Pos.CENTER);
        aboutPane.add(aboutArea,0,0);
    }
    private void resetSim(){
        x = 50;
        xSlider.setValue(50);
        xField.setText("50");
        y = 50;
        ySlider.setValue(50);
        yField.setText("50");
        flora = 20d;
        floraMultiplier.getValueFactory().setValue(flora);
        t = 1000;
        tField.setText("1000");
        
        predHealth = 10.0;
        healthSlider.setValue(predHealth);
        healthField.setText("10.0");
        preyHealth = 10.0;
        preyHealthSlider.setValue(10.0);
        preyHealthField.setText("10.0");
        maxHealth = 15.0;
        maxHealthSlider.setValue(15.0);
        maxHealthField.setText("15.0");
        hunt = 0.20;
        huntSlider.setValue(hunt);
        huntField.setText("0.20");
        predDecay = 2.0;
        predDecaySlider.setValue(predDecay);
        predDecayField.setText("2.0");
        preyDecay = 4.0;
        preyDecaySlider.setValue(4.0);
        preyDecayField.setText("4.0");
        breed = 0.5;
        breedSlider.setValue(breed);
        breedField.setText("0.5");
        predMaxAge = 10;
        predMaxAgeSlider.setValue(10);
        predMaxAgeField.setText("10");
        preyMaxAge = 10;
        preyMaxAgeSlider.setValue(10);
        preyMaxAgeField.setText("10");
        
        area = x*y;
        npred = (int)Math.round(area/4);
        predSlider.setValue(Double.valueOf(area/4));
        predField.setText(Integer.toString(npred));
        nprey = (int)Math.round(area/4);
        preySlider.setValue(Double.valueOf(area/4));
        preyField.setText(Integer.toString(nprey));
        population = npred+nprey;
        isRunning = false;
        paused = false;
        simulation = null;
        aniRun = false;
        aniPause = false;
        animate = null;
        play.setText("play");
        aniSlider.setValue(0);
        aniSlider.setMax(evolutions.size());
        
        life = null;
        evolutions.clear();
        
        lineChart.getData().clear();
        GraphicsContext cg = aniCan.getGraphicsContext2D();
        cg.setFill(Color.WHITE);
        cg.fillRect(0,0,aniCan.getWidth(),aniCan.getHeight());
        
        mainPane.setCenter(advPane);
        
    }
    private void updateCanvas(){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                paintGrid(index);
            }
        });
    }
    private void initAnimation(){
        w = 600/y;
        h = 600/x;
        index = 0;
        
        aniPane = new GridPane();
        aniPane.setPadding(new Insets(10,10,10,10));
        HBox conbox = new HBox();
        conbox.setAlignment(Pos.CENTER);
        HBox rbox = new HBox();
        rbox.setPadding(new Insets(10,10,10,10));
        reverse = new Button("<");
        rbox.getChildren().add(reverse);
        reverse.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                if(index == 0) return;
                index--;
                updateCanvas();
                aniSlider.setValue(index);
            }
        });
        conbox.getChildren().add(rbox);
        HBox fbox = new HBox();
        fbox.setPadding(new Insets(10,10,10,10));
        forward = new Button(">");
        fbox.getChildren().add(forward);
        forward.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                if(index == evolutions.size()) return;
                index++;
                updateCanvas();
                aniSlider.setValue(index);
            }
        });
        conbox.getChildren().add(fbox);
        HBox sbox = new HBox();
        sbox.setPadding(new Insets(10,10,10,10));
        aniSlider = new Slider();
        aniSlider.setMax(evolutions.size()-1);
        aniSlider.setValue(0);
        aniSlider.setPrefWidth(325);
        aniSlider.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
               
                index = (int)Math.round(aniSlider.getValue());
                updateCanvas();
            }
        });
        sbox.getChildren().add(aniSlider);
        conbox.getChildren().add(sbox);
        HBox resbox = new HBox();
        resbox.setPadding(new Insets(10,10,10,10));
        reset = new Button("reset");
        reset.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                index = 0;
                aniSlider.setValue(0);
                updateCanvas();
                
            }
        });
        resbox.getChildren().add(reset);
        conbox.getChildren().add(resbox);
        HBox pbox = new HBox();
        pbox.setPadding(new Insets(10,10,10,10));
        play = new Button("play");
        play.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent ev){
                if(play.getText().equals("play")){
                    if(animate==null){
                        animate = new Thread(animation);
                        aniRun = true;
                        animate.setDaemon(true);
                        animate.start();
                    }
                    else{
                        synchronized (ani){
                            aniPause = false;
                            ani.notifyAll();
                        }
                        
                    }
                    play.setText("pause");
                    return;
                }
                if(play.getText().equals("pause")){
                    aniPause = true;
                    play.setText("play");
                }
                
            }
        });
        pbox.getChildren().add(play);
        conbox.getChildren().add(pbox);
        aniCan = new Canvas();
        aniCan.setWidth(600);
        aniCan.setHeight(600);
        paintGrid(0);
        
        aniPane.add(aniCan, 0, 0);
        aniPane.add(conbox,0,1);
        
        
        
    }
    double w;
    double h;
    private void paintGrid(int dex){
        if(!aniRun){
            return;
        }
        GraphicsContext cg = aniCan.getGraphicsContext2D();
        Animal[][] an = evolutions.get(dex);
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                int typ = an[i][j].type();
                switch(typ){
                    case 0:
                        cg.setFill(Color.CORAL);
                        break;
                    case 1:
                        cg.setFill(Color.DARKSEAGREEN);
                        break;
                    case 2: 
                        cg.setFill(Color.ALICEBLUE);
                        break;
                }
                cg.fillRect(w*j,h*i, w, h);
                
            }
        }
        
    }
    private Animal[][] cloneLife(){
        Animal[][] an = new Animal[x][y];
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                an[i][j] = new Animal(life[i][j].type(),
                        life[i][j].age(), life[i][j].health());
            }
        }
        return an;
    }
    private void endCompile(){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                advPane.setOpacity(1);
                compileStage.close();
                control.requestFocus();
                simulation = null;
                getData();
                initAnimation();
            }
        });
        
        
    }
    public void stop(){
            isRunning = false;
            
            
        }
    public void pause(){
            paused = true;
            
            
            
        }
    public void resume(){
            synchronized (tc){
                paused = false;
                tc.notifyAll();
            }
        }
    private void compile(){
        if(aniRun){
            aniRun = false;
            aniPause = false;
            animate = null;
            play.setText("play");
        }
        compileStage.show();
        advPane.setOpacity(0.5);
        if(life == null){
            
            validate();
            ArrayList<Animal> temp = new ArrayList<>();
            for(int i=0; i<npred; i++){
                temp.add(new Animal(0,0,predHealth));
            }
            for(int i=0; i<nprey; i++){
                temp.add(new Animal(1,0,preyHealth));
            }
            while(temp.size()<area){
                temp.add(new Animal(2,0,0));
            }

            SecureRandom rnd = new SecureRandom();
            Collections.shuffle(temp,rnd);
            life = new Animal[x][y];
            for(int i=0; i<x; i++){
                for(int j=0; j<y; j++){
                    life[i][j] = temp.remove(0);
                }
            }
            aph = getAverage(1,1);

            evolutions.add(cloneLife());
        }
        
        
        simulation = new Thread(simul);
        isRunning = true;
        simulation.setDaemon(true);
        simulation.start();
        
        
        
        
        
    }
    public Animal[][] getHood(Point anDex){
        
        Animal[][] hood = new Animal[3][3];
        
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                int x1 = anDex.x-1+i;
                int y1 = anDex.y-1+j;
                try{
                    hood[i][j]=life[x1][y1];
            
                }catch(IndexOutOfBoundsException e){
                    hood[i][j] = null;
                    
                }
            }
        }
        
        return hood;
    }
    public void setHood(Point cell, Animal[][] hood){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(hood[i][j]==null) continue;
                int x1 = cell.x-1+i;
                int y1 = cell.y-1+j;
                try{
                    life[x1][y1] = hood[i][j];
                }catch(IndexOutOfBoundsException e){}
            }
        }
    }
    public int census(int type){
        int c = 0;
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                if(life[i][j].type() == type){
                    c++;
                }
            }
            
        }
        return c;
    }
    public double getAverage(int stat, int type){ //0 for age, 1 for health
        double[] stats = new double[2];
        double n = 0;
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                if(life[i][j].type() == type){
                    stats[0]+= life[i][j].age();
                    stats[1]+= life[i][j].health();
                    n++;
                }
            }
        }
        return stats[stat]/n;
    }
    public Point locate(Animal an, Animal[][] array){
        Point p = new Point();
        for(int i=0; i<array.length; i++){
            for(int j=0; j<array[i].length; j++){
                if(an.equals(array[i][j])){
                    p.x = i;
                    p.y = j;
                    return p;
                }
            }
        }
        return p;
    }
    public ArrayList<Point> getCoordinates(){
        ArrayList<Point> points = new ArrayList<>();
        for(int i=0; i<x; i++){
            for(int j = 0; j<y; j++){
                points.add(new Point(i,j));
            }
        }
        return points;
    }
    public void evolve(){
        //System.out.println("Pred: "+census(0)+", Prey: "+census(1)+", Space: "+census(2));
        SecureRandom rnd = new SecureRandom();
        //per iteration:
        ArrayList<Point> coordinates = new ArrayList<>();
        coordinates.addAll(getCoordinates());
        Collections.shuffle(coordinates,rnd); // this is the random order in which cells will be selected
        //loop through each of the elements in temp
        while(!coordinates.isEmpty()){
           Point p = coordinates.remove(0);
           
           if(life[p.x][p.y].hadTurn() || life[p.x][p.y].type()==2){
               continue;
           } //skip an animal that has had its turn or skip empty space
           
          // System.out.println("anDex: "+anDex);
           Animal[][] hood = getHood(p);
           ArrayList<Animal> Space = new ArrayList<>();
           ArrayList<Animal> Pred = new ArrayList<>();
           ArrayList<Animal> Prey = new ArrayList<>();
           Point h = locate(life[p.x][p.y],hood);
           
           for(int i=0; i<3; i++){
               for(int j=0; j<3; j++){
                   if(hood[i][j]==null) continue;
                   int typ = hood[i][j].type();
                    
                    switch(typ){
                        case 0:
                            Pred.add(hood[i][j]);
                            break;
                        case 1:
                            Prey.add(hood[i][j]);
                            break;
                        case 2:
                            Space.add(hood[i][j]);
                            break;
                    }
           
                }
           }
               
           for(int i=0; i<Pred.size(); i++){
               if(Pred.get(i).didBreed()){
                   Pred.remove(i);
                   i--;
               }
           }
           Collections.shuffle(Space,rnd);
           Collections.shuffle(Pred,rnd);
           Collections.shuffle(Prey,rnd);
           
           //begin the animal type-specific actions
           if(hood[h.x][h.y].type()==0){//pred
               Pred.remove(hood[h.x][h.y]);
               
               if(rnd.nextDouble() <= hunt && !Prey.isEmpty()){
                   Point pdex = locate(Prey.remove(0),hood);
                   double sust = rnd.nextDouble()*preyHealth;
                   double phealth = hood[pdex.x][pdex.y].health();
                   hood[pdex.x][pdex.y].health(-sust);
                   if(phealth<=sust){
                    hood[pdex.x][pdex.y] = new Animal(2,0,0);
                    hood[h.x][h.y].health(phealth);
                   }
                   else{
                       hood[h.x][h.y].health(sust);
                   }
                   hood[h.x][h.y].didEat(true);
                   
                   
               }
               
               if(rnd.nextDouble() <= breed && !Pred.isEmpty() && !Space.isEmpty()){
                   
                   Point pdex = locate(Pred.remove(0),hood);
                   Point sdex = locate(Space.remove(0),hood);
                   hood[h.x][h.y].didBreed(true);
                   hood[pdex.x][pdex.y].didBreed(true);
                   hood[sdex.x][sdex.y] = new Animal(0,0,predHealth);
                   hood[sdex.x][sdex.y].hadTurn(true);
                   hood[sdex.x][sdex.y].didEat(true);
                   
               }
               
               if(!Space.isEmpty()){
                   Point sdex = locate(Space.remove(0),hood);
                   Animal pr = hood[h.x][h.y];
                   hood[sdex.x][sdex.y] = pr;
                   hood[h.x][h.y] = new Animal(2,0,0);
               }
           }
           else{//prey
               double food = area*flora;
               double need = nprey*aph;
               double eats = 1-need/food;
               double sust = rnd.nextDouble()*flora;
               if(rnd.nextDouble()<eats){
                   hood[h.x][h.y].health(sust);
                   hood[h.x][h.y].didEat(true);
               }
               if(hood[h.x][h.y].health()>=maxHealth){
                   if(!Space.isEmpty()){
                        Point sdex = locate(Space.remove(0),hood);
                        hood[sdex.x][sdex.y] = new Animal(1,0,preyHealth);
                        hood[sdex.x][sdex.y].hadTurn(true);
                        hood[sdex.x][sdex.y].didEat(true);
                        double phealth = hood[h.x][h.y].health();
                        hood[h.x][h.y].health(-phealth);
                        hood[h.x][h.y].health(preyHealth);
                   
                    }
                   else{
                       hood[h.x][h.y] = new Animal(2,0,0);
                   }
               }
           }
           
           hood[h.x][h.y].hadTurn(true);
            setHood(p, hood);
           
        }
        
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                int typ = life[i][j].type();
                if(typ==2) continue;
                
                if(!life[i][j].didEat()){
                    switch(typ){
                        case 0: 
                            life[i][j].health(-predDecay);
                            break;
                        case 1:
                            life[i][j].health(-preyDecay);
                            break;
                    }
                }
                
                life[i][j].hadTurn(false);
                life[i][j].didEat(false);
                if(typ==0 && life[i][j].age()==predMaxAge){
                    life[i][j] = new Animal(2,0,0);
                }
                else{life[i][j].age(1);}
                if(typ==1 && life[i][j].age()==preyMaxAge){
                    life[i][j] = new Animal(2,0,0);
                }
                else{life[i][j].age(1);}
                
                
                if(life[i][j].health()<=0){
                    life[i][j] = new Animal(2,0,0);
                }
                
                
            }
        }
        
        
        //System.out.println("Pred: "+census(0)+", Prey: "+census(1)+", Space: "+census(2));
        
        aph = getAverage(1,1);
        nprey = census(1);
        npred = census(0);
        
       
    }
    public void getData(){
        
        
        lineChart.setTitle("population trends with time");
        lineChart.setCreateSymbols(false);
        
        XYChart.Series preySeries = new XYChart.Series();
        preySeries.setName("nprey");
        XYChart.Series predSeries = new XYChart.Series();
        predSeries.setName("npred");
        ArrayList<Integer> predPop = new ArrayList<>();
        ArrayList<Integer> preyPop = new ArrayList<>();
        for(int i=0; i<evolutions.size(); i++){
            Animal[][] an = evolutions.get(i);
            int npr=0;
            int npy = 0;
            for(int p=0; p<x; p++){
                for(int q=0; q<y; q++){
                    if(an[p][q].type() == 1){
                        npy++;
                        continue;
                    }
                    if(an[p][q].type()==0){
                        npr++;
                    }
                }
            }
            preyPop.add(npy);
            predPop.add(npr);
        }
        
        for(int j=0; j<predPop.size(); j++){
            predSeries.getData().add(new XYChart.Data(j,predPop.get(j)));
        }
        for(int j=0; j<preyPop.size(); j++){
            preySeries.getData().add(new XYChart.Data(j,preyPop.get(j)));
        }
        
        lineChart.getData().add(predSeries);
        lineChart.getData().add(preySeries);
        
        
    }
    
    //Global Controls
    private MenuItem resetMI;
    private GridPane aboutPane;
    private TextArea aboutArea;
    private InputStream aboutURL;
    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
    private Button configButton;
    private Button lineGraphButton;
    private Button aniButton;
    private VBox col0;
    private Slider xSlider;
    private TextField xField;
    private Slider ySlider;
    private TextField yField;
    private Spinner floraMultiplier;
    private TextField tField;
    private Button compile;
    private VBox col1;
    private Slider predSlider;
    private TextField predField;
    private Slider healthSlider;
    private TextField healthField;
    private Slider huntSlider;
    private TextField huntField;
    private Slider predDecaySlider;
    private TextField predDecayField;
    private Slider breedSlider;
    private TextField breedField;
    private Slider predMaxAgeSlider;
    private TextField predMaxAgeField;
    private Slider preySlider;
    private TextField preyField;
    private Slider preyHealthSlider;
    private TextField preyHealthField;
    private Slider maxHealthSlider;
    private TextField maxHealthField;
    private Slider preyMaxAgeSlider;
    private TextField preyMaxAgeField;
    private Slider preyDecaySlider;
    private TextField preyDecayField;
    private BorderPane mainPane;
    private MenuBar simMenu;
    private MenuItem aboutMI;
    private Stage lineStage;
    private Scene lineScene;
    private Stage control;
    private Scene advScene;
    private GridPane advPane;
    private Stage dataStage;
    private Stage compileStage;
    private Scene compileScene;
    private GridPane compilePane;
    private ProgressBar compileBar;
    private Button compileCancel;
    private Button compilePause;
    private Scene dataScene;
    private GridPane dataPane;
    private ComboBox graphBox;
    private Button graphButton;
    private Button addSeries;
    private Button remSeries;
    private ComboBox yData0;
    private ComboBox yData1;
    private ComboBox yData2;
    private ComboBox yData3;
    private TextField sName0;
    private TextField sName1;
    private TextField sName2;
    private TextField sName3;
    private ScrollPane seriesPane;
    private Scene aniScene;
    private Stage aniStage;
    private Canvas aniCan;
    private Button forward;
    private Button reverse;
    private Slider aniSlider;
    private Button play;
    private Button reset;
    private GridPane aniPane;
    //Threads and Runnables
    Thread simulation;
    private volatile boolean isRunning = false;
    private volatile boolean paused = false;
    private final Object tc = new Object();
    private final Runnable simul = new Runnable(){
        
            @Override
            public void run(){
                
                int iter = 0;
                while(isRunning){
                
                    synchronized (tc){
                        if(!isRunning){
                            endCompile();
                            break;
                        }
                        if(paused){
                            System.out.println("paused");
                            try{
                                tc.wait();
                            }
                            catch(InterruptedException ex){
                                
                                break;
                            }
                        }
                    }
                    evolve();
                    evolutions.add(cloneLife());
                    iter++;
                    if(iter==t){
                        isRunning=false;
                        
                        endCompile();
                    }
                   
                }
            }
        };
    
    private Thread animate;
    private volatile boolean aniRun = false;
    private volatile boolean aniPause = false;
    private final Object ani = new Object();
    private final Runnable animation = new Runnable(){
        @Override
        public void run(){
            while(aniRun){
                
                    synchronized (ani){
                        if(!aniRun){
                            index = 0;
                            aniSlider.setValue(0);
                            updateCanvas();
                            break;
                        }
                        if(aniPause){
                            try{
                                ani.wait();
                            }
                            catch(InterruptedException ex){
                                
                                break;
                            }
                        }
                    }
                    index++;
                    if(index==evolutions.size()){
                        index = 0;
                        aniSlider.setValue(0);
                    }
                    updateCanvas();
                    aniSlider.setValue(index);
                    try{
                        Thread.sleep(250);
                    }
                    catch(InterruptedException ie){}
                    
            }
        }
            
    };
    
    //Global Variables
    private int index;
    private final double X = 20;
    private final double Y = 20;
    //private String stylesheetURL;
    private int x;
    private int y;
    private double flora;
    private int t;
    private int npred;
    private int nprey;
    private double predHealth;
    private double preyHealth;
    private double hunt;
    private double predDecay;
    private double breed;
    private int predMaxAge;
    private double preyDecay;
    private double maxHealth;
    private int preyMaxAge;
    private int area;
    private int population;
    private double aph;
    private ArrayList<Animal[][]> evolutions;
    private Animal[][] life;
    private final String nl = System.getProperty("line.separator");
    private boolean buttonState = false;
    int graphIndex;
    ObservableList<String> gtypes;
    ObservableList<String> dtypes;
    private final DecimalFormat df = new DecimalFormat("#.#");
    private final DecimalFormat pf = new DecimalFormat("#.##");
    
    public static void main(String[] args) {
        launch(args);
    }
}
