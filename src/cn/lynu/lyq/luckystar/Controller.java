package cn.lynu.lyq.luckystar;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
//    @FXML
//    private Button startButton;
//    @FXML
//    private Button pauseButton;
    @FXML
    private Label nameLabel;

    private Service<String> service;

    private String[] names = new String[]{"1", "2", "3", "4", "5", "6", "7"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //1. 试着从当前目录下找xlsx文件
        try {
            System.out.println("当前目录:"+new File(getProjectPath() + "/students.xlsx").getAbsolutePath());
            names = ExcelUtils.readNamesFromXLSX(new File(getProjectPath() + "/students.xlsx"));
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //2. 试着从运行目录下找xlsx文件（直接双击jar文件，运行目录可能是cmd.exe所在目录，即c:\windows\system32 !）
        try {
//            FileWriter fw = new FileWriter(new File(System.getProperty("user.dir")+"/luckstarlog.txt"));
//            fw.write(new File(System.getProperty("user.dir")+"/luckystarlog.txt").getAbsolutePath());
//            fw.flush();
//            fw.close();
            System.out.println("运行目录:"+new File(System.getProperty("user.dir") + "/students.xlsx").getAbsolutePath());
            names = ExcelUtils.readNamesFromXLSX(new File(System.getProperty("user.dir") + "/students.xlsx"));
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //3. 当前目录 和 运行目录下 都没有，再试着读取jar打包内部的文件
        try {
            System.out.println("jar文件中");
            names = ExcelUtils.readNamesFromXLSX(getClass().getResourceAsStream("/students.xlsx"));
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onStartBtnClick(ActionEvent event) {
        service = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        for (int a = 1; a <= 2000; a++) {
                            if (this.isCancelled()) break;
                            //更新service的value属性
                            updateValue(names[new Random().nextInt(names.length)]);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                if (this.isCancelled()) {
                                    break;
                                }
                            }
                        }
                        return names[new Random().nextInt(names.length)];
                    }

                };
            }

        };
        //任务完成时会调用
        service.setOnSucceeded((WorkerStateEvent event1) -> {
            System.out.println("任务处理完成！");
        });
        nameLabel.textProperty().bind(service.valueProperty());
        //启动任务start()一定是最后才调用的
        if (service.isRunning()) {
            service.restart();
        } else {
            service.start();
        }
    }

    @FXML
    public void onPauseBtnClick(ActionEvent event) {
        service.cancel();
    }

    /**
     * 获取项目所在路径(包括jar)
     */
    public String getProjectPath() {
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar"))
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        return new File(filePath).getAbsolutePath();
    }

}
